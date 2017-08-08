 package com.hanfeng.guildsdk;

import android.R.string;

import com.hanfeng.guildsdk.bean.RoleInfo;
import com.hanfeng.guildsdk.services.DeviceInfo;

/**
 * YhSDK 常数变量
 */
public final class Constants {
    public static final String TAG = "YYSDK";
    public static final String VERSION = "2.3.0";
    public static final int PLATFORM = 4;

    public static AppInfo APPINFO = new AppInfo();
    public static DeviceInfo DEVICE_INFO = null;
    public static OrderInfo ORDER_INFO = null;
    public static String COMPANY = null;
    public static RoleInfo ROLE_INFO= null;
    public static Mode mode = Mode.release;
    public final static int USERNAME_LOGIN_MIN_LEN = 4;// 银汉用户名最小长度（登录）
    public final static int USERNAME_LOGIN_MAX_LEN = 12;// 银汉用户名最大长度（登录）
    public final static int PASSWORD_MIN_LEN = 6;// 银汉密码最小长度
    public final static int PASSWORD_MAX_LEN = 12;// 银汉密码最大长度
    public static String AUTH_SERVER_URL = "http://119.29.50.47:4003/";//正式
    public static String AUTH_SERVER_URL1 = "http://119.29.50.47:4003/";//正式
    public static String AUTH_SERVER_URL2 = "http://119.29.50.47:4003/";//备用
    public static String PAY_SERVER_URL = "http://119.29.50.47:4004/";
    public static String USERCENTER_SERVER_URL = "http://id.yh.cn/";
    public static String VERIFY = "http://119.29.50.47:4005/";
    
    public static String ULO_WX_URL;
    public static String TD_RY_CHANNEL;
    
    public static String USERAPPCENTER_URL = "http://192.168.31.88:9000/";//浮标访问正式地址
    
    public static boolean isPORTRAIT = false;
    
    private Constants() {
    	
    }
    public static void init() {
//    	mode == Mode.release
        if (mode == Mode.release) {
        	
//        	联运正式
            AUTH_SERVER_URL = "http://119.29.50.47:4003/";//正式
            PAY_SERVER_URL = "http://119.29.50.47:4004/";
            USERCENTER_SERVER_URL = "http://id.yh.cn/";
            USERAPPCENTER_URL = "http://119.29.50.47:4007/"; //浮标访问正式地址
            VERIFY = "http://119.29.50.47:4005/";
            
/*//           专服正式
            AUTH_SERVER_URL = "http://119.29.50.143:4003/";//正式
            PAY_SERVER_URL = "http://119.29.50.143:4004/";
            USERAPPCENTER_URL = "http://119.29.50.143:4007/"; //浮标访问正式地址;
*/            
        } else {
            //汉风服测试的地址
            AUTH_SERVER_URL = "http://123.207.92.126:8085/";
            AUTH_SERVER_URL1 = "http://123.207.92.126:8085/";
            AUTH_SERVER_URL2 = "http://123.207.92.126:8085/";
            PAY_SERVER_URL = "http://123.207.92.126:8085/";
            USERCENTER_SERVER_URL = "http://123.207.92.126:8085/";
            USERAPPCENTER_URL = "http://123.207.92.126:8001/"; //浮标访问测试地址
            VERIFY = "http://123.207.92.126:5003/";
        }
    }
	
/*  private String testVurl = "http://123.207.92.126:5003/verifyToken";
	private String url = "http://119.29.50.47:4005/verifyToken";
	private String vurl = "http://119.29.50.47:4005/verifyToken";
    */
	
	/**
	 * sid 验证接口
	 */
	public static String getVerifyUrl(){
		return VERIFY+"verifyToken";
	}
	
    /* 登录   */

    /**
     * login/v2/nlogin
     */
    public static String getLoginUrl() {
        return AUTH_SERVER_URL + "login/v2/nlogin";
    }
	
	/* 账号注册  */
    /**
     * 账号注册 检查账户是否已经存在
     * register/v2/caccoun
     */
    public static String getCheckAccountUrl() {
        return AUTH_SERVER_URL + "register/v2/caccount";
    }

    /**
     * 账号注册  预处理
     * register/v2/prer
     */
    public static String getRegisterAccountUrl() {
        return AUTH_SERVER_URL + "register/v2/prer";
    }

