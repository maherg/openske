package com.openske.model.security.unix;

import java.util.List;

import com.openske.model.hardware.Host;

public class UnixGroup {

    protected String name;
    protected Host host;
    protected List<UnixAccount> accounts;
    
    public UnixGroup(Host host) {
        this.host = host;
    }

    public List<UnixAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UnixAccount> accounts) {
        this.accounts = accounts;
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
