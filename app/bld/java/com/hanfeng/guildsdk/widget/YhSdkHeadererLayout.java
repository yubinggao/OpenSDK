package com.hanfeng.guildsdk.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.bean.ConfBean;
import com.hanfeng.guildsdk.services.Dispatcher;
import com.hanfeng.guildsdk.tool.UITool;

public class YhSdkHeadererLayout extends LinearLayout {

	private YhSdkHeadererLayout(Context context) {
		super(context);
	}

	public static class Builder {
		private Context context;
		private ConfBean left;
		private ConfBean center;
		
		

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setLeft(ConfBean left) {
			this.left = left;
			return this;
		}

		public Builder setCenter(ConfBean center) {
			this.center = center;
			return this;
		}
		public YhSdkHeadererLayout build() {
			//主要布局
			YhSdkHeadererLayout mainlayout = new YhSdkHeadererLayout(context);
			LayoutParams params = new LayoutParams(-1,(int) (Constants.DEVICE_INFO.windowHeightPx * 0.125),1);
			mainlayout.setLayoutParams(params);
			mainlayout.setOrientation(LinearLayout.VERTICAL);
			//header title 包含返回登录按钮和标题
			YhSdkHeadererLayout headerlayout = new YhSdkHeadererLayout(context);
			LayoutParams linearParams = new LayoutParams(-1,-2,1);
			linearParams.setMargins(0, 0, 0, 0);
			headerlayout.setLayoutParams(linearParams);
			headerlayout.setOrientation(LinearLayout.HORIZONTAL);
			headerlayout.setGravity(Gravity.CENTER);
			//返回登录按钮
			if (left != null) {
				LinearLayout leftLayout = new LinearLayout(context);
				LayoutParams leftParams = new LayoutParams(-1, -1,1);
				leftParams.setMargins((int) (Constants.DEVICE_INFO.windowWidthPx * 0.05), 0, 0, 0);
				leftLayout.setLayoutParams(leftParams);
				leftLayout.setGravity(Gravity.LEFT);
				leftLayout.setOrientation(LinearLayout.HORIZONTAL);
				Button leftBtn = new Button(context);
				leftBtn.setLayoutParams(new LayoutParams(-2, -1));
				leftBtn.setPadding(0, 0, 0, 0);
				leftBtn.setText("   ");
				leftBtn.setBackgroundColor(Color.rgb(250, 251, 252));
				leftBtn.setGravity(Gravity.CENTER_VERTICAL);
				if (left.rect != null) {
					leftBtn.setCompoundDrawablesWithIntrinsicBounds(left.rect.left, left.rect.top, left.rect.right, left.rect.bottom);
				}

				if (left.isClickable && left.activity != null) {
					leftBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Dispatcher.getInstance().showActivity(context, left.activity, null);
						}
					});
				}
				leftLayout.addView(leftBtn);
				headerlayout.addView(leftLayout);
			}
			if (center != null) {
				LinearLayout centerLayout = new LinearLayout(context);
				LayoutParams centerParams = new LayoutParams(-1, -1, 1);
				centerLayout.setLayoutParams(centerParams);
				centerLayout.setGravity(Gravity.CENTER);
                TextView tv = new TextView(context);
                tv.setText(center.text);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(22F, false));
                tv.setTextColor(center.textColor);
                centerLayout.addView(tv);
                headerlayout.addView(centerLayout);
                //为了设置标题居中的处理
                LinearLayout blackLayout = new LinearLayout(context);
				LayoutParams blankParams = new LayoutParams(-1, -1, 1);
				blackLayout.setLayoutParams(blankParams);
                headerlayout.addView(blackLayout);
			}
			
			mainlayout.addView(headerlayout);
			//横线
			View v= new ImageView(context);
			v.setLayoutParams(new LayoutParams(-1,1));
			v.setBackgroundColor(Color.rgb(179, 179, 179));
			mainlayout.addView(v);
			return mainlayout;
		}
	}

}
