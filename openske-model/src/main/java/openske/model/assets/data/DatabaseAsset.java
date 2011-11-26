package openske.model.assets.data;

import openske.model.assets.Asset;
import openske.model.assets.AssetType;
import openske.model.hardware.Host;

public class DatabaseAsset extends Asset {

    protected String databaseName;

    public DatabaseAsset(String name, Host host, String databaseName) {
        super(name, host, AssetType.DATABASE);
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
