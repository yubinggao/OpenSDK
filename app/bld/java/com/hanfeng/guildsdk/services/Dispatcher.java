package com.hanfeng.guildsdk.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.alipay.sdk.app.PayTask;
import com.reyun.sdk.ReYunTrack;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.ulopay.android.h5_library.manager.WebViewManager;
import com.unionpay.UPPayAssistEx;
import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.RequestCallback;
import com.hanfeng.guildsdk.YhCallbackListener;
import com.hanfeng.guildsdk.YhSDKActivity;
import com.hanfeng.guildsdk.YhStatusCode;
import com.hanfeng.guildsdk.activity.ActivityFactory;
import com.hanfeng.guildsdk.bean.Result;
import com.hanfeng.guildsdk.bean.RoleInfo;
import com.hanfeng.guildsdk.bean.TDandRyCallListenerBean;
import com.hanfeng.guildsdk.bean.Unifypay;
import com.hanfeng.guildsdk.bean.VerifyBean;
import com.hanfeng.guildsdk.services.IDataService.UidType;
import com.hanfeng.guildsdk.tool.AssetTool;
import com.hanfeng.guildsdk.tool.CommonTool;
import com.hanfeng.guildsdk.tool.DesTool;
import com.hanfeng.guildsdk.tool.MD5Tool;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.BrowserNotification;
import com.hanfeng.guildsdk.widget.OkDialog;
import com.hanfeng.guildsdk.widget.YhSdkToast;

/**
 * 业务处理
 */
public final class Dispatcher {
	private static Dispatcher processor = null;
	private static IDataService dao = null;
	private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
	private static final String TAG = "HFSDK";
	/***union data**/
	public static final int PLUGIN_VALID = 0;
	public static final int PLUGIN_NOT_INSTALLED = -1;
	public static final int PLUGIN_NEED_UPGRADE = 2;
	protected static final String LOG_TAG = "union_log";
	/***union data**/
	public YhCallbackListener<String> listener = null;

	private int errorTime = 0;
	private YhSdkLog log = null;

	private Dispatcher() {
		log = YhSdkLog.getInstance();
	}

