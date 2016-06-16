package com.jointeach.iauditor.entity;

import java.util.ArrayList;

/**
 * 作者: ws
 * 日期: 2016/6/16.
 * 介绍：iauditor
 */
public class SelectAccontBack {
    private ArrayList<String> accounts;
    private String account;
    private boolean isShare;
    public SelectAccontBack() {
    }

    public boolean isShare() {
        return isShare;
    }

    public SelectAccontBack(boolean isShare) {
        this.isShare = isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public SelectAccontBack(ArrayList<String> accounts) {
        this.accounts = accounts;
    }

    public SelectAccontBack(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ArrayList<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<String> accounts) {
        this.accounts = accounts;
    }
}
