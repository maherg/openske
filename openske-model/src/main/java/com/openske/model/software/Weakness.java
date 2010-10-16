package com.openske.model.software;

import java.util.ArrayList;
import java.util.List;

import com.openske.model.datasets.CWE;

public class Weakness {

    protected String identifier;
    protected Vulnerability vulnerability;
    protected Software software;

    public static List<Weakness> forVulnerability(Vulnerability vuln) {
        List<Weakness> weaknesses = new ArrayList<Weakness>();
        List<String> cweIds = CWE.findByVulnerability(vuln.getIdentifier());
        for(String cweId : cweIds) {
            weaknesses.add(new Weakness(cweId, vuln));
        }
        return weaknesses;
    }
    
    public Weakness(String identifier, Vulnerability vuln) {
        this.identifier = identifier;
        this.vulnerability = vuln;
        this.software = vuln.getSoftware();
        this.software.addWeakness(this);
    }
    
    public Weakness(String identifier, Software software) {
        this.identifier = identifier;
        this.software = software;
        this.software.addWeakness(this);
        this.vulnerability = null;
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

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }
}
