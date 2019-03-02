package com.peng.customwidget;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
