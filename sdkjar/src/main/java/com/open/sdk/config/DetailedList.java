package com.open.sdk.config;

import android.content.Context;
import com.open.sdk.utlis.DipUtils;

/**
 * Created by Administrator on 2017/8/28.
 */
public class DetailedList {
    public static int d10;
    public static int d12;
    public static int d40;
    public static int d48;
    public static int d56;
    public static int d100;
    public static int w;
    public static int h;

    public static void initData(Context mContext) {
        d10 = DipUtils.dipPx(mContext,10);
        d12 = DipUtils.dipPx(mContext,12);
        d40= DipUtils.dipPx(mContext,40);
        d48 = DipUtils.dipPx(mContext,48);
        d56 = DipUtils.dipPx(mContext,56);
        d100 = DipUtils.dipPx(mContext,100);
        w = DipUtils.dipPx(mContext,320);
        h = DipUtils.dipPx(mContext,298);
    }


}
