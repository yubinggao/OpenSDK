package com.hanfeng.guildsdk.activity;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.guildsdk.YhSDKActivity;
import com.hanfeng.guildsdk.bean.ComDrawableRect;
import com.hanfeng.guildsdk.bean.ConfBean;
import com.hanfeng.guildsdk.services.Dispatcher;
import com.hanfeng.guildsdk.services.IDataService;
import com.hanfeng.guildsdk.services.IDataService.UidType;
import com.hanfeng.guildsdk.tool.AssetTool;
import com.hanfeng.guildsdk.tool.StringTool;
import com.hanfeng.guildsdk.tool.UITool;
import com.hanfeng.guildsdk.tool.YhSdkLog;
import com.hanfeng.guildsdk.widget.PopupDropdown;
import com.hanfeng.guildsdk.widget.YhSdkButton;
import com.hanfeng.guildsdk.widget.YhSdkFooterLayout;
import com.hanfeng.guildsdk.widget.YhSdkToast;

final class LoginActivity extends ActivityUI {
	
	
	
	
	
	
	private YhSdkButton lgnBtn = null;
	private EditText uidEdtx = null;
	private EditText pwdEdtx = null;
	private ImageView dropDownSel = null;
	private TextView forgetpwdTxvw = null;
	private ImageView clean = null;
	private static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
	Activity me=null;
	String prefix  = "yhsdk/images/";
	LoginActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		me= activity;
	    //输入账号文本框 "yhsdk_main_person.png"
		uidEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "uid_edtx_hint"),
				INPUT_TYPE , "yhsdk_my_c.png");
		uidEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		uidEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				String string = INPUT_TYPE_CONTENT+"_";
				char[] acceptedChars = string.toCharArray();
				return acceptedChars;
			}
		});
        //下拉
		LinearLayout.LayoutParams dropDownParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		dropDownParams.setMargins(0, 0, (int) (Constants.DEVICE_INFO.windowWidthPx * 0.03), 0);
		dropDownSel = new ImageView(activity);
		dropDownSel.setLayoutParams(dropDownParams);
		dropDownSel.setScaleType(ImageView.ScaleType.CENTER);
		dropDownSel.setImageDrawable(AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"yhsdk_d.png",0.5f));
        //uid布局包含文本输入框和下拉
		final LinearLayout uidLayout = UITool.getInstance().edtxLinearLayout(activity, true, uidEdtx, dropDownSel);
		//输入密码文本框
		 final int PASS_INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD;
		pwdEdtx = UITool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "pwd_edtx_hint"),
				PASS_INPUT_TYPE, "yhsdk_new_lock.png");
		pwdEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		pwdEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				String string = INPUT_TYPE_CONTENT;
				char[] acceptedChars = string.toCharArray();
				return acceptedChars;
			}
		});
         //忘记密码 
		LinearLayout.LayoutParams fgetPwdParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		fgetPwdParams.setMargins(0, 0, (int) (Constants.DEVICE_INFO.windowWidthPx * 0.03),0 );
		forgetpwdTxvw = new TextView(activity);
		forgetpwdTxvw.setLayoutParams(fgetPwdParams);
		forgetpwdTxvw.setPadding(0, 0, 0, 0);
		forgetpwdTxvw.setText(asset.getLangProperty(activity, "login_fgetpwd_txvw"));
		forgetpwdTxvw.setTextColor(Color.rgb(105, 155, 235));
		forgetpwdTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(16F, false));
		forgetpwdTxvw.setGravity(Gravity.CENTER);
		 //下拉
		clean = new ImageView(activity);
		LinearLayout.LayoutParams cleanparam = new LinearLayout.LayoutParams(-2, -1);
		cleanparam.setMargins(0, 0, (int) (Constants.DEVICE_INFO.windowWidthPx * 0.03), 0);
		clean.setLayoutParams(cleanparam);
		clean.setScaleType(ImageView.ScaleType.CENTER);
		clean.setImageDrawable(AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"yhsdk_reset_pwd.png",1.2f));
