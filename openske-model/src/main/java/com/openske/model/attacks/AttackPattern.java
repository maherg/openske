package com.openske.model.attacks;

import java.util.List;

import com.openske.model.software.Vulnerability;
import com.openske.model.software.Weakness;

public class AttackPattern {

    protected String identifier;
    protected String description;
    protected List<Weakness> weaknesses;
    protected List<Vulnerability> vulnerabilities;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Weakness> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<Weakness> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

}
