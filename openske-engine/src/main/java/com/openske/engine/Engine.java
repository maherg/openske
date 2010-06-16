package com.openske.engine;

import java.util.HashMap;

import com.openske.drools.DroolsFacade;
import com.openske.world.networking.Network;

/**
 * OpenSKE's Engine
 *
 */
public class Engine
{
	protected EngineConfiguration configured;
	protected DroolsFacade drools;
	protected HashMap<String,Network> networks;
	
	public Engine(EngineConfiguration configuration) {
		
	}
	
	public void configure(EngineConfiguration configuration) {
		
	}
	
	public Network addNetwork(String name) {
		
	}
	
	public boolean networkExists(String name) {
		return networks.containsKey(name);
	}
}