//		密码的输入框有效
		if(pwdEdtx.getText() != null && pwdEdtx.getText().length() != 0){
			forgetpwdTxvw.setVisibility(View.VISIBLE);
			clean.setVisibility(View.VISIBLE);
		}else{
			clean.setVisibility(View.GONE);
			forgetpwdTxvw.setVisibility(View.INVISIBLE);
		}
		forgetpwdTxvw.setVisibility(View.VISIBLE);
         //加入忘记密码布局中
		LinearLayout pwdLayout = UITool.getInstance().edtxLinearLayout(activity, true, pwdEdtx, forgetpwdTxvw,clean);
		//登录按钮
		lgnBtn = new YhSdkButton(activity, asset.getLangProperty(activity, "login_account_btn")) {
			@Override
			public void click(View view) {
				if (uidEdtx != null && TextUtils.isEmpty(uidEdtx.getText())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if(!StringTool.isBetween(uidEdtx.getText().toString(), Constants.USERNAME_LOGIN_MIN_LEN, Constants.USERNAME_LOGIN_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}			
				if (pwdEdtx != null && TextUtils.isEmpty(pwdEdtx.getText())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_pwd_is_empty"));
					return;
				}
				if(!StringTool.isBetween(pwdEdtx.getText().toString(), Constants.PASSWORD_MIN_LEN, Constants.PASSWORD_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_pwd_is_empty"));
					return;
				}
				try {
					Dispatcher.getInstance().login(activity, uidEdtx.getText(), pwdEdtx.getText());
				} catch (Exception e) {
					YhSdkLog.getInstance().e("[ 账号登录 ] 提交信息异常：", e);
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_excep_toast"));
				}
			}
		};
//		
        //手机注册
		ConfBean left = new ConfBean();
		left.activity = ActivityFactory.PHONE_REGISTER_ACTIVITY;
		left.text = asset.getLangProperty(activity, "phone_register_activity");
		left.textColor = Color.rgb(128, 128, 128);
		left.textSize = UITool.getInstance().textSize(20F, true);
		left.rect = new ComDrawableRect();
		left.rect.left = AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"yhsdk_tt_phone.png",1.2f);
		left.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		
		//账号注册
		ConfBean right = new ConfBean();
		right.activity = ActivityFactory.ACCOUNT_REGISTER_ACTIVITY;
		right.text = " "+asset.getLangProperty(activity, "account_register_activity")+" ";
		right.textColor = Color.rgb(128, 128, 128);
		right.textSize = UITool.getInstance().textSize(20F, true);
		right.rect = new ComDrawableRect();
		right.rect.left = AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"yhsdk_tt_count.png",1.2f);
		right.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		
		YhSdkFooterLayout.Builder builder = new YhSdkFooterLayout.Builder(activity);
		builder.setLeft(left);
		builder.setRight(right);
		addViewListener(activity, uidLayout, pwdLayout,left, right);
		//将所有元素加入到整个布局中，
		return UITool.getInstance().homeparent(activity, builder.build(), uidLayout, pwdLayout, lgnBtn);
	}
	   //事件处理   
	private void addViewListener(final Activity activity, final LinearLayout uidLayout,final LinearLayout pwdLayout,ConfBean left, ConfBean right) {
		final IDataService operatData = Dispatcher.getInstance().getIdaoFactory(activity);
		Intent intent = activity.getIntent();
		String uid = intent.getStringExtra("username");
		String pwd = intent.getStringExtra("password");
		String type = intent.getStringExtra("type");
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isFastClick()) {
					return;
				}
