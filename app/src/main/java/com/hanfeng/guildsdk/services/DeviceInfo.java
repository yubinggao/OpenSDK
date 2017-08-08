package com.hanfeng.guildsdk.services;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;







import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.tool.ChannelUtil;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.YhSdkToast;

/**
 * 设备信息的获取
 * 
 */
public class DeviceInfo {

	private Map<Integer, Windows> densityWin;
	public String sid;
	/** 设备IP地址 */
	public String ip;
	/** 设备mac地址 */
	public String mac;
	/** 设备imei码 */
	public String imei;
	/** 设备电话号码 */
	public String phoneNum;
	/** apk的渠道码*/
	public String channel;
	/** 设备密度 */
	public int densityDpi = -1;
	/** 设备屏幕宽度 */
	public int widthPixels = -1;
	/** 设备屏幕高度 */
	public int heightPixels = -1;
	/** 窗口显示宽度 */
	public int windowWidthPx = 0;
	/** 窗口显示高度 */
	public int windowHeightPx = 0;

	private static WifiManager wifiManager;

	@SuppressLint("UseSparseArrays")
	private DeviceInfo() {
		densityWin = new HashMap<Integer, Windows>();
//		densityWin.put(160, new Windows(374, 230));
//		densityWin.put(240, new Windows(561, 345));
//		densityWin.put(320, new Windows(841, 517));
//		densityWin.put(440, new Windows(1351, 830));
//		densityWin.put(480, new Windows(1351, 830));
//		densityWin.put(560, new Windows(1465, 900));
//		densityWin.put(640, new Windows(1628, 1000));
	}

	/**
	 * 获取本机IMEI码
	 * @param context
	 * @return
	 */
	public static String getDeviceImei(Context context) {
		TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imie = telephonyMgr.getDeviceId();
		if(imie == null || "".equals(imie)){
			final IDataService operatData = Dispatcher.getInstance().getIdaoFactory(context);
			if(operatData.getImie() == null || "".equals(operatData.getImie())){
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				Random r = new Random();
			    imie = "autoimei"+sdf.format(date).toString()+String.valueOf(r.nextInt(100));
			    operatData.writeImie(imie);
			    return imie;
			}else{
				return operatData.getImie();
			}
			
		}else{
			return imie;
		}
	}

	/**
	 * 获取本机IP
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getDeviceIP(Context context) {
		if (wifiManager == null) {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			// 获取32位整型IP地址
			int ipAddress = wifiInfo.getIpAddress();
			return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
		}
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";

	}

	/**
	 * 获取本机MAC码
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceMac(Context context) {
		if (wifiManager == null) {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}

		if (wifiManager.isWifiEnabled()) {
			WifiInfo info = wifiManager.getConnectionInfo();
			return info.getMacAddress();
		}
		return "";
	}
	
	
	/**
	 * 获取Manifest.xml 中MetaData属性
	 * 
	 * @param activity
	 * @param key metaData的名字
	 * @return
	 */
	public static String getMetaData(Context activity, String key) {
		try {
			ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
			return "" + appInfo.metaData.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	public String getPhoneNum(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneManager.getLine1Number();
	}
	/**
	 * 计算窗口的宽高
	 */
	public void calculateWindows() {
		Windows windows = densityWin.get(densityDpi);
//		windows = new Windows((int) (widthPixels * 0.675), (int) (heightPixels * 0.73));
			if (Constants.isPORTRAIT) {
//			竖屏:	//
					windows = new Windows((int) (widthPixels * 0.55), (int) (heightPixels * 0.76));
				}else{
//			横屏:	//
					windows = new Windows((int) (widthPixels * 0.675), (int) (heightPixels * 0.73));
				}
		
		
		
		if (widthPixels < windows.width) {
			windowWidthPx = widthPixels;
		} else {
			windowWidthPx = windows.width;
		}

		if (heightPixels < windows.height) {
			windowHeightPx = heightPixels;
		} else {
			windowHeightPx = windows.height;
		}
		windowHeightPx=(int) (windowHeightPx*0.9);
	}
	/**
	 * 判断网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 本机信息初始化
	 * @param context
	 */
	public static DeviceInfo init(Context context) {
		DeviceInfo device = new DeviceInfo();
		device.ip = device.getDeviceIP(context);
		device.mac = device.getDeviceMac(context);
		device.imei = device.getDeviceImei(context);
		device.phoneNum = device.getPhoneNum(context);
		device.channel=device.getApkChannel(context);

		
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int currentOrientantion = context.getResources().getConfiguration().orientation;
		device.densityDpi = metrics.densityDpi;
		float density = metrics.density;
		YhSdkLog.getInstance().i("density=" + density + ", densityDpi=" + metrics.densityDpi);
		// 如果是竖屏，则把宽高换过来
		if (currentOrientantion == Configuration.ORIENTATION_PORTRAIT) {
			device.widthPixels = metrics.heightPixels;
			device.heightPixels = metrics.widthPixels;
		} else {
			device.widthPixels = metrics.widthPixels;
			device.heightPixels = metrics.heightPixels;
		}
		device.calculateWindows();
		YhSdkLog.getInstance().i("初始化：屏幕密度：" + device.densityDpi+"机型："+Build.MODEL);
		YhSdkLog.getInstance().i("初始化：屏幕宽度：" + device.widthPixels + ",屏幕高度：" + device.heightPixels);
		YhSdkLog.getInstance().i("初始化：窗口宽度：" + device.windowWidthPx + ",窗口高度：" + device.windowHeightPx);
//		YhSdkToast.getInstance().show(context,"初始化：屏幕密度：" + device.densityDpi+"真实dpi："+metrics.densityDpi+"真实的desity:"+metrics.density+"机型："+Build.MODEL+"\n初始化：屏幕宽度：" + device.widthPixels + ",屏幕高度：" + device.heightPixels+"\n初始化：窗口宽度：" + device.windowWidthPx + ",窗口高度：" + device.windowHeightPx );
		return device;
	}
	
	public static String getApkChannel(Context context) {
		return ChannelUtil.getChannel(context);
	}

	private static class Windows {
		public int width;
		public int height;

		public Windows(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
}