	/**
	 * 首页账户登录
	 * @param activity
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void login(Activity activity, final CharSequence username, final CharSequence password) throws Exception {
		Intent intent = activity.getIntent();
		dao = getIdaoFactory(activity);
		if (ActivityFactory.PHONE_REGISTER_ACTIVITY.toString().equals(intent.getStringExtra("from"))) {
			String sid = intent.getStringExtra("sid");
			Constants.DEVICE_INFO.sid = sid;
			listener.callback(YhStatusCode.SUCCESS, sid);
			TDandRyCallListenerBean.loginAccount = username.toString();
			
			TalkingDataAppCpa.onLogin(username.toString());
//			热云：当用户有登陆、切换账号操作时调用setLoginSuccessBusiness方法。
			ReYunTrack.setLoginSuccessBusiness (username.toString());
			activity.finish();
			return;
		}
		// 记录错误次数，如果超过5次则需要输入验证码
		if (errorTime >= 4) {
			Map<String, String> intentMap = new HashMap<String, String>();
			intentMap.put("username", username.toString());
			intentMap.put("password", password.toString());
			showActivity(activity, ActivityFactory.PWD_ERROR_ACTIVITY, intentMap);
			return;
		}

		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("account", username.toString()));
				list.add(new Entry("password", password.toString()));
				return list;
			}
		}.create();

		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getLoginUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHDL_000".equals(message.getString("status"))) {
					String sid = message.getString("sid");
					
					Constants.DEVICE_INFO.sid = sid;
					dao.delteUid(username.toString());
					dao.writeUid(UidType.account, username.toString(), password.toString());
					TDandRyCallListenerBean.loginAccount = username.toString();
//					AT：在用户帐号登录成功的时候调用TalkingDataAppCpa的onLogin方法。
					TalkingDataAppCpa.onLogin(username.toString());
//					热云：当用户有登陆、切换账号操作时调用setLoginSuccessBusiness方法。
					ReYunTrack.setLoginSuccessBusiness (username.toString());
					activity.finish();
					errorTime = 0;
					listener.callback(YhStatusCode.SUCCESS, sid);
				} else if ("YHDL_001".equals(message.getString("status"))) {// 用户名或密码错误
					errorTime++;
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					listener.callback(YhStatusCode.ACCOUNT_LOGIN_FAIL, message.getString("msg"));
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					listener.callback(YhStatusCode.ACCOUNT_LOGIN_FAIL, message.getString("msg"));
				}
			}
		};
		task.execute(param);
	}

//	sid验证
	public  void verify(Activity activity,VerifyBean verifyBean){
		String appKey = Constants.APPINFO.appKey;
//		也就是appinfo.appId
		String gameId= verifyBean.gameId;
		String sid = verifyBean.sid;
		String userId = verifyBean.userId;
		String channel = verifyBean.channel;
		String version = verifyBean.version;
		StringBuilder sbSign = new StringBuilder();
		String preSign = gameId + "|" + channel + "|" + userId + "|" + sid + "|" + version + "|" + appKey;
		
		try {
			JSONObject object = new JSONObject();
			object.put("sid", sid);
			object.put("channel", channel);
			object.put("version", version);
			object.put("userId", userId);
			object.put("gameId", gameId);
			object.put("sign", MD5Tool.calcMD5(preSign.getBytes()));
			
			RequestBody requestBody = RequestBody.create(JSON, object.toString().trim());
			Log.i(TAG, "hfsdk验证url:"+Constants.getVerifyUrl());
			Request request = new Request.Builder().url(Constants.getVerifyUrl()).post(requestBody).build();
			OkHttpClient mOkHttpClient = new OkHttpClient();
			mOkHttpClient.newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					// TODO Auto-generated method stub
					if (response != null && response.isSuccessful()) {
					/*	String status = message.getString("status");
						if ("YHYZ_000".equals(status)) {
							uid = message.getString("userId");
							resulttv.setText( message.getString("msg") + "，返回的uid为：" + uid);
						} else {
							resulttv.setText(message.getString("msg"));
						}*/
						String responseBody = response.body().toString();
						try {
							JSONObject  json = new JSONObject(responseBody);
							String status = json.getString("status");
							String uid = json.getString("userId");
							Log.i(TAG, "hfsdk：sid验证success：uid:"+uid);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				
				@Override
				public void onFailure(Call call, IOException e) {
					// TODO Auto-generated method stub
					Log.i(TAG, "hfsdk：sid请求失败："+e);
				}
			});
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	//	提交游戏信息
	public void submitGameInfo(Activity activity, RoleInfo roleinfo) throws Exception {

		String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;

		String userId = roleinfo.userId;
		String roleId = roleinfo.roleId;
		String roleName = roleinfo.roleName;
		String roleLevel = roleinfo.roleLevel;
		String zoneId = roleinfo.zoneId;
		String zoneName = roleinfo.zoneName;
		String dataType = roleinfo.dataType;

		StringBuilder sbSign = new StringBuilder();
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("roleId=").append(roleId).append("&");
		sbSign.append("roleName=").append(roleName).append("&");
		sbSign.append("roleLevel=").append(roleLevel).append("&");
		sbSign.append("zoneId=").append(zoneId).append("&");
		sbSign.append("zoneName=").append(zoneName).append("&");
		sbSign.append("dataType=").append(dataType).append("||").append(appKey);
		

		Log.i(TAG, "提交游戏信息：sbSign:" + sbSign);

		JSONObject param = new JSONObject();
		try {
			param.put("appId", appId);
			param.put("userId", userId);
			param.put("roleId", roleId);
			param.put("roleName", roleName);
			param.put("roleLevel", roleLevel);
			param.put("zoneId", zoneId);
			param.put("zoneName", zoneName);
			param.put("dataType", dataType);
			param.put("ip", Constants.DEVICE_INFO.ip);
			param.put("mac", Constants.DEVICE_INFO.mac);
			param.put("imei", Constants.DEVICE_INFO.imei);
			param.put("platformType", "2");
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));

			Log.i(TAG, "提交游戏信息：param:" + param);

		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("提交游戏信息-数据组装异常", e);
		}

		//		String  interviewUrl="http://203.195.145.113:4003/game/subPlayerInfo";
		RequestBody requestBody = RequestBody.create(JSON, param.toString().trim());
		Request request = new Request.Builder().url(Constants.getsubmitGameInfoUrl()).post(requestBody).build();
		OkHttpClient mOkHttpClient = new OkHttpClient();
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				// TODO Auto-generated method stub
				if (response != null && response.isSuccessful()) {
					//					服务器回传响应体
					String responseBody = response.body().toString();
					Log.i(TAG, "成功：getsubmitGameInfo为：" + responseBody);
				}
			}
			@Override
			public void onFailure(Call call, IOException e) {
				// TODO Auto-generated method stub
				Log.i(TAG, "失败：getsubmitGameInfo：" + e.toString());
			}
		});
	}

	/**
	 * 输错5次密码后，
	 * 
	 * @param activity
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void pwdError(Activity activity) throws Exception {
		Intent intent = activity.getIntent();
		final String username = intent.getStringExtra("username");
		final String password = intent.getStringExtra("password");

		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("account", username));
				list.add(new Entry("password", password));
				return list;
			}
		}.create();

		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getLoginUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHDL_000".equals(message.getString("status"))) {
					String sid = message.getString("sid");
					Constants.DEVICE_INFO.sid = sid;
					getIdaoFactory(activity).delteUid(username.toString());
					getIdaoFactory(activity).writeUid(UidType.account, username.toString(), password.toString());
					activity.finish();
					errorTime = 0;
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					listener.callback(YhStatusCode.SUCCESS, sid);
				} else if ("YHDL_001".equals(message.getString("status"))) {
					errorTime++;
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					map.put("password", password);
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					showActivity(activity, ActivityFactory.ACCOUNT_LOGIN_ACTIVITY, map);
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					listener.callback(YhStatusCode.ACCOUNT_LOGIN_FAIL, message.getString("msg"));
				}
			}
		};
		task.execute(param);
	}

	public void resetErrorTime() {
		errorTime = 0;
	}

	/**
	 * 检查账户是否存在
	 * @param activity
	 * @return
	 */
	public void checkAccount(Activity activity, final String account, final String uid) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("account", account));
				return list;
			}
		}.create();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getCheckAccountUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHYZXX_000".equals(message.getString("status"))) {
					YhSdkToast.getInstance().show(activity, "账号可用");
				} else {
					YhSdkToast.getInstance().show(activity, "您输入的账号已存在，系统推荐账号：" + message.getString("msg"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("uid", uid);
					map.put("account", message.getString("msg"));
					showActivity(activity, ActivityFactory.ACCOUNT_REGISTER_ACTIVITY, map);
				}
			}
		};
		task.execute(param);

	}

	/**
	 * 账号注册获取uid和账号,页面转向账号注册
	 * 
	 * @param activity
	 * @return
	 */
	public void getAccount(Activity activity) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				return list;
			}
		}.create();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getRegisterAccountUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZCXX_000".equals(message.getString("status"))) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("uid", message.getString("uid"));
					map.put("account", message.getString("account"));
					showActivity(activity, ActivityFactory.ACCOUNT_REGISTER_ACTIVITY, map);
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};
		task.execute(param);

	}

	/**
	 * 账号注册提交
	 * 
	 * @param activity
	 * @param account
	 * @param password
	 * @param uid
	 * @throws Exception
	 */
	public void register(Activity activity, final CharSequence account, final CharSequence password, final CharSequence uid) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("uid", uid.toString()));
				list.add(new Entry("password", password.toString()));
				list.add(new Entry("newAccount", account.toString()));
				return list;
			}
		}.create();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.ModifyAccountUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZC_000".equals(message.getString("status"))) {
					// 把用户信息写在本地
					YhSdkToast.getInstance().show(activity, AssetTool.getInstance().getLangProperty(activity, "register_success"));
					dao = getIdaoFactory(activity);
					dao.writeUid(UidType.account, account.toString(), password.toString());
					//					pushRegMsg(activity, account.toString());
					Constants.DEVICE_INFO.sid = message.getString("sid");
					try {
						listener.callback(YhStatusCode.REG_SUCCESS, "");
//						TDandRyCallListenerBean：注册
						TDandRyCallListenerBean.accountRegister = account.toString();
						TDandRyCallListenerBean.registerStyle = "account";
//						AT:用户帐号注册成功的时候调用TalkingDataAppCpa的onRegister方法。
						TalkingDataAppCpa.onRegister(account.toString());
//						热云：统计用户注册数据
						ReYunTrack.setRegisterWithAccountID (account.toString());
						
					} catch (Exception e) {

					}
					listener.callback(YhStatusCode.SUCCESS, message.getString("sid"));
					activity.finish();
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};
		task.execute(param);
	}

	/**
	 * 手机号注册获取验证码，不管是否是重新获取验证码都使用新的获取验证码的接口
	 * 
	 * @param activity
	 * @param mobile
	 *            手机号
	 * @param timer
	 *            倒数器，如果重发验证码情况，timer不为空。
	 * @throws Exception
	 */
	public void registerByMobileVcode(Activity activity, final Button regBtn, final CountDownTimer timer, final EditText regPhoneEdtx, final boolean isRepost) throws Exception {
		final String mobile = regPhoneEdtx.getText().toString();
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("mobile", mobile));
				return list;
			}
		}.create();
		final AssetTool asset = AssetTool.getInstance();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getPhoneRegisterVcode()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				String status = message.getString("status");
				if ("YHZCYZ_000".equals(status)) { // 手机号在本机已经登录了
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					timer.start();
				} else if ("YHZCXX_021".equals(status)) {// 手机号已经注册
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					regPhoneEdtx.setEnabled(true);
					regBtn.setText(asset.getLangProperty(activity, "phone_login_btn"));
				} else if ("YHYZXX_018".equals(status)) {// 手机号是新注册
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					timer.start();
				} else if ("YHYZCF_000".equals(status)) {// 重发手机验证码
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					timer.start();
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					regPhoneEdtx.setEnabled(true);
				}
			}
		};

		task.execute(param);
	}

	/**
	 * 手机注册并登录
	 * @param activity
	 * @param mobile
	 * @param vcode
	 * @throws Exception
	 */
	public void phoneRegisterAndLogin(Activity activity, final String mobile, final String vcode, final String password, final String status) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("mobile", mobile));
				list.add(new Entry("vcode", vcode));
				list.add(new Entry("password", password));
				return list;
			}
		}.create();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getPhoneRegisterUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZC_000".equals(message.getString("status"))) {
					String sid = message.getString("sid");
					Constants.DEVICE_INFO.sid = sid;
					dao.writeUid(UidType.account, mobile, password);
					try {
						listener.callback(YhStatusCode.REG_SUCCESS, "");
						TDandRyCallListenerBean.phoneRegister = mobile;
						TDandRyCallListenerBean.registerStyle = "mobile";
//						AD:在用户帐号注册成功的时候调用TalkingDataAppCpa的onRegister方法。
						TalkingDataAppCpa.onRegister(mobile);
//						热云：统计用户注册数据
						ReYunTrack.setRegisterWithAccountID (mobile);
					} catch (Exception e) {
					}
					listener.callback(YhStatusCode.SUCCESS, sid);
					activity.finish();
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};
		task.execute(param);
	}
	/**
	 * 重发手机重置密码的验证码
	 * @param activity
	 * @param mobile
	 *            手机号
	 * @param timer
	 *            倒数器，如果重发验证码情况，timer不为空。
	 * @throws Exception
	 */
	public void reSendPhoneResetPwdVcode(Activity activity, final String mobile, final CountDownTimer timer) throws Exception {
		dao = getIdaoFactory(activity);
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("mobile", mobile.toString()));
				return list;
			}
		}.create();

		HttpAsyncTask task = null;
		// 提交信息之后，才开始倒数，所以，重新获取信息的时候，timer是非空的
		task = new HttpAsyncTask(activity, Constants.getRegetVcodeUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				YhSdkToast.getInstance().show(activity, message.getString("msg"));
				if (timer != null) {
					timer.start();
				}
			}
		};
		task.execute(param);
	}
	/**
	 * 手机验证码+密码 重置密码
	 * @param activity
	 * @param mobile
	 * @param vcode
	 * @throws Exception
	 */
	public void phoneResetPassword(Activity activity, final String mobile, final String vcode, final String password, final String status) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("mobile", mobile));
				list.add(new Entry("vcode", vcode));
				list.add(new Entry("password", password));
				return list;
			}
		}.create();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getPhoneResetNewPassWordUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZHMM_012".equals(message.getString("status"))) {
					dao.writeUid(UidType.account, mobile, null);
					final AssetTool asset = AssetTool.getInstance();
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage(message.getString("msg"));
					builder.setInverseBackgroundForced(true);
					builder.setPositiveButton(asset.getLangProperty(activity, "register_phone_login"), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Map<String, String> intent = new HashMap<String, String>();
							intent.put("username", mobile);
							intent.put("password", password);
							intent.put("type", "m");
							showActivity(activity, ActivityFactory.ACCOUNT_LOGIN_ACTIVITY, intent);
						}
					});
					AlertDialog ad = builder.create();
					ad.setCanceledOnTouchOutside(false);
					ad.show();
				} else if ("YHYZXX_016".equals(message.getString("status"))) {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					listener.callback(YhStatusCode.VERIFY_CODE_ERROR, message.getString("msg"));
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};
		task.execute(param);
	}
	/**
	 * 往服务器请求该账户信息绑定信息情况
	 * @param activity
	 * @param uid
	 * @throws Exception
	 */
	public void loadFindPwdType(Activity activity, final String account) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("account", account));
				return list;
			}
		}.create();

		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getFindPwdAUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZHMM_000".equals(message.getString("status"))) {
					Map<String, String> intent = new HashMap<String, String>();
					intent.put("account", account);
					intent.put("phone", message.getString("phone"));
					intent.put("email", message.getString("email"));
					showActivity(activity, ActivityFactory.FIND_PWD_TYPE_ACTIVITY, intent);
				} else if ("YHZHMM_001".equals(message.getString("status"))) {
					final AssetTool asset = AssetTool.getInstance();
					OkDialog dialog = new OkDialog(activity, "提示", message.getString("msg"), true);
					dialog.show();
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};

		task.execute(param);
	}
	/**
	 * 手机验证码+重置密码第一次发送验证码转到验证码验证界面
	 * @param activity
	 * @param uid
	 * @param resetType
	 * @throws Exception
	 */
	public void sendPhoneResetPwdVcode(Activity activity, final String contract) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("mobile", contract));
				return list;
			}
		}.create();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getFindPassWordByMobile()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				Map<String, String> intent = new HashMap<String, String>();
				intent.put("contract", contract);
				if ("YHZHMM_005".equals(message.get("status"))) {
					showActivity(activity, ActivityFactory.PHONE_VERIFY_ACTIVITY, intent);
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};

		task.execute(param);
	}

	/**
	 * 账号重置密码 --绑定的邮箱 & 绑定的手机
	 * 
	 * @param activity
	 * @param uid
	 * @param resetType
	 *            绑定类型
	 * @throws Exception
	 */
	public void resetPwd(Activity activity, final String account, final int resetType, final String contract) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("account", account));
				list.add(new Entry("resetType", "" + resetType));
				return list;
			}
		}.create();
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getFindPwdBUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				Map<String, String> intent = new HashMap<String, String>();
				YhSdkLog.getInstance().i("--->找回类型：" + message);
				intent.put("contract", contract);
				if ("YHZHMM_005".equals(message.get("status"))) {
					intent.put("account", account);
					showActivity(activity, ActivityFactory.FIND_PWD_VCODE_ACTIVITY, intent);
				} else if ("YHZHMM_006".equals(message.get("status"))) {
					intent.put("findpwd_finish", "findpwd_email_finish");
					showActivity(activity, ActivityFactory.FIND_PWD_FINISH_ACTIVITY, intent);
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};

		task.execute(param);
	}

	/**
	 * 用户账号找回 ----手机重置密码-输入验证码+密码
	 * 
	 * @param activity
	 * @param account
	 *            账号
	 * @param vcode
	 *            验证码
	 * @param mobile
	 *            手机号
	 * @throws Exception
	 */
	public void resetUserPhonePwd(Activity activity, final String account, final String vcode, final String password, final String mobile) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Entry> list = new ArrayList<Entry>();
				list.add(new Entry("account", account));
				list.add(new Entry("vcode", vcode));
				list.add(new Entry("password", password));
				return list;
			}
		}.create();

		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getPhoneFindPwd()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZHMM_012".equals(message.get("status"))) {
					final AssetTool asset = AssetTool.getInstance();
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage(message.getString("msg"));
					builder.setInverseBackgroundForced(true);
					builder.setPositiveButton(asset.getLangProperty(activity, "register_phone_login"), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Map<String, String> intent = new HashMap<String, String>();
							intent.put("username", account);
							intent.put("password", password);
							intent.put("type", "u");
							showActivity(activity, ActivityFactory.ACCOUNT_LOGIN_ACTIVITY, intent);
						}
					});
					AlertDialog ad = builder.create();
					ad.setCanceledOnTouchOutside(false);
					ad.show();
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};

		task.execute(param);
	}

	/**
	 * 支付宝支付签名
	 * 
	 * @param activity
	 * @param cpOrderId
	 * @param uid
	 * @param price
	 * @param goodsName
	 * @throws Exception
	 */
	@SuppressLint("HandlerLeak")
	public void AlipaySign(final Activity activity, final String cpOrderId, final String uid, final int price, String goodsName, int couponmoney, int couponId) throws Exception {
		final int SDK_PAY_FLAG = 1;
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;

		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(Constants.DEVICE_INFO.channel).append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("coupon_id", couponId);
			param.put("coupon_amount", couponmoney);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
			param.put("type", Constants.PLATFORM);
			param.put("ext", Constants.APPINFO.ext);
			param.put("version", Constants.VERSION);
			param.put("channel", Constants.DEVICE_INFO.channel);
			param.put("ip", Constants.DEVICE_INFO.ip);
			param.put("mac", Constants.DEVICE_INFO.mac);
			param.put("imei", Constants.DEVICE_INFO.imei);
			param.put("platformType", "2");
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("支付宝签名-数据组装异常", e);
		}
