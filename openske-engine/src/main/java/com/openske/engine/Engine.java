package com.openske.engine;

import java.util.HashMap;

import com.openske.drools.DroolsFacade;
import com.openske.model.networking.Network;

/**
 * OpenSKE's Engine
 * 
 */
public class Engine {
    protected EngineConfiguration configured;
    protected DroolsFacade drools;
    protected HashMap<String, Network> networks;

    public Engine(EngineConfiguration configuration) {
        networks = new HashMap<String, Network>();
    }

    public void configure(EngineConfiguration configuration) {

    }

    public Network addNetwork(String name) {
        if (name != null && !name.equals("") && !networkExists(name)) {
            Network network = new Network();
            networks.put(name, network);
            return network;
        } else {
            return null;
        }
    }

    public boolean networkExists(String name) {
        return networks.containsKey(name);
    }
}
