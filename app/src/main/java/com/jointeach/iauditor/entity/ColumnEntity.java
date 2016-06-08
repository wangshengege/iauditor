package com.jointeach.iauditor.entity;

import org.mylibrary.biz.BaseEntity;
import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：模版字段
 */
public class ColumnEntity extends DbBaseEntity {
    private String title;
    private int type;
    private int pId;
    private int mId;
    //1是群组2不是群组
    private int isGroup;

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
