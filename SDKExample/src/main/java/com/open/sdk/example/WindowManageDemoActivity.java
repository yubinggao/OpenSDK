package com.open.sdk.example;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/8/31.
 */
public class WindowManageDemoActivity extends Activity {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams param;
    private FloatView mLayout;

    private void showView(){
        mLayout=new FloatView(getApplicationContext());
        //获取WindowManager
        mWindowManager=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置LayoutParams(全局变量）相关参数
        param = ((MyApplication)getApplication()).getMywmParams();

        param.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;     // 系统提示类型,重要
        param.format=1;
        param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制

        param.alpha = 1.0f;

        param.gravity= Gravity.LEFT|Gravity.TOP;   //调整悬浮窗口至左上角
        //以屏幕左上角为原点，设置x、y初始值
        param.x=0;
        param.y=0;

        //设置悬浮窗口长宽数据
        param.width=140;
        param.height=140;

        //显示myFloatView图像
        mWindowManager.addView(mLayout, param);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //在程序退出(Activity销毁）时销毁悬浮窗口
        mWindowManager.removeView(mLayout);
    }
}
