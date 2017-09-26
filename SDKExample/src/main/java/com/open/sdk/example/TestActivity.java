package com.open.sdk.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.open.sdk.config.DetailedList;
import com.open.sdk.view.UniqueEditText;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailedList.initData(getApplicationContext());
        setContentView(R.layout.activity_test);
        findViewById(R.id.login1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.listener.onSucceed("1", " ", "");
            }
        });
        findViewById(R.id.login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.listener.onSucceed("2", " ", "");
            }
        });
        findViewById(R.id.login3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.listener.onSucceed("3", " ", "");
            }
        });
        findViewById(R.id.login4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.listener.onSucceed("4", " ", "");
            }
        });
        UniqueEditText viewById = new UniqueEditText(this);
        viewById.setHintTxt("请输入帐号");
        viewById.show();
        setContentView(viewById);
    }
}
