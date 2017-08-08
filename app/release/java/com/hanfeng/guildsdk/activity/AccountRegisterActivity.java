package com.hanfeng.guildsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.services.Dispatcher;
import com.hanfeng.guildsdk.tool.StringTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.YhSdkButton;
import com.hanfeng.guildsdk.widget.YhSdkCheckBox;
import com.hanfeng.guildsdk.widget.YhSdkHeadererLayout;
import com.hanfeng.guildsdk.widget.YhSdkToast;
/**
 *获取系统产生的账号显示在用户名中，用户名可以修改，用户名最长为12位字符
 * 
 */
final class AccountRegisterActivity extends ActivityUI {
	private static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    private String uid="";
	AccountRegisterActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		Intent intent = activity.getIntent();
		uid = intent.getStringExtra("uid");
		String account = intent.getStringExtra("account");
		//用户名
		final EditText usrEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "register_phone_user"), INPUT_TYPE, "yhsdk_uid_icon.png");
		usrEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		usrEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				char[] acceptedChars = INPUT_TYPE_CONTENT.toCharArray();
				return acceptedChars;
			}
		});
		usrEdtx.setText(account);
		usrEdtx.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				YhSdkToast toast = YhSdkToast.getInstance();
				if(!hasFocus){
					if (usrEdtx != null && TextUtils.isEmpty(usrEdtx.getText())) {
						YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
						return;
					}
					if(!StringTool.isBetween(usrEdtx.getText().toString(), Constants.USERNAME_LOGIN_MIN_LEN, Constants.USERNAME_LOGIN_MAX_LEN)){
						YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
						return;
					}
					if(!StringTool.isLetterOrNumer(usrEdtx.getText().toString())){
						YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
						return;
					}
					try {
						Dispatcher.getInstance().checkAccount(activity,usrEdtx.getText().toString(), uid);
					} catch (Exception e) {
						YhSdkLog.getInstance().e("用户名注册时提交信息异常：", e);
						toast.show(activity, asset.getLangProperty(activity, "register_excep_toast") + e.getMessage());
					}
				}
				
			}
		});
		LinearLayout usrLayout = UITool.getInstance().edtxLinearLayout(activity, true, usrEdtx);
		//密码
		 final int PASS_INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD;
		final EditText regPwdEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "register_pwd_edtx_hint"), PASS_INPUT_TYPE, "yhsdk_pwd_icon.png");
		regPwdEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		regPwdEdtx.setKeyListener(new NumberKeyListener() {
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
		LinearLayout regPwdLayout = UITool.getInstance().edtxLinearLayout(activity, true,regPwdEdtx);
         //注册按钮
		final YhSdkButton regBtn = new YhSdkButton(activity, asset.getLangProperty(activity, "register_account_comfirm")) {
			@Override
			public void click(View view) {
				YhSdkToast toast = YhSdkToast.getInstance();
				if (usrEdtx != null && TextUtils.isEmpty(usrEdtx.getText())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if(!StringTool.isBetween(usrEdtx.getText().toString(), Constants.USERNAME_LOGIN_MIN_LEN, Constants.USERNAME_LOGIN_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if(!StringTool.isLetterOrNumer(usrEdtx.getText().toString())){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if (TextUtils.isEmpty(regPwdEdtx.getText())) {
					toast.show(activity, asset.getLangProperty(activity, "register_pwd_is_null"));
					return;
				}
				
				if(!StringTool.isBetween(regPwdEdtx.getText().toString(), Constants.PASSWORD_MIN_LEN, Constants.PASSWORD_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "register_pwd_is_null"));
					return;
				}
				//注册时只能是数字和字母
				if(!StringTool.isLetterOrNumer(regPwdEdtx.getText().toString())){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "register_pwd_is_null"));
					return;
				}
				try {
					Dispatcher.getInstance().register(activity,usrEdtx.getText().toString(), regPwdEdtx.getText().toString() ,uid);
				} catch (Exception e) {
					YhSdkLog.getInstance().e("用户名注册时提交信息异常：", e);
					toast.show(activity, asset.getLangProperty(activity, "register_excep_toast") + e.getMessage());
				}
			}
		};
		regBtn.setMargins(0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.06), 0, (int) (Constants.DEVICE_INFO.windowHeightPx * 0.11));
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
		int top = (int) (Constants.DEVICE_INFO.windowHeightPx * 0.02);
		checkBox.setPadding(0, top, 0, 0);
		YhSdkHeadererLayout header = UITool.getInstance().getTitle(activity, "register_account_btn");
		LinearLayout body = new LinearLayout(activity);
		LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(	-1, -1);
		bodyLayoutParams.setMargins(0, 0, 0,(int) (Constants.DEVICE_INFO.windowHeightPx * 0.01));
		body.setLayoutParams(bodyLayoutParams);
		body.setOrientation(LinearLayout.VERTICAL);
		body.setGravity(Gravity.LEFT);
		body.addView(usrLayout);
		body.addView(regPwdLayout);
//		body.addView(checkBox);
		body.addView(regBtn);
		return uitool.parent(activity,header,body);
	}

}
