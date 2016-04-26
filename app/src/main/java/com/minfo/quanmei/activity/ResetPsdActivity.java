package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Map;

public class ResetPsdActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivLeft;
    private TextView tvTitle;
    private Button sure;
    private EditText editPwd;
    private EditText editPwdConfirm;
    private String phoneNumber = "";
    private String pwdStr;
    private String pwdConfirmStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_psd);
    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        sure = ((Button) findViewById(R.id.btn_reset_done));
        editPwd = ((EditText) findViewById(R.id.et_reset_password));
        editPwdConfirm = ((EditText) findViewById(R.id.et_reset_password_confirm));
    }

    @Override
    protected void initViews() {
        ivLeft.setVisibility(ImageView.VISIBLE);
        tvTitle.setVisibility(ImageView.VISIBLE);
        tvTitle.setText("重置密码");
        sure.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        Bundle bundle = getIntent().getBundleExtra("info");
        if(bundle!=null){
            phoneNumber = bundle.getString("phoneNumber");
        }
    }

    private boolean checkPwd() {

        if(TextUtils.isEmpty(pwdStr)){
            ToastUtils.show(this,"密码不能为空");
        }
        if(!MyCheck.isPsw(pwdStr)){
            ToastUtils.show(this,"密码必须是6到15位之间的字母或数字");
            return false;
        }
        if(TextUtils.isEmpty(pwdConfirmStr)){
            ToastUtils.show(this,"确认密码不能为空");
            return false;
        }
        if(!pwdStr.equals(pwdConfirmStr)){
            ToastUtils.show(this,"两次密码输入不一致");
            return false;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.show(this,"手机号不能为空");
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_reset_done:
                pwdStr = editPwd.getText().toString();
                pwdConfirmStr = editPwdConfirm.getText().toString();
                if (checkPwd()){
                    reqResetPwd();
                }
                break;
        }
    }

    /**
     * 请求修改密码接口
     */
    private void reqResetPwd() {
        String url = getResources().getString(R.string.api_baseurl)+"user/EditPsw.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+phoneNumber+"*"+pwdStr+"*"+pwdConfirmStr);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                ToastUtils.show(ResetPsdActivity.this,"修改成功");
                finish();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==10){
                    ToastUtils.show(ResetPsdActivity.this,"手机号格式不符合要求");
                }else if(errorcode==11){
                    ToastUtils.show(ResetPsdActivity.this,"密码格式不符合要求");
                }else if(errorcode==12){
                    ToastUtils.show(ResetPsdActivity.this,"两次密码输入不一致");
                }else if(errorcode==13){
                    ToastUtils.show(ResetPsdActivity.this,"手机号记录不存在");
                }else{
                    ToastUtils.show(ResetPsdActivity.this,"服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(ResetPsdActivity.this,msg);
            }
        });
    }
}
