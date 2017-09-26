package com.open.sdk.utlis;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import com.open.sdk.config.DetailedList;

/**
 * Created by Administrator on 2017/8/29.
 */
public class Shape {

    /**
     * 圆角shape
     *
     * @param context     上下文
     * @param radius      圆角角度
     * @param strokeWidth 线框大小
     * @param strokeColor 线框颜色
     * @param radiusColor 背景色
     * @return GradientDrawable
     */
    public static GradientDrawable getShapeModel(int radius, int strokeWidth, int strokeColor, int radiusColor) {
        //圆角矩形
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(radiusColor);
        return gradientDrawable;
    }

    /**
     * @return
     */
    public static GradientDrawable getShapeWhite( ) {
        return getShapeModel(DetailedList.d4, 0, 0, -1);
    }

    /**
     * 灰色框 - 圆形
     *
     * @return GradientDrawable
     */
    public static GradientDrawable getShapeGray( ) {
        return getShapeModel(DetailedList.d4, DetailedList.d1, 0xffe9e9e9, 0);
    }
    /**
     * 蓝色框 - 实心圆形
     *
     * @return GradientDrawable
     */
    public static GradientDrawable getShapeBlue( ) {
        return getShapeModel(DetailedList.d4, DetailedList.d1, 0xff3582f8, 0);
    }

    /**
     * 蓝色框 - 实心圆形
     *
     * @return GradientDrawable
     */
    public static GradientDrawable getShapeAllBlue( ) {
        return getShapeModel(DetailedList.d4, 0, 0, 0xff3582f8);
    }

    /**
     * 输入框清空红色背景
     *
     * @return GradientDrawable
     */
    public static GradientDrawable getEmptyShapeRed( ) {
        return getShapeModel(DetailedList.d15, DetailedList.d14, 0, Color.RED);
    }
}
