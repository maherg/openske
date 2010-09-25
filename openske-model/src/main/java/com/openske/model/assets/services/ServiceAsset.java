package com.openske.model.assets.services;

import com.openske.model.assets.Asset;
import com.openske.model.assets.AssetScope;
import com.openske.model.hardware.Host;

public class ServiceAsset extends Asset {

    protected int port;

    public ServiceAsset(String name, Host host, int port) {
        this(name, host, port, AssetScope.INTERNET);
    }

    public ServiceAsset(String name, Host host, int port, AssetScope scope) {
        super(name, host, scope);
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
