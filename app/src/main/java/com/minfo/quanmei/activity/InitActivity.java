package com.minfo.quanmei.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.Utils;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class InitActivity extends AppCompatActivity {

    private Handler handler;
    private ProgressBar progressBar;
    private int userid = 0;
    private long start;
    private VolleyHttpClient httpClient;
    private Utils utils;

    private NotificationManager manager;

    static {
        System.loadLibrary("QUANMEI");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        httpClient = new VolleyHttpClient(this);
        utils = new Utils(this);

        JPushInterface.init(this);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(MyReceiver.notifactionId);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        userid = utils.getUserid();
                        if (userid == 0) {
                            Intent intent = new Intent(InitActivity.this, MainActivity.class);
                            startActivity(intent);
                            InitActivity.this.finish();
                        } else {
                            reqServer();
                        }
                        break;
                }
            }
        };


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {

            handler.sendEmptyMessageDelayed(1, 1000);
        }
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
     * 请求api
     */
    private void reqServer() {
        String url = getResources().getString(R.string.api_baseurl) + "public/Init.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + userid);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                User user = response.getObj(User.class);
                Constant.user = user;
                utils.jumpAty(InitActivity.this, MainActivity.class, null);
                InitActivity.this.finish();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 12) {//userid不合法,视为未登录

                    utils.setUserid(0);

                    LoginActivity.isJumpLogin = false;
                    utils.jumpAty(InitActivity.this, MainActivity.class, null);
                    InitActivity.this.finish();
                } else {//其他
                    ToastUtils.show(InitActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(InitActivity.this, msg);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeMessages(1);
    }
}
