package com.openske.model.hardware;

import java.util.List;

import com.openske.model.software.Software;

public class Host extends Connectable {
    
    String identifier;

    protected List<Software> softwares;

    public Host(String address) {
        super(address);
    }

    public List<Software> getSoftwares() {
        return softwares;
    }

    public void setSoftwares(List<Software> softwares) {
        this.softwares = softwares;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