    /**
     * 账号注册  注册
     * /register/v2/r
     */
    public static String ModifyAccountUrl() {
        return AUTH_SERVER_URL + "register/v2/r";
    }
	
	/* 手机注册*/

    /**
     * 手机注册 - 获取验证码
     * register/v2/rvcode
     */
    public static String getPhoneRegisterVcode() {
        return AUTH_SERVER_URL + "register/v2/rvcode";
    }

    /**
     * 手机注册 - 注册
     * /register/v2/mr
     */
    public static String getPhoneRegisterUrl() {
        return AUTH_SERVER_URL + "register/v2/mr";
    }
	
	/*  ----手机号找回---- */

    /**
     * 手机找回密码    获取验证码
     * retrievePwd/v2/a
     */
    public static String getFindPassWordByMobile() {
        return AUTH_SERVER_URL + "retrievePwd/v2/a";
    }

    /**
     * 手机找回密码    重新获取验证码，
     * retrievePwd/v2/b
     */
    public static String getRegetVcodeUrl() {
        return AUTH_SERVER_URL + "retrievePwd/v2/b";
    }
    /**
     * 手机找回密码     重置密码
     * retrievePwd/v2/c
     */
    public static String getPhoneResetNewPassWordUrl() {
        return AUTH_SERVER_URL + "retrievePwd/v2/c";
    }
	
	/*   ----账号找回-----  */

    /**
     * 账号找回   获取账户绑定联系方式
     * retrievePwd/a
     */
    public static String getFindPwdAUrl() {
        return AUTH_SERVER_URL + "retrievePwd/a";
    }

    /**
     * 账号找回  （邮箱找回密码,手机找回发送验证），重新发送验证码
     * retrievePwd/b
     */
    public static String getFindPwdBUrl() {
        return AUTH_SERVER_URL + "retrievePwd/b";
    }

    /**
     * 账号找回  重置密码
     * retrievePwd/v2/d
     */
    public static String getPhoneFindPwd() {
        return AUTH_SERVER_URL + "retrievePwd/v2/d";
    }
    /**
     * 初始化及支付
     * @return
     */
    public static String getInitUrl() {
        return AUTH_SERVER_URL + "init";
    }
    /**
     * 提交游戏信息
     * @return
     */
    public static String getsubmitGameInfoUrl() {
        return AUTH_SERVER_URL + "game/subPlayerInfo";
    }
    
    public static String getAlipaySignUrl() {
        return PAY_SERVER_URL + "pay/sign/alipay";
    }

    public static String getUnionSignUrl() {
        return PAY_SERVER_URL + "pay/sign/unionpay";
    }

    public static String getShenzhoufuSignUrl() {
        return PAY_SERVER_URL + "pay/sign/szf";
    }

    public static String getWeixinpaySignUrl() {
        return PAY_SERVER_URL + "pay/sign/weixinpay";
    }

    public static String getWeixinUnionpaySignUrl() {
        return PAY_SERVER_URL + "pay/sign/weixinpayunion";
    }

    //获取代金卷的接口
    public static String getCouponCoinUrl() {
        return PAY_SERVER_URL + "pay/sign/getcouponcoin";
    }

    //获取代金卷的接口
    public static String getCouponPayUrl() {
        return PAY_SERVER_URL + "pay/sign/couponsignandpay";
    }

    public static String getUsercenterUrl() {
        return USERCENTER_SERVER_URL + "secure/authenticate";
    }

    /****/
    //获取代金卷的接口
    public static String getAllCouponCoinUrl() {
        return PAY_SERVER_URL + "pay/sign/getallcoupon";
    }

    //浮标，账户
    public static String getAPPIdwebUserInfo() {
        return USERAPPCENTER_URL + "UserAPP/userinfo";
    }

    //浮标，消息
    public static String getAPPIdwebMessageNew() {
        return USERAPPCENTER_URL + "UserAPP/messageNew";
    }

    //浮标，礼包
    public static String getAPPIdwebMyGiftBags() {
        return USERAPPCENTER_URL + "UserAPP/myGiftBags";
    }

    //浮标，帮助
    public static String getAPPIdwebHelpMessage() {
        return USERAPPCENTER_URL + "UserAPP/helpMessage";
    }
}
