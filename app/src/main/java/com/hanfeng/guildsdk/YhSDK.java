package com.hanfeng.guildsdk;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hanfeng.guildsdk.activity.ActivityFactory;
import com.hanfeng.guildsdk.bean.Coupon;
import com.hanfeng.guildsdk.bean.RoleInfo;
import com.hanfeng.guildsdk.bean.TDandRyCallListenerBean;
import com.hanfeng.guildsdk.bean.VerifyBean;
import com.hanfeng.guildsdk.exception.YhSDKException;
import com.hanfeng.guildsdk.services.DeviceInfo;
import com.hanfeng.guildsdk.services.Dispatcher;
import com.hanfeng.guildsdk.tool.AssetTool;
import com.hanfeng.guildsdk.tool.CommonTool;
import com.hanfeng.guildsdk.tool.HttpTool;
import com.hanfeng.guildsdk.tool.HttpTool.HttpResult;
import com.hanfeng.guildsdk.tool.MD5Tool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.FloatWindowMgr;
import com.hanfeng.guildsdk.widget.YhSdkToast;
import com.reyun.sdk.ReYunTrack;
import com.tendcloud.appcpa.TalkingDataAppCpa;

public final class YhSDK {
	private static YhSDK instance = null;
	private boolean isINIT = false;
	private AssetTool prop = null;
	private YhSdkLog log = null;
	public static final String TAG =  "ARSDK";
	private YhSDK() {
		prop = AssetTool.getInstance();
		log = YhSdkLog.getInstance();
	}
	/**
	 * 传入参数和配置的检验函数
	 * 
	 * @param context
	 * @param listener
	 * @throws YhSDKException
	 */
	private void checkNullListener(Context context, YhCallbackListener<?> listener) throws YhSDKException {
		if (context == null) {
			throw new YhSDKException(prop.getLangProperty(context, "context_is_null"));
		}

		if (listener == null) {
			throw new YhSDKException(prop.getLangProperty(context, "listener_is_null"));
		}
	}

	/**
	 * 设置环境
	 * 
	 * @param mode
	 *            设置SDK链接的环境，Mode.release为正式环境，Mode.debug为测试环境
	 */
	public void setSdkMode(Mode mode) {
		if (mode == null) {
			mode = Mode.release;
			return;
		}
		Constants.mode = mode;
	}

