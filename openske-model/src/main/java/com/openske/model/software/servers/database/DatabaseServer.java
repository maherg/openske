package com.openske.model.software.servers.database;

import com.openske.model.networking.Host;
import com.openske.model.software.Software;

public class DatabaseServer extends Software {
	
	protected Long port;

	public DatabaseServer(String name, String version, Host host) {
		super(name, version, host);
	}
	
}