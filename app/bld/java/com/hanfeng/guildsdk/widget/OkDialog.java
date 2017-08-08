package com.hanfeng.guildsdk.widget;

import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSDKRes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author yich
 * @version  1.0.0 
 * 时间：2016年9月13日下午4:44:38
 * 作用：
 **/
public class OkDialog extends  Dialog {
    private String title;
    private String content;
    private TextView titleTv;
    private TextView contentTv;
    private TextView leftBtn;
    private TextView rightBtn;
    
    
    
    private  View.OnClickListener rightListener;
    private  View.OnClickListener leftListener;
    boolean isSingleBtn=false;
    private  LinearLayout mainLayout;
	
	/**
	 * 
	 * @param context
	 * @param title  标题
	 * @param content 内容
	 * @param isSingleBtn  是否是一个按钮
	 */
	public OkDialog(Context context,String title,String content,boolean isSingleBtn) {
		super(context);
		this.title=title;
		this.content=content;
		this.isSingleBtn=isSingleBtn;
		init();
	}
	
	
	
	/*初始化布局*/
	private void init() {
		
		LayoutParams lpmainLay=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	     
		
		mainLayout =new LinearLayout(getContext());
		mainLayout.setMinimumWidth(UITool.dp2px(getContext(), 250));
		mainLayout.setMinimumHeight(UITool.dp2px(getContext(), 140));
		mainLayout.setLayoutParams(lpmainLay);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundResource(YhSDKRes.getRes().getDrawableId(getContext(),
				"yhsdk_corner_windows"));
		
		LinearLayout  titleLay =new LinearLayout(getContext());
		LayoutParams lptitleLay=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleLay.setLayoutParams(lptitleLay);
		titleLay.setOrientation(LinearLayout.VERTICAL);
		titleLay.setBackgroundResource(YhSDKRes.getRes().getDrawableId(getContext(),
				"yhsdk_top_concer"));
		
		LayoutParams lptitletv=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lptitletv.gravity=Gravity.CENTER_HORIZONTAL;
		lptitletv.setMargins(UITool.dp2px(getContext(), 8), UITool.dp2px(getContext(), 10),UITool.dp2px(getContext(), 8), UITool.dp2px(getContext(), 10));
		titleTv=new TextView(getContext());
		titleTv.setText(title);
		titleTv.setTextSize(16);
		titleTv.setTextColor(Color.BLACK);
		titleTv.setLayoutParams(lptitletv);
		
		titleLay.addView(titleTv);
		
		RelativeLayout  contentLay =new RelativeLayout(getContext());
		LayoutParams lpcontentLay=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
		lpcontentLay.setMargins(UITool.dp2px(getContext(), 16), UITool.dp2px(getContext(), 6), UITool.dp2px(getContext(), 16), UITool.dp2px(getContext(), 6));
		contentLay.setLayoutParams(lpcontentLay);
		
		
		RelativeLayout.LayoutParams lpcontenttv=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpcontenttv.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentTv=new TextView(getContext());
		contentTv.setText(content);
		contentTv.setSingleLine(false);
		contentTv.setTextSize(15);
		contentTv.setTextColor(Color.BLACK);
		contentTv.setMaxWidth(UITool.dp2px(getContext(), 200));
		contentTv.setLayoutParams(lpcontenttv);
		contentLay.addView(contentTv);
		
		
		LinearLayout  footerLay =new LinearLayout(getContext());
		LayoutParams lpfooterLay=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		footerLay.setPadding(0, UITool.dp2px(getContext(), 6), 0, UITool.dp2px(getContext(), 20));
		footerLay.setLayoutParams(lpfooterLay);
		footerLay.setOrientation(LinearLayout.HORIZONTAL);
		if (!isSingleBtn) {
			LinearLayout  leftLay =new LinearLayout(getContext());
			LayoutParams lpleftLay=new LayoutParams(0, LayoutParams.MATCH_PARENT,1);
			leftLay.setLayoutParams(lpleftLay);
			leftLay.setOrientation(LinearLayout.VERTICAL);
			
			LinearLayout  rightLay =new LinearLayout(getContext());
			LayoutParams lprightLay=new LayoutParams(0, LayoutParams.MATCH_PARENT,1);
			rightLay.setLayoutParams(lprightLay);
			
			rightLay.setOrientation(LinearLayout.HORIZONTAL);
			
			LayoutParams lpLbtn=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpLbtn.setMargins(0, 0, UITool.dp2px(getContext(), 8), 0);
			lpLbtn.gravity=Gravity.RIGHT;
			
			LayoutParams lpRbtn=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpRbtn.setMargins(UITool.dp2px(getContext(), 8), 0, 0, 0);
			lpRbtn.gravity=Gravity.LEFT;
			
			rightBtn=new TextView(getContext());
			rightBtn.setText("确定");
			rightBtn.setTextSize(16);
			rightBtn.setTextColor(Color.WHITE);
			rightBtn.setBackgroundResource(YhSDKRes.getRes().getDrawableId(getContext(),
					"yhsdk_corner_submit_btn"));
			rightBtn.setPadding(UITool.dp2px(getContext(), 26), UITool.dp2px(getContext(),3), UITool.dp2px(getContext(), 26), UITool.dp2px(getContext(), 3));
			rightBtn.setLayoutParams(lpRbtn);
			rightBtn.setSelected(true);
			rightBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (rightListener!=null) {
						rightListener.onClick(rightBtn);
					}
					dismiss();
				}
			});
			rightLay.addView(rightBtn);
			
			leftBtn=new TextView(getContext());
			leftBtn.setText("取消");
			leftBtn.setTextSize(16);
			leftBtn.setTextColor(Color.WHITE);
			leftBtn.setPadding(UITool.dp2px(getContext(), 26), UITool.dp2px(getContext(), 3), UITool.dp2px(getContext(), 26), UITool.dp2px(getContext(), 3));
			leftBtn.setBackgroundResource(YhSDKRes.getRes().getDrawableId(getContext(),
					"yhsdk_corner_submit_btn"));
			leftBtn.setLayoutParams(lpLbtn);
			leftBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (leftListener!=null) {
						leftListener.onClick(leftBtn);
					}
					dismiss();
				}
			});
			leftLay.addView(leftBtn);
			
			footerLay.addView(leftLay);
			footerLay.addView(rightLay);
			
		}else {
			footerLay.setOrientation(LinearLayout.VERTICAL);
			LayoutParams lprightv=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lprightv.gravity=Gravity.CENTER_HORIZONTAL;
			rightBtn=new TextView(getContext());
			rightBtn.setText("确定");
			rightBtn.setTextSize(16);
			rightBtn.setTextColor(Color.WHITE);
			rightBtn.setLayoutParams(lprightv);
			rightBtn.setSelected(true);
			rightBtn.setPadding(UITool.dp2px(getContext(), 26), UITool.dp2px(getContext(), 3), UITool.dp2px(getContext(), 26), UITool.dp2px(getContext(), 3));
			rightBtn.setBackgroundResource(YhSDKRes.getRes().getDrawableId(getContext(),
					"yhsdk_corner_submit_btn"));
			rightBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (rightListener!=null) {
						rightListener.onClick(rightBtn);
					}
					dismiss();
				}
			});
			footerLay.addView(rightBtn);
		}
		
	
		mainLayout.addView(titleLay);
		mainLayout.addView(contentLay);
		mainLayout.addView(footerLay);
	}


     
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.setCanceledOnTouchOutside(false);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		if (mainLayout!=null) {
			setContentView(mainLayout);
		}
	
	}
	
  public	void setOnConfirmClickListener(View.OnClickListener listener){
		this.rightListener=listener;
	}

}
