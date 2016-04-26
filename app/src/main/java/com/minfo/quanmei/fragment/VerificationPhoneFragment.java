package com.minfo.quanmei.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.ResetPwdActivity;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.ToastUtils;

import java.lang.ref.WeakReference;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class VerificationPhoneFragment extends BaseFragment implements View.OnClickListener{
    private EditText etPhoneNumber;
    private EditText etVerifyCode;
    private Button btnGetVerifyCode;
    private Button btnNextStep;

    private String appKey = "9cd73017821c";
    private String appSecret = "c778e08238429825f16d651e366a92b8";
    private MyHandler myHandler;

    private String phoneNumber;
    private String verifyCode;

    private ResetPwdNextStepListener resetPwdNextStepListener;

    public static VerificationPhoneFragment newInstance() {
        VerificationPhoneFragment fragment = new VerificationPhoneFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.resetPwdNextStepListener = (ResetPwdNextStepListener) activity;
    }

    public VerificationPhoneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(mActivity, appKey, appSecret);
        EventHandler eh=new EventHandler(){

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
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_verification_phone,null);
        etPhoneNumber = (EditText) view.findViewById(R.id.et_phone_number);
        etVerifyCode = (EditText) view.findViewById(R.id.et_verification_code);
        btnGetVerifyCode = (Button) view.findViewById(R.id.btn_get_code);
        btnNextStep = (Button) view.findViewById(R.id.btn_bind_mobile);
        btnNextStep.setOnClickListener(this);
        btnGetVerifyCode.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bind_mobile:
                verifyCode = etVerifyCode.getText().toString();
                if(!TextUtils.isEmpty(verifyCode)){
                    SMSSDK.submitVerificationCode("86", phoneNumber, verifyCode);
                }else{
                    ToastUtils.show(mActivity,"验证码不能为空");
                }
                break;
            case R.id.iv_left:

                break;
            case R.id.btn_get_code:
                //TODO:发短信
                if(checkPhone()){
                    SMSSDK.getVerificationCode("86", phoneNumber);
                    new CountDownTimer(60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            btnGetVerifyCode.setText(millisUntilFinished / 1000 + "s后重新发送");
                            btnGetVerifyCode.setEnabled(false);
                        }
                        public void onFinish() {
                            btnGetVerifyCode.setText("获取验证码");
                            btnGetVerifyCode.setEnabled(true);
                        }
                    }.start();
                }

                break;
        }
    }

    /**
     * 验证手机号
     * @return
     */
    private boolean checkPhone() {
        phoneNumber = etPhoneNumber.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.show(mActivity, "手机号不能为空");
            return false;
        }
        if(!MyCheck.isTel(phoneNumber)){
            ToastUtils.show(mActivity,"手机号不合法");
            return false;
        }
        return true;
    }

    public interface ResetPwdNextStepListener{
        void nextStep();
    }

    private static class MyHandler extends Handler {
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
                    ToastUtils.show(activity, "提交验证码成功", Toast.LENGTH_SHORT);
                    //activity.nextStep();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    ToastUtils.show(activity, "验证码已经发送", Toast.LENGTH_SHORT);
                }
            } else {
//                ((Throwable) data).printStackTrace();
//                int resId = getStringRes(Register4Activity.this, "smssdk_network_error");
//                Toast.makeText(Register4Activity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//                if (resId > 0) {
//                    Toast.makeText(Register4Activity.this, resId, Toast.LENGTH_SHORT).show();
//                }
                ToastUtils.show(activity, "");
            }

        }

    }



}
