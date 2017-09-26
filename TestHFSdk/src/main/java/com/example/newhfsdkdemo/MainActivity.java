package com.example.newhfsdkdemo;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import com.hanfeng.guildsdk.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hanfeng.nsdk.NSdk;
import com.hanfeng.nsdk.NSdkListener;
import com.hanfeng.nsdk.NSdkStatusCode;
import com.hanfeng.nsdk.bean.NSAppInfo;
import com.hanfeng.nsdk.bean.NSLoginResult;
import com.hanfeng.nsdk.bean.NSPayInfo;
import com.hanfeng.nsdk.bean.NSRoleInfo;
import com.hanfeng.nsdk.exception.NSdkException;

public class MainActivity extends Activity {
    private static final String TAG = "YYSDK";
    private String testVurl = "http://123.207.92.126:5003/verifyToken";
    private String url = "http://119.29.50.47:4005/verifyToken";
    private String vurl = "http://119.29.50.47:4005/verifyToken";
    private TextView uidtv;
    private TextView sidtv;
    private TextView resulttv;
    private TextView mTvshow;
    private EditText editText;
    private String uid = "";
    private String sid = "";
    //	appId和appKey记得要替换成自己的哟
    private String appId = "10000";
    private String appKey = "424ae8a4fe9a342a1fbb64";

    private Activity mActivity;
    private NSdk nsdk = null;
    private NSAppInfo appinfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Constants.isPORTRAIT = true;
        mActivity = this;
        nsdk = NSdk.getInstance();
        appinfo = new NSAppInfo();
        appinfo.appId = appId;//填写自己游戏appId(由sdk的商务提供)
        appinfo.appKey = appKey;//填写自己游戏的appKey(由sdk的商务提供)

