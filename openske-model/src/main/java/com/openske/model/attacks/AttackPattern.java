package com.openske.model.attacks;

import com.openske.model.software.Weakness;

public class AttackPattern {

    protected String identifier;
    protected String description;
    protected Weakness weakness;
    protected AttackPatternState state;
    
    public static AttackPattern forName(String capecId) {
        return null;
    }

    public AttackPattern(String identifier, Weakness weakness) {
        this.identifier = identifier;
        this.weakness = weakness;
        this.state = AttackPatternState.READY;
        // TODO : Collect additional information from the CAPEC data-set
    }

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

    public AttackPatternState getState() {
        return state;
    }

    public void setState(AttackPatternState state) {
        this.state = state;
    }

    public Weakness getWeakness() {
        return weakness;
    }

    public void setWeakness(Weakness weakness) {
        this.weakness = weakness;
    }

}
