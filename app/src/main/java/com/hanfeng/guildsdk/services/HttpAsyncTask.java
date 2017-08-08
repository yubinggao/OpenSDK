package com.hanfeng.guildsdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.hanfeng.guildsdk.tool.AssetTool;
import com.hanfeng.guildsdk.tool.HttpTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.tool.HttpTool.HttpResult;
import com.hanfeng.guildsdk.widget.YhSdkToast;


/**
 * 请求服务器的Http异步任务
 */
abstract class HttpAsyncTask extends AsyncTask<String, Integer, HttpResult> {
	private static AssetTool asset = AssetTool.getInstance();

	Activity activity = null;
	private String url = null;
	private YhSdkLog log = null;
	private Dialog dialog = null;

	HttpAsyncTask(Activity activity, String url) {
		this(activity, url, asset.getLangProperty(activity, "progress"));
	}

	HttpAsyncTask(Activity activity, String url, String loadingMsg) {
		super();
		this.url = url;
		this.activity = activity;
		this.log = YhSdkLog.getInstance();
		this.dialog =UITool.getLoadingDialog(loadingMsg, activity);
	}

	@Override
	protected HttpResult doInBackground(String... param) {
		if (param == null || param.length != 1 || param[0] == null) {
			log.e("传入参数异常，不进行任何处理");
			return null;
		}
		if(!DeviceInfo.isNetAvailable(activity)){
			Toast.makeText(activity, "网络连接不可用,请稍后重试", Toast.LENGTH_SHORT).show();
			return null;
		}

		try {
			return HttpTool.post(url,HttpTool.CONNECT_TIMEOUT, param[0]);
		} catch (Exception e) {
			log.e("链接" + url + "时失败：", e);
			YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "http_error"));
			return null;
		}
	}
   
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}
	@Override
	protected void onPostExecute(HttpResult result) {
		super.onPostExecute(result);
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

		if (result == null) {
			return;
		}

		if (result.code <= 0) {
			YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "http_error") );
			return;
		}

		if (result.code != 200) {
			YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "response_error") );
			return;
		}

		//log.i("服务器返回结果：" + result.message);
		if (TextUtils.isEmpty(result.message)) {
			YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "result_format_error"));
			return;
		}
		try {
			JSONObject json = new JSONObject(result.message);
			onHandleResult(json);
		} catch (JSONException e) {
			log.e("返回结果格式错误：" + result.message, e);
			YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "result_format_error"));
		} catch (Exception e) {
			YhSdkToast.getInstance().show(activity, "网络连接不可用,请稍后重试");
			
		}
	}

	protected abstract void onHandleResult(JSONObject message) throws JSONException;

}
