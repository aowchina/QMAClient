package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.ToastUtils;

import java.util.Map;

/**
 * 注册 填写登录信息 手机号和登录密码
 */
public class Register2Activity extends BaseActivity implements View.OnClickListener {
    private EditText etPhoneNumber;
    private EditText etPwd;

    //top
    private ImageView ivLeft;
    private TextView tvTitle;
    private TextView tvRight;


    private String nickname;
    private String birthday;
    private String phoneNumber;
    private String password;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Bundle bundle = new Bundle();
                bundle.putString("phoneNumber",phoneNumber);
                bundle.putString("password",password);
                bundle.putString("nickname",nickname);
                bundle.putString("birthday",birthday);
                utils.jumpAty(Register2Activity.this,Register3Activity.class,bundle);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
    }

    @Override
    protected void findViews() {
        //top
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText("登录信息");
        tvRight.setText("下一步");
        tvRight.setOnClickListener(this);
        ivLeft.setOnClickListener(this);

        //手机号,密码
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etPwd = (EditText) findViewById(R.id.et_password);
    }

    @Override
    protected void initViews() {
        Bundle bundle = getIntent().getBundleExtra("info");
        nickname = bundle.getString("nickname");
        birthday = bundle.getString("birthday");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right:
                check();
                if(check()) {
                    checkTel(phoneNumber);
                }
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    private boolean check() {
        phoneNumber = etPhoneNumber.getText().toString();
        password = etPwd.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.show(this, "手机号不能为空");
            return false;
        }
        if(!MyCheck.isTel(phoneNumber)){
            ToastUtils.show(this,"手机号不合法");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.show(this,"密码不能为空");
        }
        if(!MyCheck.isPsw(password)){
            ToastUtils.show(this,"密码必须是6到15位之间的字母或数字");
            return false;
        }
        return true;
    }

    /**
     * 手机号验重
     * @param phoneNumber
     */
    public void checkTel(String phoneNumber){
        String url = getResources().getString(R.string.api_baseurl)+"public/CheckTel.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+phoneNumber);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                if (response.getErrorcode() == 0) {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 12) {
                    ToastUtils.show(Register2Activity.this, "手机号已存在");
                }else{
                    ToastUtils.show(Register2Activity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(Register2Activity.this,msg);
            }

        });
    }
}
