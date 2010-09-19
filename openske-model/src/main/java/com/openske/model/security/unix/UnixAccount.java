package com.openske.model.security.unix;

import java.util.List;

import com.openske.model.hardware.Host;
import com.openske.model.security.User;
import com.openske.model.security.UserAccountState;

public class UnixAccount {
    
    protected User user;
    protected Host host;
    protected String username;
    protected String homeDirectory;
    protected List<UnixGroup> groups;
    protected UserAccountState state;
    protected String shell;
    
    public UnixAccount(User user, Host host) {
        super();
        this.user = user;
        this.host = host;
        this.shell = "/bin/bash";
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
    public String getHomeDirectory() {
        return homeDirectory;
    }
    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }
    public List<UnixGroup> getGroups() {
        return groups;
    }
    public void setGroups(List<UnixGroup> groups) {
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
    public String getShell() {
        return shell;
    }
    public void setShell(String shell) {
        this.shell = shell;
    }
}
