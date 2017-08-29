package com.open.sdk.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
}