//				
				if (view == dropDownSel) {
					try {
						List<JSONObject> list = operatData.readUids(UidType.account);
						if (list == null || list.size() == 0) {
							return;
						}
						new PopupDropdown(activity, list, uidEdtx.getHeight()).create(uidLayout, uidEdtx, pwdEdtx);
					} catch (Exception e) {
						YhSdkLog.getInstance().e("[ 账号登录 ] 展示下拉列表出现异常：", e);
					}
				} else if (view == forgetpwdTxvw) {
					if(forgetpwdTxvw.getText()!=null && forgetpwdTxvw.length()!=0){
						Intent intent = new Intent(activity, YhSDKActivity.class);
						intent.putExtra("layoutId", ActivityFactory.FIND_PWD_ACTIVITY.toString());
					if (!TextUtils.isEmpty(uidEdtx.getText())) {
						//送过去一个值
						intent.putExtra("uid", uidEdtx.getText().toString());
					}
					activity.startActivity(intent);
					activity.finish();
					}else{
						pwdEdtx.setText("");
						forgetpwdTxvw.setGravity(Gravity.CENTER);
						forgetpwdTxvw.setText(asset.getLangProperty(me, "login_fgetpwd_txvw"));
						forgetpwdTxvw.setTextColor(Color.rgb(105, 155, 235));
						forgetpwdTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(16F, false));
						forgetpwdTxvw.setBackgroundDrawable(null);
					}
				}else if(view == clean){
					pwdEdtx.setText("");
				}
			}
		};

		try {
			JSONObject user = operatData.readCurntUid(UidType.account);
			String username = user.getString("username");
			String password = user.getString("password");
			uidEdtx.setText(username);
			pwdEdtx.setText(password);
			if(type!= null){
			    uidEdtx.setText(uid);
				pwdEdtx.setText(pwd);
				}
		} catch (Exception e) {
			uidEdtx.setText("");
			pwdEdtx.setText("");
		}

		uidEdtx.setSelection(uidEdtx.getText().length());
		dropDownSel.setOnClickListener(clickListener);
		forgetpwdTxvw.setOnClickListener(clickListener);
		clean.setOnClickListener(clickListener);
		pwdEdtx.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				if (isFocus) {
					pwdEdtx.setText("");
				}
			}
		});
		
		pwdEdtx.addTextChangedListener(textwatcher);
	}
	
	TextWatcher textwatcher = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable arg0) {
			if(pwdEdtx.getText() != null && pwdEdtx.getText().length() != 0){
				forgetpwdTxvw.setVisibility(View.VISIBLE);
				clean.setVisibility(View.VISIBLE);
			}else{
				forgetpwdTxvw.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.GONE);
			}
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
		}
		
	};
	
	
