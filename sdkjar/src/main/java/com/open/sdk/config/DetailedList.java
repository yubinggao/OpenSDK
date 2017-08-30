package com.open.sdk.config;

import android.content.Context;
import com.open.sdk.utlis.DipUtils;

/**
 * Created by Administrator on 2017/8/28.
 */
public class DetailedList {
    //登录
    public static String sdkLoginTitle = "帐号密码登录";
    public static String phoneHint = "请输入手机号码";
    public static String pwdHint = "请输入密码";
    public static String login = "登录";
    public static String resetPwd = "重置密码";
    public static String registeredAccount = "快速注册";

    //重置密码
    public static String setPwdTitle = "重置密码";
    public static String phoneCodeHint = "请输入验证码";
    public static String ok = "确定";
    //注册
    public static String registeredAccountTitle = "注册帐号";
    public static String registered = "注册";

    public static int d1;
    public static int d4;
    public static int d10;
    public static int d12;
    public static int d16;
    public static int d39;
    public static int d40;
    public static int d48;
    public static int d56;
    public static int d68;
    public static int d79;
    public static int d86;
    public static int d100;
    public static int d118;
    public static int w;//320 300
    public static int h;//298 278

    public static void initData(Context mContext) {
        d1 = DipUtils.dipPx(mContext, 1);
        d4 = DipUtils.dipPx(mContext, 4);
        d10 = DipUtils.dipPx(mContext, 10);
        d12 = DipUtils.dipPx(mContext, 12);
        d16 = DipUtils.dipPx(mContext, 16);
        d39 = DipUtils.dipPx(mContext, 39);
        d40 = DipUtils.dipPx(mContext, 40);
        d48 = DipUtils.dipPx(mContext, 48);
        d56 = DipUtils.dipPx(mContext, 56);
        d68 = DipUtils.dipPx(mContext, 68);
        d79 = DipUtils.dipPx(mContext, 79);
        d86 = DipUtils.dipPx(mContext, 86);
        d100 = DipUtils.dipPx(mContext, 100);
        d118 = DipUtils.dipPx(mContext, 118);
        w = DipUtils.dipPx(mContext, 320);
        h = DipUtils.dipPx(mContext, 272);
    }


}
