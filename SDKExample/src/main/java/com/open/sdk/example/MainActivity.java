package com.open.sdk.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.open.sdk.config.DetailedList;
import com.open.sdk.ui.SdkActivity;
import com.open.sdk.view.FloatView;
import com.open.sdk.view.UniqueEditText;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DetailedList.initData(getApplicationContext());
        setContentView(R.layout.activity_main);
        Intent i = new Intent(MainActivity.this, SdkActivity.class);
//        startActivity(i);
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
        showView();
        UniqueEditText uniqueEditText = (UniqueEditText)  findViewById(R.id.uniqueEditText);
        uniqueEditText.setHintTxt("请输入帐号");
        uniqueEditText.show();

        UniqueEditText pwdEditText = (UniqueEditText)  findViewById(R.id.pwdEditText);
        pwdEditText.setHintTxt("请输入密码");
        pwdEditText.show();
    }


    private FloatView mLayout;

    private void showView() {
        mLayout = new FloatView(getApplicationContext());
        mLayout.init(R.mipmap.logo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在程序退出(Activity销毁）时销毁悬浮窗口
        mLayout.isShowView(false);
    }
}