//	--------------------------------------
//	--------------------------------------
//	--------------------------------------
//	---------------以下是专服的布局 实在是没办法了-----------------------
//	--------------------------------------
//	--------------------------------------
//	--------------------------------------
//	--------------------------------------
	/*
	private EditText uidEdtx = null;
	private EditText pwdEdtx = null;
	private ImageView dropDownSel = null;
	private TextView forgetpwdTxvw = null;
	private ImageView clean = null;
	private boolean jizhuwo = true;
	public  LinearLayout uidLayout = null;
	private static final String password = "123456";
	private ImageView rememberMe= null;
	private LinearLayout image= null;
	private static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
	Activity me=null;
	private Button loginBtn = null; 
	private Button speedBtn = null; 
	String prefix  = "yhsdk/images/";
	private Context context;
	public SharedPreferences sp_Jizhuwo;
	public  Editor edit;
	LoginActivity() {
	}
	public LoginActivity(Context context){
		this.context = context;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	@Override
	public LinearLayout onCreate(final Activity activity) {

		me= activity;
	    //账号,文本框
		uidEdtx = UITool.getInstance().createLoginEdtx(activity, "账号/手机号：",
				INPUT_TYPE , "yhsdk_uid_icon.png");
		uidEdtx = UITool.getInstance().createLoginEdtx(activity, "账号",
				INPUT_TYPE , "");
		
		uidEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		uidEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				String string = INPUT_TYPE_CONTENT+"_";
				char[] acceptedChars = string.toCharArray();
				return acceptedChars;
			}
		});

        //下拉
		LinearLayout.LayoutParams dropDownParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		dropDownParams.setMargins(0, 0, (int) (Constants.DEVICE_INFO.windowWidthPx * 0.04), 0);
		dropDownSel = new ImageView(activity);
		dropDownSel.setLayoutParams(dropDownParams);
		dropDownSel.setScaleType(ImageView.ScaleType.CENTER);
		dropDownSel.setScaleY(3.0f);
		dropDownSel.setScaleX(3.2f);
		dropDownSel.setImageDrawable(AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"yhsdk_new_version_draw.jpg",1f));
		
		//占位置的ImageView
		ImageView uidImageview = new ImageView(activity);
		LinearLayout.LayoutParams tanparam = new LinearLayout.LayoutParams(0, -2,2f);
		uidImageview.setBackgroundColor(Color.WHITE);
		uidImageview.setLayoutParams(tanparam);
		//将账号和下拉框 添加在账号输入框里面
	final LinearLayout uidLayout = UITool.getInstance().edtxSpeedLinearLayout(activity, true, uidEdtx, dropDownSel);
		
		//最终显示账号布局层
		LinearLayout tanUidLayout = new LinearLayout(activity);
		tanUidLayout.setOrientation(LinearLayout.HORIZONTAL);
		tanUidLayout.setBackgroundColor(Color.argb(0, 255, 255,255));
		LinearLayout.LayoutParams tanUidLp = new LinearLayout.LayoutParams(
				-1, LayoutParams.WRAP_CONTENT,8f);
		tanUidLayout.setLayoutParams(tanUidLp);

		tanUidLayout.addView(uidLayout);
		tanUidLayout.addView(uidImageview);
		
//-------------------------------------------密码输出框-----------------------		
		//输入密码,文本框
		final int PASS_INPUT_TYPE = 
				InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD;
//		密码框带logo：
		pwdEdtx = UITool.getInstance().createLoginEdtx(activity, "密码:",
				PASS_INPUT_TYPE, "yhsdk_pwd_icon.png");
		pwdEdtx = UITool.getInstance().createLoginEdtx(activity, "密码",
				PASS_INPUT_TYPE, "");
		pwdEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		pwdEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				String string = INPUT_TYPE_CONTENT;
				char[] acceptedChars = string.toCharArray();
				return acceptedChars;
			}
		});
         //忘记密码
		LinearLayout.LayoutParams fgetPwdParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		fgetPwdParams.setMargins(0, 0, (int) (Constants.DEVICE_INFO.windowWidthPx * 0.03),0 );
		forgetpwdTxvw = new TextView(activity);
		forgetpwdTxvw.setLayoutParams(fgetPwdParams);
		forgetpwdTxvw.setPadding(0, 0, 0, 0);
		forgetpwdTxvw.setText("忘记密码");
		forgetpwdTxvw.setTextColor(Color.rgb(30,169,234));
		forgetpwdTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, UITool.getInstance().textSize(14F, false));
		forgetpwdTxvw.setGravity(Gravity.CENTER);
		 //下拉
		clean = new ImageView(activity);
		LinearLayout.LayoutParams cleanparam = new LinearLayout.LayoutParams(-2, -1);
		cleanparam.setMargins(0, 0, (int) (Constants.DEVICE_INFO.windowWidthPx * 0.03), 0);
		clean.setLayoutParams(cleanparam);
		clean.setScaleType(ImageView.ScaleType.CENTER);
		clean.setImageDrawable(AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"yhsdk_new_tan_version_arrow.png",0.9f));
		clean.setScaleX(3f);
		clean.setScaleY(3f);
		//密码的输入框有效
		if(pwdEdtx.getText() != null && pwdEdtx.getText().length() != 0){
			forgetpwdTxvw.setVisibility(View.VISIBLE);
			clean.setVisibility(View.VISIBLE);
		}else{
			clean.setVisibility(View.GONE);
			forgetpwdTxvw.setVisibility(View.INVISIBLE);
		}
		forgetpwdTxvw.setVisibility(View.VISIBLE);
//		输入密码布局
		LinearLayout pwdLayout = UITool.getInstance().edtxSpeedLinearLayout(activity, true, pwdEdtx, forgetpwdTxvw);

//		最终显示的密码布局层:endPassWordLayout
		LinearLayout endPassWordLayout = new LinearLayout(activity);
		endPassWordLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams passwordLp = new LinearLayout.LayoutParams(
				-1, LayoutParams.WRAP_CONTENT,8f);
		endPassWordLayout.setLayoutParams(passwordLp);
		
//		设置一个LinearLayout 来绑定“忘记我”图片
		image= new LinearLayout(activity);
		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(0, -1,2f);
		image.setOrientation(LinearLayout.VERTICAL);
		image.setGravity(Gravity.CENTER);
		image.setLayoutParams(imageParams);
//		"忘记我"
		rememberMe = new ImageView(activity);
//		LinearLayout.LayoutParams rememberMeparam = new LinearLayout.LayoutParams(0, -2,2f);
		LinearLayout.LayoutParams rememberMeparam = new LinearLayout.LayoutParams(-1,-1);
		rememberMe.setScaleType(ImageView.ScaleType.CENTER);
		rememberMe.setScaleX(0.96f);
		rememberMe.setScaleY(0.7f);
//		rememberMe.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"yhsdk_select_remember_me.png"));
		
		//按下返回键后，activity会将一切都初始化，所以用sp存储将boolean类型的值记录起来
		sp_Jizhuwo = me.getSharedPreferences("jizhuwoheheda", Context.MODE_PRIVATE);
		edit = sp_Jizhuwo.edit();
		edit.putBoolean("sure_Jizhuwo", jizhuwo);
		
		rememberMe.setLayoutParams(rememberMeparam);
//		对"忘记我"进行监听
			rememberMe.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (jizhuwo) {
						//默认是选中，点击之后变成未选中
						rememberMe.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity, "yhsd_no_select_rember.png"));
//						YhSdkToast.getInstance().show(activity, "记住我没有被选中");
					}else{
						//
						rememberMe.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"yhsdk_select_remember_me.png"));
//						YhSdkToast.getInstance().show(activity, "记住我被选中了");
					}
					jizhuwo = !jizhuwo;
					edit.putBoolean("sure_Jizhuwo", jizhuwo);
					edit.commit();
				}
			});
		
		if(sp_Jizhuwo.getBoolean("sure_Jizhuwo", jizhuwo)){
			rememberMe.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"yhsdk_select_remember_me.png"));
		}else{
			rememberMe.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity, "yhsd_no_select_rember.png"));
		}
		
		image.addView(rememberMe);
//		将“密码框”+“忘记我”添加到最终显示的View层
		endPassWordLayout.addView(pwdLayout);
		endPassWordLayout.addView(image);
		
//		立即登录  
		loginBtn = new Button(activity);
//		一键注册
		speedBtn = new Button(activity);
//		宽：权重为1       高：包裹
		loginBtn.setLayoutParams(new LinearLayout.LayoutParams(0,-1,1));
		speedBtn.setLayoutParams(new LinearLayout.LayoutParams(0,-1,1));

		loginBtn.setScaleY(0.86f);
		speedBtn.setScaleY(0.86f);
		loginBtn.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity, "yhsdk_new_version_tan_normllogin.png"));
		speedBtn.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity, "yhsdk_new_version_tan_speedlogin.png"));
		
//		登录按钮---》监听事件
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (uidEdtx != null && TextUtils.isEmpty(uidEdtx.getText())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if(!StringTool.isBetween(uidEdtx.getText().toString(), Constants.USERNAME_LOGIN_MIN_LEN, Constants.USERNAME_LOGIN_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}			
				if (pwdEdtx != null && TextUtils.isEmpty(pwdEdtx.getText())) {
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_pwd_is_empty"));
					return;
				}
				if(!StringTool.isBetween(pwdEdtx.getText().toString(), Constants.PASSWORD_MIN_LEN, Constants.PASSWORD_MAX_LEN)){
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_pwd_is_empty"));
					return;
				}
				try {
					Dispatcher.getInstance().login(activity, uidEdtx.getText(), pwdEdtx.getText());
				} catch (Exception e) {
					YhSdkLog.getInstance().e("[ 账号登录 ] 提交信息异常：", e);
					YhSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_excep_toast"));
				}
			}
		});
//		一键注册---》按钮监听事件
		speedBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Dispatcher.getInstance().login(activity, null, password, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//将立即登录、一键注册添加到 最外层dbuttonLayout
		LinearLayout buttonLayout = new LinearLayout(activity);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.setBackgroundColor(Color.argb(0, 255, 255, 255));
//		buttonLayout.setTop(UITool.px2dp(activity,10));
		LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(
				-1, UITool.dp2px(activity,50),7f);
		buttonLayout.setLayoutParams(buttonLp);
		
		//立即登录、一键注册两个button之间的缝隙
		FrameLayout buttonGap = new FrameLayout(activity);
		buttonGap.setBackgroundColor(Color.parseColor("#00ffffff"));
		buttonGap.setLayoutParams(
				new FrameLayout.LayoutParams(UITool.dp2px(activity, 10), LayoutParams.WRAP_CONTENT));
		buttonLayout.addView(loginBtn);
		buttonLayout.addView(buttonGap);
		buttonLayout.addView(speedBtn);
		buttonLayout.setAlpha(1.0f);
		
      //手机注册
		final ConfBean left = new ConfBean();
		left.activity = ActivityFactory.PHONE_REGISTER_ACTIVITY;
		left.text =" 手机注册";
		left.textColor = Color.rgb(250,250,250);
		left.textSize = UITool.getInstance().textSize(17F, true);
		left.rect = new ComDrawableRect();
//		left.rect.left = AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"new_tan_phone.png",1f);
		left.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		//账号注册------
		ConfBean right = new ConfBean();
		right.activity = ActivityFactory.ACCOUNT_REGISTER_ACTIVITY;
		right.text =" 账号注册";
		right.textColor = Color.rgb(250,250,250);
		right.textSize = UITool.getInstance().textSize(17F, true);
		right.rect = new ComDrawableRect();
//		right.rect.left = AssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"new_new_tan.png",1f);
		right.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		//底部的View--》添加手机注册 账号注册		21358646
		TanBottomLayout.Builder builder = new TanBottomLayout.Builder(activity);
		builder.setLeft(left);
		builder.setRight(right);

		Button myLeftBtn = new Button(activity);
		myLeftBtn.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
		myLeftBtn.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"yhsdk_new_version_tan_bottom_phoneregister.png"));
		myLeftBtn.setScaleX(2.9f);
		myLeftBtn.setScaleY(0.65f);
		myLeftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Dispatcher.getInstance().showActivity(activity, ActivityFactory.PHONE_REGISTER_ACTIVITY, null);
			}
		});

		Button myRightBtn = new Button(activity);
		myRightBtn.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
		myRightBtn.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"yhsdk_new_version_tan_bottom_countregister.png"));
		myRightBtn.setScaleX(2.9f);
		myRightBtn.setScaleY(0.65f);
		myRightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					Dispatcher.getInstance().getAccount(activity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

//		myLeft 手机注册
		ImageView myLeft = new ImageView(activity);
		myLeft.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		myLeft.setScaleY(0.6f);
		myLeft.setScaleX(1f);
//		myLeft.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"tt_phone.png"));
//		myLeft.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"shoujizhuche.png"));
		myLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				YhSdkToast.getInstance().show(activity, "yayayaya--手机注册");
				Dispatcher.getInstance().showActivity(activity, ActivityFactory.PHONE_REGISTER_ACTIVITY, null);
			}
		});
//		myRight 账号注册
		ImageView myRight = new ImageView(activity);
		myRight.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		myRight.setScaleY(0.6f);
		myRight.setScaleX(1f);
//		myRight.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"tt_count.png"));
//		myRight.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"zhanghaozhuche.png"));
		myRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Dispatcher.getInstance().getAccount(activity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
//		！！！最终显示底部的foot（账号注册+手机注册）！！！
		LinearLayout dibuLayout = new LinearLayout(activity);
		dibuLayout.setOrientation(LinearLayout.HORIZONTAL);
		dibuLayout.setBackgroundColor(Color.argb(0, 255, 255, 255));
		LinearLayout.LayoutParams dibuLp = new LinearLayout.LayoutParams(
				-1, LayoutParams.WRAP_CONTENT,8f);
		dibuLayout.setLayoutParams(dibuLp);
		
//		创建两个LiearLayout的布局--平分一个LiearLayout 宽：平分  ----  高：matchparent》 
//		这两个LiearLayout 分别添加 手机注册 账号注册 到具体的布局当中 图片的大小设置为包裹内容 然后重心垂直居中
//		手机注册布局
		LinearLayout a = new LinearLayout(activity);
		a.setOrientation(LinearLayout.HORIZONTAL);
		a.setGravity(Gravity.CENTER);
		a.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
//		a.addView(myLeft);
		a.addView(myLeftBtn);
//		账号注册布局
		LinearLayout b = new LinearLayout(activity);
		b.setOrientation(LinearLayout.HORIZONTAL);
		b.setGravity(Gravity.CENTER);
		b.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
//		b.addView(myRight);
		b.addView(myRightBtn);

		dibuLayout.addView(a);
		dibuLayout.addView(b);
//		addViewListener(activity, uidLayout, pwdLayout,left, right);
		addViewListener(activity, uidLayout, pwdLayout,myLeft, myRight);

		ImageView line = new ImageView(activity);
//		LinearLayout.LayoutParams imageView = new LinearLayout
		LinearLayout.LayoutParams lineSpearator = new LinearLayout.LayoutParams(-1,-2);
		lineSpearator.setMargins(0,UITool.dp2px(activity,2),0,0);
		line.setBackgroundDrawable(LoginPictureTools.loadImageFromAsserts(activity,"yhsdk_new_version_tan_line.png"));
		line.setLayoutParams(lineSpearator);



	
*/
	
}
