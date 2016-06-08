package org.mylibrary.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.mylibrary.biz.MyApplication;

import java.util.Set;


/** 公共的存储数据的帮助类 */
public class CommonFunction {
	private static CommonFunction instance;
	private static final String SPFS_NAME = "safecoo";
	private SharedPreferences preferences;
	private SharedPreferences.Editor edit;

	public static CommonFunction getInstance() {
		if (instance == null) {
			instance = new CommonFunction();
		}
		return instance;
	}

	public CommonFunction() {
		Context ctx = MyApplication.getContext();
		preferences = ctx.getSharedPreferences(SPFS_NAME, Context.MODE_PRIVATE);
		edit = preferences.edit();
	}

	/**
	 * 存储boolean的值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * */
	public static void putBoolean(String key, boolean value) {
		getInstance().edit.putBoolean(key, value);
		getInstance().edit.commit();
	}

	/**
	 * 取出boolean类型的值
	 * 
	 * @param key
	 *            键
	 * @param defValue
	 *            缺省值
	 * */
	public static boolean getBoolean(String key, boolean defValue) {
		return getInstance().preferences.getBoolean(key, defValue);
	}

	/**
	 * 存储String的值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * */
	public static void putString(String key, String value) {
		getInstance().edit.putString(key, value);
		getInstance().edit.commit();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void putStrings(String key, Set<String> value) {
		getInstance().edit.putStringSet(key, value);
		getInstance().edit.commit();
	}

	/**
	 * 取出String类型的值
	 * 
	 * @param key
	 *            键
	 * @param defValue
	 *            缺省值
	 * */
	public static String getString(String key, String defValue) {
		return getInstance().preferences.getString(key, defValue);
	}

	/**
	 * 存储Int的值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * */
	public static void putInt(String key, int value) {
		getInstance().edit.putInt(key, value);
		getInstance().edit.commit();
	}

	/**
	 * 取出int类型的值
	 * 
	 * @param key
	 *            键
	 * @param defValue
	 *            缺省值
	 * */
	public static int getInt(String key, int defValue) {
		return getInstance().preferences.getInt(key, defValue);
	}

	/**
	 * 存储Long的值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * */
	public static void putLong(String key, long value) {
		getInstance().edit.putLong(key, value);
		getInstance().edit.commit();
	}

	/**
	 * 取出long类型的值
	 * 
	 * @param key
	 *            键
	 * @param defValue
	 *            缺省值
	 * */
	public static long getLong(String key, long defValue) {
		return getInstance().preferences.getLong(key, defValue);
	}

	/**
	 * 存储Float的值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * */
	public static void putFloat(String key, float value) {
		getInstance().edit.putFloat(key, value);
		getInstance().edit.commit();
	}

	/**
	 * 取出float类型的值
	 * 
	 * @param key
	 *            键
	 * @param defValue
	 *            缺省值
	 * */
	public static float getFloat(String key, float defValue) {
		return getInstance().preferences.getFloat(key, defValue);
	}

	/**
	 * 清除所有的存储
	 * */
	public static void clear() {
		getInstance().edit.clear();
		getInstance().edit.commit();
	}
}
