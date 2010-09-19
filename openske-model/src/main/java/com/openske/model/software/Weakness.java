package com.openske.model.software;

import java.util.List;

public class Weakness {
    
    protected String identifier;
    protected List<Vulnerability> vulnerabilities;
    
    public static List<Weakness> forVulnerability(Vulnerability vuln) {
        return null;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }
}
