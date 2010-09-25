package com.openske.model.hardware;

import java.util.ArrayList;
import java.util.List;

public abstract class Connectable {

    protected String address;
    
    protected List<Connectable> connections;

    protected Connectable(String address) {
        this.address = address;
        this.connections = new ArrayList<Connectable>();
    }

    public List<Connectable> getConnections() {
        return connections;
    }

    public Connectable addConnection(Connectable connectable) {
        if (connectable != null && !this.connections.contains(connectable)) {
            this.connections.add(connectable);
            connectable.addConnection(this);
        }
        return this;
    }

    public Connectable removeConnection(Connectable connectable) {
        if (connectable != null && this.connections.contains(connectable)) {
            this.connections.remove(connectable);
            connectable.removeConnection(this);
        }
        return this;
    }

    public void setConnections(List<Connectable> connections) {
        this.connections = connections;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return address;
    }
}
