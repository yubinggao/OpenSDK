package com.open.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.open.sdk.config.DetailedList;
import com.open.sdk.view.LoginLayout;


/**
 * Created by Administrator on 2017/8/28.
 */
public class SdkActivity extends Activity {
    private Context mContext;

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
        return layout;
    }

    private View loginLayout() {
        LoginLayout layout = new LoginLayout(this);
        return layout;
    }
}
