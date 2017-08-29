package com.open.sdk.utlis;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import com.open.sdk.config.DetailedList;

/**
 * Created by Administrator on 2017/8/29.
 */
public class Shape {

    /**
     * 用代码写圆角
     *
     * @param context     上下文
     * @param radius      圆角角度
     * @param strokeWidth 线框大小
     * @param strokeColor 线框颜色
     * @param radiusColor 圆角背景色
     * @return GradientDrawable
     */
    public static GradientDrawable getShapeModel(Context context, int radius, int strokeWidth, int strokeColor, int radiusColor) {
        //圆角矩形
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(radiusColor);
        return gradientDrawable;
    }

    public static GradientDrawable getShapeWhite(Context context) {
        return getShapeModel(context, DetailedList.d4, 0, 0, -1);
    }
    public static GradientDrawable getShapeGray(Context context) {
        return getShapeModel(context, DetailedList.d4, DetailedList.d1, 0xffe9e9e9, 0);
    }
    public static GradientDrawable getShapeBlue(Context context) {
        return getShapeModel(context, DetailedList.d4, 0,0 , 0xff3582f8);
    }
}
