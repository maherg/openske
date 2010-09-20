package com.openske.model.software;

import java.util.List;

public class Weakness {
    
    protected String identifier;
    protected Vulnerability vulnerability;
    
    public static List<Weakness> forVulnerability(Vulnerability vuln) {
        return null;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }
}
