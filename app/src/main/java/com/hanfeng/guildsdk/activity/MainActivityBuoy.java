package com.hanfeng.guildsdk.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hanfeng.guildsdk.AppInfo;
import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.Mode;
import com.hanfeng.guildsdk.R;
import com.hanfeng.guildsdk.YhCallbackListener;
import com.hanfeng.guildsdk.YhSDK;
import com.hanfeng.guildsdk.YhStatusCode;
import com.hanfeng.guildsdk.exception.YhSDKException;
import com.hanfeng.guildsdk.widget.OkDialog;
import com.reyun.sdk.ReYunTrack;
import com.tendcloud.appcpa.TalkingDataAppCpa;
/**
 * 测试的activity的，不要打到jar中
 * */
public class MainActivityBuoy extends Activity {
	private static final String TAG = "MainActivity";
	private static boolean loginStats = false;
	private Button testbtu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Constants.isPORTRAIT = true;
		
		AppInfo appInfo = new AppInfo();
		appInfo.appId = "80113";
		appInfo.appKey = "c35975397917b2f62031d0";
		
/*		private String appId = "10000";
		private String appKey = "424ae8a4fe9a342a1fbb64";*/
		
		appInfo.ext = "1|2";
		try {
			YhSDK.getInstance().setSdkMode(Mode.debug);
			YhSDK.getInstance().init(this, appInfo,
					new YhCallbackListener<String>() {
						@Override
						public void callback(int code, String response) {
							Log.i(TAG, "初始化处理代码：" + code + "，返回信息：" + response);
							if (YhStatusCode.SUCCESS == code) {
								// TODO: 银汉SDK初始化成功的情况处理
								Toast.makeText(MainActivityBuoy.this, "初始化成功",
										Toast.LENGTH_SHORT).show();
							} else {
								// TODO: 银汉SDK初始化不成功的情况处理
								Toast.makeText(MainActivityBuoy.this, response,
										Toast.LENGTH_SHORT).show();
							}
						}
					});
		} catch (Exception e) {
			Toast.makeText(MainActivityBuoy.this,
					"YhSDK初始化异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		Button sid = (Button) findViewById(R.id.sidverfiy);
		sid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button button = (Button) findViewById(R.id.gameLoginBtn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					YhSDK.getInstance().login(MainActivityBuoy.this,
							new YhCallbackListener<String>() {
								@Override
								public void callback(int code, String response) {
									Log.i(TAG, "登录返回代码：" + code + "，返回信息是："
											+ response);
									if (YhStatusCode.SUCCESS == code) {
										loginStats = true;
										// TODO: 登录成功，response为sid，进行处理
										Toast.makeText(MainActivityBuoy.this,
												"登录成功", Toast.LENGTH_SHORT)
												.show();
//										只登陆一次，用sp 存储账号和密码
										SharedPreferences sp = getApplicationContext().getSharedPreferences("data_content",Context.MODE_PRIVATE);
										Editor edit = sp.edit();
										
										
									} else if (YhStatusCode.REG_SUCCESS == code) {
										// 登录成功，response为错误信息
										/*Toast.makeText(MainActivityBuoy.this,
												"调试信息：注册成功", Toast.LENGTH_SHORT)
												.show();*/
									} else {
										Toast.makeText(MainActivityBuoy.this,
												response, Toast.LENGTH_SHORT)
												.show();
									}
								}
							});
				} catch (Exception e) {
					Toast.makeText(MainActivityBuoy.this,
							"YhSDK登录异常：" + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
//		支付。。。。。。。。。。。。。。。。
		Button payBtn = (Button) findViewById(R.id.gamePayBtn);

		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					YhSDK.getInstance().startPay(MainActivityBuoy.this,
							"CP5_3456345","80113" , "猎人金币", 1, "时空猎人",new YhCallbackListener<String>() {

								@Override
								public void callback(int code, String response) {
									// TODO Auto-generated method stub
									
								}
							});
				} catch (YhSDKException e) {
					Toast.makeText(MainActivityBuoy.this, e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
//		提交游戏信息
		Button sub = (Button) findViewById(R.id.subInfo);
		sub.setOnClickListener(new OnClickListener() {
			/**
			 * @param context
			 * @param uid  
			 * @param roleId  角色ID 
			 * @param roleName 角色名
			 * @param roleLevel 角色等级
			 * @param zoneId  区ID
			 * @param zoneName 区名称 
			 * @param dataType 数据类型
			 * @throws Exception
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					YhSDK.getInstance().submitGameInfo(MainActivityBuoy.this, "uid:111", "123", "张三", "129", "9527", "齐云楼", "1");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		
		Button verfiry = (Button) findViewById(R.id.verfersid);
		verfiry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				YhSDK.getInstance().verify(context, sid, channel, version, userId, gameId);
			}
		});
		
		
		testbtu = (Button) findViewById(R.id.testBtn);
		testbtu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (testbtu.getText().toString().trim().equals("竖屏")) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//					YhSDK.getInstance().setOrientation(
//							ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					testbtu.setText("横屏");
				} else {
					testbtu.setText("竖屏");
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//					YhSDK.getInstance().setOrientation(
//							ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}

			}
		});
		Button testbtu1 = (Button) findViewById(R.id.testBtn1);
		testbtu1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivityBuoy.this, "成功点击测试按钮",
						Toast.LENGTH_SHORT).show();
			  Dialog	dailog =new OkDialog(MainActivityBuoy.this, "银联支付结果通知", "支付成功", false);
			  dailog.show();
			}
		});
//13787145134
		Button showFloatBtn = (Button) findViewById(R.id.showFloatBtn);
		showFloatBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				YhSDK.getInstance().showToolBar(MainActivityBuoy.this);
			}
		});

		Button hideFloatBtn = (Button) findViewById(R.id.hideFloatBtn);
		hideFloatBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				YhSDK.getInstance().hideToolBar(MainActivityBuoy.this);
			}
		});

		//YhSDK.getInstance().hideToolBar(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//YhSDK.getInstance().showToolBar(this);
//		 Toast.makeText(MainActivityBuoy.this, "显示浮标",
//		 Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//YhSDK.getInstance().hideToolBar(this);
	}

	@Override
	protected void onDestroy() {
		YhSDK.getInstance().onSdkDestory(this);
		ReYunTrack.exitSdk();
		super.onDestroy();
	}
}
