package com.openske.model.software;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.openske.model.assets.Asset;
import com.openske.model.assets.AssetAccess;
import com.openske.model.assets.AssetAccessor;
import com.openske.model.assets.AssetType;
import com.openske.model.hardware.Host;
import com.openske.model.security.SecurityState;
import com.openske.model.security.UserAccount;
import com.openske.model.security.UserGroup;

public class Software implements AssetAccessor {

    protected List<UserAccount> accounts;
    protected boolean anAsset;
    protected List<AssetAccess> assetAccesses;
    protected List<Software> dependencies;
    protected List<UserGroup> groups;
    protected Host host;
    protected String name;
    protected SecurityState securityState;
    protected String vendor;
    protected String version;
    protected List<Vulnerability> vulnerabilities;
    protected List<Weakness> weaknesses;

    public Software(String vendor, String name, String version, Host host) {
        this.vendor = vendor;
        this.name = name;
        this.version = version;
        this.host = host;
        this.vulnerabilities = new ArrayList<Vulnerability>();
        this.weaknesses = new ArrayList<Weakness>();
        this.dependencies = new ArrayList<Software>();
        this.accounts = new ArrayList<UserAccount>();
        this.groups = new ArrayList<UserGroup>();
        this.securityState = SecurityState.UNKNOWN;
    }

    public Software addAccount(String username, String password) {
        UserAccount account = new UserAccount(username, password, this);
        return this.addAccount(account);
    }
    
    public Software addAccount(UserAccount account) {
        if (!this.accounts.contains(account)) {
            this.accounts.add(account);
        }
        return this;
    }

    public Software addVulnerabilities(String... identifiers) {
        if (identifiers != null && identifiers.length > 0) {
            for (String id : identifiers) {
                this.addVulnerability(id);
            }
        }
        return this;

    }

    public Software addVulnerability(String identifier) {
        Vulnerability vuln = new Vulnerability(identifier, this);
        if (vuln != null && !this.hasVulnerability(vuln)) {
            this.vulnerabilities.add(vuln);
            vuln.setSoftware(this);
            this.addWeaknesses(Weakness.forVulnerability(vuln));
        }
        return this;
    }

    public Software addWeaknesses(List<Weakness> weaknesses) {
        if (weaknesses != null) {
            for (Weakness weakness : weaknesses) {
                if (!this.weaknesses.contains(weakness)) {
                    this.weaknesses.add(weakness);
                }
            }
        }
        return this;
    }

    public List<UserAccount> getAccounts() {
        return accounts;
    }

    public List<AssetAccess> getAssetAccesses() {
        return assetAccesses;
    }

    public List<Software> getDependencies() {
        return dependencies;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public Host getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public UserAccount getRandomAccount() {
        if (this.accounts.isEmpty()) {
            return null;
        } else {
            int randomIndex = new Random().nextInt(this.accounts.size());
            return this.accounts.get(randomIndex);
        }
    }

    public Asset getRandomAsset(AssetType type) {
        if (this.assetAccesses.isEmpty()) {
            return null;
        } else {
            List<Asset> matchedAssets = new ArrayList<Asset>();
            for (AssetAccess access : this.assetAccesses) {
                if (access.getAsset().getType() == type) {
                    matchedAssets.add(access.getAsset());
                }
            }
            if (matchedAssets.isEmpty()) {
                return null;
            } else {
                int randomIndex = new Random().nextInt(matchedAssets.size());
                return matchedAssets.get(randomIndex);
            }
        }
    }

    public SecurityState getSecurityState() {
        return securityState;
    }

    public String getVendor() {
        return vendor;
    }

    public String getVersion() {
        return version;
    }

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public List<Weakness> getWeaknesses() {
        return weaknesses;
    }

    public boolean hasVulnerability(Vulnerability vuln) {
        return vuln != null && this.vulnerabilities.contains(vuln);
    }

    public boolean isAnAsset() {
        return anAsset;
    }

    public void setAccounts(List<UserAccount> accounts) {
        this.accounts = accounts;
    }

    public void setAnAsset(boolean anAsset) {
        this.anAsset = anAsset;
    }

    public void setAssetAccesses(List<AssetAccess> assetAccesses) {
        this.assetAccesses = assetAccesses;
    }

    public void setDependencies(List<Software> dependencies) {
        this.dependencies = dependencies;
    }

    public void setGroups(List<UserGroup> groups) {
        this.groups = groups;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecurityState(SecurityState securityState) {
        this.securityState = securityState;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public void setWeaknesses(List<Weakness> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public String toString() {
        return String.format("cpe:/a:%s:%s:%s", this.vendor, this.name, this.version);
    }
}
