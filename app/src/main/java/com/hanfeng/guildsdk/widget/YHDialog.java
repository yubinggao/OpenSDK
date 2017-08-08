package com.hanfeng.guildsdk.widget;

import com.hanfeng.guildsdk.activity.MyCouponLay;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * @author yich
 * @version  1.0.0 
 * 时间：2016年9月2日下午5:07:00
 * 作用：
 **/
public class YHDialog extends Dialog{
	private Activity activity;
	private View view;
	private boolean cancel=false;
	public YHDialog(Activity activity, View view) {
		super(activity);
		this.view = view;
		this.activity = activity;
	}
	public YHDialog(Activity activity2, View viewLay, boolean b) {
		super(activity2);
		this.view = viewLay;
		this.activity = activity2;
		this.cancel=b;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setCanceledOnTouchOutside(cancel);
		setCancelable(cancel);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		setContentView(view);
	}
}
