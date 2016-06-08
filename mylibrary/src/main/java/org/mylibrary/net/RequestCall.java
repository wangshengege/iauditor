package org.mylibrary.net;


import org.json.JSONObject;

import org.mylibrary.utils.Tools;

/**
 * 请求参数封装
 */
public class RequestCall {
    private String url;
    private int requestCode = -1;
    private int state = -1;
    private JSONObject json;
    private String msg;
    private boolean isAnalytical=true;//是否解析

    /**
     * 无参构造函数
     */
    public RequestCall() {

    }

    /**
     * 构造函数
     *
     * @param url 请求码
     */
    public RequestCall(String url) {
        this.url = url;
    }

    public boolean isAnalytical() {
        return isAnalytical;
    }
    public void setIsAnalytical(boolean isAnalytical) {
        this.isAnalytical = isAnalytical;
    }

    /**
     * 构造函数
     *
     * @param url
     * @param requestCode 请求码
     */
    public RequestCall(String url, int requestCode) {
        this.url = url;
        this.requestCode = requestCode;
    }

    /**
     * 获取返回信息
     */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取url
     */
    public String getUrl() {
        if (url.contains("http://")) {
            return url;
        } else {
            url = String.format("%s/%s/%s/", IpConfig.IP, IpConfig.FOLDER,url);
            return url;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取请求码
     */
    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    /**
     * 获取状态码
     */
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 获取josnData
     */
    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
        if (isAnalytical) {
            getAnalyticalState(json);
            getAnalyticalMsg(json);
            this.json = Tools.getJJson(json, "data");
        }
    }

    /**
     * 获取解析状态码
     */
    private void getAnalyticalState(JSONObject json) {
        state = Tools.getJNum(json, "code");
        if (state == -1) {
            state = Tools.getJNum(json, "Code");
        }
    }

    /**
     * 获取解析信息
     */
    private void getAnalyticalMsg(JSONObject json) {
        msg = Tools.getJStr(json, "message");
        if (Tools.isEmpty(msg)) {
            msg = Tools.getJStr(json, "Message");
        }
    }

}
