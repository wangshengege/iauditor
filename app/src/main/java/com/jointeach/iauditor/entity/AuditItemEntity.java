package com.jointeach.iauditor.entity;

import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计条目
 */
public class AuditItemEntity extends QusBaseEntity implements Cloneable {
    private int type;//审计类型

    //0是未操作，1是是，2是否，3是不适用
    private int state;//审计状态信息
    private int mId;
    private int gId;
    private String describe="";

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getgId() {
        return gId;
    }

    public void setgId(int gId) {
        this.gId = gId;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public int getState() {
        return state;
    }
    /**0是未操作，1是是，2是否，3是不适用*/
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public Object clone()  {
        AuditItemEntity itemEntity=null;
        try {
            itemEntity= (AuditItemEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return itemEntity;
    }
}