//		热云准备支付
		TDandRyCallListenerBean.transactionId = cpOrderId;
		TDandRyCallListenerBean.paymentType = "alipay";
		TDandRyCallListenerBean.currencyType = "CNY";
		TDandRyCallListenerBean.currencyAmount = price+0.0f;
		
	ReYunTrack.setPaymentStart(cpOrderId,"alipay","CNY",price+0.0f);
		
		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == SDK_PAY_FLAG) {
					Result resultObj = new Result((String) msg.obj);
					String resultStatus = resultObj.resultStatus;
					AssetTool tool = AssetTool.getInstance();

					if (!TextUtils.equals(resultStatus, "6001")) {
						if (TextUtils.equals(resultStatus, "9000")) {
							YhSdkToast.getInstance().show(activity, tool.getLangProperty(activity, "pay_success"));
//							支付成功时调用TalkingDataAppCpa的onPay方法
//							public static void onPay(String userId , String orderId , int amount , String currency, String payType);
							TalkingDataAppCpa.onPay(uid, cpOrderId, price, "CNY", "支付宝");
//							TalkingDataAppCpa.onPay(uid, cpOrderId, price, "支付宝");
//							热云：阿里支付
							ReYunTrack.setPayment(cpOrderId,"alipay","CNY",price+0.0f);
							
							Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_SUCCESS, "支付宝支付成功");
							activity.finish();
							return;
						}

						if (TextUtils.equals(resultStatus, "8000")) {
							YhSdkToast.getInstance().show(activity, tool.getLangProperty(activity, "pay_confirm"));
							Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_CONFIRM, "支付宝支付确认中");
							activity.finish();
							return;
						}
						Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_FAIL, "支付宝支付失败");
						YhSdkToast.getInstance().show(activity, tool.getLangProperty(activity, "pay_failure"));
						activity.finish();
					}
				} else {
					Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_FAIL, "支付宝支付失败");
				}
			}
		};

		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getAlipaySignUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				log.v("支付宝返回信息：" + message);
				if ("YHZFQM_000".equals(message.getString("status"))) {
					final String orderInfo = message.getString("msg");
					log.v("解析订单后的信息：" + orderInfo);
					new Thread() {
						public void run() {
							Looper.prepare();
							PayTask alipay = new PayTask(activity);
							String result = alipay.pay(orderInfo);
							log.v("[支付宝] RQF_PAY=" + SDK_PAY_FLAG + ",result=\"" + result + "\"");
							Message msg = new Message();
							msg.what = SDK_PAY_FLAG;
							msg.obj = result;
							mHandler.sendMessage(msg);
						}
					}.start();
				} else {
					Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_FAIL, "支付宝支付失败");
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					activity.finish();
				}
			}
		};
		log.v("[支付宝充值] 游戏订单号：" + cpOrderId + "，用户ID：" + uid + "，充值金额：" + price + "，商品名称：" + goodsName);
		task.execute(param.toString());
	}

	/**
	 * 银联支付签名
	 * 
	 * @param activity
	 * @param cpOrderId
	 * @param uid
	 * @param price
	 * @param goodsName
	 * @throws Exception
	 */
	public void UnionSign(Activity activity, final String cpOrderId, final String uid, final int price, final String goodsName, int couponmoney, int couponId) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;

		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(Constants.DEVICE_INFO.channel).append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("coupon_id", couponId);
			param.put("coupon_amount", couponmoney);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
			param.put("type", Constants.PLATFORM);
			param.put("ext", Constants.APPINFO.ext);
			param.put("version", Constants.VERSION);
			param.put("channel", Constants.DEVICE_INFO.channel);
			param.put("ip", Constants.DEVICE_INFO.ip);
			param.put("mac", Constants.DEVICE_INFO.mac);
			param.put("imei", Constants.DEVICE_INFO.imei);
			param.put("platformType", "2");
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("银联签名-数据组装异常", e);
		}
		
