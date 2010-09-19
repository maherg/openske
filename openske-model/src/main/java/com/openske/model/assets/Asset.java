package com.openske.model.assets;

import com.openske.model.hardware.Host;
import com.openske.model.security.SecurityState;

public class Asset {

    protected String name;
    protected AssetScope scope;
    protected Host host;
    protected SecurityState securityState;

    protected Asset(String name, Host host) {
        this(name, host, AssetScope.INTERNET);
    }

    protected Asset(String name, Host host, AssetScope scope) {
        this.name = name;
        this.host = host;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetScope getScope() {
        return scope;
    }

    public void setScope(AssetScope scope) {
        this.scope = scope;
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
}
