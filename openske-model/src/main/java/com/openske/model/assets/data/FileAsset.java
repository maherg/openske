package com.openske.model.assets.data;

import com.openske.model.assets.Asset;
import com.openske.model.assets.AssetScope;
import com.openske.model.networking.Host;

public class FileAsset extends Asset {

	protected String filepath;

	public FileAsset(String name, Host host, String filepath) {
		this(name, host, AssetScope.INTERNET, filepath);
	}
	
	public FileAsset(String name, Host host, AssetScope scope, String filepath) {
		super(name, host, scope);
		this.filepath = filepath;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
