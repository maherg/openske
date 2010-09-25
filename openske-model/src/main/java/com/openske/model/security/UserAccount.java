package com.openske.model.security;

import java.util.List;

import com.openske.model.assets.Asset;
import com.openske.model.hardware.Host;

public class UserAccount {

    protected User user;
    protected Host host;
    protected String username;
    protected List<UserGroup> groups;
    protected UserAccountState state;
    protected List<Asset> assets;

    public UserAccount(User user, Host host) {
        super();
        this.user = user;
        this.host = host;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroup> groups) {
        this.groups = groups;
    }

    public UserAccountState getState() {
        return state;
    }

    public void setState(UserAccountState state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
