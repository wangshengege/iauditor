package com.jointeach.iauditor.entity;

import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计条目
 */
public class AuditItemEntity extends DbBaseEntity {
    private int type;//审计类型
    private String title;
    private int state;//审计状态信息

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
