package com.jointeach.iauditor.entity;

/**
 * 作者: ws
 * 日期: 2016/6/12.
 * 介绍：iauditor
 */
public class UpdataBack {
    private int mId;
    public boolean isAudit;

    public UpdataBack(int mId) {
        this.mId = mId;
    }

    public UpdataBack(boolean isAudit) {
        this.isAudit = isAudit;
    }

    public UpdataBack() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
