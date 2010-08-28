package com.openske.model.assets.data;

import com.openske.model.assets.Asset;
import com.openske.model.assets.AssetScope;
import com.openske.model.software.servers.database.DatabaseServer;

public class DatabaseAsset extends Asset {
	
	protected String databaseName;
	protected DatabaseServer databaseServer;

	public DatabaseAsset(String name, String databaseName, DatabaseServer databaseServer, AssetScope scope) {
		super(name, databaseServer.getHost(), scope);
		this.databaseName = databaseName;
		this.databaseServer = databaseServer;
	}
}
