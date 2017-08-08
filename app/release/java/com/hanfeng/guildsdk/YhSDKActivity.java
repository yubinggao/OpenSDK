package com.hanfeng.guildsdk;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hanfeng.guildsdk.activity.ActivityFactory;
import com.hanfeng.guildsdk.activity.ActivityUI;
import com.hanfeng.guildsdk.tool.YhSdkLog;

public final class YhSDKActivity extends Activity {
	private ActivityUI handler = null;
	//定义一个过滤器；
    //定义一个广播监听器；
//    private PackageReceiver netChangReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 窗口去标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 窗口去黑边
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		if(Constants.isPORTRAIT){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		// 窗口横屏
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//		// 窗口竖屏
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// android2.3以后要设置setFinishOnTouchOutside(false)，否则窗口会消失
		if (Build.VERSION.SDK_INT >= 11) {
			setFinishOnTouchOutside(false);
		}
		Intent intent = getIntent();
		String layoutId = intent.getStringExtra("layoutId");
		YhSdkLog.getInstance().i("即将跳转的页面是：" + layoutId);
		try {
			ActivityFactory activityType = ActivityFactory.valueOf(layoutId.toUpperCase(Locale.getDefault()));
			handler = activityType.getService();
			if (handler == null) {
				return;
			}
			LinearLayout layout = handler.onCreate(this);
			setContentView(layout);
		} catch (Exception e) {
			YhSdkLog.getInstance().e("在展示页面的时候出现异常：", e);
			return; 
		}
		handler.onSetWindows(this);
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		handler.onActivityResult(this, requestCode, resultCode, data);
	}
	@Override
	protected void onResume() {
		super.onResume();
		handler.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.onDestroy();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.onBackPressed();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		handler.onSetWindows(this);
	}
	
	

	
}
