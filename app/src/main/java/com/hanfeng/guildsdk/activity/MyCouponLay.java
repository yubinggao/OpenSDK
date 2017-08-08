package com.hanfeng.guildsdk.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.hanfeng.guildsdk.bean.Coupon;
import com.hanfeng.guildsdk.tool.AssetTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSDKRes;
import com.unionpay.mobile.android.nocard.utils.f;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author yich
 * @version 1.0.0 时间：2016年9月2日上午11:29:23 作用：
 **/
public class MyCouponLay extends LinearLayout {
	private TextView tiltle;
	private ListView couponList;
	private CouponAdapter adapter;
	private LinearLayout listWrapLayout;
	private boolean hasSetScrollBar=false;
	public MyCouponLay(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
		DrawUI();
		regEvents();
	}

	public MyCouponLay(Context context) {
		this(context, null);
	}

	@SuppressLint("NewApi")
	public void initViews() {
		setOrientation(LinearLayout.HORIZONTAL);
		// setBackgroundDrawable(AssetTool.getInstance().decodeDrawableFromAsset(getContext(),
		// "yhsdk/images/yhsdk_dialog_coupon_left.png", 1.5f));
		setBackgroundColor(Color.parseColor("#00000000"));
		tiltle = new TextView(getContext());
		LayoutParams lpTitle = new LayoutParams(
				LayoutParams.WRAP_CONTENT, UITool.dp2px(getContext(), 200));
		tiltle.setGravity(Gravity.CENTER);
		tiltle.setText("代\n\n金\n\n劵");
		tiltle.setTextColor(Color.parseColor("#457DE0"));
		tiltle.setTextSize(20);
		tiltle.setLayoutParams(lpTitle);
		tiltle.setBackgroundDrawable(AssetTool.getInstance()
				.decodeDrawableFromAsset(getContext(),
						"yhsdk/images/yhsdk_dialog_coupon_left.png", 2.0f));
		LayoutParams lpList = new LayoutParams(
				LayoutParams.MATCH_PARENT, UITool.dp2px(getContext(), 200));
		lpList.setMargins(UITool.dp2px(getContext(), 4), 0,
				UITool.dp2px(getContext(), 4), 0);
		listWrapLayout = new LinearLayout(getContext());
		couponList = new ListView(getContext());

		couponList.setLayoutParams(lpList);
		couponList.setVerticalScrollBarEnabled(true);
		listWrapLayout.addView(couponList);
		listWrapLayout.setBackgroundDrawable(AssetTool.getInstance()
				.decodeDrawableFromAsset(getContext(),
						"yhsdk/images/yhsdk_dialog_coupon_right.png", 2.0f));
		listWrapLayout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, UITool.dp2px(getContext(), 200)));
		couponList.setDivider(null);
		couponList.setSelector(YhSDKRes.getRes().getDrawableId(getContext(),
				"yhsdk_white_bg"));
		adapter = new CouponAdapter(getContext());
		couponList.setAdapter(adapter);
	
		
		
	}

	

	public void DrawUI() {
		addView(tiltle);
		addView(listWrapLayout);
		
		setListCostumeScrollBar();

		// 自己布局的参数
		LayoutParams lParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, UITool.dp2px(getContext(), 200));
		setLayoutParams(lParams);
	}

	private void setListCostumeScrollBar() {
		try {
			 Field f = View.class.getDeclaredField("mScrollCache");
			 f.setAccessible(true); Object scrollabilityCache = f.get(couponList);
			 f = f.getType().getDeclaredField("scrollBar"); 
			 f.setAccessible(true);
			 Object scrollBarDrawable = f.get(scrollabilityCache); 
			 f = f.getType().getDeclaredField("mVerticalThumb"); 
			 f.setAccessible(true);
			 Drawable drawable = (Drawable) f.get(scrollBarDrawable); 
			 drawable = getResources().getDrawable(
						YhSDKRes.getRes().getDrawableId(getContext(),
								"yhsdk_scroll_bar"));
			 f.set(scrollBarDrawable, drawable); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void regEvents() {
	}

	public void addData(Coupon coupon) {
		adapter.add(coupon);
	}

	public void addDatas(ArrayList<Coupon> coupons) {
		adapter.addAll(coupons);
	}

	public void setItemChecked(int pos) {
		adapter.setItemChecked(pos);
	}

	public void setOnListItemClickListener(OnItemClickListener listener) {
		couponList.setOnItemClickListener(listener);
	}

	public void setItemCheckedByCoupon(int id) {
		int pos = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).id == id) {
				if (adapter.getItem(i).state != 0) {
					pos = 0;
				} else {
					pos = i;
				}
				break;
			}
		}
		adapter.setItemChecked(pos);
	}
}
