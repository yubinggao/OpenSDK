package com.open.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.open.sdk.callback.SdkListener;
import com.open.sdk.config.DetailedList;
import com.open.sdk.view.LoginLayout;
import com.open.sdk.view.RegisteredAccountLayout;
import com.open.sdk.view.SetPasswordLayout;


/**
 * Created by Administrator on 2017/8/28.
 */
public class SdkActivity extends Activity implements SdkListener {
    private Context mContext;
    private LoginLayout loginLayout;
    private SetPasswordLayout setPasswordLayout;
    private RegisteredAccountLayout registeredAccountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailedList.initData(this);
        setContentView(layout());
    }

    private View layout() {
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.addView(loginLayout());
        layout.addView(setPasswordLayout());
        layout.addView(registeredAccountLayout());
        return layout;
    }


    private View loginLayout() {
        loginLayout = new LoginLayout(this);
        loginLayout.setSdkListener(this);
        return loginLayout;
    }

    private View setPasswordLayout() {
        setPasswordLayout = new SetPasswordLayout(this);
        setPasswordLayout.setSdkListener(this);
        setPasswordLayout.setVisibility(View.GONE);
        return setPasswordLayout;
    }

    private View registeredAccountLayout() {
        registeredAccountLayout = new RegisteredAccountLayout(this);
        registeredAccountLayout.setSdkListener(this);
        registeredAccountLayout.setVisibility(View.GONE);
        return registeredAccountLayout;
    }

    @Override
    public void login() {

    }

    @Override
    public void setPwd() {
        loginLayout.setVisibility(View.GONE);
        setPasswordLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPhone() {
        loginLayout.setVisibility(View.GONE);
        registeredAccountLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (loginLayout.getVisibility() == View.GONE) {
                loginLayout.setVisibility(View.VISIBLE);
                setPasswordLayout.setVisibility(View.GONE);
                registeredAccountLayout.setVisibility(View.GONE);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
