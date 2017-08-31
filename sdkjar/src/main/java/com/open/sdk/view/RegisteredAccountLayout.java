package com.open.sdk.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.open.sdk.callback.SdkListener;
import com.open.sdk.config.DetailedList;
import com.open.sdk.utlis.Shape;

/**
 * Created by Administrator on 2017/8/29.
 */
public class RegisteredAccountLayout extends RelativeLayout {
    //ViewGroup.LayoutParams.MATCH_PARENT = -1
    private Context mContext;
    private SdkListener listener;

    public RegisteredAccountLayout(Context context) {
        super(context);
        init(context);
    }

    public RegisteredAccountLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RegisteredAccountLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setSdkListener(SdkListener sdkListener) {
        listener = sdkListener;
    }

    /**
     * 初始化登录控件宽高
     */
    private void initWidthHeight() {
        LayoutParams rl = new LayoutParams(DetailedList.w, DetailedList.h);
        rl.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl.addRule(RelativeLayout.CENTER_VERTICAL);
        setLayoutParams(rl);
        setBackground(Shape.getShapeWhite(mContext));
        setPadding(DetailedList.d10, 0, DetailedList.d10, DetailedList.d12);
    }

    /**
     * 初始化UI
     *
     * @param context 上下文
     */
    private void init(Context context) {
        mContext = context;
        initWidthHeight();
        initTitle();
        initPhonePassworld();
        initLogin();
    }

    /**
     * 找回密码标题
     */
    private void initTitle() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d68);
        TextView title = new TextView(mContext);
        title.setTextColor(0xff464646);
        title.setText(DetailedList.registeredAccountTitle);
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        title.setId(1992001);
        title.setLayoutParams(layoutParams);
        addView(title);
    }

    /**
     * 输入帐号与密码
     */
    private void initPhonePassworld() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d118);
        RelativeLayout layout = new RelativeLayout(mContext);
        layoutParams.addRule(RelativeLayout.BELOW, 1992001);
        layout.setLayoutParams(layoutParams);
        layout.setBackground(Shape.getShapeGray(mContext));
        layout.setId(1992002);

        //帐号与验证码的分割线
        LayoutParams wireLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d1);
        View wire = new View(mContext);
        wireLayoutParams.topMargin = DetailedList.d39;
        wire.setLayoutParams(wireLayoutParams);
        wire.setBackgroundColor(0xffe9e9e9);


        //手机号码输入框
        LayoutParams phoneLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d40);
        EditText phone = new EditText(mContext);
        phone.setBackgroundColor(0);
        phone.setHint(DetailedList.phoneHint);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setLayoutParams(phoneLayoutParams);

        //验证码输入框
        LayoutParams phoneCodeParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d40);
        phoneCodeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        EditText code = new EditText(mContext);
        code.setBackgroundColor(0);
        code.setHint(DetailedList.phoneCodeHint);
        code.setInputType(InputType.TYPE_CLASS_PHONE);
        code.setLayoutParams(phoneCodeParams);

        //密码输入框
        LayoutParams pwdLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d40);
        EditText pwd = new EditText(mContext);
        pwdLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        pwd.setBackgroundColor(0);
        pwd.setHint(DetailedList.pwdHint);
        pwd.setLayoutParams(pwdLayoutParams);
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //密码与验证码的分割线
        LayoutParams wireLayoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d1);
        View wire2 = new View(mContext);
        wireLayoutParams2.topMargin = DetailedList.d79;
        wire2.setLayoutParams(wireLayoutParams2);
        wire2.setBackgroundColor(0xffe9e9e9);
        //添加到视图
        layout.addView(wire);
        layout.addView(wire2);
        layout.addView(pwd);
        layout.addView(code);
        layout.addView(phone);
        addView(layout);
    }

    /**
     * 注册
     */
    private void initLogin() {
        //注册
        LayoutParams loginLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d40);
        loginLayoutParams.addRule(RelativeLayout.BELOW, 1992002);
        loginLayoutParams.topMargin = DetailedList.d16;
        TextView login = new TextView(mContext);
        login.setText(DetailedList.registered);
        login.setTextSize(17);
        login.setTextColor(-1);
        login.setGravity(Gravity.CENTER);
        login.setBackground(Shape.getShapeBlue(mContext));
        login.setLayoutParams(loginLayoutParams);
        login.setId(1992003);
        login.setOnClickListener(onClickListener);
        addView(login);
    }


    /**
     * 点击事件
     */
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case 1992003:
                    Toast.makeText(mContext, "注册", Toast.LENGTH_SHORT).show();
                    return;
            }
        }
    };
}
