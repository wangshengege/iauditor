package org.mylibrary.net;

import org.mylibrary.biz.MyApplication;
import org.mylibrary.common.ACache;
import org.mylibrary.utils.Tools;

/**
 * 网络地址
 */
public class IpConfig {
    /**项目域名*/
    public static final String IP = "http://192.168.0.11:80";
    //public static final String IP="http://www.safety999.com";
    //public static final String PORT = "8787";
    /**项目文件夹*/
    public static final String FOLDER="safetyapp/Index.php";



    /**
     * 设置请求头
     */
    public static void setMyHeader(MyHeader MyHeader) {
        ACache aCache = ACache.get(MyApplication.getContext());
        if (Tools.isEmpty(MyHeader)) {
            aCache.remove("MyHeader");
        } else {
            aCache.put("MyHeader", MyHeader);
        }
    }

    /**
     * 获取请求头
     */
    public static MyHeader getMyHeader() {
        MyHeader myHeader = null;
        ACache aCache = ACache.get(MyApplication.getContext());
        myHeader = (MyHeader) aCache.getAsObject("MyHeader");
        return myHeader;
    }

    /**
     * 清除请求头
     */
    public static void cleanMyHeader() {
        MyHeader MyHeader = getMyHeader();
        if (MyHeader != null) {
            MyHeader.setToken("");
            MyHeader.setUserId("");
            setMyHeader(MyHeader);
        }
    }
}
