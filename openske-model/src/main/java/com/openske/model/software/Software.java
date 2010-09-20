package com.openske.model.software;

import java.util.ArrayList;
import java.util.List;

import com.openske.model.hardware.Host;
import com.openske.model.security.SecurityState;

public class Software {

    protected String name;
    protected String version;
    protected Host host;
    protected List<Software> dependencies;
    protected List<Vulnerability> vulnerabilities;
    protected SecurityState securityState;

    public Software(String name, String version, Host host) {
        this.name = name;
        this.version = version;
        this.host = host;
        this.vulnerabilities = new ArrayList<Vulnerability>();
        this.dependencies = new ArrayList<Software>();
        this.securityState = SecurityState.SAFE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public SecurityState getSecurityState() {
        return securityState;
    }

    public void setSecurityState(SecurityState securityState) {
        this.securityState = securityState;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public void addVulnerability(Vulnerability vuln) {
        if (vuln != null && !this.vulnerabilities.contains(vuln)) {
            this.vulnerabilities.add(vuln);
            vuln.setSoftware(this);
        }
    }

    public List<Software> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Software> dependencies) {
        this.dependencies = dependencies;
    }
}
