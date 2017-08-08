package com.hanfeng.guildsdk.widget;


import com.hanfeng.guildsdk.tool.AssetTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSDKRes;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author yich
 * @version  1.0.0 
 * 时间：2016年7月8日下午3:26:08
 * 作用：
 **/
public class LoadingLay extends LinearLayout {
    private ImageView bar;
    private TextView msgTv;
	public LoadingLay(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
		DrawUI();
		regEvents();
	}

	public LoadingLay(Context context) {
		this(context,null);
	}

	public void initViews() {
		bar=new ImageView(getContext());
		msgTv=new TextView(getContext());
	}

	@SuppressLint("NewApi")
	public void DrawUI() {
		setOrientation(LinearLayout.HORIZONTAL);
		setBackgroundResource(YhSDKRes.getRes().getDrawableId(getContext(),
				"yhsdk_loading_windows"));
		RelativeLayout wrapLayout=new RelativeLayout(getContext());
		LayoutParams lpwrap=  new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lpwrap.gravity=Gravity.CENTER_VERTICAL;
		wrapLayout.setLayoutParams(lpwrap);
		bar.setImageDrawable(AssetTool.getInstance().decodeDrawableFromAsset(getContext(), "yhsdk/images/yhsdk_loading.png", 1.8f));
//		ObjectAnimator animator=ObjectAnimator.ofFloat(bar, "rotation", 0f,360f);
//		animator.setRepeatCount(ObjectAnimator.INFINITE); 
//		animator.setDuration(500);
//		animator.start();
		RotateAnimation animation=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(800);
		animation.setInterpolator(new LinearInterpolator());
		animation.setRepeatCount(Animation.INFINITE);
		bar.startAnimation(animation);
		
		
		RelativeLayout.LayoutParams barLp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		barLp.addRule(RelativeLayout.CENTER_IN_PARENT);
		bar.setLayoutParams(barLp);
		wrapLayout.addView(bar);
		addView(wrapLayout);
		LayoutParams tvLp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tvLp.setMargins(UITool.dp2px(getContext(), 12), UITool.dp2px(getContext(), 4),UITool.dp2px(getContext(), 6), UITool.dp2px(getContext(), 4));
		tvLp.gravity=Gravity.CENTER_VERTICAL;
		msgTv.setTextSize(12);
		msgTv.setLayoutParams(tvLp);
		msgTv.setTextColor(Color.parseColor("#5BC3E0"));
		addView(msgTv);
	}

	public void regEvents() {
	}
   public void setMsg(String msg){
	   if (msg!=null) {
		   msgTv.setText(msg);
	    }
	   
   }
}