	/**
	 * 银汉SDK初始化
	 * 
	 * @param context
	 *            上下文
	 * @param info
	 *            游戏信息
	 * @param listener
	 *            回调侦听器
	 * @throws YhSDKException
	 */
	public void init(final Context context, final AppInfo info, final YhCallbackListener<String> listener) throws YhSDKException {
		log.i("[ init ] 初始化状态：" + isINIT);
		// 重新设置输入密码错误次数
		Dispatcher.getInstance().resetErrorTime();
		checkNullListener(context, listener);

		if (info == null) {
			throw new YhSDKException(prop.getLangProperty(context, "init_appinfo_is_null"));
		}

		if (isINIT) {
			listener.callback(YhStatusCode.SUCCESS, prop.getLangProperty(context, "init_success"));
			return;
		}

		if (!DeviceInfo.isNetAvailable(context)) {
			listener.callback(YhStatusCode.NET_UNAVAILABLE, prop.getLangProperty(context, "net_unavailable"));
			return;
		}

		Constants.DEVICE_INFO = DeviceInfo.init(context);
		Constants.init();

		final Dialog dialog = UITool.getLoadingDialog(prop.getLangProperty(context, "init_progress"), context);
		dialog.show();
		StringBuilder preSign = new StringBuilder();
		preSign.append("appId=").append(info.appId);
		preSign.append("&type=").append(Constants.PLATFORM);
		preSign.append("&packageName=").append(context.getPackageName());
		preSign.append("&version=").append(Constants.VERSION);
		preSign.append("&ip=").append(Constants.DEVICE_INFO.ip);
		preSign.append("&mac=").append(Constants.DEVICE_INFO.mac);
		preSign.append("&imei=").append(Constants.DEVICE_INFO.imei);
		preSign.append("||").append(info.appKey);
		String sign = MD5Tool.calcMD5(preSign.toString().getBytes());

		JSONObject json = new JSONObject();
		try {
			json.put("appId", info.appId);
			json.put("type", Constants.PLATFORM);
			json.put("packageName", context.getPackageName());
			json.put("version", Constants.VERSION);
			json.put("ip", Constants.DEVICE_INFO.ip);
			json.put("mac", Constants.DEVICE_INFO.mac);
			json.put("imei", Constants.DEVICE_INFO.imei);
			json.put("channel",Constants.DEVICE_INFO.channel);
			json.put("sign", sign);
		} catch (JSONException e) {
			throw new YhSDKException(prop.getLangProperty(context, "param_error"));
		}

		
		String param = json.toString();

		Log.i("YYSDK", "init"+param);
		AsyncTask<String, Integer, HttpResult> iniTask = new AsyncTask<String, Integer, HttpResult>() {
			@Override
			protected HttpResult doInBackground(String... param) {
				if (param == null) {
					listener.callback(YhStatusCode.INIT_FAIL, prop.getLangProperty(context, "param_is_null"));
					return null;
				}
				HttpResult result = new HttpResult();
				try {
					result = HttpTool.post(Constants.getInitUrl(), HttpTool.CONNECT_TIMEOUT2, param[0]);
					if (result != null && result.code != 200) {
						Constants.AUTH_SERVER_URL = Constants.AUTH_SERVER_URL2;
						result = HttpTool.post(Constants.getInitUrl(), HttpTool.CONNECT_TIMEOUT2, param[0]);
						if (result != null && result.code != 200) {
							Constants.AUTH_SERVER_URL = Constants.AUTH_SERVER_URL1;
							result = HttpTool.post(Constants.getInitUrl(), HttpTool.CONNECT_TIMEOUT, param[0]);
						}
					}
					return result;
				} catch (Exception e) {
					log.e("[ INIT ] 初始化网络异常：", e);
					listener.callback(YhStatusCode.INIT_FAIL, prop.getLangProperty(context, "init_http_error"));
					return null;
				}
			}

			@Override
			protected void onPostExecute(HttpResult result) {
				super.onPostExecute(result);
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				if (result == null) {
					return;
				}

				log.v("初始化返回结果：" + result.message);
				if (result.code == 0) {
					listener.callback(YhStatusCode.SERVER_ERROR, prop.getLangProperty(context, "http_error"));
				} else if (result.code != 200) {
					listener.callback(YhStatusCode.SERVER_ERROR, prop.getLangProperty(context, "response_error"));
				} else if (TextUtils.isEmpty(result.message)) {
					listener.callback(YhStatusCode.RESULT_FORMAT_ERROR, prop.getLangProperty(context, "result_format_error"));
				} else {
					try {
						JSONObject json = new JSONObject(result.message);
						if ("YHCSH_000".equals(json.getString("status"))) {
							isINIT = true;
							Constants.APPINFO = info;
							Constants.COMPANY = json.getString("msg");
							
							Toast.makeText(context, "初始化成功", Toast.LENGTH_SHORT).show();
							listener.callback(YhStatusCode.SUCCESS, "初始化成功");
						} else {
							listener.callback(YhStatusCode.CLIENT_INVALID, prop.getLangProperty(context, "init_sdk_invalid"));
						}
					} catch (JSONException e) {
						listener.callback(YhStatusCode.RESULT_FORMAT_ERROR, prop.getLangProperty(context, "result_format_error"));
						return;
					}
				}
			}
		};

		iniTask.execute(param);
	}

