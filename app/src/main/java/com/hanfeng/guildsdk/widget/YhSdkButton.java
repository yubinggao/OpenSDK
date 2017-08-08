package com.hanfeng.guildsdk.widget;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSDKRes;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 自定义按钮控件
 */
public abstract class YhSdkButton extends Button implements View.OnClickListener {
	private Context context = null;
	
	private LinearLayout.LayoutParams params = null;
	public YhSdkButton(Context context, String text) {
		super(context);
		this.context = context;
		int mmtop = (int) (Constants.DEVICE_INFO.windowHeightPx * 0.05);
		
		if (Constants.DEVICE_INFO.densityDpi == 160) {
			params = new LinearLayout.LayoutParams(-1, 35, 1);
			this.setPadding(0, 0, 0, 0);
		} else {
			params = new LinearLayout.LayoutParams(-1, -2);
		}
		
		params.setMargins(0, mmtop, 0, 5);
		this.setLayoutParams(params);
		this.setBackgroundColor(Color.rgb(250, 251, 252));
		this.setText(text);
		this.setTextColor(Color.WHITE);
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(22F, false));
		this.setGravity(Gravity.CENTER);
		this.setBackgroundResource(YhSDKRes.getRes().getDrawableId(context, "yhsdk_corner_submit_btn"));
		this.setOnClickListener(this);
		setSelected(true);
	}

	@Override
	public void onClick(View view) {
		if (UITool.getInstance().isFastClick()) {
			return;
		}
		click(view);
	}

	public abstract void click(View view);

	public void clickable(boolean clickable) {
		super.setClickable(clickable);
		if (clickable) {
			this.setBackgroundResource(YhSDKRes.getRes().getDrawableId(context, "yhsdk_corner_submit_btn"));
		} else {
			this.setBackgroundResource(YhSDKRes.getRes().getDrawableId(context, "yhsdk_corner_not_submit_btn"));
		}
	}
	
	public void setMargins(int left, int top, int right, int bottom) {
		params.setMargins(left, top, right, bottom);
	}
}
