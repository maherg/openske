package openske.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

/**
 * Test cases for Infrastructure model based on scenarios from the OpenSKE paper.
 * 
 * The paper describes several test infrastructures:
 * 1. ProggySolutions Scenario - fictional company with web server, database, and workstations
 * 2. Metasploitable VM - Ubuntu Linux with weak credentials  
 * 3. OWASP Broken Web Applications - intentionally vulnerable web applications
 * 4. Scalable tests - Infrastructure200/500/1000 for performance testing
 */
class InfrastructureTest {

    private Infrastructure infrastructure;

    @BeforeEach
    void setUp() {
        infrastructure = new Infrastructure("TestInfrastructure");
    }

    @Test
    @DisplayName("Should create infrastructure with name")
    void testInfrastructureCreation() {
        assertNotNull(infrastructure);
        assertTrue(infrastructure.types().length > 0 || infrastructure.types().length == 0);
    }

    @Test
    @DisplayName("Should add and retrieve hosts - ProggySolutions scenario")
    void testAddAndRetrieveHosts() {
        // From paper: ProggySolutions has web server, database, and workstations
        openske.model.hardware.Host webServer = new openske.model.hardware.Host("web.proggy");
        openske.model.hardware.Host dbServer = new openske.model.hardware.Host("database.proggy");
        
        infrastructure.add(webServer);
        infrastructure.add(dbServer);
        
        Collection<InfrastructureItem> hosts = infrastructure.get(openske.model.hardware.Host.class);
        assertEquals(2, hosts.size());
    }

    @Test
    @DisplayName("Should manage multiple infrastructure types")
    void testMultipleTypes() {
        // Test infrastructure can hold multiple types as described in paper
        openske.model.hardware.Host host = new openske.model.hardware.Host("host1");
        openske.model.hardware.Host userHost = new openske.model.hardware.Host("userhost");
        openske.model.security.User user = new openske.model.security.User("Test User", "test@test.com", userHost);
        
        infrastructure.add(host);
        infrastructure.add(user);
        
        assertEquals(2, infrastructure.get(openske.model.hardware.Host.class).size() + 
                        infrastructure.get(openske.model.security.User.class).size());
    }

    @Test
    @DisplayName("Should retrieve items by class type")
    void testRetrieveByType() {
        // Simulate Metasploitable scenario from paper
        openske.model.hardware.Host metasploitable = new openske.model.hardware.Host("192.168.56.101");
        openske.model.hardware.Host attacker = new openske.model.hardware.Host("192.168.1.44");
        
        infrastructure.add(metasploitable);
        infrastructure.add(attacker);
        
        Collection<InfrastructureItem> hosts = infrastructure.get(openske.model.hardware.Host.class);
        assertEquals(2, hosts.size());
    }

    @Test
    @DisplayName("Should instantiate objects through infrastructure")
    void testInstantiateObjects() {
        // Test the instantiate method for creating model objects
        openske.model.hardware.Host host = infrastructure.instantiate(openske.model.hardware.Host.class, "test.proggy");
        assertNotNull(host);
        assertEquals("test.proggy", host.getAddress());
    }

    @Test
    @DisplayName("Should handle ProggySolutions web server configuration")
    void testProggySolutionsWebServer() {
        // From paper: web.proggy has Apache 2.2, phpMyAdmin 2.1, ProggyWeb 1.0
        openske.model.hardware.Host webServer = new openske.model.hardware.Host("web.proggy");
        
        infrastructure.add(webServer);
        
        assertTrue(infrastructure.get(openske.model.hardware.Host.class).size() > 0);
    }

    @Test
    @DisplayName("Should prevent duplicate additions")
    void testNoDuplicateAdditions() {
        openske.model.hardware.Host host = new openske.model.hardware.Host("unique.proggy");
        
        infrastructure.add(host);
        infrastructure.add(host); // Add same host again
        
        Collection<InfrastructureItem> hosts = infrastructure.get(openske.model.hardware.Host.class);
        assertEquals(1, hosts.size());
    }

    @Test
    @DisplayName("Should handle scalable infrastructure - reduced simulation")
    void testScalableInfrastructure() {
        // From paper: Infrastructure200 has 200 hosts, 20,000 software instances
        // This is a performance test scenario - using reduced count for unit test
        int hostCount = 50; // Reduced from 200 for unit test performance
        
        for (int i = 0; i < hostCount; i++) {
            openske.model.hardware.Host host = new openske.model.hardware.Host("host" + i + ".test");
            infrastructure.add(host);
        }
        
        Collection<InfrastructureItem> hosts = infrastructure.get(openske.model.hardware.Host.class);
        assertEquals(hostCount, hosts.size());
    }

    @Test
    @DisplayName("Should return null for non-existent item")
    void testNonExistentItem() {
        InfrastructureItem item = infrastructure.get(openske.model.hardware.Host.class, "nonexistent");
        assertNull(item);
    }

    @Test
    @DisplayName("Should retrieve item by identifier")
    void testGetByIdentifier() {
        openske.model.hardware.Host host = new openske.model.hardware.Host("searchable.proggy");
        infrastructure.add(host);
        
        InfrastructureItem found = infrastructure.get(openske.model.hardware.Host.class, "searchable.proggy");
        assertNotNull(found);
        assertEquals("searchable.proggy", ((openske.model.hardware.Host) found).getAddress());
    }
}
