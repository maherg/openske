package com.openske.model.software;

import java.util.ArrayList;
import java.util.List;

import com.openske.model.hardware.Host;
import com.openske.model.security.SecurityState;

public class Software {

    protected List<Software> dependencies;
    protected Host host;
    protected String name;
    protected SecurityState securityState;
    protected String version;
    protected List<Vulnerability> vulnerabilities;
    protected SoftwareScope scope;
    
    
    public Software(String name, String version) {
        this(name, version, null);
    }

    public Software(String name, String version, Host host) {
        this.name = name;
        this.version = version;
        this.host = host;
        this.vulnerabilities = new ArrayList<Vulnerability>();
        this.dependencies = new ArrayList<Software>();
        this.securityState = SecurityState.UNKNOWN;
        this.scope = SoftwareScope.LOCAL;
    }

    public Software addVulnerability(String identifier) {
        Vulnerability vuln = new Vulnerability(identifier, this);
        if (vuln != null && !this.hasVulnerability(vuln)) {
            this.vulnerabilities.add(vuln);
            vuln.setSoftware(this);
        }
        return this;
    }
    
    public Software addVulnerabilities(String... identifiers) {
        if(identifiers != null && identifiers.length > 0) {
            for(String id : identifiers) {
                this.addVulnerability(id);
            }
        }
        return this;
        
    }

    public List<Software> getDependencies() {
        return dependencies;
    }

    public Host getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public SecurityState getSecurityState() {
        return securityState;
    }

    public String getVersion() {
        return version;
    }

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public boolean hasVulnerability(Vulnerability vuln) {
        return vuln != null && this.vulnerabilities.contains(vuln);
    }

    public void setDependencies(List<Software> dependencies) {
        this.dependencies = dependencies;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecurityState(SecurityState securityState) {
        this.securityState = securityState;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public SoftwareScope getScope() {
        return scope;
    }

    public void setScope(SoftwareScope scope) {
        this.scope = scope;
    }
}
