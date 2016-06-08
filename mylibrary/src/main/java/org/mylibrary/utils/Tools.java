package org.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.mylibrary.base.AbstractBaseActivity;

public class Tools {
    public static final String TAG="Tools";
    /**
     * 弹出短时间的提示框
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        ;
    }

    /**
     * 弹出长时间时间的提示框
     */
    public static void showLToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 判断是否为空
     */
    public static boolean isEmpty(Object ob) {
        if (ob == null || ob.toString().isEmpty() || ob.toString().length() < 1 || ob.equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取应用商店去评价
     */
    public static void toStore(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Fixed:解决了一个手机没有应用市场会崩溃的bug
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Tools.showToast(context, "没有应用市场!");
        }
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取格式化时间
     *
     * @param timeStamp 时间戳
     * @param pattern   格式化格式（默认yyyy-MM-dd HH:mm:ss）
     */
    public static String getFormatTime(long timeStamp, String pattern) {
        String time = null;
        if (new Long(timeStamp).toString().length() < 11) {
            timeStamp *= 1000;
        }
        if (Tools.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getDefault());
        time = format.format(timeStamp);
        return time;
    }

    /**
     * 聊天用的获取格式化时间
     *
     * @param timeStamp 时间戳
     */
    public static String getChatFormatTime(long timeStamp) {
        String time = null;
        LogTools.i(TAG,"chat time:"+timeStamp);
        if (new Long(timeStamp).toString().length() < 11) {
            timeStamp *= 1000;
        }
        long nTime = getTimeStamp();
        long timeSub = nTime- timeStamp;
        SimpleDateFormat format;
        if (timeSub < 60 * 1000) {//一分钟内
            time = timeSub / 1000 + "秒前";
        } else if (timeSub >= ((long) (60 * 1000)) && timeSub < ((long) (60 * 60 * 1000))) {
            time = timeSub / (60 * 1000) + "分前";
        } else if (timeSub >= ((long) (60 * 60 * 1000)) && timeSub < ((long) (24 * 60 * 60 * 1000))) {
            format = new SimpleDateFormat("HH:mm");
            time = format.format(timeStamp);
        } else if (timeSub >= ((long) (24 * 60 * 60 * 1000)) && timeSub < ((long) (2 * 24 * 60 * 60 * 1000))) {
            format = new SimpleDateFormat("昨天 HH:mm");
            time =format.format(timeStamp);
        } else if (timeSub >= ((long) (2 * 24 * 60 * 60 * 1000)) && timeSub < ((long) (3 * 24 * 60 * 60 * 1000))) {
            format = new SimpleDateFormat("前天 HH:mm");
            time = format.format(timeStamp);
        } else {
            format = new SimpleDateFormat("MM-dd");
            time = format.format(timeStamp);
        }
        return time;

    }

    /**
     * 获取包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取版本名字
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取系统时间
     *
     * @param time   时间戳
     * @param format 日期的格式 默认yyyy-MM-dd HH:mm:ss
     * @return 格式化的时间
     */
    public static String getCurrentDateStr(long time, String format) {
        if (new Long(time).toString().length() < 13) {
            time *= 1000;
        }
        if (isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(format).format(time);
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * @param str 去除字符串制表 换行 空格
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\u0008*|\\u0009|\\u000d|\\u000a");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 格式化数字（金额）
     *
     * @param obj 字符串或doule
     * @return
     */
    public static String getFormatAmt(Object obj) {
        BigDecimal amount = null;
        if (obj instanceof BigDecimal)
            amount = (BigDecimal) obj;
        else if (obj instanceof String) {
            amount = new BigDecimal((String) obj);
        } else if (obj instanceof Double) {
            amount = new BigDecimal((Double) obj);
        } else {
            return null;
        }

        DecimalFormat format = new DecimalFormat("#,##0.00");

        return format.format(amount);
    }

    /**
     * 得到手机miei号
     */
    public static String getDeviceId(Context context) {

        TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "";
        try {
            imei = telephonemanage.getDeviceId();
            if (isEmpty(imei)) {
                imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        } catch (Exception e) {
            LogTools.showLoge(e.getMessage());
        }
        return imei;
    }

    /**
     * 得到mac地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 得到手机系统版本号
     *
     * @return
     */
    public static String getPhoneVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 校验输入EditText输入 只能輸入中文 英文 数字 以及下划线
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5_]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 校验输入EditText输入 只能輸入英文 数字 以及下划线
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String pwdFilter(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9_]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

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

    /**
     * 获取JSON中的字符串
     *
     * @param json
     * @param key
     * @return
     */
    public static Object getJObj(JSONObject json, String key) {
        try {
            Object obj = json.get(key);
            return obj;
        } catch (JSONException e) {
            Tools.catchException(e);
        }
        return null;
    }

    public static void catchException(Exception e) {
        LogTools.showLogw(e.toString());
    }

    /**
     * 将字符串转换为数字
     *
     * @param string
     * @return
     */
    public static int toNumber(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return 0;
        }

    }

    public static long toLongNumber(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 电话中间4位换*
     *
     * @param phone
     * @return
     */
    public static String formartPhoneNum(String phone) {
        if (TextUtils.isEmpty(phone) ||  phone.length()<11) {
            return "";
        }
        String bf = phone.substring(0, 3);
        String ed = phone.substring(7, phone.length());
        StringBuffer sb = new StringBuffer();
        sb.append(bf);
        sb.append("****");
        sb.append(ed);
        return sb.toString();
    }

    /**
     * 获得屏幕截图
     */
    public static Bitmap screenShot(AbstractBaseActivity ctx) {
        View view = ctx.getWindow().getDecorView();
        Display display = ctx.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        return bmp;
    }

    /**
     * 跳转界面
     *
     * @param context 当前上下文
     * @param c       跳转的界面
     */
    public static void toActivity(Context context, Class<? extends Activity> c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }
}
