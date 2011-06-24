package openske.model.security;

import java.util.ArrayList;
import java.util.List;

import openske.model.assets.AssetAccess;
import openske.model.assets.AssetAccessor;
import openske.model.hardware.Host;


public class User implements AssetAccessor {

    protected String fullName;
    protected String email;
    protected Host host;
    protected List<UserAccount> accounts;
    protected List<AssetAccess> assetAccesses;
    protected boolean attacker;

    public User(String fullName, String email, Host host) {
        this(fullName, email, host, false);
    }

    public User(String fullName, String email, Host host, boolean attacker) {
        this.fullName = fullName;
        this.email = email;
        this.host = host;
        this.attacker = attacker;
        this.accounts = new ArrayList<UserAccount>();
        this.assetAccesses = new ArrayList<AssetAccess>();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UserAccount> accounts) {
        this.accounts = accounts;
    }

    public boolean isAttacker() {
        return attacker;
    }

    public void setAttacker(boolean attacker) {
        this.attacker = attacker;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public User addAccount(UserAccount account) {
        if (account != null) {
            if (!this.accounts.contains(account)) {
                this.accounts.add(account);
            }
        }
        return this;
    }

    public User addAssetAccess(AssetAccess access) {
        if (!this.assetAccesses.contains(access)) {
            this.assetAccesses.add(access);
        }
        return this;
    }

    public UserAccount getRecentAccount() {
        if (this.accounts.isEmpty()) {
            return null;
        } else {
            return this.accounts.get(this.accounts.size() - 1);
        }
    }

    public AssetAccess getRecentAssetAccess() {
        if (this.assetAccesses.isEmpty()) {
            return null;
        } else {
            return this.assetAccesses.get(this.assetAccesses.size() - 1);
        }
    }

    public List<AssetAccess> getAssetAccesses() {
        return assetAccesses;
    }

    public void setAssetAccesses(List<AssetAccess> assetAccesses) {
        this.assetAccesses = assetAccesses;
    }
}
