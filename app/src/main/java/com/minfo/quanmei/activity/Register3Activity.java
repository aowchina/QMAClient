package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Register3Activity extends BaseActivity implements View.OnClickListener{
    //top
    private ImageView ivLeft;
    private TextView tvTitle;

    private Button btnGetVerifyCode;
    private Button btnRegister;
    private EditText etVerify;


    private String phoneNumber;
    private String password;
    private String nickname;
    private String birthday;
    private String verifyCode;

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        SMSSDK.initSDK(this, getString(R.string.sms_appkey), getString(R.string.sms_secret));
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);

    }

    @Override
    protected void findViews() {
        //top
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("最后一步");
        ivLeft.setOnClickListener(this);

        btnGetVerifyCode = (Button) findViewById(R.id.btn_get_verification_code);
        btnRegister = (Button) findViewById(R.id.btn_register);
        etVerify = (EditText) findViewById(R.id.et_verification_code);

        btnGetVerifyCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    protected void initViews() {

        Bundle bundle = getIntent().getBundleExtra("info");
        phoneNumber = bundle.getString("phoneNumber");
        password = bundle.getString("password");
        nickname = bundle.getString("nickname");
        birthday = bundle.getString("birthday");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_verification_code:
                //TODO:发短信
                if(phoneNumber!=null) {
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
                break;
            case R.id.btn_register:
                verifyCode = etVerify.getText().toString();
                if(verifyCode!=null&&!verifyCode.equals("")) {
                    SMSSDK.submitVerificationCode("86", phoneNumber, verifyCode);
                }else{
                    ToastUtils.show(this,"验证码不能为空");
                }
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    private  class MyHandler extends Handler {
        private final WeakReference<Register3Activity> mActivity;
        public MyHandler(Register3Activity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Register3Activity activity = mActivity.get();
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    ToastUtils.show(Register3Activity.this, "提交验证码成功");
                    reqServer();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    ToastUtils.show(Register3Activity.this, "验证码已经发送");
                }
            } else {
                ToastUtils.show(Register3Activity.this, "验证失败");
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    private void reqServer(){
        String url = getResources().getString(R.string.api_baseurl)+"public/Register.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr() + "*" + nickname + "*" + birthday + "*" + phoneNumber + "*" + password);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                User user = response.getObj(User.class);
                Constant.user = user;
                utils.setUserid(user.getUserid());
                utils.jumpAty(Register3Activity.this, MainActivity.class, null);
                Register3Activity.this.finish();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==10){
                    ToastUtils.show(Register3Activity.this,"昵称不符合要求");
                }else if(errorcode==11){
                    ToastUtils.show(Register3Activity.this,"生日不符合要求");
                }else if(errorcode==12){
                    ToastUtils.show(Register3Activity.this,"手机号不符合要求");
                }else if(errorcode==13){
                    ToastUtils.show(Register3Activity.this,"密码不符合要求");
                }else if(errorcode==15){
                    ToastUtils.show(Register3Activity.this,"手机号已被占用");
                }else if(errorcode==16){
                    User user = response.getObj(User.class);
                    Constant.user = user;
                    utils.setUserid(user.getUserid());
                    utils.jumpAty(Register3Activity.this, MainActivity.class, null);
                    Register3Activity.this.finish();
                }else{
                    ToastUtils.show(Register3Activity.this,"服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(Register3Activity.this,msg);
            }
        });
    }
}
