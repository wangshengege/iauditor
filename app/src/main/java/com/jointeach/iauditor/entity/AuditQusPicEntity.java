package com.jointeach.iauditor.entity;

import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/6/15.
 * 介绍：问题的照片路径
 */
public class AuditQusPicEntity extends DbBaseEntity {
    private String imgPath;
    private int qusId;
    private int gId;
    private int mId;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getQusId() {
        return qusId;
    }

    public void setQusId(int qusId) {
        this.qusId = qusId;
    }

    public int getgId() {
        return gId;
    }

    public void setgId(int gId) {
        this.gId = gId;
    }
}
