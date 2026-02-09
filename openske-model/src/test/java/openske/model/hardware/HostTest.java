package openske.model.hardware;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test cases for Host connectivity and networking.
 * 
 * Based on scenarios from the OpenSKE paper including:
 * - ProggySolutions infrastructure with router connections
 * - Metasploitable VM scenario with network reachability
 * - Attack patterns requiring host-to-host connectivity
 */
class HostTest {

    private Host webServer;
    private Host databaseServer;
    private Host attackerHost;
    private Host workstation;

    @BeforeEach
    void setUp() {
        // ProggySolutions scenario from paper
        webServer = new Host("web.proggy");
        databaseServer = new Host("database.proggy");
        attackerHost = new Host("attacker.proggy");
        workstation = new Host("station1.proggy");
    }

    @Test
    @DisplayName("Should create host with address")
    void testHostCreation() {
        assertNotNull(webServer);
        assertEquals("web.proggy", webServer.getAddress());
    }

    @Test
    @DisplayName("Should add bidirectional connections between hosts")
    void testAddConnection() {
        webServer.addConnection(databaseServer);
        
        // Connection should be bidirectional
        assertTrue(webServer.getConnections().contains(databaseServer));
        assertTrue(databaseServer.getConnections().contains(webServer));
    }

    @Test
    @DisplayName("Should prevent duplicate connections")
    void testNoDuplicateConnections() {
        webServer.addConnection(databaseServer);
        webServer.addConnection(databaseServer); // Add again
        
        assertEquals(1, webServer.getConnections().size());
        assertEquals(1, databaseServer.getConnections().size());
    }

    @Test
    @DisplayName("Should support multiple connections from one host")
    void testMultipleConnections() {
        // Web server connects to database and workstation
        webServer.addConnection(databaseServer);
        webServer.addConnection(workstation);
        
        assertEquals(2, webServer.getConnections().size());
    }

    @Test
    @DisplayName("Should detect direct reachability - Metasploitable scenario")
    void testCanReachDirect() {
        // From paper: attacker can reach target when directly connected
        Host metasploitable = new Host("192.168.56.101");
        Host attacker = new Host("192.168.1.44");
        
        attacker.addConnection(metasploitable);
        
        assertTrue(attacker.canReach(metasploitable));
        assertTrue(metasploitable.canReach(attacker)); // Bidirectional
    }

    @Test
    @DisplayName("Should detect self-reachability")
    void testCanReachSelf() {
        assertTrue(webServer.canReach(webServer));
    }

    @Test
    @DisplayName("Should detect unreachable hosts")
    void testCannotReachUnconnected() {
        Host isolatedHost = new Host("isolated.proggy");
        
        // Web server is not connected to isolated host
        assertFalse(webServer.canReach(isolatedHost));
    }

    @Test
    @DisplayName("Should remove connections")
    void testRemoveConnection() {
        webServer.addConnection(databaseServer);
        assertTrue(webServer.getConnections().contains(databaseServer));
        
        webServer.removeConnection(databaseServer);
        
        assertFalse(webServer.getConnections().contains(databaseServer));
        assertFalse(databaseServer.getConnections().contains(webServer));
    }

    @Test
    @DisplayName("Should handle null connections gracefully")
    void testNullConnection() {
        // Should not throw exception
        assertDoesNotThrow(() -> webServer.addConnection(null));
        assertTrue(webServer.getConnections().isEmpty());
    }

    @Test
    @DisplayName("Should support complex network topology")
    void testComplexTopology() {
        // Router connecting multiple hosts (ProggySolutions topology)
        Host router = new Host("router.proggy");
        Host station1 = new Host("station1.proggy");
        Host station2 = new Host("station2.proggy");
        
        router.addConnection(webServer);
        router.addConnection(databaseServer);
        router.addConnection(station1);
        router.addConnection(station2);
        
        assertEquals(4, router.getConnections().size());
    }

    @Test
    @DisplayName("Should add software to host")
    void testAddSoftware() {
        openske.model.software.Software apache = new openske.model.software.Software(
            "cpe:2.3:a:apache:http_server:2.2:*:*:*:*:*:*:*", webServer);
        
        webServer.addSoftware(apache);
        
        assertTrue(webServer.hasSoftware(apache));
        assertEquals(webServer, apache.getHost());
    }

    @Test
    @DisplayName("Should prevent duplicate software")
    void testNoDuplicateSoftware() {
        openske.model.software.Software apache = new openske.model.software.Software(
            "cpe:2.3:a:apache:http_server:2.2:*:*:*:*:*:*:*", webServer);
        
        webServer.addSoftware(apache);
        webServer.addSoftware(apache);
        
        assertEquals(1, webServer.getSoftwares().size());
    }

    @Test
    @DisplayName("Should compare hosts by address")
    void testHostComparison() {
        Host hostA = new Host("alpha.proggy");
        Host hostB = new Host("beta.proggy");
        
        assertTrue(hostA.compareTo(hostB) < 0);
        assertTrue(hostB.compareTo(hostA) > 0);
        assertEquals(0, hostA.compareTo(new Host("alpha.proggy")));
    }

    @Test
    @DisplayName("Should return address as string representation")
    void testToString() {
        assertEquals("web.proggy", webServer.toString());
    }

    @Test
    @DisplayName("Should provide statistics")
    void testStatistics() {
        webServer.addConnection(databaseServer);
        
        openske.model.software.Software mysql = new openske.model.software.Software(
            "cpe:2.3:a:mysql:mysql:5.0:*:*:*:*:*:*:*", webServer);
        webServer.addSoftware(mysql);
        
        String stats = webServer.statistics();
        assertNotNull(stats);
        assertTrue(stats.contains("web.proggy"));
        assertTrue(stats.contains("connections = 1"));
        assertTrue(stats.contains("software = 1"));
    }
}
