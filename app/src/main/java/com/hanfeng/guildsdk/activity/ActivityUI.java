package com.hanfeng.guildsdk.activity;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.tool.AssetTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSdkLog;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.WindowManager;
import android.widget.LinearLayout;

public abstract class ActivityUI {
	public static final String INPUT_TYPE_CONTENT = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String INPUT_TYPE_NUMBER = "0123456789";
	
	protected AssetTool asset;
	protected UITool uitool;
	
	private long lastClickTime = 0;
	
	ActivityUI() {
		asset = AssetTool.getInstance();
		uitool = UITool.getInstance();
	}

	/**
	 * 生成界面代码
	 * @param activity
	 * @return
	 */
	public abstract LinearLayout onCreate(Activity activity);

	/**
	 * 设置窗口宽高
	 * @param activity
	 */
	public void onSetWindows(Activity activity) {
		WindowManager.LayoutParams wmparams = activity.getWindow().getAttributes();
		
		// 竖屏:
		if (Constants.isPORTRAIT) {
			YhSdkLog.getInstance().i("cur windows is portira");
			wmparams.width = Constants.DEVICE_INFO.windowWidthPx ;
			if (Constants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = Constants.DEVICE_INFO.windowHeightPx;
			}
		} else {

			YhSdkLog.getInstance().i("cur windows is LANDSCAPE");
			wmparams.width = Constants.DEVICE_INFO.windowWidthPx;
			if (Constants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = Constants.DEVICE_INFO.windowHeightPx;
			}

		}
	}
		/*if(Constants.isPORTRAIT){
			if (activity.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
				YhSdkLog.getInstance().i("cur windows is LANDSCAPE");
				wmparams.width = Constants.DEVICE_INFO.windowWidthPx;
				if (Constants.DEVICE_INFO.windowHeightPx >0 ) {
					wmparams.height = Constants.DEVICE_INFO.windowHeightPx;
				}
			}else{
				YhSdkLog.getInstance().i("cur windows is portira");
				wmparams.width = Constants.DEVICE_INFO.windowWidthPx/6 * 5;
				if (Constants.DEVICE_INFO.windowHeightPx > 0) {
					wmparams.height = Constants.DEVICE_INFO.windowHeightPx;
				}
			}
		}else{
			 if (activity.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
					YhSdkLog.getInstance().i("cur windows is LANDSCAPE");
					wmparams.width = Constants.DEVICE_INFO.windowWidthPx;
					if (Constants.DEVICE_INFO.windowHeightPx >0 ) {
						wmparams.height = Constants.DEVICE_INFO.windowHeightPx;
					}
				}else{
					YhSdkLog.getInstance().i("cur windows is portira");
					wmparams.width = Constants.DEVICE_INFO.windowHeightPx;
					if (Constants.DEVICE_INFO.windowHeightPx > 0) {
						wmparams.height = Constants.DEVICE_INFO.windowWidthPx;
					}
				}
		}
	}*/
		
/*		if (activity.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
			YhSdkLog.getInstance().i("cur windows is LANDSCAPE");
			wmparams.width = Constants.DEVICE_INFO.windowWidthPx;
			if (Constants.DEVICE_INFO.windowHeightPx >0 ) {
				wmparams.height = Constants.DEVICE_INFO.windowHeightPx;
			}
		}else{
			YhSdkLog.getInstance().i("cur windows is portira");
			wmparams.width = Constants.DEVICE_INFO.windowHeightPx;
			if (Constants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = Constants.DEVICE_INFO.windowWidthPx;
			}
		}
		YhSdkLog.getInstance().i("cur windows width:"+wmparams.width+"cur windows height:"+wmparams.height);
		wmparams.alpha = 1F;
		wmparams.dimAmount = 0F;
		activity.getWindow().setAttributes(wmparams);
	}*/

	/**
	 * 代理Activity方法的实现
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
	}
	
	/**
	 * 判断按钮是否快速点击
	 * @return true, 快速点击
	 */
	protected boolean isFastClick() {
		long now = System.currentTimeMillis();
		long timediff = now - lastClickTime;
		lastClickTime = now;
		if (timediff < 1000) {
			return true;
		}
		return false;
	}
	
	/**
	 * 代理Activity 的onResume方法的实现
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void  onResume(){
	}
	public void  onDestroy(){
	}
	public void onBackPressed() {
		
	}
}
