package com.example.newhfsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.open.sdk.config.DetailedList;
import com.open.sdk.ui.SdkActivity;

/**
 * Created by Administrator on 2017/9/28.
 */
public class TestSDKActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, SdkActivity.class));

    }
}
