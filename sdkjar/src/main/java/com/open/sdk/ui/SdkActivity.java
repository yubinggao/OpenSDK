package com.open.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.open.sdk.config.DetailedList;


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
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(DetailedList.w, DetailedList.h);
        rl.addRule(RelativeLayout.CENTER_VERTICAL);
        rl.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.setPadding(DetailedList.d10, 0, DetailedList.d10, DetailedList.d12);
        layout.setBackgroundColor(0xffff0000);
        layout.setLayoutParams(rl);
        return layout;
    }
}
