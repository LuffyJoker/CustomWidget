package com.peng.customwidget.custom_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.blankj.utilcode.util.LogUtils;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 */
public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    boolean isIntercept;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //必须调用super.dispatchTouchEvent(ev)，否则如果直接返回true 或 false，
        // 则事件传递到这里时便终止了，既不会继续分发也不会回传给父元素
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ev.getXPrecision();
        ev.getYPrecision();
        float y = ev.getY();
        LogUtils.d("onTouchEvent  y:" + y);

        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        LogUtils.logd("onScrollChanged l=" + l + "  t=" + t + " oldl " + oldl + " oldt=" + oldt);

    }

    @Override
    public void scrollTo(int x, int y) {

        if (!isIntercept) {
            super.scrollTo(x, y);
            LogUtils.d("不拦截。。。");
        } else {
            LogUtils.d("拦截。。。");
        }

    }
}