//		热云准备支付
		TDandRyCallListenerBean.transactionId = cpOrderId;
		TDandRyCallListenerBean.paymentType = "unionpay";
		TDandRyCallListenerBean.currencyType = "CNY";
		TDandRyCallListenerBean.currencyAmount = price+0.0f;
		ReYunTrack.setPaymentStart(cpOrderId,"unionpay","CNY",price+0.0f);
		
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getUnionSignUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZFQM_000".equals(message.getString("status"))) {
					final String TN = message.getString("msg");
					
					log.v("[ 银联充值 ] 游戏订单号：" + cpOrderId + "，用户ID：" + uid + "，充值金额：" + price + "SDK订单号：" + TN + "，商品名称：" + goodsName);
					int ret = UPPayAssistEx.startPay(activity, null, null, TN, "00");// "00"为生产环境;"01"为测试环境
/*//					热云：银联支付
					TalkingDataAppCpa.onPay(uid, cpOrderId, price, "CNY", "unionpay");
//					TalkingDataAppCpa.onPay(uid, cpOrderId, price, "unionpay");
//					热云：微信支付
					ReYunTrack.setPayment(cpOrderId,"unionpay","CNY",price+0.0f);*/
					
					
					if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
						// 需要重新安装控件
						YhSdkLog.getInstance().e(" plugin not found or need upgrade!!!");
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setTitle("提示");
						builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

						builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								UPPayAssistEx.installUPPayPlugin(activity);	
								dialog.dismiss();
							}
						});

						builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
				} else {
					Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_FAIL, "银联支付失败");
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};
		task.execute(param.toString());
	}
	
	public void WeixinUnionPaySign(Activity activity, final  String cpOrderId, final String uid, 
			final int price, String goodsName, final int couponmoney,final int couponId,
			final RequestCallback callback) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("deviceIp=").append(CommonTool.getIp(activity)).append("&");
		sbSign.append("channel=").append(Constants.DEVICE_INFO.channel).append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("coupon_id", couponId);
			param.put("coupon_amount", couponmoney);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
			param.put("type", Constants.PLATFORM);
			param.put("channel", Constants.DEVICE_INFO.channel);
			param.put("ext", Constants.APPINFO.ext);
			param.put("deviceIp", CommonTool.getIp(activity));
			param.put("version", Constants.VERSION);
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("银联微信签名-数据组装异常", e);
		}
