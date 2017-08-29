package com.open.sdk.utlis;

import android.graphics.drawable.StateListDrawable;

/**
 * Created by Administrator on 2017/8/29.
 */
public class Selector {
    public void s(){
        // 状态选择器
        StateListDrawable stateListDrawable = new StateListDrawable();
        // 按下状态的背景
//        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable2);
        // 常规状态的背景
//        stateListDrawable.addState(new int[]{}, gradientDrawable1);// 应用到控件 -- API level 16
    }
}
