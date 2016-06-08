package com.jointeach.iauditor.entity;

import org.mylibrary.biz.BaseEntity;
import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：封面条目
 */
public class CoverEntity extends DbBaseEntity {
    private String title;
    private int type;
    private int mId;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
