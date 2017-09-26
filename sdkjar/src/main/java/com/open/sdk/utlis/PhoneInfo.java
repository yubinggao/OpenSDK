package com.open.sdk.utlis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 设备信息的获取
 */
public class PhoneInfo {
    private static WifiManager wifiManager;
    /**
     * 设备IP地址
     */
    public static String ip;
    /**
     * 设备mac地址
     */
    public static String mac;
    /**
     * 设备imei码
     */
    public static String imei;
    /**
     * 屏幕宽度
     */
    public static int width;
    /**
     * 屏幕高度
     */
    public static int height;
    /**
     * 状态栏高度
     */
    public static int statusBarHeight;

    private PhoneInfo() {
    }

    private static volatile PhoneInfo phoneInfo = null;

    public static PhoneInfo getInstance() {
        return phoneInfo;
    }

    public static PhoneInfo init(Context context) {
        if (phoneInfo == null) {
            synchronized (PhoneInfo.class) {
                if (phoneInfo == null) {
                    phoneInfo = new PhoneInfo();
                    initInfo(context);
                }
            }
        }
        return phoneInfo;
    }

    private static void initInfo(Context context) {
        imei = getDeviceImei(context);
        ip = getDeviceIP(context);
        mac = getDeviceMac(context);
        width = getDeviceWidth(context);
        height = getDeviceHeight(context);
        statusBarHeight = getStatusBarHeight(context);
    }

    /**
     * 获取本机IMEI码
     *
     * @param context 上下文
     * @return
     */
    private static String getDeviceImei(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imie = telephonyMgr.getDeviceId();
        if (imie == null || imie.isEmpty()) {
            imie = "" + Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +
                    Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +
                    Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
        }
        return imie;
    }

    /**
     * 获取本机IP
     *
     * @param context 上下文
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static String getDeviceIP(Context context) {
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
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
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
     * @param context 上下文
     * @return
     */
    private static String getDeviceMac(Context context) {
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
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return
     */
    private static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
        return  wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕宽度 高度
     *
     * @param context 上下文
     * @return
     */
    private static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 状态栏高度
     *
     * @param context 上下文
     * @return
     */
    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
