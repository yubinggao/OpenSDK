package com.hanfeng.guildsdk.activity;

import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanfeng.guildsdk.bean.FloatSendBean;
import com.hanfeng.guildsdk.services.JavaScriptService;
import com.hanfeng.guildsdk.tool.CommonTool;
import com.hanfeng.guildsdk.tool.YhSDKRes;

/**
 * 嵌套网页，浮标点击跳转到该activity
 */
public class FloatWindowActivity extends Activity {
	private ImageView mIvBack;
	private ImageView mIvLeft;
	private WebView mWvMain;
	private String mUrl;
	private String paramStr;
	private TextView mTvTitle;
	private String TAG = "hanfengsdk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(YhSDKRes.getRes().getLayoutId(this, "yhsdk_layout_float_ui"));
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		// 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		// 窗口全屏
		setFinishOnTouchOutside(false);

		initView();
		initData();
		initEvent();
		
		
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(CommonTool.getResourceId(this, "iv_back", "id"));
		mIvLeft = (ImageView) findViewById(CommonTool.getResourceId(this, "iv_left", "id"));
		mWvMain = (WebView) findViewById(CommonTool.getResourceId(this, "wv_main", "id"));
		mTvTitle = (TextView) findViewById(CommonTool.getResourceId(this, "yhsdk_float_main", "id"));
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		mUrl = getIntent().getStringExtra("mUrl");
		paramStr = getIntent().getStringExtra("paramStr");
		FloatSendBean floatSendBean = (FloatSendBean) getIntent().getSerializableExtra("floatSendBean");
		mWvMain.getSettings().setJavaScriptEnabled(true);
		mWvMain.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWvMain.getSettings().setBuiltInZoomControls(false);
		mWvMain.getSettings().setUseWideViewPort(true);
		mWvMain.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWvMain.loadData("", "text/html", null);
		mWvMain.setDrawingCacheEnabled(true);
		//mWvMain.postUrl(mUrl, EncodingUtils.getBytes(postDate, "BASE64"));
		//Log.d(TAG, "使用Get方式提交数据");
		mWvMain.addJavascriptInterface(new JavaScriptService(this, floatSendBean, FloatWindowActivity.this), "android");
		mWvMain.loadUrl(mUrl + "?" + paramStr);
		//Log.d(TAG, "请求URL：" + mUrl + "?" + paramStr);
		mWvMain.reload();
		mTvTitle.setText("");
		
		mWvMain.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				mWvMain.loadUrl(url);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				//Log.d(TAG, "监控URL:" + url);
				if (url.contains("isshowback=true")) {
					Log.d(TAG, "设置显示返回按钮");
					mIvLeft.setVisibility(View.VISIBLE);
				}

				if (url.contains("isshowback=false")) {
					Log.d(TAG, "设置隐藏返回按钮");
					mIvLeft.setVisibility(View.GONE);
				}

				String decodeInfo = "utf-8";
				if (url.contains("decodeinfo=")) {
					Log.d(TAG, "设置URL编码信息");
					try {
						decodeInfo = Uri.parse(url).getQueryParameter("decodeinfo");
					} catch (Exception e) {
						Log.d(TAG, "设置URL编码信息出错");
						e.printStackTrace();
					}
				}

				if (url.contains("titlecontent=")) {
					try {
						String titleInfo = Uri.parse(url).getQueryParameter("titlecontent");
						titleInfo = URLDecoder.decode(titleInfo, decodeInfo);
						mTvTitle.setText(titleInfo);
						Log.d(TAG, "设置标题信息:" + titleInfo);
					} catch (Exception e) {
						Log.d(TAG, "解析URL标题信息出错");
						e.printStackTrace();
					}
				}
				if (url.contains("/UserAPP/userinfo")) {
					Log.d(TAG, "浮标：用户中心页面");
					mTvTitle.setText("账户");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/removeBindMobile")) {
					Log.d(TAG, "浮标：变更绑定页面");
					mTvTitle.setText("密保手机");
					mIvLeft.setVisibility(View.VISIBLE);
				} else if (url.contains("/UserAPP/bindMobileNum")) {
					Log.d(TAG, "浮标：添加绑定手机");
					mTvTitle.setText("密保手机");
					mIvLeft.setVisibility(View.VISIBLE);
				} else if (url.contains("/UserAPP/changeAppPW")) {
					Log.d(TAG, "浮标：设定密码");
					mTvTitle.setText("设定密码");
					mIvLeft.setVisibility(View.VISIBLE);
				} else if (url.contains("/UserAPP/messageNew")) {
					Log.d(TAG, "浮标：消息");
					mTvTitle.setText("消息");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/myGiftBags")) {
					Log.d(TAG, "浮标：进入礼包页面");
					mTvTitle.setText("礼包");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/helpMessage")) {
					Log.d(TAG, "浮标：进入帮助页面");
					mTvTitle.setText("帮助");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/restartLogin")) {
					Log.d(TAG, "浮标：进入提示页面");
					mTvTitle.setText("提示");
					mIvLeft.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void initEvent() {
		mIvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//YhSDK.getInstance().showToolBar(FloatWindowActivity.this);
				FloatWindowActivity.this.finish();
			}
		});

		mIvLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//mWvMain.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);	
				mWvMain.goBack();
			}
		});
	}
}