//		热云准备支付
		TDandRyCallListenerBean.transactionId = cpOrderId;
		TDandRyCallListenerBean.paymentType = "weixinpay";
		TDandRyCallListenerBean.currencyType = "CNY";
		TDandRyCallListenerBean.currencyAmount = price+0.0f;
		ReYunTrack.setPaymentStart(cpOrderId,"weixinpay","CNY",price+0.0f);
		
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getWeixinUnionpaySignUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				YhSdkLog.getInstance().i("银联微信生成订单返回：" + message.toString());
				if ("YHZFQM_000".equals(message.getString("status"))) {
					JSONObject jSONObject = new JSONObject(message.getString("msg"));
					if ("".equals(jSONObject.optString("sign", ""))) {
						YhSdkLog.getInstance().e("解析微信订单失败：" + message.getString("msg"));
						YhSdkToast.getInstance().show(activity, "生成订单失败");
//						activity.finish();
					} else {
						Unifypay unifypay = new Unifypay();
						unifypay.setSign(jSONObject.optString("sign", ""));
						unifypay.setPrepay_id(jSONObject.optString("prepay_id", ""));
						unifypay.setResult_code(jSONObject.optString("result_code", ""));
						unifypay.setMch_id(jSONObject.optString("mch_id", ""));
						unifypay.setPrepay_url(jSONObject.optString("prepay_url", ""));
						unifypay.setReturn_code(jSONObject.optString("return_code", ""));
						unifypay.setTrade_type(jSONObject.optString("trade_type", ""));
						unifypay.setReturn_msg(jSONObject.optString("return_msg", ""));
						unifypay.setErr_code(jSONObject.optString("err_code", ""));
						unifypay.setErr_code_des(jSONObject.optString("err_code_des", ""));
						unifypay.setOut_trade_no(jSONObject.optString("out_trade_no", ""));
						//分析结果
						if (unifypay != null && !"".equals(unifypay)) {
							if ("SUCCESS".equalsIgnoreCase(unifypay.getReturn_code())) {
								if ("SUCCESS".equalsIgnoreCase(unifypay.getResult_code())) {
									if (!TextUtils.isEmpty(unifypay.getPrepay_id())) {
										String prepay_url = unifypay.getPrepay_url() + "&type=android";
										YhSdkLog.getInstance().i("prepay_url-->:" + prepay_url);
										new WebViewManager(activity, true).showWeiXinView(prepay_url);
//										
										Constants.ULO_WX_URL=unifypay.getPrepay_url();
										callback.callback("weixin", unifypay.getPrepay_id());
										Log.i(TAG, "WXs:"+unifypay.getPrepay_id());
										
									}
								}
							} else {
								YhSdkToast.getInstance().show(activity, unifypay.getReturn_msg());
								Log.i(TAG, "WXf:unifypay"+unifypay.getReturn_msg());
							}
						} else {
							YhSdkToast.getInstance().show(activity, "系统繁忙");
						}
					}
				} else {
					Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_FAIL, "微信支付失败");
					
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					Log.i(TAG, "WXf:message"+ message.getString("msg"));
//					activity.finish();
				}
			}
		};
		task.execute(param.toString());
	}
	/**
	 * 微信支付签名
	 * 
	 * @param activity
	 * @param cpOrderId
	 * @param uid
	 * @param price
	 * @param goodsName
	 * @throws Exception
	 */
	@Deprecated
	public void WeixinPaySign(Activity activity, final String cpOrderId, final String uid, final int price, final String goodsName) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(Constants.DEVICE_INFO.channel).append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
			param.put("type", Constants.PLATFORM);
			param.put("ext", Constants.APPINFO.ext);
			param.put("version", Constants.VERSION);
			param.put("channel", Constants.DEVICE_INFO.channel);
			param.put("ip", Constants.DEVICE_INFO.ip);
			param.put("mac", Constants.DEVICE_INFO.mac);
			param.put("imei", Constants.DEVICE_INFO.imei);
			param.put("platformType", "2");
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("微信签名-数据组装异常", e);
		}
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getWeixinpaySignUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				if ("YHZFQM_000".equals(message.getString("status"))) {
					final String content = message.getString("msg");
					log.v("[ 微信支付 ] 游戏订单号：" + cpOrderId + "，用户ID：" + uid + "，充值金额：" + price + "，商品名称：" + goodsName);
					try {
					} catch (Exception e) {
						log.e("支付异常：" + e);
						YhSdkToast.getInstance().show(activity, "系统繁忙");
					}
					return;
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}

		};
		task.execute(param.toString());
	}

	/**
	 * 卡类支付签名
	 * 
	 * @param cpOrderId
	 *            cp签名订单id
	 * @param uid
	 *            当前用户ID
	 * @param price
	 *            商品金额
	 * @param cardPrice
	 *            充值卡面额
	 * @param cardType
	 *            充值卡类型
	 * @param account
	 *            充值卡序列号
	 * @param passwd
	 *            充值卡密码
	 */
	@Deprecated
	public void CardPay(Activity activity, String cpOrderId, String uid, int price, int cardPrice, String account, String passwd) throws Exception {
		log.v("[ 卡类充值 ] uid=" + uid + "，商品金额=" + price + "，充值卡面额=" + cardPrice);
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		StringBuilder sbInfo = new StringBuilder();
		String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;
		String cardInfo = "";
		sbInfo.append(price).append("|").append(cardPrice).append("|");
		sbInfo.append(account).append("|").append(passwd);
		try {
			if (appKey.length() > 8) {
				cardInfo = DesTool.encode(appKey.substring(0, 8), sbInfo.toString());
			} else {
				cardInfo = DesTool.encode(appKey, sbInfo.toString());
			}
		} catch (Exception e) {
			throw new Exception("神州付支付-加密支付信息异常：", e);
		}
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("cardType=").append(0).append("&");
		sbSign.append("cardInfo=").append(cardInfo).append("&");
		sbSign.append("channel=").append(Constants.DEVICE_INFO.channel).append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("uid", uid);
			param.put("channel", Constants.DEVICE_INFO.channel);
			param.put("cardType", 0);
			param.put("cardInfo", cardInfo);
			param.put("type", Constants.PLATFORM);
			param.put("ext", Constants.APPINFO.ext);
			param.put("version", Constants.VERSION);
			
			param.put("ip", Constants.DEVICE_INFO.ip);
			param.put("mac", Constants.DEVICE_INFO.mac);
			param.put("imei", Constants.DEVICE_INFO.imei);
			param.put("platformType", "2");
			
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("神州付签名-数据组装异常", e);
		}
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getShenzhoufuSignUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				final String result = message.getString("msg");
				YhSdkToast.getInstance().show(activity, result);
				if ("YHZFQM_000".equals(message.getString("status"))) {
					// 支付成功后隔1秒之后支付窗口消息
					CountDownTimer timer = new CountDownTimer(1500, 100) {
						@Override
						public void onTick(long millisUntilFinished) {
						}

						@Override
						public void onFinish() {
							activity.finish();
						}
					};
					timer.start();
				}
			}
		};
		log.v("[ 卡类支付 ] 游戏订单号：" + cpOrderId + "，用户ID：" + uid + "，充值金额：" + price);
		task.execute(param.toString());
	}

	/**
	 * 根据layoutId展示页面
	 * 
	 * @param context
	 *            当前页面上下文
	 * @param layoutId
	 *            展示的页面ID
	 * @param param
	 *            页面间传递的参数
	 */
	public void showActivity(Context context, ActivityFactory layoutId, Map<String, String> param) {
		Intent intent = new Intent(context, YhSDKActivity.class);

		intent.putExtra("layoutId", layoutId.toString());
		if (param != null) {
			Set<Map.Entry<String, String>> keySet = param.entrySet();
			Iterator<Map.Entry<String, String>> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}

		context.startActivity(intent);
		if (context instanceof Activity) {
			((Activity) context).finish();
		}
	}

	public IDataService getIdaoFactory(Context context) {
		if (dao == null) {
			dao = new SharedPrefDataService(context);
		}
		return dao;
	}

	/**
	 * 注册成功后推送的消息
	 * 
	 * @param activity
	 * @param account
	 */
	private void pushRegMsg(Activity activity, String account) {
		AssetTool asset = AssetTool.getInstance();
		String title = asset.getLangProperty(activity, "register_finish_notifi_title");
		String content = String.format(asset.getLangProperty(activity, "register_finish_notifi_content"), account);
		String url = Constants.getUsercenterUrl() + "?from=yhsdk&username=" + account;
		BrowserNotification notification = new BrowserNotification(activity);
		notification.showNotification(title, content, url);
	}

	public static Dispatcher getInstance() {
		if (processor == null) {
			processor = new Dispatcher();
		}
		return processor;
	}

	private abstract class JsonParameters {
		public abstract List<Entry> other();

		public String create() throws Exception {
			List<Entry> entries = other();

			StringBuilder preSign = new StringBuilder();
			preSign.append("appId=").append(Constants.APPINFO.appId);
			preSign.append("&type=").append(Constants.PLATFORM);

			for (Entry entry : entries) {
				preSign.append("&" + entry.key + "=").append(entry.value);
			}
			preSign.append("&ext=").append(Constants.APPINFO.ext);
			preSign.append("&version=").append(Constants.VERSION);
			preSign.append("&ip=").append(Constants.DEVICE_INFO.ip);
			preSign.append("&mac=").append(Constants.DEVICE_INFO.mac);
			preSign.append("&imei=").append(Constants.DEVICE_INFO.imei);
			preSign.append("&channel=").append(Constants.DEVICE_INFO.channel);
			preSign.append("||").append(Constants.APPINFO.appKey);
			String sign = MD5Tool.calcMD5(preSign.toString().getBytes());

			try {
				JSONObject param = new JSONObject();
				param.put("appId", Constants.APPINFO.appId);
				param.put("type", Constants.PLATFORM);
				for (Entry entry : entries) {
					param.put(entry.key, entry.value);
				}
				param.put("ext", Constants.APPINFO.ext);
				param.put("version", Constants.VERSION);
				param.put("ip", Constants.DEVICE_INFO.ip);
				param.put("mac", Constants.DEVICE_INFO.mac);
				param.put("imei", Constants.DEVICE_INFO.imei);
				param.put("channel", Constants.DEVICE_INFO.channel);
				param.put("platformType", "2");
				param.put("sign", sign);
				return param.toString();
			} catch (JSONException e) {
				throw new Exception("数据组装异常", e);
			}
		}
	}

	private class Entry {
		String key;
		String value;

		Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}



	public void getMyCouponCoin(Activity activity, String username, final RequestCallback callback) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("uid=").append(username).append("&");
		sbSign.append("channel=").append(Constants.DEVICE_INFO.channel).append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("uid", username);
			param.put("type", Constants.PLATFORM);
			param.put("channel", Constants.DEVICE_INFO.channel);
			param.put("ext", Constants.APPINFO.ext);
			param.put("deviceIp", CommonTool.getIp(activity));
			param.put("version", Constants.VERSION);
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("获取代金卷签名-数据组装异常", e);
		}

		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getCouponCoinUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				YhSdkLog.getInstance().i("---getmycoin--->" + message);
				if ("YHYZQM_000".equals(message.getString("status"))) {
					callback.callback("getAllMyCoupon", message.getString("msg"));
				} else {
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};
		task.execute(param.toString());
	}

	public void payMyCouponCoin(Activity activity, final String cpOrderId, final String uid, final int price, String goodsName, int couponid, final RequestCallback callback) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = Constants.APPINFO.appId;
		String appKey = Constants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(Constants.DEVICE_INFO.channel).append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("coupon_id", couponid);
			param.put("cpOrderId", cpOrderId);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
			param.put("type", Constants.PLATFORM);
			param.put("channel", Constants.DEVICE_INFO.channel);
			param.put("ext", Constants.APPINFO.ext);
			param.put("deviceIp", CommonTool.getIp(activity));
			param.put("version", Constants.VERSION);
			param.put("sign", MD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("代金卷签名-数据组装异常", e);
		}