        //		初始化
        init();
        //		初始化控件
        initView();
        //		初始化监听事件
        initListener();
        //		初始化环境（区分正式？测试）
        initEnvironment();
        //		环境回调监听（必接）
        initCallBack();
    }

    private void init() {
        try {
            nsdk.init(mActivity, appinfo, new NSdkListener<String>() {
                @Override
                public void callback(int code, String response) {
                    Toast.makeText(mActivity, response, 0).show();
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.editText);
        uidtv = (TextView) findViewById(R.id.uid);
        sidtv = (TextView) findViewById(R.id.sid);
        mTvshow = (TextView) findViewById(R.id.tvshow);
        resulttv = (TextView) findViewById(R.id.result);
    }

    private void initListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.loginBtn:
                        login();
                        break;
                    case R.id.switchBtn:
                        nsdk.accountSwitch(mActivity);
                        break;
                    case R.id.payBtn:
                        pay();
                        break;
                    case R.id.exitBtn:
                        try {
                            nsdk.exit(mActivity, new NSdkListener<Void>() {
                                @Override
                                public void callback(int code, Void arg1) {
                                    if (code == NSdkStatusCode.EXIT_COMFIRM) {
                                        nsdk.logout(MainActivity.this);
                                        System.exit(0);
                                    } else if (code == NSdkStatusCode.EXIT_CANCLE) {
                                        //返回游戏，不退出
                                    }
                                }
                            });
                        } catch (NSdkException e) {
                            e.printStackTrace();
                        }
                    case R.id.showFloatBtn:
                        nsdk.showToolBar(mActivity);
                        break;
                    case R.id.hideFloatBtn:
                        nsdk.hideToolBar(mActivity);
                        break;

                    case R.id.getChannelIdBtn:
                        Toast.makeText(mActivity, "当前渠道码是：" + nsdk.getChannel(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.getSDKVersionBtn:
                        Toast.makeText(mActivity, "当前SDK版本是：" + nsdk.getSdkVersion(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.verify:
                        verify();
                        break;
                    case R.id.serBtn:
                        Button ser = (Button) findViewById(R.id.serBtn);
                        if (ser.getText().toString().trim().equals("正式服验证地址")) {
                            ser.setText("测试服验证地址");
                            vurl = testVurl;
                        } else {
                            ser.setText("正式服验证地址");
                            vurl = url;
                        }
                        break;

                    case R.id.subPlayerBtn:
                        submitGameInfo();
                    default:
                        break;
                }
            }
        };

        findViewById(R.id.loginBtn).setOnClickListener(listener);
        findViewById(R.id.payBtn).setOnClickListener(listener);
        findViewById(R.id.switchBtn).setOnClickListener(listener);
        findViewById(R.id.exitBtn).setOnClickListener(listener);
        findViewById(R.id.showFloatBtn).setOnClickListener(listener);
        findViewById(R.id.hideFloatBtn).setOnClickListener(listener);

        findViewById(R.id.getChannelIdBtn).setOnClickListener(listener);
        findViewById(R.id.getSDKVersionBtn).setOnClickListener(listener);
        findViewById(R.id.verify).setOnClickListener(listener);
        findViewById(R.id.serBtn).setOnClickListener(listener);
        findViewById(R.id.subPlayerBtn).setOnClickListener(listener);
    }

    private void initEnvironment() {
        Map<String, String> sdkconfig = new HashMap<String, String>();
        XmlPullParser parser = Xml.newPullParser();
        String xmlstr = AssetsTool.getInstance().getAssetConfigs(MainActivity.this, "hfnsdk/config/sdkconf.xml");
        try {
            parser.setInput(new StringReader(xmlstr));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if ("configer".equals(tag) || parser.getAttributeCount() == 0) {
                        eventType = parser.next();
                        continue;
                    } else if ("component".equals(tag)) {
                    } else {
                        sdkconfig.put(tag, parser.getAttributeValue(0));
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Boolean isdebug = Boolean.parseBoolean(sdkconfig.get("debug"));
        Log.d(TAG, "" + xmlstr);
        mTvshow.setText("appId:" + appId + "    当前环境:" + (isdebug ? "测试" : "正式"));
    }

    private void initCallBack() {
        nsdk.setAccountSwitchListener(new NSdkListener<String>() {
            @Override
            public void callback(int code, String resp) {
                if (code == NSdkStatusCode.SWITCH_SUCCESS) {
                    //todo清空游戏信息，回到登陆界面
                    uid = "";
                    sid = "";
                    uidtv.setText("");
                    sidtv.setText("");
                    resulttv.setText("");
                } else if (code == NSdkStatusCode.SWITCH_FAILURE) {

                }
            }
        });
    }

    //	登录：
    private void login() {
        try {
            nsdk.login(MainActivity.this, new NSdkListener<NSLoginResult>() {

                @Override
                public void callback(int code, NSLoginResult response) {
                    Toast.makeText(mActivity, response.msg, Toast.LENGTH_LONG).show();
                    switch (code) {
                        case NSdkStatusCode.LOGIN_SUCCESS:
                            sid = response.sid;
                            uid = response.uid;
                            uidtv.setText(uid);
                            sidtv.setText(sid);
                            Log.i(TAG, response.msg + ":sid=" + response.sid + "uid=" + response.uid);
                            break;
                        case NSdkStatusCode.LOGIN_CANCLE:
                            Log.i(TAG, response.msg);
                            break;
                        case NSdkStatusCode.LOGIN_OTHER:
                            Log.i(TAG, response.msg);
                            break;
                        case NSdkStatusCode.INIT_FAILURE:
                            Log.i(TAG, response.msg);
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (NSdkException e) {
            e.printStackTrace();
        }
    }

    private void pay() {
        String priceStr = editText.getText().toString().trim();
        int price = 100;
        if (TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            price = Integer.valueOf(priceStr);
        } catch (Exception e) {
            Toast.makeText(this, "输入不合法", Toast.LENGTH_LONG).show();
            return;
        }

        final NSPayInfo info = new NSPayInfo();
        info.gameName = "时空猎人";
        info.productId = "2344";
        info.productName = "金币";
        info.productDesc = "猎人金币";
        info.price = price;//金额
        info.ratio = 10;
        info.buyNum = 0;
        info.coinNum = 0;
        info.uid = uid;
        info.serverId = 1005100; //区服ID 必传
        info.roleId = "角色ID:65554884434";//角色id 必传
        info.roleName = "李宗伟"; //角色名 必传
        info.privateField = "";//订单透传字段
        info.roleLevel = 20;
        try {
            nsdk.pay(MainActivity.this, info, new NSdkListener<String>() {
                @Override
                public void callback(int code, String response) {
                    Log.i(TAG, "code=" + code + ", response=" + response);
                    Log.i(TAG, "info:" + info.toString());
                    Toast.makeText(mActivity, response, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verify() {
        if (TextUtils.isEmpty(sid)) {
            Toast.makeText(mActivity, "请先进行登录", Toast.LENGTH_SHORT).show();
            return;
        }

//        HttpProgressAsyncTask task = new HttpProgressAsyncTask(mActivity, vurl, "正在进行验证……") {//正式环境sid
        HttpProgressAsyncTask task = new HttpProgressAsyncTask(mActivity, testVurl, "正在进行验证……") {//测试环境sid
            @Override
            protected void onHandleResult(JSONObject message) throws JSONException {
                String status = message.getString("status");
                if ("YHYZ_000".equals(status)) {
                    uid = message.getString("userId");
                    resulttv.setText(message.getString("msg") + "，返回的uid为：" + uid);
                } else {
                    resulttv.setText(message.getString("msg"));
                }
            }

            @Override
            protected void onHandleError(String msg) {
                resulttv.setText("验证异常：" + msg);
            }
        };
        VerifyBean bean = new VerifyBean();
        bean.gameId = appinfo.appId;
        bean.sid = sid;
        bean.userId = uid;
        bean.channel = nsdk.getChannel();
        bean.version = nsdk.getSdkVersion();
        task.execute(bean.toJson(appinfo.appKey));
    }

    //	Activity生命周期回调
    @Override
    protected void onResume() {
        super.onResume();
        nsdk.onResume(mActivity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nsdk.onPause(mActivity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        nsdk.onStop(mActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nsdk.onDestroy(mActivity);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        nsdk.onConfigurationChanged(mActivity, newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        nsdk.onActivityResult(mActivity, requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        nsdk.onRestart(mActivity);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nsdk.onNewIntent(mActivity, intent);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed call");
        super.onBackPressed();
        nsdk.onBackPressed(this);
    }

    /**
     * 该接口需要在玩家登录之后调用！
     * 提交游戏信息
     */
    private void submitGameInfo() {
        NSRoleInfo roleinfo = new NSRoleInfo();
        roleinfo.userId = uid; //传登录获取的uid即可
        roleinfo.roleId = "9527"; //角色ID
        roleinfo.roleName = "骑小猪看流星";//角色名
        roleinfo.roleLevel = "129";//角色等级
        roleinfo.zoneId = "520";//区服ID
        roleinfo.zoneName = "齐云楼";//区服名字
        roleinfo.dataType = "1";//数据类型 1，进入游戏（登录后）；2，创建角色；3角色升级
        Log.i(TAG, "上传信息：" + roleinfo.toJson());
        nsdk.submitGameInfo(MainActivity.this, roleinfo);
    }
}
