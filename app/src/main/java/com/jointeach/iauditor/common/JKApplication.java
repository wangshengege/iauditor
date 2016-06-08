package com.jointeach.iauditor.common;

import com.jointeach.iauditor.MainActivity;

import org.mylibrary.biz.CrashHandler;
import org.mylibrary.biz.MyApplication;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：
 */
public class JKApplication extends MyApplication {
    private static JKApplication ourInstance;
    public static JKApplication getInstance() {
        if(ourInstance==null){
            ourInstance=new JKApplication();
        }
        return ourInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ctx=getApplicationContext();
        CrashHandler.getInstance().init(ctx, MainActivity.class);
    }
}
