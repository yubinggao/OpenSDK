package com.hanfeng.guildsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanfeng.guildsdk.tool.CommonTool;
import com.hanfeng.guildsdk.tool.YhSDKRes;

/**
 * 浮窗点击‘账户’跳转页面
 */
public class FloatMineActivity extends Activity {
	private ImageView mIvBack;
	private RelativeLayout mRlName;
	private RelativeLayout mRlUid;
	private RelativeLayout mRlPW;
	private RelativeLayout mRlBind;
	private Intent mIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(YhSDKRes.getRes().getLayoutId(this, "yhsdk_layout_mine_ui"));
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		// 设置横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		// 窗口全屏
		setFinishOnTouchOutside(false);
		
		initView();
		initData();
		initEvent();
	}
	private void initView(){
		mIvBack = (ImageView) findViewById(CommonTool.getResourceId(this,
				"iv_back", "id"));
		mRlName = (RelativeLayout) findViewById(CommonTool.getResourceId(this,
				"rl_name", "id"));
		mRlUid = (RelativeLayout) findViewById(CommonTool.getResourceId(this,
				"rl_uid", "id"));
		mRlPW = (RelativeLayout) findViewById(CommonTool.getResourceId(this,
				"rl_pw", "id"));
		mRlBind = (RelativeLayout) findViewById(CommonTool.getResourceId(this,
				"rl_bind", "id"));
	}
	private void initData(){
		if (mIntent == null) {
			mIntent = new Intent(FloatMineActivity.this, FloatWindowActivity.class);
		}
	}
	private void initEvent(){
		mIvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				FloatMineActivity.this.finish();
			}
		});
		mRlName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mIntent.putExtra("url", "www.baidu.com");
				startActivity(mIntent);
			}
		});
		mRlPW.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mIntent.putExtra("url", "www.baidu.com");
				startActivity(mIntent);
			}
		});
		mRlBind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mIntent.putExtra("url", "www.baidu.com");
				startActivity(mIntent);
			}
		});
	}
}
