package com.hanfeng.guildsdk.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;

import com.hanfeng.guildsdk.Constants;

/**
 * Asset文件夹读取类
 */
public class AssetTool {
	private static AssetTool tool;
	private Properties langProperties = null;
	private YhSdkLog log = null;
	
	private AssetTool() {
		log = YhSdkLog.getInstance();
	}

	public BitmapDrawable decodeDensityDpiDrawable(Context context, String assetFile) {
		assetFile = "yhsdk/images/" + assetFile;
		return decodeDrawableFromAsset(context, assetFile, 1);
	}
	/**
	 * 根据语言读取相应的配置文件
	 * com.hanfeng.guildsdk.tool.AssetTool
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
	/**
	 * 读取Asseet文件下的图片
	 * 
	 * @param context
	 * @param assetFile
	 * @return
	 * @throws Exception
	 */
	public BitmapDrawable decodeDrawableFromAsset(Context context, String assetFile, float mutiple) {
		
		InputStream inputStream = null;
		if (mutiple <= 0) {
			mutiple = 1;
		}
		if (Constants.DEVICE_INFO.densityDpi > 320) {
			if(Build.MODEL.equals("XT1085")){//moto x
				mutiple *= (Constants.DEVICE_INFO.densityDpi / 320 );
			}else{
				mutiple *= (Constants.DEVICE_INFO.densityDpi / 320 * 2);
				}
		} else if (Constants.DEVICE_INFO.densityDpi < 320) {
		 if (Constants.DEVICE_INFO.densityDpi == 160 && Build.MODEL.equals("XT1085")) {
			int exponent = 320 / Constants.DEVICE_INFO.densityDpi;
			mutiple *= (1F / (float) Math.pow(2, exponent));
		 }else{
			int exponent = 320 / Constants.DEVICE_INFO.densityDpi;
			mutiple *= (1F / (float) Math.pow(2, exponent));
			}
		}
		try {
			inputStream = context.getResources().getAssets().open(assetFile);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			if(Constants.DEVICE_INFO.densityDpi == 160 && Build.MODEL.equals("XT1085")){
				bitmap.setDensity(480);
			}
			Matrix matrix = new Matrix();
			matrix.postScale(mutiple, mutiple);
			Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			//new BitmapDrawable(bitmap map)改方法在没有给定density，在设置图片的会有误差
			return new BitmapDrawable(newbm);
		} catch (IOException e) {
			log.e("读取\"" + assetFile + "\"图片资源异常：", e);
			return new BitmapDrawable();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.e("读取图片资源时输入流关闭异常：", e);
				}
			}
		}
	}

	/**
	 * 根据语言读取相应的配置文件
	 * 
	 * @param key
	 * @return
	 */
	public String getLangProperty(Context context, String key) {
		if (langProperties == null) {
			langProperties = new Properties();
			InputStream inputStream = null;
			String propPath = readPropertyName(context);
			log.v("当前语言环境为：" + propPath);
			try {
				inputStream = context.getResources().getAssets().open(propPath);
				langProperties.load(inputStream);
			} catch (IOException e) {
				log.e("读取语言配置文件失败：", e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						log.e("读取语言配置文件时，关闭输入流异常：", e);
					}
				}
			}
		}

		try {
			String temp = langProperties.getProperty(key, "");
			return new String(temp.getBytes("ISO-8859-1"), "utf-8");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 根据国家和地区读取SDK内相应的配置文件
	 * 
	 * @param context
	 * @return
	 */
	private String readPropertyName(Context context) {
		String propFilePath = "yhsdk/conf/lang";
		try {
			String[] filelist = context.getResources().getAssets().list(propFilePath);
			String fileName = "yhsdk_" + Locale.getDefault().toString() + ".properties";
			for (int i = 0; i < filelist.length; i++) {
				if (fileName.equals(filelist[i])) {
					return propFilePath + "/" + fileName;
				}
			}
		} catch (Exception e) {
			log.e("读取国际化文件失败，将默认语言转换成英文：", e);
		}
		return propFilePath + "/yhsdk_en.properties";
	}

	public static AssetTool getInstance() {
		if (tool == null) {
			tool = new AssetTool();
		}
		return tool;
	}
}
