package openske.model.security;

import java.util.List;

import openske.model.Infrastructure;
import openske.model.InfrastructureItem;
import openske.model.assets.AssetAccessor;
import openske.model.software.Software;


public class UserGroup extends InfrastructureItem implements AssetAccessor {

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

    @Override
    public String statistics() {
        return String.format("%s : %s", getClass().getSimpleName(), name);
    }
    
    @Override
    public String inspect() {
        return statistics();
    }
}
