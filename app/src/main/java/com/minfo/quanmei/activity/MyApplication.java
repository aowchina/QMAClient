package com.minfo.quanmei.activity;

import android.app.Application;

/**
 * Created by liujing on 15/8/25.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    public String registrationId = "";
    public static long start;

    @Override
    public void onCreate() {
        start = System.currentTimeMillis();
        super.onCreate();
        mInstance = this;
//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush

    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }



}