	/**
	 * 调起SDK登录界面
	 * 
	 * @param context
	 *            上下文
	 * @param listener
	 *            回调侦听器
	 * @throws YhSDKException
	 */
	public void login(Context context, YhCallbackListener<String> listener) throws YhSDKException {
		if (CommonTool.isFastClick()) {
			return;
		}
		log.i("[ yhsdk登录 ] 初始换状态：" + isINIT);
		if (!isINIT) {
			throw new YhSDKException(prop.getLangProperty(context, "sdk_not_init"));
		}
		checkNullListener(context, listener);
		Dispatcher.getInstance().listener = listener;
		Intent intent = new Intent(context, YhSDKActivity.class);
		String layoutId = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		;
		intent.putExtra("layoutId", layoutId);
		context.startActivity(intent);
	}

	/**
	 * 兼容一哥哥用户保存接口
	 * 
	 * @param context
	 *            上下文
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param listener
	 * @throws YhSDKException
	 */
	public void login(Context context, String username, String password, YhCallbackListener<String> listener) throws YhSDKException {
		if (CommonTool.isFastClick()) {
			return;
		}
		log.i("进入一哥哥登录接口201510211556");
		if (!isINIT) {
			throw new YhSDKException(prop.getLangProperty(context, "sdk_not_init"));
		}
		checkNullListener(context, listener);
		Dispatcher.getInstance().listener = listener;
		Intent intent = new Intent(context, YhSDKActivity.class);
		String layoutId = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		;
		if (username != null && username.length() > 0) {
			intent.putExtra("username", username);
			intent.putExtra("password", password == null ? "" : password);
		}
		intent.putExtra("layoutId", layoutId);
		log.i("进入" + layoutId);
		context.startActivity(intent);
	}

	/**
	 * 发起YhSDK支付
	 * 
	 * @param context
	 *            上下文
	 * @param cpOrderId
	 *            渠道订单号
	 * @param uid
	 *            用户ID
	 * @param goodsName
	 *            商品名称
	 * @param price
	 *            商品价格
	 * @param gameName
	 *            游戏名称
	 * @throws YhSDKException
	 */
	public void startPay(final Context context, String cpOrderId, String uid, 
			String goodsName, int price, String gameName, YhCallbackListener<String> listener) throws YhSDKException {
		if (CommonTool.isFastClick()) {
			return;
		}
		checkNullListener(context, listener);
		Dispatcher.getInstance().listener = listener;
		log.i("[ 支付 ] 初始化状态：" + isINIT);
		if (!isINIT) {
			throw new YhSDKException(prop.getLangProperty(context, "sdk_not_init"));
		}

		if (context == null) {
			throw new YhSDKException(prop.getLangProperty(context, "context_is_null"));
		}

		if (TextUtils.isEmpty(cpOrderId)) {
			throw new YhSDKException(prop.getLangProperty(context, "pay_cporderid_is_null"));
		}

		if (TextUtils.isEmpty(uid)) {
			throw new YhSDKException(prop.getLangProperty(context, "pay_uid_is_null"));
		}

		if (TextUtils.isEmpty(goodsName)) {
			throw new YhSDKException(prop.getLangProperty(context, "pay_goodsname_is_null"));
		}

		if (price <= 0) {
			throw new YhSDKException(prop.getLangProperty(context, "pay_price_value_exception"));
		}

		if (TextUtils.isEmpty(gameName)) {
			throw new YhSDKException(prop.getLangProperty(context, "pay_gamename_is_null"));
		}

		if (TextUtils.isEmpty(Constants.DEVICE_INFO.sid)) {
			YhSdkToast.getInstance().show(context, prop.getLangProperty(context, "pay_nologin"));
			return;
		}

		// 签名
		final Intent intent = new Intent(context, YhSDKActivity.class);
		intent.putExtra("layoutId", ActivityFactory.PAY_ACTIVITY.toString());
		OrderInfo info = new OrderInfo();
		info.cpOrderId = cpOrderId;
		info.uid = uid;
		info.gameName = gameName;
		info.goodsName = goodsName;
		info.price = price;
		Constants.ORDER_INFO = info;
		try {
			Dispatcher.getInstance().getMyCouponCoin((Activity) context, uid, new RequestCallback() {
				@Override
				public void callback(String code, String allCouponsJson) {
					boolean hasCoupon = false;
					if ("getAllMyCoupon".equals(code)) {
						List<Coupon> coupons = CommonTool.getCouponsFromJsonStr(allCouponsJson);
						if (coupons != null && coupons.size() > 0) {
							hasCoupon = true;
						}
					}
					intent.putExtra("hasCoupon", hasCoupon);
					context.startActivity(intent);
				}
			});
		} catch (Exception e) {
			intent.putExtra("hasCoupon", false);
			context.startActivity(intent);
		}

	}

