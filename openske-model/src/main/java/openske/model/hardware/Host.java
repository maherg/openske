package openske.model.hardware;

import java.util.ArrayList;
import java.util.List;

import openske.model.Infrastructure;
import openske.model.InfrastructureItem;
import openske.model.assets.Asset;
import openske.model.software.Software;

public class Host extends InfrastructureItem implements Comparable<Host> {

    protected String address;
    protected List<Host> connections;
    protected List<Software> softwares;
    protected List<Asset> assets;

    public Host(String address) {
        this.address = address;
        this.connections = new ArrayList<Host>();
        this.assets = new ArrayList<Asset>();
        this.softwares = new ArrayList<Software>();
    }
    
    @Override
    public String statistics() {
        return String.format("%s : %s (connections = %d, software = %d)", this.getClass().getSimpleName(), address, connections.size(), softwares.size());
    }
    
    @Override
    public String inspect() {
        return statistics();
    }
    
    @Override
    public int compareTo(Host cnt) {
        return address.compareToIgnoreCase(cnt.getAddress());
    }

    public List<Host> getConnections() {
        return connections;
    }

    public Host addConnection(Host host) {
        if (host != null && !this.connections.contains(host)) {
            this.connections.add(host);
            host.addConnection(this);
        }
        return this;
    }

    public Host removeConnection(Host host) {
        if (host != null && this.connections.contains(host)) {
            this.connections.remove(host);
            host.removeConnection(this);
        }
        return this;
    }

    public void setConnections(List<Host> connections) {
        this.connections = connections;
    }
    
    public boolean canReach(Host target) {
        if(this == target || this.connections.contains(target)) {
            return true;
        }
//        for(Connectable neighbor : this.connections) {
//            if(neighbor.canReach(target)) {
//                return true;
//            }
//        }
        return false;
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
    
    public Host addAsset(Asset asset) {
        if (asset != null && !this.hasAsset(asset)) {
            this.assets.add(asset);
            asset.setHost(this);
        }
        return this;
    }

    private boolean hasAsset(Asset asset) {
        return asset != null && this.assets.contains(asset);
    }

    public boolean hasSoftware(Software software) {
        return this.softwares.contains(software);
    }

    public List<Software> getSoftwares() {
        return softwares;
    }

    public void setSoftwares(List<Software> softwares) {
        this.softwares = softwares;
    }

    public Host addSoftware(Software software) {
        if (!this.hasSoftware(software)) {
            software.setHost(this);
            this.softwares.add(software);
        }
        return this;
    }
}
