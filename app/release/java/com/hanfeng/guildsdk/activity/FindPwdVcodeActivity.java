package com.hanfeng.guildsdk.activity;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.services.Dispatcher;
import com.hanfeng.guildsdk.tool.SmsContent;
import com.hanfeng.guildsdk.tool.StringTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSDKRes;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.YhSdkButton;
import com.hanfeng.guildsdk.widget.YhSdkHeadererLayout;
import com.hanfeng.guildsdk.widget.YhSdkToast;

final class FindPwdVcodeActivity extends ActivityUI {
	private TextView reGetVcodeTxvw = null;
	private EditText vcodeEdtx = null;
	private EditText passwordEdtx = null;
	FindPwdVcodeActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		Intent intent = activity.getIntent();
		final String mobile = intent.getStringExtra("contract");
		final String account = intent.getStringExtra("account");
		//header
	    YhSdkHeadererLayout header = UITool.getInstance().getTitle(activity, "findpwd_vcode_btn");
		//
		vcodeEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "phone_vcode_input_hint"),
				InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_CLASS_NUMBER, "yhsdk_valicode.png");
		vcodeEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(-2, -1,1);
		linearParams.setMargins(0, 0, 0, 0);
		vcodeEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
		vcodeEdtx.setBackgroundResource(YhSDKRes.getRes().getDrawableId(activity, "yhsdk_corner_edtx"));
		int paddingv = (int) (Constants.DEVICE_INFO.windowHeightPx * 0.02);
		int paddingh = (int) (Constants.DEVICE_INFO.windowWidthPx * 0.02);
		vcodeEdtx.setPadding(paddingh, paddingv, paddingh, paddingv);
		reGetVcodeTxvw = new Button(activity);
		LinearLayout.LayoutParams linearParam = new LinearLayout.LayoutParams(-1, -1,1.5f);
		int vpadding = (int) (Constants.DEVICE_INFO.windowHeightPx * 0.02);
		linearParam.setMargins((int) (Constants.DEVICE_INFO.windowWidthPx * 0.02),0,0,0);
		reGetVcodeTxvw.setPadding(vpadding, 0, vpadding, 0);
		reGetVcodeTxvw.setLayoutParams(linearParam);
		reGetVcodeTxvw.setBackgroundDrawable(null);
		reGetVcodeTxvw.setTextColor(Color.rgb(255, 165, 79));
		reGetVcodeTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(16F, false));
		reGetVcodeTxvw.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		reGetVcodeTxvw.setText("  "+asset.getLangProperty(activity, "register_phone_vcode") + "  ");
		reGetVcodeTxvw.setBackgroundResource(YhSDKRes.getRes().getDrawableId(activity, "yhsdk_corner_vcode"));
		 final int PASS_INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD;
		passwordEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "phone_vcode_new_passwoed_hint"),
				PASS_INPUT_TYPE, "yhsdk_pwd_icon.png");
		passwordEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		passwordEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return PASS_INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				char[] acceptedChars = INPUT_TYPE_CONTENT.toCharArray();
				return acceptedChars;
			}
		});
		LinearLayout passwordLayout = UITool.getInstance().phoneregLinearLayout(activity, true, passwordEdtx);
		
		TextView sendedPhoneTxvw = new TextView(activity);
		LinearLayout.LayoutParams txvwParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,0.7f);
		txvwParams.setMargins(0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.03), 0, 0);
		sendedPhoneTxvw.setLayoutParams(txvwParams);
		sendedPhoneTxvw.setTextColor(Color.rgb(90, 102, 127));
		sendedPhoneTxvw.setGravity(Gravity.CENTER);
		sendedPhoneTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(18F, false));

		final YhSdkButton verifySubBtn = new YhSdkButton(activity, asset.getLangProperty(activity, "phone_vcode_post")) {
			@Override
			public void click(View view) {
				if (vcodeEdtx == null || TextUtils.isEmpty(vcodeEdtx.getText())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "phone_vcode_input_hint"));
					return;
				}
				if (!Pattern.matches("^[0-9]{4}$", vcodeEdtx.getText().toString())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "phone_vcode_format_error"));
					return;
				}
				if (passwordEdtx.getText()==null || "".equals(passwordEdtx.getText().toString())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "findpwd_password_null"));
					return;
				}
			
				if(!StringTool.isBetween(passwordEdtx.getText().toString(), Constants.PASSWORD_MIN_LEN, Constants.PASSWORD_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "verifyinfo_password_length"));
					return;
				}
				if(!StringTool.isLetterOrNumer(passwordEdtx.getText().toString())){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "verifyinfo_password_letter"));
					return;
				}
				try {
					//String status = activity.getIntent().getStringExtra("status");
					//填写验证码和密码之后点击
					Dispatcher.getInstance().resetUserPhonePwd(activity, account, vcodeEdtx.getText().toString(),passwordEdtx.getText().toString(), mobile);
				} catch (Exception e) {
					YhSdkLog.getInstance().e("[手机登录验证码] 提交异常：", e);
				}
			}
		};
		verifySubBtn.setMargins(0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.05), 0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.05));
		addViewListener(activity, mobile,account, sendedPhoneTxvw, vcodeEdtx);
		SmsContent content = new SmsContent(activity, new Handler(), vcodeEdtx);
		 // 注册短信变化监听
		activity.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
		LinearLayout edtxLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1,1);
		params.setMargins(0, 0, 0, 0);
		edtxLayout.setGravity(Gravity.CENTER);
		edtxLayout.setLayoutParams(params);
		edtxLayout.setOrientation(LinearLayout.HORIZONTAL);
		edtxLayout.addView(vcodeEdtx);
		    LinearLayout body = new LinearLayout(activity);
		    LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(	-1, 0,1f);
			bodyLayoutParams.setMargins(0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.025), 0,0);
			body.setLayoutParams(bodyLayoutParams);
			body.setOrientation(LinearLayout.HORIZONTAL);
			body.setGravity(Gravity.CENTER);
			body.addView(edtxLayout);
			body.addView(reGetVcodeTxvw);
		
		return UITool.getInstance().parent(activity, header, sendedPhoneTxvw,body,passwordLayout,verifySubBtn);
	}

	private void addViewListener(final Activity activity, final String mobile, final String account,TextView sendedPhoneTxvw, final EditText vcodeEdtx) {
		final CountDownTimer timer = new CountDownTimer(60000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				long second = millisUntilFinished / 1000;
				reGetVcodeTxvw.setClickable(false);

				String text = asset.getLangProperty(activity, "phone_vcode_countdowntimer");
				int start = text.indexOf("%s");

				if (second < 10) {
					text = String.format(text, "0" + (millisUntilFinished / 1000));
				} else {
					text = String.format(text, millisUntilFinished / 1000 + "");
				}

				SpannableStringBuilder builder = new SpannableStringBuilder(text);
				builder.setSpan(new ForegroundColorSpan(Color.rgb(128, 128, 128)), start+3, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				reGetVcodeTxvw.setText(builder);
			}

			@Override
			public void onFinish() {
				reGetVcodeTxvw.setClickable(true);
				String text = asset.getLangProperty(activity, "phone_vcode_reget") + "  ";
				SpannableStringBuilder builder = new SpannableStringBuilder(text);
				builder.setSpan(new ForegroundColorSpan(Color.rgb(128, 128, 128)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				reGetVcodeTxvw.setText(builder);
			}
		};

		String text = asset.getLangProperty(activity, "phone_vcode_desc_text");
		int start = text.indexOf("%s");
		SpannableStringBuilder builder = new SpannableStringBuilder(String.format(text, mobile));
		builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 102, 0)), start, start + mobile.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		sendedPhoneTxvw.setText(builder);
		timer.start();
		reGetVcodeTxvw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isFastClick()) {
					return;
				}
				try {
					Dispatcher.getInstance().resetPwd(activity, account, 1, mobile);
				} catch (Exception e) {
					YhSdkLog.getInstance().e("[手机登录提交验证码] 重新获取验证码异常：", e);
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "phone_vcode_excpt_toast"));
				}
			}
		});

	}

}
