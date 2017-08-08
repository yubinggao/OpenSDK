package com.hanfeng.guildsdk.tool;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.Mode;

import android.util.Log;

public final class YhSdkLog {
	private static YhSdkLog log = null;
	private boolean isDebug;

	private YhSdkLog() {
		isDebug = (Constants.mode == Mode.debug);
	}

	/**
	 * NSDK内部调试日志
	 * 
	 * @param msg
	 */
	public void v(String msg) {
		if (isDebug) {
			Log.v(Constants.TAG, msg);
		}
	}

	/**
	 * NSDK内部调试日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void v(String msg, Throwable throwable) {
		if (isDebug) {
			Log.v(Constants.TAG, msg, throwable);
		}
	}

	/**
	 * 游戏接入SDK时需要查看的日志
	 * 
	 * @param msg
	 */
	public void i(String msg) {
		Log.i(Constants.TAG, msg);
	}

	/**
	 * 游戏接入SDK时需要查看的日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void i(String msg, Throwable throwable) {
		Log.i(Constants.TAG, msg, throwable);
	}

	/**
	 * 正式上线所需要的日志
	 * 
	 * @param msg
	 */
	public void w(String msg) {
		Log.w(Constants.TAG, msg);
	}

	/**
	 * 正式上线所需要的日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void w(String msg, Throwable throwable) {
		Log.w(Constants.TAG, msg, throwable);
	}

	/**
	 * 异常错误日志
	 * 
	 * @param msg
	 */
	public void e(String msg) {
		Log.e(Constants.TAG, msg);
		if (!isDebug) {
			// TODO: 上传到服务器中
		}
	}

	/**
	 * 异常错误日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void e(String msg, Throwable throwable) {
		Log.e(Constants.TAG, msg, throwable);
		if (!isDebug) {
			// TODO: 上传到服务器中
		}
	}

	public static YhSdkLog getInstance() {
		if (log == null) {
			log = new YhSdkLog();
		}
		return log;
	}
}
