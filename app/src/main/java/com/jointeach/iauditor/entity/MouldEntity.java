package com.jointeach.iauditor.entity;

import org.mylibrary.biz.BaseEntity;
import org.mylibrary.biz.DbBaseEntity;

/**
 * 作者: ws
 * 日期: 2016/5/26.
 * 介绍：
 */
public class MouldEntity extends DbBaseEntity implements Cloneable{
    //模版图标
    private String icPath="";
    //标题
    private String title="";
    //描述
    private String describe="";
    //最后修改时间
    private long lastRevise;
    //状态0是不显示，1是未完成，2是已完成
    private int state;
    //0为模版1为审计
    private int type=0;
    private String industry="-";
    private String cIndustry="-";
    private long createTime;
    private String author="-";
    private int score;
    private String location="";
    private String report;
    private String participants="-";

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getcIndustry() {
        return cIndustry;
    }

    public void setcIndustry(String cIndustry) {
        this.cIndustry = cIndustry;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public void setState(int state) {
        this.state = state;
    }

    public String getIcPath() {
        return icPath;
    }

    public void setIcPath(String icPath) {
        this.icPath = icPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public long getLastRevise() {
        return lastRevise;
    }

    public void setLastRevise(long lastRevise) {
        this.lastRevise = lastRevise;
    }

    @Override
    public Object clone() {
        MouldEntity stu = null;
        try{
            stu = (MouldEntity)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
