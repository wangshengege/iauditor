package com.jointeach.iauditor.common;

import com.jointeach.iauditor.entity.MouldEntity;

import org.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/6/15.
 * 介绍：iauditor
 */
public class CoverTypeHelper {
    public static  String getTitleByType(int type, MouldEntity mould){
        String str=null;
        switch (type){//1是创建日期，2是作者，3是地点，4是参与者
            case 1:
                str= Tools.getFormatTime(mould.getCreateTime(),"yyyy/MM/dd HH:mm");
                break;
            case 2:
                str=mould.getAuthor();
                break;
            case 3:
                str=mould.getLocation();
                break;
            case 4:
                str=mould.getParticipants();
                break;
        }
        return str;
    }
}
