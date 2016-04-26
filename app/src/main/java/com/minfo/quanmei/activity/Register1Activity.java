package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.ChangeBirthDialog;

import java.io.UnsupportedEncodingException;

/**
 * 注册，填写个人信息，昵称，年龄
 */
public class Register1Activity extends BaseActivity implements View.OnClickListener {
    private EditText etNickName;
    private TextView etBirthDate;
    private ImageView ivLeft;
    private TextView tvRight;
    private TextView tvTitle;

    private String nickName;
    private String birthday;
    private ChangeBirthDialog birthDialog;
    private int age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

    }

    @Override
    protected void findViews() {
        //top
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivLeft.setVisibility(ImageView.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("下一步");
        tvTitle.setText("个人信息");
        tvRight.setOnClickListener(this);

        etNickName = (EditText) findViewById(R.id.et_user_name);
        etBirthDate = (TextView) findViewById(R.id.et_birth_date);
        etBirthDate.setOnClickListener(this);
//        tvBirthDate.setOnClickListener(this);


    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (check()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("birthday", birthday);
                    try {
                        String temp = "";
                        byte[] strTemp = nickName.getBytes("utf-8");
                        for (int i = 0; i < strTemp.length; i++) {
                            temp += strTemp[i] + "#";
                        }
                        bundle.putString("nickname", temp);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    utils.jumpAty(this, Register2Activity.class, bundle);
                }
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.et_birth_date:
                birthDialog = new ChangeBirthDialog(this);
                birthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {
                    @Override
                    public void onClick(String year, String month, String day) {
                        birthday = year+"-"+month+"-"+day;
                        etBirthDate.setText(birthday);
                    }
                });
                birthDialog.show();
                break;
        }
    }


    private boolean check() {
        nickName = etNickName.getText().toString();
        birthday = etBirthDate.getText().toString();
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.show(this, "昵称不能为空");
            return false;
        }
        if (!MyCheck.isNiCheng(nickName)) {
            ToastUtils.show(this, "昵称必须是2到10位的汉字或大小写字母");
            return false;
        }
        if (TextUtils.isEmpty(birthday)) {
            ToastUtils.show(this, "生日不能为空");
            return false;
        }
        if (!MyCheck.isDate(birthday)) {
            ToastUtils.show(this, "生日格式不对");
            return false;
        }

        return true;
    }
}
