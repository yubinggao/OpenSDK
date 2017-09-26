package com.example.newhfsdkdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;


/**
 * 
 * @author TZW
 * Asset文件夹读取类
 */
public class AssetsTool {
	private static AssetsTool tool;

	private AssetsTool() {
	}

	/**
	 * 根据语言读取相应的配置文件
	 * 
	 * @param key
	 * @return
	 */
	public String getAssetConfigs(Context context, String file) {
		InputStream inputStream = null;
		BufferedReader reader = null;
		String line = null;
		StringBuilder buffer = new StringBuilder();
		try {
			inputStream = context.getResources().getAssets().open(file);
			reader = new BufferedReader(new InputStreamReader(inputStream));
			line = reader.readLine();
			while (line != null) {
				buffer.append(line);
				line = reader.readLine();
			}

		} catch (IOException e) {
			Log.w("读取配置文件【" + file +"】失败：", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Log.w("[assets] Input stream close exception：", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.w("[assets] Buffered reader close exception：", e);
				}
			}
		}
		return buffer.toString();
	}

	public static AssetsTool getInstance() {
		if (tool == null) {
			tool = new AssetsTool();
		}
		return tool;
	}
}
