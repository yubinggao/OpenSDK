package com.open.sdk.config;

import android.content.Context;
import com.open.sdk.utlis.DipUtils;
import com.open.sdk.utlis.PhoneInfo;

/**
 * Created by Administrator on 2017/8/28.
 */
public class DetailedList {
    //登录
    public static String sdkLoginTitle = "帐号密码登录";
    public static String phoneHint = "请输入手机号码";
    public static String pwdHint = "请输入密码";
    public static String login = "登录";
    public static String resetPwd = "找回密码";
    public static String registeredAccount = "快速注册";

    //重置密码
    public static String setPwdTitle = "找回密码";
    public static String phoneCodeHint = "请输入验证码";
    public static String ok = "确定";

    //注册
    public static String registeredAccountTitle = "注册帐号";
    public static String registered = "注册";

    //自定义

    public static int d1;
    public static int d2;
    public static int d4;
    public static int d10;
    public static int d12;
    public static int d13;
    public static int d14;
    public static int d15;
    public static int d16;
    public static int d17;
    public static int d20;
    public static int d22;
    public static int d24;
    public static int d30;
    public static int d34;
    public static int d39;
    public static int d40;
    public static int d48;
    public static int d56;
    public static int d60;
    public static int d64;
    public static int d68;
    public static int d79;
    public static int d86;
    public static int d100;
    public static int d118;
    public static int w;//320 300
    public static int h;//298 278
    public static int floatWidthScope;
    public static int floatHeightScope;

    public static void initData(Context mContext) {
        d1 = DipUtils.dipPx(mContext, 1);
        d2 = DipUtils.dipPx(mContext, 2);
        d4 = DipUtils.dipPx(mContext, 4);
        d10 = DipUtils.dipPx(mContext, 10);
        d12 = DipUtils.dipPx(mContext, 12);
        d13 = DipUtils.dipPx(mContext, 13);
        d14 = DipUtils.dipPx(mContext, 14);
        d15 = DipUtils.dipPx(mContext, 15);
        d16 = DipUtils.dipPx(mContext, 16);
        d17 = DipUtils.dipPx(mContext, 17);
        d20 = DipUtils.dipPx(mContext, 20);
        d22 = DipUtils.dipPx(mContext, 22);
        d24 = DipUtils.dipPx(mContext, 24);
        d30 = DipUtils.dipPx(mContext, 30);
        d34 = DipUtils.dipPx(mContext, 34);
        d39 = DipUtils.dipPx(mContext, 39);
        d40 = DipUtils.dipPx(mContext, 40);
        d48 = DipUtils.dipPx(mContext, 48);
        d56 = DipUtils.dipPx(mContext, 56);
        d60 = DipUtils.dipPx(mContext, 60);
        d64 = DipUtils.dipPx(mContext, 64);
        d68 = DipUtils.dipPx(mContext, 68);
        d79 = DipUtils.dipPx(mContext, 79);
        d86 = DipUtils.dipPx(mContext, 86);
        d100 = DipUtils.dipPx(mContext, 100);
        d118 = DipUtils.dipPx(mContext, 118);
        w = DipUtils.dipPx(mContext, 320);
        h = DipUtils.dipPx(mContext, 272);
        PhoneInfo.init(mContext);
        floatWidthScope = PhoneInfo.width;
        floatHeightScope = PhoneInfo.height;
    }


}
