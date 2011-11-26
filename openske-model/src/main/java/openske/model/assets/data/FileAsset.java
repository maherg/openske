package openske.model.assets.data;

import openske.model.assets.Asset;
import openske.model.assets.AssetType;
import openske.model.hardware.Host;

public class FileAsset extends Asset {

    protected String filepath;

    public FileAsset(String name, Host host, String filepath) {
        super(name, host, AssetType.FILE);
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
