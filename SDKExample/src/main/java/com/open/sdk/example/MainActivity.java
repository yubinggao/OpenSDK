package com.open.sdk.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.open.sdk.ui.SdkActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(MainActivity.this, SdkActivity.class);
        startActivity(i);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Utils.login(MainActivity.this, new ResultListener() {
                    @Override
                    public void onSucceed(String hint, String resultCode, String result) {
                        System.out.println(" onSucceed " + hint + resultCode + result);
                    }

                    @Override
                    public void onOther(String hint, String resultCode, String errorMessage) {
                        System.out.println(" onOther " + hint + resultCode + errorMessage);
                    }

                    @Override
                    public void onFailure(String hint, String resultCode, String errorMessage) {
                        System.out.println(" onFailure " + hint + resultCode + errorMessage);
                    }
                });*/
                Intent i = new Intent(MainActivity.this, SdkActivity.class);
                startActivity(i);
            }
        });
    }
}
