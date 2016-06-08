package org.mylibrary.net;

import java.io.Serializable;

/**
 * Created by ws on 2015/11/28.
 */
public class MyHeader implements Serializable{
    /**用户id*/
    private String userId;
    /**识别码*/
    private String token;
    /**版本名称*/
    private String versionName;
    /**版本号*/
    private String versionCode;
    /**系统类型*/
    private String phoneSysType;
    /**手机串号*/
    private String phoneID;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getPhoneSysType() {
        return phoneSysType;
    }

    public void setPhoneSysType(String phoneSysType) {
        this.phoneSysType = phoneSysType;
    }

    public String getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(String phoneID) {
        this.phoneID = phoneID;
    }
}
