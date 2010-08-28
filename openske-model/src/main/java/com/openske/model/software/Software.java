package com.openske.model.software;

import com.openske.model.networking.Host;

public class Software {

    protected String name;
    protected String version;
    protected Host host;

    public Software(String name, String version, Host host) {
        this.name = name;
        this.version = version;
        this.host = host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

}
