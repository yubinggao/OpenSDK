package com.hanfeng.guildsdk.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.unionpay.mobile.android.hce.f;

/**
 * 悬浮窗管理器
 * 
 * @author yich
 *
 */
public class FloatWindowMgr {

	/** 用于控制在屏幕上添加或移除悬浮窗 */
	private static WindowManager mWindowManager;

	/** 小悬浮窗view的实例 */
	private static FloatWindowSmallView smallWindow;

	/** 大悬浮窗view的实例 */
	private static FloatWindowBigView bigWindow;

	/** 隐藏悬浮窗view的实例 */
	private static FloatWindowHideView hideWindow;

	/** 大小悬浮窗view的参数 */
	private static LayoutParams windowParams;

	/** 屏幕的宽和高 */
	public static int screenWidth;
	public static int screenHeight;

	/** 显示的位置 */
	public static float posX;
	public static float posY;

	public static final int WIN_SMALL = 1;
	public static final int WIN_BIG = 2;
	public static final int WIN_HIDE = 3;// 无操作隐藏
	public static final int WIN_NONE = 4;
	public static final int WIN_SHAKE_INVISBLE = 5;// 摇动隐藏
	public static final int WIN_DEFULT = 0;
	public static int winStatus;
	public static boolean flag = true;
	private static String TAG = "hanfengsdk";

	/**
	 * 如果windowmanager还未创建，则创建一个新的windowmanager返回。否则返回当前以创建的windowmanager
	 * 
	 * @param context
	 *            必须为应用程序的context
	 * 
	 * @return windowmanager的实例，用于控制屏幕上添加或移除悬浮窗
	 */
	private static WindowManager getManager(Context context) {
		try {
			if (screenWidth == 0) {
				screenWidth = context.getResources().getDisplayMetrics().widthPixels;

				screenHeight = context.getResources().getDisplayMetrics().heightPixels;
			}
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		} catch (Exception e) {
			Log.d(TAG, "浮标创建mWindowManager异常");
		}
		return mWindowManager;
	}

	/************************************************ 创建窗口 ************************************************************/
	/**
	 * 创建一个小悬浮窗。初始位置在屏幕的左上角位置
	 * 
	 * @param context
	 *            必须为应用程序的Context
	 */
	public static void createSmallWindow(Context context) {
		boolean flag = true;
		int barHeight = 0;//getStatusBarHeight(context);
		try {
			barHeight = getStatusBarHeight(context);
		} catch (Exception e) {
			e.printStackTrace();
		}

		WindowManager windowManager = getManager(context);
		if (smallWindow == null) {
			smallWindow = new FloatWindowSmallView(context);
			if (windowParams == null) {
				windowParams = new LayoutParams();
				windowParams.type = 1028;
				windowParams.format = PixelFormat.RGBA_8888;
				windowParams.gravity = Gravity.LEFT | Gravity.TOP;
				windowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
				// 在设置宽高
				windowParams.x = (int) FloatWindowMgr.posX;
				windowParams.y = (int) FloatWindowMgr.posY + barHeight;
				Log.d(TAG, "浮标，X:" + windowParams.x + "，Y:" + windowParams.y + "，通知栏高度：" + barHeight);
				windowParams.width = FloatWindowSmallView.viewWidth;
				windowParams.height = FloatWindowSmallView.viewHeight;

			}
		}
		smallWindow.setParams(windowParams);

			try{
//				额....
				windowManager.addView(smallWindow, windowParams);
			}catch(Exception e){
				//e.printStackTrace();
				Log.d(TAG,"已添加过浮标");
			}

		//			解决办法:
	/*	if (smallWindow.getParent() == null) {
			windowManager.addView(smallWindow, windowParams);
		}*/

		YhSdkLog.getInstance().i("create smallWindow view ");
		YhSdkLog.getInstance().i("FloatWindowMgr.posY :" + FloatWindowMgr.posY);
		YhSdkLog.getInstance().i("FloatWindowMgr.posX :" + FloatWindowMgr.posX);

		smallWindow.timehide();
	}

	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 创建一个大悬浮窗。
	 * 
	 * @param context
	 *            必须为应用程序的Context
	 */
	public static void createBigWindow(Context context) {
		WindowManager windowManager = getManager(context);
		float halfScreen = screenWidth / 2;
		if (bigWindow == null) {
			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int height = 0;
			int width = 0;
			if (smallWindow != null) {
				smallWindow.measure(w, h);
				height = smallWindow.getMeasuredHeight();
				width = smallWindow.getMeasuredWidth();
			} else if (hideWindow != null) {
				hideWindow.measure(w, h);
				height = hideWindow.getMeasuredHeight();
				width = hideWindow.getMeasuredWidth();
			}
			if ((windowParams.x + width / 2) <= halfScreen) {
				bigWindow = new FloatWindowBigView(context, 0);
			} else {
				bigWindow = new FloatWindowBigView(context, 1);
			}

		}
		YhSdkLog.getInstance().i("create BigWindow view ");
		windowManager.addView(bigWindow, windowParams);
	}

