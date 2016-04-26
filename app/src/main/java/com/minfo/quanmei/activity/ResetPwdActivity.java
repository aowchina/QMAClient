package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener {

    //top
    private ImageView ivLeft;
    private TextView tvTitle;

    private Button btnNext;


    private EditText etPhoneNumber;
    private EditText etVerifyCode;
    private Button btnGetVerifyCode;
    private Button btnNextStep;
    private MyHandler myHandler= new MyHandler(this);

    private String phoneNumber;
    private String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        SMSSDK.initSDK(this,getString(R.string.sms_appkey), getString(R.string.sms_secret));
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                myHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);

    }

    @Override
    protected void findViews() {
        //top
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etVerifyCode = (EditText) findViewById(R.id.et_verification_code);
        btnGetVerifyCode = (Button) findViewById(R.id.btn_get_code);
        btnNextStep = (Button) findViewById(R.id.btn_bind_mobile);
        ivLeft.setVisibility(ImageView.VISIBLE);
        tvTitle.setVisibility(ImageView.VISIBLE);
        tvTitle.setText("重置密码");

    }

    @Override
    protected void initViews() {

        btnNextStep.setOnClickListener(this);
        btnGetVerifyCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_mobile:
                verifyCode = etVerifyCode.getText().toString();
                if (!TextUtils.isEmpty(verifyCode)) {
                    SMSSDK.submitVerificationCode("86", phoneNumber, verifyCode);
                } else {
                    ToastUtils.show(ResetPwdActivity.this, "验证码不能为空");
                }
                break;
            case R.id.iv_left:
                onBackPressed();

                break;
            case R.id.btn_get_code:
                if (checkPhone()) {
                    reqCheckPhone();
                }
                break;
        }
    }

    /**
     * 验证手机号
     *
     * @return
     */
    private boolean checkPhone() {
        phoneNumber = etPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.show(ResetPwdActivity.this, "手机号不能为空");
            return false;
        }
        if (!MyCheck.isTel(phoneNumber)) {
            ToastUtils.show(ResetPwdActivity.this, "手机号不合法");
            return false;
        }
        return true;
    }

    private  class MyHandler extends Handler {
        private final WeakReference<ResetPwdActivity> mActivity;
        public MyHandler(ResetPwdActivity activity){
            this.mActivity = new WeakReference<ResetPwdActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ResetPwdActivity activity = mActivity.get();
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    ToastUtils.show(ResetPwdActivity.this, "提交验证码成功");
                    Bundle bundle = new Bundle();
                    bundle.putString("phoneNumber", phoneNumber);
                    utils.jumpAty(ResetPwdActivity.this,ResetPsdActivity.class,bundle);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    ToastUtils.show(ResetPwdActivity.this, "验证码已经发送");
                }
            } else {

                ToastUtils.show(ResetPwdActivity.this, "验证失败");
            }

        }

    }

    /**
     * 检查手机号是否已注册
     */
    private void reqCheckPhone(){
        String url = getResources().getString(R.string.api_baseurl)+"public/CheckTel.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+phoneNumber);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                ToastUtils.show(ResetPwdActivity.this,"手机号未注册");
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==12) {//表示为已注册的手机号，
                    sendVerifyCode();
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(ResetPwdActivity.this,msg);
            }
        });
    }

    private void sendVerifyCode(){
        SMSSDK.getVerificationCode("86", phoneNumber);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnGetVerifyCode.setText("("+millisUntilFinished / 1000 + "秒)后重新获取");
                btnGetVerifyCode.setEnabled(false);
            }

            public void onFinish() {
                btnGetVerifyCode.setText("获取验证码");
                btnGetVerifyCode.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
