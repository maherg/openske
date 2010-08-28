package com.openske.model.assets.data;

import com.openske.model.assets.Asset;
import com.openske.model.hardware.Host;

public class DatabaseAsset extends Asset {

    protected DatabaseAsset(String name, Host host) {
        super(name, host);
        // TODO Auto-generated constructor stub
    }

    protected String databaseName;
//    protected DatabaseServer databaseServer;
//
//    public DatabaseAsset(String name, String databaseName,
//            DatabaseServer databaseServer, AssetScope scope) {
//        super(name, databaseServer.getHost(), scope);
//        this.databaseName = databaseName;
//        this.databaseServer = databaseServer;
//    }
}
