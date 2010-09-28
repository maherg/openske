package com.openske.model.assets.services;

import com.openske.model.assets.Asset;
import com.openske.model.assets.AssetType;
import com.openske.model.software.Software;

public class ServiceAsset extends Asset {

    protected Software software;

    public ServiceAsset(String name, Software software) {
        super(name, software.getHost(), AssetType.SERVICE);
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }
}
