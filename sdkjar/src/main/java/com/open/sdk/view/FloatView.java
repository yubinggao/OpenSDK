package com.open.sdk.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/8/31.
 */
public class FloatView extends View {
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    public FloatView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - 25;   //25是系统状态栏的高度
        Log.i("currP", "currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartY = event.getY();
                mTouchStartX = event.getX();

                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);

                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:
                endViewPosition();
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return true;
    }

    private void updateViewPosition() {
        //更新浮动窗口位置参数
        wmParams.x = (int) (x - mTouchStartX);
        wmParams.y = (int) (y - mTouchStartY);
        Log.i("update", "startX" + wmParams.x + " ==== startY" + wmParams.y);
        wm.updateViewLayout(this, wmParams);
    }

    private void endViewPosition() {
        //更新浮动窗口位置参数
        wmParams.y = (int) (y - mTouchStartY);
        wmParams.x = (int) (x - mTouchStartX);
        if (wmParams.y < 0) {
            wmParams.y = 0;
        } else if (wmParams.y > 1710) {
            wmParams.y = 1710;
        }
        if (wmParams.x < 470) {
            wmParams.x = 0;
        } else if (wmParams.x > 470) {
            wmParams.x = 940;
        }


        Log.i("end", "startX" + wmParams.x + " ==== startY" + wmParams.y);
        wm.updateViewLayout(this, wmParams);
    }

    public void init(int logo) {
        setBackgroundResource(logo);
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;     // 系统提示类型,重要
        wmParams.format = 1;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        wmParams.flags = wmParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParams.flags = wmParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
        wmParams.alpha = 1.0f;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角
        //以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;
        //设置悬浮窗口长宽数据
        wmParams.width = 140;
        wmParams.height = 140;
        //显示myFloatView图像
        wm.addView(this, wmParams);
    }

    public void isShowView(boolean isShow) {
        if (isShow) {
            wm.addView(this, wmParams);
        } else {
            wm.removeView(this);
        }
    }
}