//		热云准备支付
		TDandRyCallListenerBean.transactionId = cpOrderId;
		TDandRyCallListenerBean.paymentType = "weixinpay";
		TDandRyCallListenerBean.currencyType = "CNY";
		TDandRyCallListenerBean.currencyAmount = price+0.0f;
		
		ReYunTrack.setPaymentStart(cpOrderId,"daijinquan","CNY",price+0.0f);
		
		HttpAsyncTask task = new HttpAsyncTask(activity, Constants.getCouponPayUrl()) {
			@Override
			protected void onHandleResult(JSONObject message) throws JSONException {
				YhSdkLog.getInstance().i("--->代金卷支付返回---" + message);
				YhSdkToast.getInstance().show(activity, message.getString("msg"));
				if ("YHDJZF_000".equals(message.getString("status"))) {
//					支付成功时调用TalkingDataAppCpa的onPay方法
//					public static void onPay(String userId , String orderId , int amount , String currency, String payType);
					TalkingDataAppCpa.onPay(uid, cpOrderId, price, "CNY", "daijinquan");
//					热云：daijinquan支付
					ReYunTrack.setPayment(cpOrderId,"daijinquan","CNY",price+0.0f);
					
					activity.finish();
					Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_SUCCESS, "代金卷支付成功");
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
					return;
				} else {
					Dispatcher.getInstance().listener.callback(YhStatusCode.PAY_FAIL, "代金卷支付失败");
					YhSdkToast.getInstance().show(activity, message.getString("msg"));
				}
			}
		};
		task.execute(param.toString());
	}

}
