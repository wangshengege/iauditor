package org.mylibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

import org.mylibrary.net.JsonCallback;
import org.mylibrary.net.RequestCall;

/**
 * 所有forgment的基类
 * */
public abstract class AbstractBaseFragment extends Fragment implements JsonCallback {
	public View rootView;
	public Context self;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return rootView;
	}
	/** 初始化或者更新数据用 */
	public void startLoadingData() {
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self=getActivity();
	}
	@Override
	public void onBeforeRequest(RequestCall call) {
	}
	@Override
	public void onResponseError(Throwable t, RequestCall call) {
	}
	@Override
	public void onResponseSuccess(RequestCall call) {
	}
	@Override
	public void onFinishRequest(RequestCall call) {
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
}
