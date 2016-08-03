package com.minfo.quanmei.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by liujing on 15/8/25.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    public String registrationId = "";
    public static long start;
    public static int count;
    private NotificationManager manager;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate() {
        start = System.currentTimeMillis();
        super.onCreate();
        mInstance = this;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                manager.cancelAll();
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}
