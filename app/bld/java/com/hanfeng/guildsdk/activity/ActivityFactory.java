package com.hanfeng.guildsdk.activity;

public enum ActivityFactory {
	PHONE_REGISTER_ACTIVITY, // 注册
	ACCOUNT_REGISTER_ACTIVITY, // 账号登录
	AGREEMENT_ACTIVITY, // 银汉服务协议
	ACCOUNT_LOGIN_ACTIVITY, // 账号登录
	PHONE_VERIFY_ACTIVITY, // 手机验证码
	FIND_PWD_ACTIVITY, // 寻找密码
	FIND_PWD_TYPE_ACTIVITY, // 找回密码类型选择
	FIND_PWD_VCODE_ACTIVITY, // 手机找回密码验证码
	FIND_PWD_FINISH_ACTIVITY, // 找回密码完成页面
	PWD_ERROR_ACTIVITY,
	PAY_ACTIVITY, // 支付
	CARDPAY_ACTIVITY,// 卡类支付
	GUILD_PAY_ACTIVITY;//公会支付（）

	public ActivityUI getService() {
		switch (this) {
			case ACCOUNT_LOGIN_ACTIVITY:
				return new LoginActivity();
			case PHONE_REGISTER_ACTIVITY:
				return new PhoneRegisterActivity();
			case ACCOUNT_REGISTER_ACTIVITY:
				return new AccountRegisterActivity();
			case AGREEMENT_ACTIVITY:
				return new AgreementActivity();
			case PHONE_VERIFY_ACTIVITY:
				return new PhoneVerifyCodeActivity();
			case FIND_PWD_ACTIVITY:
				return new FindPwdActivity();
			case FIND_PWD_TYPE_ACTIVITY:
				return new FindPwdTypeActivity();
			case FIND_PWD_VCODE_ACTIVITY:
				return new FindPwdVcodeActivity();
			case FIND_PWD_FINISH_ACTIVITY:
				return new FindPwdFinishActivity();
			case PAY_ACTIVITY:
				return new HfPayActivity();
			case CARDPAY_ACTIVITY:
				return new CardPayActivity();
			case PWD_ERROR_ACTIVITY:
				return new PwdErrorActivity();
			case GUILD_PAY_ACTIVITY:
				return new GuildPayActivity();
			default:
				break;
			}
		return null;
	}
	

}
