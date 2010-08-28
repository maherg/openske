package com.openske.model.assets.services;

import com.openske.model.assets.Asset;
import com.openske.model.assets.AssetScope;
import com.openske.model.networking.Host;

public class ServiceAsset extends Asset {

	protected Long port;

	public ServiceAsset(String name, Host host, Long port) {
		this(name, host, AssetScope.INTERNET, port);
	}
	
	public ServiceAsset(String name, Host host, AssetScope scope, Long port) {
		super(name, host, scope);
		this.port = port;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}
}
