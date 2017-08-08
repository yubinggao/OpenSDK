package com.hanfeng.guildsdk.services;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.activity.FloatWindowActivity;
import com.hanfeng.guildsdk.bean.FloatSendBean;
import com.hanfeng.guildsdk.tool.MD5Tool;

import org.json.JSONObject;

/**
 * Created by lidongjie on 2016-11-21.
 */

public class JavaScriptService {
	private Context context;
	private FloatSendBean floatSendBean;
	private FloatWindowActivity activity;
	private static IDataService dao = null;
	private String TAG = "hanfengsdk";

	public JavaScriptService(Context c, FloatSendBean floatSendBean, FloatWindowActivity activity) {
		this.context = c;
		this.floatSendBean = floatSendBean;
		this.activity = activity;
	}

	/**
	 * 获取appid和appkey
	 */
	@JavascriptInterface
	public String getAppIdAndKey() {
		String result = floatSendBean.getAppId() + "_" + floatSendBean.getAppKey();
		Log.d("hanfengsdk", "获取appId和Key");
		return result;
	}

	/**
	 * 修改密码操作
	 */
	@JavascriptInterface
	public void changePasswd(String passwd) {
		dao = Dispatcher.getInstance().getIdaoFactory(context);
		JSONObject user = dao.readCurntUid(IDataService.UidType.account);
		try {
			String username = user.getString("username");
			dao.delteUid(username);
			dao.writeUid(IDataService.UidType.account, username, passwd);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "读取用户信息出错");
		}
		Toast.makeText(context, "修改密码成功！", Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示提示信息
	 */
	@JavascriptInterface
	public void showToast(String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	/**
	 * 关闭webview
	 */
	@JavascriptInterface
	public void closeWebView() {
		//YhSDK.getInstance().showToolBar(context);
		Log.d(TAG, "关闭浮标webview");
		activity.finish();
	}

	/**
	 * param参数+“||”+appkey加密md5
	 */
	@JavascriptInterface
	public String md5GetSign(String paramStr) {
		String sign = MD5Tool.calcMD5(paramStr + "||" + floatSendBean.getAppKey(), "UTF-8");
		Log.d(TAG, "加密Sign：" + sign);
		return sign;
	}

	/**
	 * param参数+“||”+appkey加密md5
	 */
	@JavascriptInterface
	public String md5GetSignAuto(String paramStr) {
		String sign = MD5Tool.calcMD5(paramStr + floatSendBean.getAppKey(), "UTF-8");
		Log.d(TAG, "加密Sign：" + sign);
		return sign;
	}

	/**
	 * 获取channel信息
	 * 
	 * @author ldj
	 * @return
	 * @verson 2016年12月9日下午5:15:19
	 * @description 
	 */
	@JavascriptInterface
	public String getChannelInfo() {
		String channel = Constants.DEVICE_INFO.channel;
		Log.d(TAG, "获取的Channel：" + channel);
		return channel;
	}
	
	/**
	 * 获取IP信息
	 * 
	 * @author ldj
	 * @return
	 * @verson 2016年12月9日下午5:16:25
	 * @description 
	 */
	@JavascriptInterface
	public String getIPInfo() {
		String ip = Constants.DEVICE_INFO.ip;
		Log.d(TAG, "获取的ip：" + ip);
		return ip;
	}
	
	/**
	 * 获取MAC
	 * 
	 * @author ldj
	 * @return
	 * @verson 2016年12月9日下午5:18:01
	 * @description 
	 */
	@JavascriptInterface
	public String getMacInfo() {
		String mac = Constants.DEVICE_INFO.mac;
		Log.d(TAG, "获取的mac：" + mac);
		return mac;
	}
	
	/**
	 * 获取imei
	 * 
	 * @author ldj
	 * @return
	 * @verson 2016年12月9日下午5:18:41
	 * @description 
	 */
	@JavascriptInterface
	public String getImeiInfo() {
		String imei = Constants.DEVICE_INFO.imei;
		Log.d(TAG, "获取的imei：" + imei);
		return imei;
	}
	
	/**
	 * 获取phoneNum
	 * 
	 * @author ldj
	 * @return
	 * @verson 2016年12月9日下午5:19:16
	 * @description 
	 */
	@JavascriptInterface
	public String getPhoneNumInfo() {
		String phoneNum = Constants.DEVICE_INFO.phoneNum;
		Log.d(TAG, "获取的phoneNum：" + phoneNum);
		return phoneNum;
	}
}
