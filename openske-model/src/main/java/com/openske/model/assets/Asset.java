package com.openske.model.assets;

import com.openske.model.Element;
import com.openske.model.networking.Host;

public class Asset extends Element {

	protected String name;
	protected AssetScope scope;
	protected Host host;

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
}