	/*public String sid;
	public String channel;
	public String version;
	public String userId;
	public String gameId;
	*/
	public  void verify(final Context context,String sid,String channel ,String version,String userId,String gameId)  throws Exception{
		if (context == null) {
			throw new YhSDKException(prop.getLangProperty(context, "context_is_null"));
		}
		if (TextUtils.isEmpty(sid)) {
			throw new YhSDKException("sid为空");
		}
		if (TextUtils.isEmpty(channel)) {
			throw new YhSDKException("channel为空");
		}
		if (TextUtils.isEmpty(version)) {
			throw new YhSDKException("version为空");
		}
		if (TextUtils.isEmpty(userId)) {
			throw new YhSDKException("userId为空");
		}
		if (TextUtils.isEmpty(gameId)) {
			throw new YhSDKException("gameId为空");
		}
		/*String gameId= verifyBean.gameId;
		String sid = verifyBean.sid;
		String uid = verifyBean.userId;
		String channel = verifyBean.channel;
		String version = verifyBean.version;*/
		VerifyBean bean = new VerifyBean();
		bean.sid = sid;
		bean.userId = userId;
		bean.channel = channel;
		bean.gameId = gameId;
		bean.version = version;
		
		try {
			Dispatcher.getInstance().verify((Activity) context, bean);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	
	
	
	
	
	
	/**
		提交玩家游戏信息
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
	public void submitGameInfo(final Context context, String userId, String roleId, String roleName, String roleLevel, String zoneId, String zoneName, String dataType) throws Exception {
		if (context == null) {
			throw new YhSDKException(prop.getLangProperty(context, "context_is_null"));
		}

		if (TextUtils.isEmpty(roleId)) {
			throw new YhSDKException("roleId为空");
		}
		if (TextUtils.isEmpty(roleName)) {
			throw new YhSDKException("roleName为空");
		}
		if (TextUtils.isEmpty(roleLevel)) {
			throw new YhSDKException("roleLevel为空");
		}
		if (TextUtils.isEmpty(zoneId)) {
			throw new YhSDKException("zoneId为空");
		}
		if (TextUtils.isEmpty(zoneName)) {
			throw new YhSDKException("zoneName为空");
		}
		if (TextUtils.isEmpty(dataType)) {
			throw new YhSDKException("dataType为空");
		}

		RoleInfo roleinfo = new RoleInfo();
		roleinfo.userId = userId;
		roleinfo.roleId = roleId;
		roleinfo.roleName = roleName;
		roleinfo.roleLevel = roleLevel;
		roleinfo.zoneId = zoneId;
		roleinfo.zoneName = zoneName;
		roleinfo.dataType = dataType;

		try {
			Dispatcher.getInstance().submitGameInfo((Activity) context, roleinfo);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void logout() {
		isINIT = false;
		Constants.APPINFO = null;
		Constants.DEVICE_INFO = null;
	}

	public static synchronized YhSDK getInstance() {
		if (instance == null) {
			instance = new YhSDK();
		}
		return instance;
	}

	public void showToolBar(Context context) {
		if (isINIT && Constants.DEVICE_INFO.sid != null) {
			FloatWindowMgr.showSmallwin(context);
		}
	}

	public void hideToolBar(Context context) {
		FloatWindowMgr.removeAllwin(context);
	}

	public void onSdkDestory(Context context) {
		FloatWindowMgr.removeAllwin(context);
	}
}
