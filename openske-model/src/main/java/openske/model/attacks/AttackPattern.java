package openske.model.attacks;

import openske.model.measurablesecurity.CapecEntry;
import openske.model.software.Weakness;

public class AttackPattern implements CapecEntry {

    protected String capecId;
    protected String description;
    protected Weakness weakness;
    protected AttackPatternState state;
    
    public static AttackPattern forName(String capecId) {
        return null;
    }

    public AttackPattern(String capecId, Weakness weakness) {
        this.capecId = capecId;
        this.weakness = weakness;
        this.state = AttackPatternState.READY;
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
    
    public String capecId() {
        return this.capecId;
    }

}
