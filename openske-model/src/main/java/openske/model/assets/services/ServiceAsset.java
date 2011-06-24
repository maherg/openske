package openske.model.assets.services;

import openske.model.assets.Asset;
import openske.model.assets.AssetType;
import openske.model.software.Software;

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
