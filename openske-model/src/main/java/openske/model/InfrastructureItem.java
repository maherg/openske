package openske.model;

import openske.model.security.SecurityState;

public abstract class InfrastructureItem {
    
    protected Infrastructure infrastructure = null;
    
    protected SecurityState securityState;
    
    public abstract String statistics();
    
    public abstract String inspect();
    
}
