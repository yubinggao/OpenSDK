package com.hanfeng.guildsdk.widget;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.hanfeng.guildsdk.tool.CommonTool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hanfeng.guildsdk.tool.YhSdkLog;

public class FloatWindowSmallView extends LinearLayout {

	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int viewHeight;

	/**
	 * 记录系统状态栏的高度
	 */
	private static int statusBarHeight;
	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;
	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;

	// private WMDialog dialog;

	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float StartX = 0;
	private float StartY = 0;
	public boolean isMove = false;
	private boolean isDialogShowing = false;
	private boolean isVisible = true;
	private final int LEFT = 0;
	private final int RIGHT = 1;
	private int leftOrRight;
	private Context context;
	/** 小悬浮窗对象 */
	private ImageView view;

	/** 红点提示 */
	// private ImageView viewTip;
	public FloatWindowSmallView(Context context) {
		super(context);
		// 获取WindowManager对象
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		this.context = context;
		// 获取布局文件
		LayoutInflater.from(context).inflate(
				CommonTool.getResourceId(context, "yhsdk_layout_small_float",
						"layout"), this);
		view = (ImageView) findViewById(CommonTool.getResourceId(context,
				"yh_samll_float_iv", "id"));
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		timeListener.removeCallbacks(noTouchRunnable);
		// 获取到状态栏的高度
		Rect frame = new Rect();
		getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight; // statusBarHeight是系统状态栏的高度
		//YhSdkLog.getInstance().i("X="+x+"   Y="+y);
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchX = event.getX();
			mTouchY = event.getY();
			StartX = x;
			StartY = y;
			isMove = false;
			break;
		case MotionEvent.ACTION_MOVE:
			int ll = (int) Math.sqrt(Math.abs((StartX - (int) (x))
					* (StartX - (int) (x)) + (StartY - (int) (y))
					* (StartY - (int) (y))));
			if (ll >= ViewConfiguration.getTouchSlop()) {
				isMove = true;
				updateViewPosition();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (isMove) {
				isMove = false;
				float halfScreen = screenWidth / 2;
				if (x <= halfScreen) {
					x = 0;
				} else {
					x = screenWidth;
				}
				FloatWindowMgr.posX = x - mTouchX;
				FloatWindowMgr.posY = y - mTouchY;
				YhSdkLog.getInstance().i("moving smallWindow view ");
				YhSdkLog.getInstance().i(
						"FloatWindowMgr.posY :" + FloatWindowMgr.posY);
				YhSdkLog.getInstance().i(
						"FloatWindowMgr.posX :" + FloatWindowMgr.posX);
				YhSdkLog.getInstance().i("计时开始");
				updateViewPosition();
				// 3s无操作触发
				timeListener.postDelayed(noTouchRunnable, 3000);
			} else {
				openBigWindow();
			}
			mTouchX = mTouchY = 0;

			break;

		}
		return true;
	}

	/**
	 * 无触碰隐藏监听
	 */
	private Handler timeListener = new Handler();
	private Runnable noTouchRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			YhSdkLog.getInstance().i("3秒无点击 ");
			if((FloatWindowMgr.posX) <= (FloatWindowMgr.screenWidth/2)){
				YhSdkLog.getInstance().i(
						"FloatWindowMgr.posX :" + FloatWindowMgr.posX+" +   screenWidth"+FloatWindowMgr.screenWidth);
				smallFloatExitAnimation(0);
			}else{
				smallFloatExitAnimation(1);
			}
			
		}
	};

	/**
	 * 显示小浮标是否添加3秒隐藏
	 */
	public void timehide() {
		timeListener.postDelayed(noTouchRunnable, 3000);
	}

	// 退出动画
	private void smallFloatExitAnimation(int type) {

		Animation mAnimation = null;
		if (type == LEFT) {
			mAnimation = AnimationUtils.loadAnimation(context, CommonTool
					.getResourceId(context, "yhsdk_slide_out_left", "anim"));
		} else if (type == RIGHT) {
			mAnimation = AnimationUtils.loadAnimation(context, CommonTool
					.getResourceId(context, "yhsdk_slide_out_right", "anim"));
		}
		view.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				FloatWindowMgr.winStatus = FloatWindowMgr.WIN_HIDE;
				FloatWindowMgr.createHideWindow(context);
				FloatWindowMgr.removeSmallWindow(context);
			}
		});

	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * 获得小悬浮窗参数
	 */
	public WindowManager.LayoutParams getParams() {
		return mParams;
	}

	/** 更新小悬浮窗在屏幕中的位置 */
	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		mParams.x = (int) (x - mTouchX);
		mParams.y = (int) (y - mTouchY);
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		if (x > screenWidth / 2) {
			view.setImageResource(CommonTool.getResourceId(this.getContext(),
					"yhsdk_floatimg_shadow", "drawable"));
		} else {
			view.setImageResource(CommonTool.getResourceId(this.getContext(),
					"yhsdk_floatimg_shadow", "drawable"));
		}
		windowManager.updateViewLayout(this, mParams); // 刷新显示
		if (isDialogShowing) {
			this.setVisibility(INVISIBLE);
		} else {
			this.setVisibility(VISIBLE);
		}
	}

	/** 打开大悬浮窗，同时关闭小悬浮窗 */
	private void openBigWindow() {
		FloatWindowMgr.winStatus = FloatWindowMgr.WIN_BIG;
		FloatWindowMgr.createBigWindow(getContext());
		FloatWindowMgr.removeSmallWindow(getContext());
	}

	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * 是否将其影藏
	 * 
	 * @param b
	 */
	public void setVisible(boolean b) {
		isVisible = b;
		if (!b) {
			setVisibility(INVISIBLE);
		} else {
			setVisibility(VISIBLE);
		}
	}

	/**
	 * @return 得到显示的对话框是否显示
	 */
	public boolean isDialogShowing() {
		return isDialogShowing;
	}
	//停止无点击监听
	public void stopDelayed(){
		if(timeListener != null){
			timeListener.removeCallbacks(noTouchRunnable);
		}
	}
}
