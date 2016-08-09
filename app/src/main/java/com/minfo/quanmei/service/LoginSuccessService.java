package com.minfo.quanmei.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.PocketActivity;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Utils;
import com.minfo.quanmei.widget.SignDialog;

import java.util.Map;

/**
 * Created by liujing on 8/5/16.
 */
public class LoginSuccessService extends IntentService implements SignDialog.SignSuccessListener{

    private VolleyHttpClient httpClient;
    private Utils utils;
    private int userid;
    private String signStateUrl;
    private String signUrl;

    private SignDialog signDialog;

    public LoginSuccessService(String name) {
        super(name);
    }


    @Override
    public void onCreate() {
        httpClient = new VolleyHttpClient(this);
        utils = new Utils(this);
        signDialog = new SignDialog(this,this);


        signStateUrl = getString(R.string.api_baseurl)+"user/QdStatus.php";
        signUrl = getString(R.string.api_baseurl)+"user/Qd.php";

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    private void reqSignState() {
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+utils.getUserid());
        httpClient.post(signStateUrl,params,R.string.loading_msg,new RequestListener(){
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                reqSign();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {

            }

            @Override
            public void onRequestError(int code, String msg) {

            }
        });
    }

    private void reqSign() {
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+utils.getUserid());
        httpClient.post(signUrl,params,R.string.loading_msg,new RequestListener(){
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                signDialog.show();
                signDialog.setPoint(response.toString());

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                signDialog.dismiss();


            }

            @Override
            public void onRequestNoData(BaseResponse response) {

            }

            @Override
            public void onRequestError(int code, String msg) {

            }
        });
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        reqSignState();
    }

    @Override
    public void onSignSuccess() {
        Intent intent = new Intent(this, PocketActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }
}
