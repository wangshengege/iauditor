package com.jointeach.iauditor.entity;

import org.mylibrary.biz.BaseEntity;

/**
 * 作者: ws
 * 日期: 2016/5/30.
 * 介绍：信息条目
 */
public class InfoEntity extends BaseEntity {
    private String title;
    private String subTitle;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public InfoEntity() {
    }

    public InfoEntity(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public InfoEntity(String title, String subTitle,int type) {
        this.title = title;
        this.subTitle = subTitle;
        this.type=type;
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

}
