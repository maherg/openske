package com.openske.model.security;

import java.util.List;


public class User {
    
    protected String fullName;
    protected String email;
    protected List<UserAccount> accounts;
    
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
}
