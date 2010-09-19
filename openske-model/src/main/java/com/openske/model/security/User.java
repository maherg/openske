package com.openske.model.security;

import java.util.List;

import com.openske.model.security.unix.UnixAccount;

public class User {
    
    protected String fullName;
    protected String email;
    protected List<UnixAccount> accounts;
    
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
    public List<UnixAccount> getAccounts() {
        return accounts;
    }
    public void setAccounts(List<UnixAccount> accounts) {
        this.accounts = accounts;
    }
}
