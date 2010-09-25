package com.openske.model.hardware;

import java.util.ArrayList;
import java.util.List;

import com.openske.model.assets.Asset;
import com.openske.model.software.Software;

public class Host extends Connectable {

    String identifier;

    protected List<Software> softwares;
    protected List<Asset> assets;

    public Host(String address) {
        super(address);
        this.assets = new ArrayList<Asset>();
        // TODO : Pull the softwares into Connectable since we may 
        // have other hardware devices which we would like to describe
        // their installed software packages
        this.softwares = new ArrayList<Software>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public Host addSoftware(String name, String version) {
        return this.addSoftware(new Software(name, version, this));
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
