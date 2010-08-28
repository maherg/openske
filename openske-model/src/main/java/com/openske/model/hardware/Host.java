package com.openske.model.hardware;

import java.util.List;

public class Host {

    protected String address = "";
    protected List<Host> connections;
    
    public Host(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Host> getConnections() {
        return connections;
    }

    public void setConnections(List<Host> connections) {
        this.connections = connections;
    }
    
    public void addConnection(Host host) {
        if(host != null && ! this.connections.contains(host)) {
            this.connections.add(host);
            host.addConnection(this);
        }
    }
}