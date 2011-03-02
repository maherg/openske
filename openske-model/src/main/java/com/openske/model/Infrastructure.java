package com.openske.model;

import java.util.ArrayList;
import java.util.List;

import com.openske.model.assets.Asset;
import com.openske.model.hardware.Connectable;
import com.openske.model.security.User;

public class Infrastructure {

    protected List<Connectable> connectables = new ArrayList<Connectable>();
    
    protected List<User> users = new ArrayList<User>();
    
    protected List<Asset> assets = new ArrayList<Asset>();

    public List<Connectable> getConnectables() {
        return connectables;
    }

    public void setConnectables(List<Connectable> connectables) {
        this.connectables = connectables;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
