package openske.model.assets;

import openske.model.InfrastructureItem;
import openske.model.hardware.Host;
import openske.model.security.SecurityState;

public abstract class Asset extends InfrastructureItem {

    protected String name;
    protected Host host;
    protected AssetType type;

    protected Asset(String name, Host host, AssetType type) {
        this.name = name;
        this.host = host;
        this.type = type;
    }
    
    @Override
    public String statistics() {
        return String.format("%s : %s", this.getClass().getSimpleName(), name);
    }
    
    @Override
    public String inspect() {
        return statistics();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public SecurityState getSecurityState() {
        return securityState;
    }

    public void setSecurityState(SecurityState securityState) {
        this.securityState = securityState;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }
}
