package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.AppManager;
import com.minfo.quanmei.utils.ImgUtils;
import com.minfo.quanmei.utils.Utils;

import cn.jpush.android.api.JPushInterface;

/**
 * liujing
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected VolleyHttpClient httpClient;
    protected Utils utils;
    protected String TAG = "";
    protected AppManager appManager;
    protected ImgUtils imgUtils;
    static{
        System.loadLibrary("QUANMEI");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        httpClient = new VolleyHttpClient(this);
        TAG = getClass().getSimpleName();
        utils = new Utils(this);
        appManager = AppManager.getAppManager();
        appManager.addActivity(this);
        this.imgUtils = new ImgUtils(this);



    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        findViews();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.resumePush(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.resumePush(this);
    }

    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 获取布局控件
     */
     protected abstract void findViews();

    /**
     * 初始化view的一些数据
     */
     protected abstract void initViews();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appManager.finishActivity();
    }
}
