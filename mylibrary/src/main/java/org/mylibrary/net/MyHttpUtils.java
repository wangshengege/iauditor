package org.mylibrary.net;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.mylibrary.biz.MyApplication;
import org.mylibrary.biz.MyException;
import org.mylibrary.utils.LogTools;

public class MyHttpUtils {
    public static final String TAG = "HttpUtils";
    private HttpUtils httpClient;
    private MyHeader header;

    /**
     * 构造函数
     */
    public MyHttpUtils() {
        httpClient = new HttpUtils();
        setCurrentHttpCacheExpiry(1000*10);
    }
    /**设置缓存时间*/
    public void setCurrentHttpCacheExpiry(long time){
        httpClient.configCurrentHttpCacheExpiry(time);
    }
    /**
     * 设置heald
     */
    public void setHeader(MyHeader header) {
        this.header = header;
    }

    /**
     * 得到header
     * @param params 请求数据
     * @param requestType 请求类型1是get
     */
    public RequestParams parseHeader(RequestParams params,int requestType) throws NoSuchMethodException, IllegalAccessException, Exception {
        if (params == null || header == null) {
            return params;
        }
        Field[] field = header.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder("header:");
        for (int i = 0; i < field.length; i++) {
            String key = field[i].getName();

            key = key.substring(0, 1).toUpperCase() + key.substring(1); // 将属性的首字符大写，方便构造get，set方法
            String type = field[i].getGenericType().toString(); // 获取属性的类型
            if (type.equals("class java.lang.String")) {
                Method m = header.getClass().getMethod("get" + key);
                String value = (String) m.invoke(header); // 调用getter方法获取属性值
                sb.append( key + "=" + value+"-->");
                if (value != null) {
                    if(requestType==1){
                        params.addQueryStringParameter(key, value);
                    }else{
                        params.addBodyParameter(key, value);
                    }

                }
            }

        }
        LogTools.i(TAG, sb.toString());
        return params;
    }

    /**
     * POST提交
     *
     * @param call         请求参数
     * @param jsonCallback 请求返回接口
     * @param params       请求封装
     */
    public void postBaseJSON(RequestCall call, JsonCallback jsonCallback,
                             RequestParams params) {
        if (NetworkUtil.isNetworkAvailable(MyApplication.getContext())) {
            try {
                params = parseHeader(params,2);
            } catch (Exception e) {
                e.printStackTrace();
                LogTools.logd(TAG, "请求头设置出错" + e.getMessage());
            }
            httpClient.send(HttpRequest.HttpMethod.POST, call.getUrl(),
                    params, getCallBack(jsonCallback, call));
        } else {
            LogTools.logd(TAG, "没有网络");
            jsonCallback.onResponseError(new MyException(new Throwable(
                    "没有网络"), MyException.ERR_NO_AVAILABLE_NETWORK), call);
            jsonCallback.onFinishRequest(call);
        }
    }

    /**
     * GET基础提交
     *
     * @param call         请求参数
     * @param jsonCallback 数据返回接口
     */
    public void getBaseJSON(RequestCall call, JsonCallback jsonCallback) {
        RequestParams params = new RequestParams();
        getBaseJSON(call, jsonCallback, params);
    }

    /**
     * GET基础提交
     *
     * @param call         请求参数
     * @param jsonCallback 数据返回接口
     */
    public void getBaseJSON(RequestCall call, JsonCallback jsonCallback, RequestParams params) {
        if (jsonCallback == null) {
            LogTools.loge(TAG, "jsonCallback 为null");
            return;
        }
        jsonCallback.onBeforeRequest(call);
        if (NetworkUtil.isNetworkAvailable(MyApplication.getContext())) {
            String url = call.getUrl();
            try {
                params = parseHeader(params,1);
            } catch (Exception e) {
                e.printStackTrace();
                LogTools.logd(TAG, "请求头设置出错" + e.getMessage());
            }
            httpClient.send(HttpRequest.HttpMethod.GET, url, params,
                    getCallBack(jsonCallback, call));
        } else {
            LogTools.logd(TAG, "没有网络");
            jsonCallback.onResponseError(new MyException(new Throwable("没有网络"),
                    MyException.ERR_NO_AVAILABLE_NETWORK), call);
            jsonCallback.onFinishRequest(call);
        }

    }

    /**
     * 下载
     *
     * @param call       请求参数
     * @param path       文件路径
     * @param addDown    续传
     * @param autoRename 自动更名
     * @param callback   请求返回参数
     */
    public HttpHandler<File> downLoad(RequestCall call, String path,
                                      boolean addDown, boolean autoRename, RequestCallBack<File> callback) {
        if (NetworkUtil.isNetworkAvailable(MyApplication.getContext())) {
            return httpClient.download(call.getUrl(), path, addDown,
                    autoRename, callback);
        }
        if (callback != null) {
            callback.onFailure(new HttpException("没有网络"), "没有网络");
        }
        return null;
    }

    private RequestCallBack<String> getCallBack(final JsonCallback jsonCallback,
                                                final RequestCall call) {
        return new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> org0) {
                JSONObject json = null;
                try {
                    LogTools.i(TAG, "back data:" + org0.result);
                    json = new JSONObject(org0.result);
                    call.setJson(json);
                    jsonCallback.onResponseSuccess(call);
                } catch (JSONException e) {
                    e.printStackTrace();//
                    jsonCallback.onResponseError(new MyException(new Exception(
                            "JSON解析错误")), call);
                    LogTools.d(TAG, "json解析失败");
                }
                jsonCallback.onFinishRequest(call);
            }

            @Override
            public void onFailure(HttpException org0, String org1) {
                jsonCallback.onResponseError(org0, call);
                jsonCallback.onFinishRequest(call);
                LogTools.e(TAG, "Failure msg:" + org0.getMessage() + "--" + org1);
            }
        };
    }
}
