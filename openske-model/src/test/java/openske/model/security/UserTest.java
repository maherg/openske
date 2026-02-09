package openske.model.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import openske.model.hardware.Host;
import openske.model.software.Software;
import openske.model.assets.AssetAccess;
import openske.model.assets.AssetAccessType;
import openske.model.assets.data.DatabaseAsset;

/**
 * Test cases for User and UserAccount security entities.
 * 
 * Based on scenarios from the OpenSKE paper including:
 * - Attackers (e.g., "Mr. X", "Lone Attacker") 
 * - Legitimate users with accounts on various software
 * - Account states: ACTIVE, LOCKED, etc.
 * - Asset access permissions
 */
class UserTest {

    private Host attackerHost;
    private Host victimHost;
    private Software webApp;

    @BeforeEach
    void setUp() {
        attackerHost = new Host("attacker.proggy");
        victimHost = new Host("web.proggy");
        webApp = new Software("cpe:2.3:a:proggy:proggyweb:1.0:*:*:*:*:*:*:*", victimHost);
    }

    @Test
    @DisplayName("Should create attacker user - Mr. X scenario")
    void testAttackerCreation() {
        // From paper: "Mr. X" on attacker.proggy
        User attacker = new User("Mr. X", "mrx@evil.com", attackerHost, true);
        
        assertNotNull(attacker);
        assertEquals("Mr. X", attacker.getFullName());
        assertTrue(attacker.isAttacker());
        assertEquals(attackerHost, attacker.getHost());
    }

    @Test
    @DisplayName("Should create legitimate user")
    void testLegitimateUserCreation() {
        User user = new User("John Doe", "john@proggy.com", victimHost);
        
        assertNotNull(user);
        assertEquals("John Doe", user.getFullName());
        assertFalse(user.isAttacker());
    }

    @Test
    @DisplayName("Should add and retrieve user accounts")
    void testAddAccount() {
        User user = new User("Test User", "test@test.com", victimHost);
        UserAccount account = new UserAccount("testuser", "password", webApp);
        
        user.addAccount(account);
        
        assertEquals(1, user.getAccounts().size());
        assertTrue(user.getAccounts().contains(account));
    }

    @Test
    @DisplayName("Should manage account states - CAPEC-2 scenario")
    void testAccountLockout() {
        // CAPEC-2: Inducing Account Lockout
        User user = new User("Victim", "victim@proggy.com", victimHost);
        UserAccount account = new UserAccount("victim", "password", webApp);
        account.setState(UserAccountState.ACTIVE);
        
        user.addAccount(account);
        
        assertEquals(UserAccountState.ACTIVE, account.getState());
        
        // Simulate lockout
        account.setState(UserAccountState.LOCKED);
        
        assertEquals(UserAccountState.LOCKED, account.getState());
    }

    @Test
    @DisplayName("Should get recent account - CAPEC-16 scenario")
    void testGetRecentAccount() {
        User attacker = new User("Attacker", "attacker@evil.com", attackerHost, true);
        UserAccount breachedAccount = new UserAccount("admin", "breached", webApp);
        
        attacker.addAccount(breachedAccount);
        
        UserAccount recent = attacker.getRecentAccount();
        assertEquals(breachedAccount, recent);
    }

    @Test
    @DisplayName("Should add asset access - CAPEC-7 scenario")
    void testAssetAccess() {
        // CAPEC-7: SQL Injection grants database access
        User attacker = new User("Mr. X", "mrx@evil.com", attackerHost, true);
        DatabaseAsset database = new DatabaseAsset("ProggyBook Database", victimHost, "proggybook");
        AssetAccess access = new AssetAccess(database, attacker, AssetAccessType.READ_WRITE);
        
        attacker.addAssetAccess(access);
        
        assertEquals(1, attacker.getAssetAccesses().size());
        assertEquals(AssetAccessType.READ_WRITE, attacker.getAssetAccesses().get(0).getType());
    }

    @Test
    @DisplayName("Should get recent asset access")
    void testGetRecentAssetAccess() {
        User attacker = new User("Mr. X", "mrx@evil.com", attackerHost, true);
        DatabaseAsset database = new DatabaseAsset("ProggyBook Database", victimHost, "proggybook");
        AssetAccess access = new AssetAccess(database, attacker, AssetAccessType.READ);
        
        attacker.addAssetAccess(access);
        
        AssetAccess recent = attacker.getRecentAssetAccess();
        assertNotNull(recent);
        assertEquals(database, recent.getAsset());
    }

    @Test
    @DisplayName("Should return Object.toString when not overridden")
    void testToString() {
        User user = new User("John Doe", "john@test.com", victimHost);
        
        // User doesn't override toString(), so it uses Object.toString()
        // which returns class name and hashcode
        String str = user.toString();
        assertNotNull(str);
        assertTrue(str.contains("User"));
    }

    @Test
    @DisplayName("Should update email")
    void testSetEmail() {
        User user = new User("John Doe", "old@email.com", victimHost);
        
        user.setEmail("new@email.com");
        
        assertEquals("new@email.com", user.getEmail());
    }

    @Test
    @DisplayName("Should provide statistics")
    void testStatistics() {
        User user = new User("John Doe", "john@test.com", victimHost);
        UserAccount account = new UserAccount("john", "password", webApp);
        user.addAccount(account);
        
        String stats = user.statistics();
        assertNotNull(stats);
        assertTrue(stats.contains("John Doe"));
    }
}
