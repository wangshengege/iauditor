package org.mylibrary.biz;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.List;

import org.mylibrary.base.AbstractBaseActivity;

public class MyApplication extends Application  {
	protected static Context ctx;

	/** 获取全局的上下文 */
	public static Context getContext() {
		return ctx;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ctx = getApplicationContext();
		Logger.init().setMethodCount(3);
	}
	/** 记录所有打开的Activity，用于退出 */
	private List<AbstractBaseActivity> activitys = null;

	// 添加Activity到容器中
	public void addActivity(AbstractBaseActivity activity) {
		if (activitys == null) {
			activitys = new LinkedList<AbstractBaseActivity>();
		}

		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}
	}

	public void removeActivity(AbstractBaseActivity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (activitys.contains(activity)) {
				activitys.remove(activity);
			}
		}
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
	/**
	 * 获取所有Activity
	 * 
	 * @return
	 */
	public List<AbstractBaseActivity> getActivitys() {
		return activitys;
	}

	// 遍历所有Activity并finish
	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (AbstractBaseActivity activity : activitys) {
				if (activity != null) {
					activity.finish();
				}
			}
		}
		System.exit(0);
	}

}
