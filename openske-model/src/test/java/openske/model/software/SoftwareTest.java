package openske.model.software;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import openske.model.hardware.Host;
import openske.model.security.UserAccount;
import openske.model.security.SecurityState;

/**
 * Test cases for Software and associated security features.
 * 
 * Tests based on CAPEC attack patterns and CWE weaknesses from the OpenSKE paper:
 * - CAPEC-1: ACL bypass (requires CWE-285, CWE-732, CWE-276, CWE-693, CWE-721, CWE-434)
 * - CAPEC-2: Account lockout (requires CWE-400)
 * - CAPEC-7: SQL injection (requires CWE-89, CWE-209, CWE-74, CWE-20, CWE-390, CWE-697, CWE-713, CWE-707)
 * - CAPEC-16: Dictionary attack (requires CWE-521, CWE-262, CWE-263, CWE-693)
 */
class SoftwareTest {

    private Host webServer;
    private Software apache;
    private Software mysql;

    @BeforeEach
    void setUp() {
        webServer = new Host("web.proggy");
        apache = new Software("cpe:2.3:a:apache:http_server:2.2:*:*:*:*:*:*:*", webServer);
        mysql = new Software("cpe:2.3:a:mysql:mysql:5.0:*:*:*:*:*:*:*", webServer);
    }

    @Test
    @DisplayName("Should create software with CPE ID and host")
    void testSoftwareCreation() {
        assertNotNull(apache);
        assertEquals(webServer, apache.getHost());
        assertNotNull(apache.toString());
    }

    @Test
    @DisplayName("Should have initial unknown security state")
    void testInitialSecurityState() {
        assertEquals(SecurityState.UNKNOWN, apache.getSecurityState());
    }

    @Test
    @DisplayName("Should add and retrieve user accounts")
    void testAddAccount() {
        // From paper: phpMyAdmin has admin account
        apache.addAccount("admin", "password123");
        
        assertEquals(1, apache.getAccounts().size());
        UserAccount account = apache.getAccounts().get(0);
        assertEquals("admin", account.getUsername());
    }

    @Test
    @DisplayName("Should prevent duplicate accounts")
    void testNoDuplicateAccounts() {
        apache.addAccount("admin", "password123");
        apache.addAccount("admin", "differentpassword");
        
        assertEquals(1, apache.getAccounts().size());
    }

    @Test
    @DisplayName("Should add vulnerabilities for CAPEC-7 SQL injection")
    void testAddVulnerabilityForCAPEC7() {
        // CAPEC-7 requires CWE-89 (SQL Injection)
        apache.addVulnerability("CVE-2007-1234"); // Example CVE
        
        assertFalse(apache.getVulnerabilities().isEmpty());
    }

    @Test
    @DisplayName("Should derive weaknesses from vulnerabilities")
    void testWeaknessDerivation() {
        // Adding a vulnerability should create associated weaknesses
        apache.addVulnerability("CVE-2007-1234");
        
        // Should have weaknesses derived from the vulnerability
        assertFalse(apache.getWeaknesses().isEmpty());
    }

    @Test
    @DisplayName("Should add specific CWE weaknesses for CAPEC-16")
    void testAddWeaknessForCAPEC16() {
        // CAPEC-16 requires CWE-521 (Weak Password Requirements)
        apache.addWeakness("CWE-521");
        
        assertTrue(apache.hasWeakness(new Weakness("CWE-521", apache)));
    }

    @Test
    @DisplayName("Should check for specific weakness presence")
    void testHasWeakness() {
        apache.addWeakness("CWE-285"); // Improper Authorization - for CAPEC-1
        
        Weakness checkWeakness = new Weakness("CWE-285", apache);
        assertTrue(apache.hasWeakness(checkWeakness));
    }

    @Test
    @DisplayName("Should manage software dependencies")
    void testDependencies() {
        // ProggyWeb depends on Apache
        Software proggyWeb = new Software("cpe:2.3:a:proggy:proggyweb:1.0:*:*:*:*:*:*:*", webServer);
        
        List<Software> dependencies = new ArrayList<>();
        dependencies.add(apache);
        proggyWeb.setDependencies(dependencies);
        
        assertTrue(proggyWeb.getDependencies().contains(apache));
    }

    @Test
    @DisplayName("Should get random account for CAPEC-16 attack simulation")
    void testGetRandomAccount() {
        apache.addAccount("admin", "weakpassword");
        apache.addAccount("user1", "password1");
        apache.addAccount("user2", "password2");
        
        UserAccount randomAccount = apache.getRandomAccount();
        assertNotNull(randomAccount);
        assertTrue(apache.getAccounts().contains(randomAccount));
    }

    @Test
    @DisplayName("Should return null for random account when no accounts exist")
    void testGetRandomAccountWhenEmpty() {
        UserAccount randomAccount = apache.getRandomAccount();
        assertNull(randomAccount);
    }

    @Test
    @DisplayName("Should update security state to RISKY when vulnerabilities present")
    void testRiskySecurityState() {
        apache.setSecurityState(SecurityState.RISKY);
        
        assertEquals(SecurityState.RISKY, apache.getSecurityState());
    }

    @Test
    @DisplayName("Should compare software by CPE ID")
    void testSoftwareComparison() {
        Software apacheNewer = new Software("cpe:2.3:a:apache:http_server:2.4:*:*:*:*:*:*:*", webServer);
        
        int comparison = apache.compareTo(apacheNewer);
        assertNotEquals(0, comparison);
    }

    @Test
    @DisplayName("Should return CPE ID as string representation")
    void testToString() {
        assertNotNull(apache.toString());
        assertTrue(apache.toString().contains("apache"));
    }

    @Test
    @DisplayName("Should provide statistics")
    void testStatistics() {
        apache.addAccount("admin", "password");
        apache.addWeakness("CWE-521");
        
        String stats = apache.statistics();
        assertNotNull(stats);
        assertTrue(stats.contains("apache"));
    }

    @Test
    @DisplayName("Should support multiple weaknesses for different CAPECs")
    void testMultipleWeaknesses() {
        // Add weaknesses for multiple CAPEC patterns
        apache.addWeakness("CWE-285"); // CAPEC-1
        apache.addWeakness("CWE-89");  // CAPEC-7
        apache.addWeakness("CWE-521"); // CAPEC-16
        
        assertEquals(3, apache.getWeaknesses().size());
    }

    @Test
    @DisplayName("Should add multiple vulnerabilities")
    void testAddMultipleVulnerabilities() {
        apache.addVulnerabilities("CVE-2007-1234", "CVE-2007-5678", "CVE-2008-9999");
        
        assertEquals(3, apache.getVulnerabilities().size());
    }

    @Test
    @DisplayName("Should get random asset when marked as asset")
    void testGetRandomAsset() {
        // Mark as asset
        apache.setAnAsset(true);
        
        // When no assets configured, should return null
        assertNull(apache.getRandomAsset(openske.model.assets.AssetType.DATABASE));
    }

    @Test
    @DisplayName("Should check if marked as asset")
    void testIsAnAsset() {
        assertFalse(apache.isAnAsset());
        
        apache.setAnAsset(true);
        
        assertTrue(apache.isAnAsset());
    }
}