	/**
	 * 创建一个隐藏悬浮窗。
	 * 
	 * @param context
	 *            必须为应用程序的Context
	 */
	public static void createHideWindow(Context context) {
		WindowManager windowManager = getManager(context);
		float halfScreen = screenWidth / 2;
		if (hideWindow == null) {
			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

			smallWindow.measure(w, h);
			int height = smallWindow.getMeasuredHeight();
			int width = smallWindow.getMeasuredWidth();
			if ((windowParams.x + width / 2) <= halfScreen) {
				hideWindow = new FloatWindowHideView(context, 0);
			} else {
				hideWindow = new FloatWindowHideView(context, 1);
				windowParams.x = windowParams.x + width / 2;
			}

		}
		YhSdkLog.getInstance().i("create BigWindow view ");
		windowManager.addView(hideWindow, windowParams);
	}

	/**
	 * 将小悬浮窗从屏幕上移除 (并且移除顶部的提示框)
	 * 
	 * @param context
	 *            必须为应用程序的context
	 */
	public static void removeSmallWindow(Context context) {
		YhSdkLog.getInstance().i("smallWindow has been removed");
		if (smallWindow != null) {
			if (context != null) {
				WindowManager windowManager = getManager(context);
				if (windowManager != null) {
					try {
						windowManager.removeView(smallWindow);
					} catch (Exception e) {
						Log.d(TAG, "已隐藏浮标");
					}
				}
				smallWindow = null;
			} else {
				smallWindow = null;
			}
		}
	}

	/**
	 * 将大悬浮窗从屏幕上移除
	 * 
	 * @param context
	 *            必须为应用程序的context
	 */
	public static void removeBigWindow(Context context) {
		YhSdkLog.getInstance().i("BigWindow has been removed");
		if (bigWindow != null) {
			if (context != null) {
				WindowManager windowManager = getManager(context);
				if (windowManager != null) {
					windowManager.removeView(bigWindow);
				}
				bigWindow = null;
			} else {
				bigWindow = null;
			}
		}

	}

	/**
	 * 将隐藏悬浮窗从屏幕上移除
	 * 
	 * @param context
	 *            必须为应用程序的context
	 */
	public static void removeHideWindow(Context context) {
		YhSdkLog.getInstance().i("BigWindow has been removed");
		if (hideWindow != null) {
			if (context != null) {
				WindowManager windowManager = getManager(context);
				if (windowManager != null) {
					windowManager.removeView(hideWindow);
				}
				hideWindow = null;
			} else {
				hideWindow = null;
			}
		}

	}

	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上
	 * 
	 * @return 有悬浮窗显示在屏幕上返回true，没有的话返回false
	 */
	public static boolean isWindowShowing() {
		return smallWindow != null || bigWindow != null || hideWindow != null;
	}

	public static boolean isSmallWindowShowing() {
		return smallWindow != null;
	}

	public static void showSmallwin(Context context) {
		winStatus = WIN_SMALL;
		removeBigWindow(context);
		removeHideWindow(context);
		createSmallWindow(context);
	}

	public static void removeAllwin(Context context) {
		winStatus = WIN_NONE;
		removeBigWindow(context);
		removeHideWindow(context);
		removeSmallWindow(context);
	}

	/**
	 * 清空所有的静态变量
	 */
	public static void onDestory() {
		mWindowManager = null;
		smallWindow = null;
		bigWindow = null;
		hideWindow = null;
		windowParams = null;
	}

}
