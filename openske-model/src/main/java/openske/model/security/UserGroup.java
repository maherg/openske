package openske.model.security;

import java.util.List;

import openske.model.assets.AssetAccessor;
import openske.model.software.Software;


public class UserGroup implements AssetAccessor {

    protected String name;
    protected Software software;
    protected List<UserAccount> accounts;

    public UserGroup(Software software) {
        this.software = software;
    }

    public List<UserAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UserAccount> accounts) {
        this.accounts = accounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
