package com.openske.model.attacks;

import java.util.ArrayList;
import java.util.List;

import com.openske.model.security.UserAccount;

public class Attacker {

    protected String nickname;
    protected List<UserAccount> accounts;

    public Attacker(String nickname) {
        this.nickname = nickname;
        this.accounts = new ArrayList<UserAccount>();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<UserAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UserAccount> accounts) {
        this.accounts = accounts;
    }
}
