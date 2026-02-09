package openske.model.attacks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import openske.model.hardware.Host;
import openske.model.software.Software;
import openske.model.software.Weakness;

/**
 * Test cases for Attack Patterns (CAPEC) from the OpenSKE paper.
 * 
 * The paper references four main CAPEC attack patterns:
 * - CAPEC-1: Accessing Functionality Not Properly Constrained by ACLs
 * - CAPEC-2: Inducing Account Lockout
 * - CAPEC-7: Blind SQL Injection
 * - CAPEC-16: Dictionary-based Password Attack
 */
class AttackPatternTest {

    private Host attackerHost;
    private Host targetHost;
    private Software targetSoftware;
    private Weakness testWeakness;

    @BeforeEach
    void setUp() {
        attackerHost = new Host("attacker.proggy");
        targetHost = new Host("web.proggy");
        targetSoftware = new Software("cpe:2.3:a:proggy:webapp:1.0:*:*:*:*:*:*:*", targetHost);
        testWeakness = new Weakness("CWE-285", targetSoftware);
    }

    @Test
    @DisplayName("Should create attack pattern")
    void testAttackPatternCreation() {
        AttackPattern pattern = new AttackPattern("CAPEC-1", testWeakness);
        
        assertNotNull(pattern);
        assertEquals("CAPEC-1", pattern.capecId());
    }

    @Test
    @DisplayName("Should have initial READY state")
    void testInitialState() {
        AttackPattern pattern = new AttackPattern("CAPEC-1", testWeakness);
        
        assertEquals(AttackPatternState.READY, pattern.getState());
    }

    @Test
    @DisplayName("Should set and retrieve attack pattern state")
    void testAttackPatternState() {
        AttackPattern pattern = new AttackPattern("CAPEC-1", testWeakness);
        
        assertEquals(AttackPatternState.READY, pattern.getState());
        
        pattern.setState(AttackPatternState.ACTIVE);
        assertEquals(AttackPatternState.ACTIVE, pattern.getState());
        
        pattern.setState(AttackPatternState.SUCCESSFUL);
        assertEquals(AttackPatternState.SUCCESSFUL, pattern.getState());
        
        pattern.setState(AttackPatternState.FAILED);
        assertEquals(AttackPatternState.FAILED, pattern.getState());
    }

    @Test
    @DisplayName("Should support CAPEC-1: ACL Bypass prerequisites")
    void testCAPEC1Prerequisites() {
        // CAPEC-1 requires: CWE-285, CWE-732, CWE-276, CWE-693, CWE-721, CWE-434
        targetSoftware.addWeakness("CWE-285"); // Improper Authorization
        targetSoftware.addWeakness("CWE-732"); // Incorrect Permission Assignment
        
        assertTrue(targetSoftware.hasWeakness(new Weakness("CWE-285", targetSoftware)));
        assertTrue(targetSoftware.hasWeakness(new Weakness("CWE-732", targetSoftware)));
    }

    @Test
    @DisplayName("Should support CAPEC-2: Account Lockout prerequisites")
    void testCAPEC2Prerequisites() {
        // CAPEC-2 requires: CWE-400 (Uncontrolled Resource Consumption)
        targetSoftware.addWeakness("CWE-400");
        
        assertTrue(targetSoftware.hasWeakness(new Weakness("CWE-400", targetSoftware)));
    }

    @Test
    @DisplayName("Should support CAPEC-7: SQL Injection prerequisites")
    void testCAPEC7Prerequisites() {
        // CAPEC-7 requires: CWE-89, CWE-209, CWE-74, CWE-20, CWE-390, CWE-697, CWE-713, CWE-707
        // Use fresh software to avoid test isolation issues
        Software freshSoftware = new Software("cpe:2.3:a:test:fresh:1.0:*:*:*:*:*:*:*", targetHost);
        int initialCount = freshSoftware.getWeaknesses().size();
        
        freshSoftware.addWeakness("CWE-89");  // SQL Injection
        freshSoftware.addWeakness("CWE-209"); // Information Exposure Through Error Messages
        freshSoftware.addWeakness("CWE-74");  // Injection
        
        assertEquals(initialCount + 3, freshSoftware.getWeaknesses().size());
    }

    @Test
    @DisplayName("Should support CAPEC-16: Dictionary Attack prerequisites")
    void testCAPEC16Prerequisites() {
        // CAPEC-16 requires: CWE-521, CWE-262, CWE-263, CWE-693
        targetSoftware.addWeakness("CWE-521"); // Weak Password Requirements
        targetSoftware.addWeakness("CWE-262"); // Not Using Password Aging
        
        assertTrue(targetSoftware.hasWeakness(new Weakness("CWE-521", targetSoftware)));
    }

    @Test
    @DisplayName("Should get associated weakness")
    void testGetWeakness() {
        AttackPattern pattern = new AttackPattern("CAPEC-1", testWeakness);
        
        assertEquals(testWeakness, pattern.getWeakness());
    }

    @Test
    @DisplayName("Should set and get description")
    void testDescription() {
        AttackPattern pattern = new AttackPattern("CAPEC-7", testWeakness);
        pattern.setDescription("SQL Injection Attack Pattern");
        
        assertEquals("SQL Injection Attack Pattern", pattern.getDescription());
    }

    @Test
    @DisplayName("Should change weakness")
    void testSetWeakness() {
        AttackPattern pattern = new AttackPattern("CAPEC-1", testWeakness);
        
        Weakness newWeakness = new Weakness("CWE-89", targetSoftware);
        pattern.setWeakness(newWeakness);
        
        assertEquals(newWeakness, pattern.getWeakness());
    }
}
