package com.example.newhfsdkdemo;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

/**
 * @author Administrator
 */
public abstract class HttpProgressAsyncTask extends AsyncTask<String, Integer, HttpResultBean>{
	private String url = null;
	private ProgressDialog dialog = null;

	public HttpProgressAsyncTask(Activity activity, String url) {
		this(activity, url, "正在创建支付环境……");
	}

	public HttpProgressAsyncTask(Activity activity, String url, String loadingMsg) {
		super();
		this.url = url;
		this.dialog = ProgressDialog.show(activity, "", loadingMsg, true, false);
	}

	@Override
	protected HttpResultBean doInBackground(String... param) {
		if (param == null || param.length != 1 || param[0] == null) {
			this.onHandleError("提交参数为空");
			return null;
		}
		return HttpTool.post(url, param[0]);
	}

	@Override
	protected void onPostExecute(HttpResultBean result) {
		super.onPostExecute(result);
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

		if (result == null) {
			return;
		}

		if (result.code <= 0) {
			this.onHandleError("网络响应异常");
			return;
		}

		if (result.code != 200) {
			onHandleError("网络异常，响应码：" + result.code);
			return;
		}

		if (TextUtils.isEmpty(result.message)) {
			onHandleError("服务端无返回结果");
			return;
		}
		try {
			JSONObject json = new JSONObject(result.message);
			System.out.println("=========================>"+json);
			onHandleResult(json);
		} catch (Exception e) {
			onHandleError("返回结果格式错误");
		}
	}

	/**
	 * 处理返回的数据
	 * 
	 * @param message
	 * @throws JSONException
	 */
	protected abstract void onHandleResult(JSONObject message) throws JSONException;

	/**
	 * 在发送数据时出现错误的情况处理
	 * 
	 * @param msg
	 *            错误信息
	 */
	protected abstract void onHandleError(String msg);

}
