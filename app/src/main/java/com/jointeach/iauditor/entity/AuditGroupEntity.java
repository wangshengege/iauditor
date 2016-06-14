package com.jointeach.iauditor.entity;

import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计标题封装类
 */
public class AuditGroupEntity extends QusBaseEntity implements Cloneable{
    private int mouldId;//模版id
    private int type;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMouldId() {
        return mouldId;
    }

    public void setMouldId(int mouldId) {
        this.mouldId = mouldId;
    }

    @Override
    public Object clone() {
        AuditGroupEntity auditGroupEntity=null;
        try {
             auditGroupEntity= (AuditGroupEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return auditGroupEntity;
    }
}
