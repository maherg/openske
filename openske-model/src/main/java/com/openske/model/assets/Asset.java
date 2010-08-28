package com.openske.model.assets;

import com.openske.model.hardware.Host;
import com.openske.model.policy.PolicyState;

public class Asset {

    protected String name;
    protected AssetScope scope;
    protected Host host;
    protected PolicyState policyState;

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

    public PolicyState getPolicyState() {
        return policyState;
    }

    public void setPolicyState(PolicyState policyState) {
        this.policyState = policyState;
    }
}
