package org.mylibrary.net;

/**
 * 作者： ws
 * 日期： 2016/1/26
 * 作用： arg.mylibrary.net
 */
public interface JsonCallback {
    public void onBeforeRequest(RequestCall call);

    public void onResponseError(Throwable t, RequestCall call);

    public void onResponseSuccess(RequestCall call);

    public void onFinishRequest(RequestCall call);
}
