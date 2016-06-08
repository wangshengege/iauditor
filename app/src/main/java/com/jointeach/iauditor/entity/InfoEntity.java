package com.jointeach.iauditor.entity;

import org.mylibrary.biz.BaseEntity;

/**
 * 作者: ws
 * 日期: 2016/5/30.
 * 介绍：
 */
public class InfoEntity extends BaseEntity {
    private String title;
    private String subTitle;
    private int state;
    private int pid;//模版id
    public InfoEntity() {
    }

    public InfoEntity(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
