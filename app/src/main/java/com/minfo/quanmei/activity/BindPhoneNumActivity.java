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
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class BindPhoneNumActivity extends BaseActivity implements View.OnClickListener{
    //top
    private TextView tvTitle;
    private ImageView ivLeft;

    private EditText etPhoneNum;
    private EditText etVerifyCode;
    private Button btnGetVerifyCode;

    private String phoneNum;
    private String verifyCode;

    private MyHandler handler = new MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_num);
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
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setText("预约详情");
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);

        etPhoneNum = (EditText) findViewById(R.id.et_phone_number);
        etVerifyCode = (EditText) findViewById(R.id.et_verification_code);

    }

    @Override
    protected void initViews() {

        btnGetVerifyCode = (Button) findViewById(R.id.btn_get_verification_code);
    }

    /**
     * 检查手机号
     * @return
     */
    private boolean checkPhoneNum(){
        phoneNum = etPhoneNum.getText().toString();
        verifyCode = etVerifyCode.getText().toString();
        if(TextUtils.isEmpty(phoneNum)){
            ToastUtils.show(this,"手机号不能为空");
            return false;
        }
        if(!MyCheck.isTel(phoneNum)){
            ToastUtils.show(this,"手机号格式不对");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_verification_code:
                if(checkPhoneNum()){
                    SMSSDK.getVerificationCode("86", phoneNum);
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
            case R.id.btn_complete_bind:
                verifyCode = etVerifyCode.getText().toString();
                if(verifyCode!=null&&!verifyCode.equals("")) {
                    SMSSDK.submitVerificationCode("86", phoneNum, verifyCode);
                }else{
                    ToastUtils.show(this,"验证码不能为空");
                }
                break;
        }
    }

    private  class MyHandler extends Handler {
        private final WeakReference<BindPhoneNumActivity> mActivity;
        public MyHandler(BindPhoneNumActivity activity){
            this.mActivity = new WeakReference<BindPhoneNumActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BindPhoneNumActivity activity = mActivity.get();
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    ToastUtils.show(BindPhoneNumActivity.this, "提交验证码成功");
                    reqBindPhoneNum();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    ToastUtils.show(BindPhoneNumActivity.this, "验证码已经发送");
                }
            } else {
                ToastUtils.show(activity, "验证失败");
            }

        }

    }

    /**
     * 请求绑定手机号接口
     */
    private void reqBindPhoneNum() {
        String url = getString(R.string.api_baseurl)+"user/BandTel.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.user.getUserid()+"*"+phoneNum);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                setResult(RESULT_OK);
                BindPhoneNumActivity.this.finish();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==10){
                    ToastUtils.show(BindPhoneNumActivity.this,"手机号格式不符合要求");
                }else if(errorcode==14){
                    ToastUtils.show(BindPhoneNumActivity.this,"手机号已被占用");
                }else {
                    ToastUtils.show(BindPhoneNumActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(BindPhoneNumActivity.this,msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
