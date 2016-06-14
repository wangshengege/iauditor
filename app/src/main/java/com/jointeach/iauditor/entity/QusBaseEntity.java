package com.jointeach.iauditor.entity;

import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/6/13.
 * 介绍：iauditor
 */
public class QusBaseEntity extends DbBaseEntity {
    private String title;
    //0为模版1为审计
    private int isAudit;

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
