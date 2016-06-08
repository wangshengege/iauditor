package org.mylibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerForZoom extends ViewPager {

	private boolean isLocked = true;

	public ViewPagerForZoom(Context context) {
		super(context);
		isLocked = false;
	}

	public ViewPagerForZoom(Context context, AttributeSet attrs) {
		super(context, attrs);
		isLocked = false;
	}

	float x = 0;
	float dx = 0;

	/**
	 * 如果return false则表示由child view处理，如果return true则表示由ViewPage处理
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isLocked) {
			try {
				return super.onInterceptTouchEvent(ev);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isLocked) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	public void toggleLock() {
		isLocked = !isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isLocked() {
		return isLocked;
	}

}
