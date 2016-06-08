package org.mylibrary.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: ws
 * 日期: 2016/4/6.
 * 介绍：
 */
public class JSONUtils {
    /**
     * 获取JSON中的数字
     *
     * @param json
     * @param key
     * @return
     */
    public static Integer getJNum(JSONObject json, String key) {
        try {
            Integer num = json.getInt(key);
            if (num == null) {
                return Tools.toNumber(Tools.getJStr(json, key));
            } else {
                return num;
            }
        } catch (JSONException e) {

        }
        return -1;
    }
    /**
     * 获取JSON中的字符串
     *
     * @param json
     * @param key
     * @return
     */
    public static String getJStr(JSONObject json, String key) {
        try {
            String str = json.getString(key);
            if (TextUtils.isEmpty(str)) {
                return "";
            } else {
                return str;
            }
        } catch (JSONException e) {
            Tools.catchException(e);
        }
        return "";
    }
    /**
     * 获取JSON中的JSONObject
     *
     * @param json
     * @param key
     * @return
     */
    public static JSONObject getJJson(JSONObject json, String key) {
        try {
            JSONObject j = json.getJSONObject(key);
            if (j == null || TextUtils.isEmpty(j.toString())) {
                return null;
            } else {
                return j;
            }
        } catch (JSONException e) {
            Tools.catchException(e);
        }
        return null;
    }

    /**
     * 获取JSON中的JSONObject数组
     *
     * @param json
     * @param key
     * @return
     */
    public static JSONArray getJJsonArr(JSONObject json, String key) {
        try {
            JSONArray j = json.getJSONArray(key);
            if (j == null || TextUtils.isEmpty(j.toString())) {
                return null;
            } else {
                return j;
            }
        } catch (JSONException e) {
            Tools.catchException(e);
        }
        return null;
    }

}
