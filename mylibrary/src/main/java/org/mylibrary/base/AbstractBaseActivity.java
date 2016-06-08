package org.mylibrary.base;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.lidroid.xutils.ViewUtils;

import org.mylibrary.autolayout.AutoLayout;
import org.mylibrary.autolayout.AutoLayoutActivity;
import org.mylibrary.biz.MyApplication;
import org.mylibrary.common.SystemBarTintManager;
import org.mylibrary.net.JsonCallback;
import org.mylibrary.net.RequestCall;
import org.mylibrary.utils.LogTools;

/**
 * 所有Activity的基类
 * */
public abstract class AbstractBaseActivity extends AutoLayoutActivity implements JsonCallback {
	/** HYApplication对象 */
	protected MyApplication myApplication;
	protected AbstractBaseActivity self;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		myApplication = (MyApplication) MyApplication.getContext();
		myApplication.addActivity(this);
		self=this;
		AutoLayout.getInstance().auto(this);
		//initBaidu();
	}
	private void initBaidu(){
		LogTools.logi(self,"baidu statrt");
		// 设置AppKey
		StatService.setAppKey("5d1f1b4790"); // appkey必须在mtj网站上注册生成，该设置建议在AndroidManifest.xml中填写，代码设置容易丢失

        /*
         * 设置渠道的推荐方法。该方法同setAppChannel（String）， 如果第三个参数设置为true（防止渠道代码设置会丢失的情况），将会保存该渠道，每次设置都会更新保存的渠道，
         * 如果之前的版本使用了该函数设置渠道，而后来的版本需要AndroidManifest.xml设置渠道，那么需要将第二个参数设置为空字符串,并且第三个参数设置为false即可。
         * appChannel是应用的发布渠道，不需要在mtj网站上注册，直接填写就可以 该参数也可以设置在AndroidManifest.xml中
         */
		// StatService.setAppChannel(this, "RepleceWithYourChannel", true);
		// 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
		StatService.setSessionTimeOut(1);
		// setOn也可以在AndroidManifest.xml文件中填写，BaiduMobAd_EXCEPTION_LOG，打开崩溃错误收集，默认是关闭的
		StatService.setOn(this, StatService.EXCEPTION_LOG);
        /*
         * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/> 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
         *
         * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/> 这个参数的设置暂时只支持代码加入，
         * 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
         */
		StatService.setLogSenderDelayed(5);
        /*
         * 用于设置日志发送策略<br /> 嵌入位置：Activity的onCreate()函数中 <br />
         *
         * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum. SET_TIME_INTERVAL, 1, false); 第二个参数可选：
         * SendStrategyEnum.APP_START SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
         * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
         * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
         */
		StatService.setSendLogStrategy(this, SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
		// 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
		StatService.setDebugOn(true);
	}
	/**设置内容布局
	 * @param layoutResID
	 * 			内容布局id
	 * @return 布局id
	 * */
	public int setViewId(int layoutResID){
		if(layoutResID!=0){
			setContentView(layoutResID);
			ViewUtils.inject(this);
			return layoutResID;
		}
		return -1;
	};
	/**
	 * 显示沉浸状态栏 xml在其主体加android:fitsSystemWindows="true"
	 * @param color
	 * 			颜色资源
	 * @return 其他设置通过返回对象设置
	 * */
	public SystemBarTintManager showImmerseStatusBar(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(color);
		return tintManager;
	}
	/**开启沉浸栏
	 * @param on 是否开启
	 * */
	public void setTranslucentStatus(boolean on) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		myApplication.removeActivity(this);
		System.gc();
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
	/**刷新数据据*/
	public void onReFresh(){
	}
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
}
