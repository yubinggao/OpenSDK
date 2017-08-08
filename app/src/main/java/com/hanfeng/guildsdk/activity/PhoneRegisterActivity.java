package com.hanfeng.guildsdk.activity;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.services.Dispatcher;
import com.hanfeng.guildsdk.tool.SmsContent;
import com.hanfeng.guildsdk.tool.StringTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSDKRes;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.YhSdkButton;
import com.hanfeng.guildsdk.widget.YhSdkCheckBox;
import com.hanfeng.guildsdk.widget.YhSdkHeadererLayout;
import com.hanfeng.guildsdk.widget.YhSdkToast;

final class PhoneRegisterActivity extends ActivityUI {
	private static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD;
	private String   mobile = null;
	private boolean  isRegetVcode = false;
	private EditText regPhoneEdtx = null;
	private EditText regVcodeEdtx = null;
	private EditText passwoedEdtx = null;
	private Button   reGetVcodeTxvw =null;
	private Boolean   isAvailable = false;


	private NumberKeyListener keylistener = new NumberKeyListener() {
		@Override
		public int getInputType() {
			return INPUT_TYPE;
		}
		@Override
		protected char[] getAcceptedChars() {
			char[] acceptedChars = INPUT_TYPE_CONTENT.toCharArray();
			return acceptedChars;
		}
	};

	PhoneRegisterActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		final String regBtnText = asset.getLangProperty(activity, "register_account_comfirm");
		final YhSdkButton regBtn = new YhSdkButton(activity, regBtnText) {
			@Override
		public void click(View view) {
				if (!isPhoneEdtxVlaidata(activity)) {
					return;
				}
				if(isAvailable == false){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "phone_reg_tips"));
					return;
				}
				YhSdkToast toast = YhSdkToast.getInstance();
				if (TextUtils.isEmpty(regVcodeEdtx.getText())) {
					toast.show(activity, asset.getLangProperty(activity, "phone_vcode_input_hint"));
					return;
				}
				if (!Pattern.matches("^[0-9]{4}$", regVcodeEdtx.getText().toString())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "phone_vcode_format_error"));
					return;
				}
				if (passwoedEdtx.getText()==null || "".equals(passwoedEdtx.getText().toString())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "findpwd_password_null"));
					return;
				}
				
				if(!StringTool.isBetween(passwoedEdtx.getText().toString(), Constants.PASSWORD_MIN_LEN, Constants.PASSWORD_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "verifyinfo_password_length"));
					return;
				}
				if(!StringTool.isLetterOrNumer(passwoedEdtx.getText().toString())){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "verifyinfo_password_letter"));
					return;
				}
			try {
					String status = "";
					if (this.getText() != null && regBtnText.equals(this.getText().toString())) {
						status = "YHYZXX_018";
					}
					Dispatcher.getInstance().phoneRegisterAndLogin(activity, regPhoneEdtx.getText().toString(), regVcodeEdtx.getText().toString(),passwoedEdtx.getText().toString(), status);
				} catch (Exception e) {
					YhSdkLog.getInstance().e("[手机注册] 提交信息异常：", e);
					toast.show(activity, asset.getLangProperty(activity, "register_excep_toast") + e.getMessage());
				}
			}
		};
		regBtn.setMargins(0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.02), 0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.04));
		YhSdkCheckBox checkBox = new YhSdkCheckBox(activity) {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				regBtn.clickable(isChecked);
				if (isChecked) {
					buttonView.setBackgroundDrawable(asset.decodeDensityDpiDrawable(activity, "yhsdk_checkbox_checked.png"));
				} else {
					buttonView.setBackgroundDrawable(asset.decodeDensityDpiDrawable(activity, "yhsdk_checkbox_uncheck.png"));
				}
			}
		};
		int top = (int)(Constants.DEVICE_INFO.windowHeightPx * 0.02);
		checkBox.setPadding(0, top, 0, 0);
	
		    YhSdkHeadererLayout header = UITool.getInstance().getTitle(activity, "register_phone_btn");
		    LinearLayout body = new LinearLayout(activity);
			LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(	-1, -1,1);
			bodyLayoutParams.setMargins(0, 0, 0,0);
			body.setLayoutParams(bodyLayoutParams);
			body.setOrientation(LinearLayout.VERTICAL);
			body.setGravity(Gravity.TOP);
			body.addView(createRegPhoneEdtxLayout(activity, regBtn));
			body.addView(createRegVcodeEdtxLayout(activity));
			body.addView(createPasswordeEdtxLayout(activity));
			body.addView(checkBox);
			body.addView(regBtn);
		 
		return uitool.parent(activity, header,body);
	}

	/**
	 * 创建注册手机号输入按钮
	 * 
	 * @param activity
	 * @return
	 */
	private LinearLayout createRegPhoneEdtxLayout(final Activity activity, final Button regBtn) {

		
		regPhoneEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "phone_uid_edtx_hint"), InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL, "yhsdk_phone_login_lefticon.png");
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(-2, -1,1);
		linearParams.setMargins(0, 0, 0, 0);
		regPhoneEdtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
		regPhoneEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		//regPhoneEdtx.setKeyListener(keylistener);
		
		regPhoneEdtx.setBackgroundResource(YhSDKRes.getRes().getDrawableId(activity, "yhsdk_corner_edtx"));
		int paddingv = (int) (Constants.DEVICE_INFO.windowHeightPx * 0.03);
		int paddingh = (int) (Constants.DEVICE_INFO.windowWidthPx * 0.03);
		regPhoneEdtx.setPadding(paddingh, paddingv, paddingh, paddingv);
		reGetVcodeTxvw = new Button(activity);
		LinearLayout.LayoutParams linearParam = new LinearLayout.LayoutParams(-1, -1,1.5f);
		int vpadding = (int) (Constants.DEVICE_INFO.windowHeightPx * 0.02);
		linearParam.setMargins((int) (Constants.DEVICE_INFO.windowWidthPx * 0.02),0,0,0);
		reGetVcodeTxvw.setPadding(vpadding, 0, vpadding, 0);
		reGetVcodeTxvw.setLayoutParams(linearParam);
		reGetVcodeTxvw.setBackgroundDrawable(null);
		reGetVcodeTxvw.setTextColor(Color.rgb(255, 144, 22));
		reGetVcodeTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(16F, false));
		reGetVcodeTxvw.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		reGetVcodeTxvw.setText("  "+asset.getLangProperty(activity, "register_phone_vcode") + "  ");
		reGetVcodeTxvw.setBackgroundResource(YhSDKRes.getRes().getDrawableId(activity, "yhsdk_corner_vcode"));

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
				regPhoneEdtx.setEnabled(true);
			}
		};

		regPhoneEdtx.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mobile != null && s != null && mobile.equals(s.toString())) {
					isRegetVcode = true;
					regBtn.setText(asset.getLangProperty(activity, "login_account_btn"));
					timer.onFinish();
				} else {
					isRegetVcode = false;
					reGetVcodeTxvw.setText(asset.getLangProperty(activity, "register_phone_vcode"));
					regBtn.setText(asset.getLangProperty(activity, "login_account_btn"));
				}
			}
		});

		reGetVcodeTxvw.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPhoneEdtxVlaidata(activity)) {
					regPhoneEdtx.setEnabled(false);
					isAvailable = true;
					try {
						mobile = regPhoneEdtx.getText().toString();
						Dispatcher.getInstance().registerByMobileVcode(activity, regBtn, timer, regPhoneEdtx, isRegetVcode);
						isRegetVcode = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		LinearLayout edtxLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1,1);
		params.setMargins(0, 0, 0, 0);
		edtxLayout.setGravity(Gravity.CENTER);
		edtxLayout.setLayoutParams(params);
		edtxLayout.setOrientation(LinearLayout.HORIZONTAL);
		edtxLayout.addView(regPhoneEdtx);
		    LinearLayout body = new LinearLayout(activity);
		    LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(	-1, 0,1f);
			bodyLayoutParams.setMargins(0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.025), 0,0);
			body.setLayoutParams(bodyLayoutParams);
			body.setOrientation(LinearLayout.HORIZONTAL);
			body.setGravity(Gravity.CENTER);
			body.addView(edtxLayout);
			body.addView(reGetVcodeTxvw);
		return body;
	}

	
	/**
	 * 创建验证码输入框
	 * 
	 * @param activity
	 * @return
	 */
	private LinearLayout createRegVcodeEdtxLayout(Activity activity) {

		regVcodeEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "phone_vcode_input_hint"), InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_CLASS_NUMBER, "yhsdk_valicode.png");
		regVcodeEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
		LinearLayout regVcodeLayout = UITool.getInstance().phoneregLinearLayout(activity, true, regVcodeEdtx);
		//if (SmsTool.hasPermission) {
		SmsContent content = new SmsContent(activity, new Handler(), regVcodeEdtx);
		  // 注册短信变化监听
		activity.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
	      //}
		return regVcodeLayout;
	}

	
	/**
	 * 创建密码输入框
	 * 
	 * @param activity
	 * @return
	 */
	private LinearLayout createPasswordeEdtxLayout(Activity activity) {
		passwoedEdtx = UITool.getInstance().createEdtx(activity," "+asset.getLangProperty(activity, "register_pwd_edtx_hint"),INPUT_TYPE , "yhsdk_pwd_icon.png");
		passwoedEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		passwoedEdtx.setKeyListener(keylistener);
		LinearLayout passwordLayout = UITool.getInstance().phoneregLinearLayout(activity, true,passwoedEdtx);
		return passwordLayout;
	}
	/**
	 * 判断手机号输入的情况
	 * 
	 * @param activity
	 * @return
	 */
	private boolean isPhoneEdtxVlaidata(Activity activity) {
		YhSdkToast toast = YhSdkToast.getInstance();

		String phoneNum = regPhoneEdtx.getText().toString();
		if (TextUtils.isEmpty(phoneNum)) {
			toast.show(activity, asset.getLangProperty(activity, "phone_num_is_empty"));
			return false;
		}

		if (!Pattern.matches("^[1][3-9][0-9]{9}$", phoneNum)) {
			YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "phone_num_format_error"));
			return false;
		}

		return true;
	}
	

}
