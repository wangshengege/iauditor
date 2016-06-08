package org.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class InputTools {
	/**
	 * 隐藏虚拟键盘
	 * */
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

		}
	}

	public static void HideKeyboard(Activity context) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(context.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		}
	}

	/**
	 * 显示虚拟键盘
	 * */
	public static void ShowKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

	}

	/**
	 * 用计时器强制显示或者关闭系统键盘
	 * 
	 * @param txtSearchKey
	 *            EditText
	 * @param isShow
	 *            是否显示软键盘
	 * */
	public static void KeyBoard(final EditText txtSearchKey,
			final Boolean isShow) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager m = (InputMethodManager) txtSearchKey
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				if (isShow) {
					m.showSoftInput(txtSearchKey,
							InputMethodManager.SHOW_FORCED);
				} else {
					m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
				}
			}
		}, 100);
	}

	/**
	 * 通过定时器强制隐藏虚拟键盘
	 * */
	public static void TimerHideKeyboard(final View v) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
				}
			}
		}, 10);
	}

	/**
	 * 输入法是否显示着
	 * 
	 * @return true是软键盘显示，false是不现实
	 * */
	public static boolean KeyBoard(EditText edittext) {
		boolean bool = false;
		InputMethodManager imm = (InputMethodManager) edittext.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			bool = true;
		}
		return bool;

	}

	/**
	 * 控制软键盘的显示/隐藏
	 * 
	 * @param context
	 */
	public static void setKeyBoard(final Context context) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}, 500);

	}
}
