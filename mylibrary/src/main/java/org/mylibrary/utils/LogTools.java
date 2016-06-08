package org.mylibrary.utils;

import com.orhanobut.logger.Logger;


/**
 * 打印log相关类
 */
public class LogTools {
    private boolean isDeubg=true;
    private static LogTools instance;

    private static LogTools getInstance() {
        if (instance == null) {
            instance = new LogTools();
        }
        return instance;
    }

    static {
        getInstance().isDeubg = true;
    }

    public static void showLogi(String msg) {
        if(getInstance().isDeubg){
            Logger.i( msg);
        }
    }

    public static void showLogi(String tag, String msg) {
        if(getInstance().isDeubg){
        Logger.i(tag, msg);
        }
    }

    public static void showLogw(String msg) {
        if(getInstance().isDeubg){
        Logger.w( msg);
        }
    }

    public static void showLoge(String msg) {
        if(getInstance().isDeubg){
        Logger.e(msg);}
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void logi(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Logger.i(cls.getClass().getSimpleName(), msg);
        }
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void logw(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Logger.w(cls.getClass().getSimpleName(), msg);
        }
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void logd(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Logger.d(cls.getClass().getSimpleName(), msg);
        }
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void loge(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Logger.e(cls.getClass().getSimpleName(), msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void e(String tag, String msg) {
        if(getInstance().isDeubg) {
            Logger.e(tag,msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void i(String tag, String msg) {
        if(getInstance().isDeubg) {
            Logger.i(tag,msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void v(String tag, String msg) {
        if(getInstance().isDeubg) {
            Logger.v(tag,msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void d(String tag, String msg) {
        if(getInstance().isDeubg) {
            Logger.d(tag,msg);
        }
    }
}
