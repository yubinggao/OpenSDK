package com.open.sdk.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.open.sdk.config.DetailedList;
import com.open.sdk.utlis.Shape;

/**
 * Created by Administrator on 2017/8/29.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LoginLayout extends RelativeLayout {
    //ViewGroup.LayoutParams.MATCH_PARENT = -1
    private Context mContext;

    public LoginLayout(Context context) {
        super(context);
        init(context);
    }

    public LoginLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoginLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化登录控件宽高
     */
    private void initWidthHeight() {
        LayoutParams rl = new LayoutParams(DetailedList.w, DetailedList.h);
        rl.addRule(RelativeLayout.CENTER_VERTICAL);
        rl.addRule(RelativeLayout.CENTER_HORIZONTAL);
        setLayoutParams(rl);
        setPadding(DetailedList.d10, 0, DetailedList.d10, DetailedList.d12);
        setBackground(Shape.getShapeWhite(mContext));
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
     * 登录标题
     */
    private void initTitle() {
        TextView title = new TextView(mContext);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d86);
        title.setText(DetailedList.sdkLoginTitle);
        title.setTextColor(0xff464646);
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);
        title.setId(1992001);
        title.setLayoutParams(layoutParams);
        title.setText(DetailedList.sdkLoginTitle + title.getTextSize());

        addView(title);
    }

    /**
     * 输入帐号与密码
     */
    private void initPhonePassworld() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d79);
        RelativeLayout layout = new RelativeLayout(mContext);
        layoutParams.addRule(RelativeLayout.BELOW, 1992001);
        layout.setLayoutParams(layoutParams);
        layout.setBackground(Shape.getShapeGray(mContext));
        layout.setId(1992002);

        //帐号与密码的分割线
        LayoutParams wireLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d1);
        View wire = new View(mContext);
        wireLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        wire.setLayoutParams(wireLayoutParams);
        wire.setBackgroundColor(0xffe9e9e9);

        //手机号码输入框
        LayoutParams phoneLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d40);
        EditText phone = new EditText(mContext);
        phone.setBackgroundColor(0);
        phone.setHint(DetailedList.phoneHint);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setLayoutParams(phoneLayoutParams);

        //密码输入框
        LayoutParams pwdLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d40);
        EditText pwd = new EditText(mContext);
        pwdLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        pwd.setBackgroundColor(0);
        pwd.setHint(DetailedList.pwdHint);
        pwd.setLayoutParams(pwdLayoutParams);
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //添加到视图
        layout.addView(wire);
        layout.addView(phone);
        layout.addView(pwd);
        addView(layout);
    }

    /**
     * 输入帐号与密码
     */
    private void initLogin() {
        //密码输入框
        LayoutParams loginLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DetailedList.d40);
        loginLayoutParams.addRule(RelativeLayout.BELOW, 1992002);
        loginLayoutParams.topMargin = DetailedList.d16;
        TextView login = new TextView(mContext);
        login.setText(DetailedList.login);
        login.setTextSize(17);
        login.setTextColor(-1);
        login.setGravity(Gravity.CENTER);
        login.setBackground(Shape.getShapeBlue(mContext));
        login.setLayoutParams(loginLayoutParams);
        login.setId(1992099);
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
                case 1992099:
                    Toast.makeText(mContext, "点击登录", Toast.LENGTH_SHORT).show();
                    return;
            }
        }
    };
}
