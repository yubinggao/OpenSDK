package com.open.sdk.example;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Administrator on 2017/8/26.
 */
public class Utils {
    public static ResultListener listener;

    public static void login(Activity activity, ResultListener listene) {
        listener = listene;
        Intent i = new Intent(activity, TestActivity.class);
        activity.startActivity(i);
    }
}

