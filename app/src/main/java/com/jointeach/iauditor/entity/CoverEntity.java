package com.jointeach.iauditor.entity;

import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：封面条目
 */
public class CoverEntity extends DbBaseEntity implements Cloneable{
    private String title;
    //1是创建日期，2是作者，3是地点，4是参与者
    private int type;
    private int mId;
    private String value;
    //0为模版1为审计
    private int isAudit;

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }
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

    @Override
    public Object clone()  {
        CoverEntity coverEntity=null;
        try {
            coverEntity= (CoverEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return coverEntity;
    }
}
