package com.jointeach.iauditor.entity;

import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计标题封装类
 */
public class AuditGroupEntity extends DbBaseEntity{
    private int mouldId;//模版id
    private String title;

    public int getMouldId() {
        return mouldId;
    }

    public void setMouldId(int mouldId) {
        this.mouldId = mouldId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
