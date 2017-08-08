package com.hanfeng.guildsdk;

public final class YhStatusCode {
	private YhStatusCode(){}
	
	public static final int SUCCESS = 0;
	/** 网络异常 */
	public static final int NET_UNAVAILABLE = 1;
	/** 服务端异常 */
	public static final int SERVER_ERROR = 2;
	/** 响应信息格式异常 */
	public static final int RESULT_FORMAT_ERROR = 3;
	
	/** 验证SDK客户端为非法 */
	public static final int CLIENT_INVALID = 101;
	/** 初始化失败 */
	public static final int INIT_FAIL = 102;
	
	/** 账户登录失败 */
	public static final int ACCOUNT_LOGIN_FAIL = 201;
	/** 手机登录失败 */
	public static final int PHONE_LOGIN_FAIL = 202;
	/** 验证码错误 */
	public static final int VERIFY_CODE_ERROR = 203;
	
	/** 注册成功之后登录 */
	
	public static final int REG_SUCCESS = 300;
	/** 注册失败 */
	
	public static final int REG_FAIL = 301;
	/*支付返回码*/
	
	public static final int PAY_SUCCESS = 600;
	public static final int PAY_FAIL = 601;
	public static final int PAY_CANCEL = 602;
	public static final int PAY_CONFIRM = 603;
}
