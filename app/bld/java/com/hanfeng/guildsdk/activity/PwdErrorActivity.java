package com.hanfeng.guildsdk.activity;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.services.Code;
import com.hanfeng.guildsdk.services.Dispatcher;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.YhSdkButton;
import com.hanfeng.guildsdk.widget.YhSdkToast;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

final class PwdErrorActivity extends ActivityUI {

	@Override
	public LinearLayout onCreate(final Activity activity) {
		ImageView vcode = new ImageView(activity);
		vcode.setImageBitmap(Code.getInstance().getBitmap());

		final EditText vcodeEdtx = uitool.createEdtx(activity, "  "+asset.getLangProperty(activity, "phone_vcode_edtx_hint"), InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, "yhsdk_valicode.png");
		vcodeEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });

		TextView splitTxvw = new TextView(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
		splitTxvw.setLayoutParams(params);
		splitTxvw.setText("|");
		splitTxvw.setTextSize(uitool.textSize(22, true));
		splitTxvw.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		final ImageView vcodeImvw = new ImageView(activity);
		vcodeImvw.setLayoutParams(params);
		vcodeImvw.setBackgroundDrawable(new BitmapDrawable(Code.getInstance().getBitmap()));

		ImageView refreshTxvw = new ImageView(activity);
		refreshTxvw.setLayoutParams(params);
		refreshTxvw.setBackgroundDrawable(asset.decodeDensityDpiDrawable(activity, "yhsdk_refresh.png"));

		refreshTxvw.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vcodeImvw.setBackgroundDrawable(new BitmapDrawable(Code.getInstance().getBitmap()));
			}
		});

		LinearLayout edtxLayout = UITool.getInstance().edtxLinearLayout(activity, true, vcodeEdtx, splitTxvw, vcodeImvw, refreshTxvw);
		edtxLayout.setPadding(0, 0, (int) (Constants.DEVICE_INFO.windowWidthPx * 0.025), 0);

		TextView sendedPhoneTxvw = new TextView(activity);
		LinearLayout.LayoutParams txvwParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		txvwParams.setMargins(0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.05), 0, 0);
		sendedPhoneTxvw.setLayoutParams(txvwParams);
		sendedPhoneTxvw.setText(asset.getLangProperty(activity, "pwd_error_txvw"));
		sendedPhoneTxvw.setTextColor(Color.rgb(90, 102, 127));
		sendedPhoneTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(18F, true));

		final YhSdkButton verifySubBtn = new YhSdkButton(activity, asset.getLangProperty(activity, "phone_vcode_post")) {
			@Override
			public void click(View view) {
				if (TextUtils.isEmpty(vcodeEdtx.getText())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "pwd_error_empty_msg"));
					return;
				}

				if (!Code.getInstance().isCode(vcodeEdtx.getText().toString())) {
					YhSdkToast.getInstance().show(activity, "验证码不正确，请重新输入");
					return;
				}

				try {
					Dispatcher.getInstance().pwdError(activity);
				} catch (Exception e) {
					YhSdkLog.getInstance().e("[ 密码错误次数验证码 ] 提交信息异常：", e);
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_excep_toast") + e.getMessage());
				}
			}
		};
		verifySubBtn.setMargins(0, 0, 0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.35));
		LinearLayout footer = new LinearLayout(activity);
		footer.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.1)));
		return uitool.parent(activity, footer, edtxLayout, sendedPhoneTxvw, verifySubBtn);
	}

}
