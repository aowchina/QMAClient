package com.minfo.quanmei.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.MySwitchButton;
import com.minfo.quanmei.widget.OnSwitchListener;

public class MessageSettingActivity extends BaseActivity implements View.OnClickListener {

    //top
    private ImageView ivLeft;
    private TextView tvTitle;
    private MySwitchButton newMessage;
    private MySwitchButton hint;
    private MySwitchButton vibrate;
    private MySwitchButton distub;
    private SharedPreferences mSharedPreferences;
    private boolean  switchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
    }

    @Override
    protected void findViews() {


        //preferences.getString("name");

        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("消息设置");
        tvTitle.setVisibility(View.VISIBLE);
        newMessage = ((MySwitchButton) findViewById(R.id.mb_switch_newmessage));
        hint = ((MySwitchButton) findViewById(R.id.mb_switch_hint));
        vibrate = ((MySwitchButton) findViewById(R.id.mb_switch_vibrate));
        distub = ((MySwitchButton) findViewById(R.id.mb_switch_distub));
    }

    @Override
    protected void initViews() {
        mSharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=mSharedPreferences.edit();


        newMessage.setImageResource(R.drawable.switch_off, R.drawable.switch_on,
                R.drawable.switch_circle);

        newMessage.setOnSwitchStateListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                if (isSwitchOn) {
                    switchButton=true;
                    editor.putBoolean("info",switchButton );
                    ToastUtils.show(MessageSettingActivity.this, "开启新消息提醒");
                } else {
                    switchButton=false;
                    editor.putBoolean("info",switchButton );
                    ToastUtils.show(MessageSettingActivity.this, "关闭新消息提醒");
                }
                editor.commit();

            }
        });
        hint.setImageResource(R.drawable.switch_off, R.drawable.switch_on,
                R.drawable.switch_circle);

        hint.setOnSwitchStateListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                if (isSwitchOn) {
                    switchButton=true;
                    editor.putBoolean("volume",switchButton);
                    ToastUtils.show(MessageSettingActivity.this, "开启提示");
                } else {
                    ToastUtils.show(MessageSettingActivity.this, "关闭提示");
                    switchButton=false;
                    editor.putBoolean("info", switchButton);
                }

            }
        });
        vibrate.setImageResource(R.drawable.switch_off, R.drawable.switch_on,
                R.drawable.switch_circle);

        vibrate.setOnSwitchStateListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                if (isSwitchOn) {
                    switchButton=true;
                    editor.putBoolean("vibrate",switchButton);
                    ToastUtils.show(MessageSettingActivity.this, "开启振动");
                } else {
                    ToastUtils.show(MessageSettingActivity.this, "关闭振动");
                    switchButton=true;
                    editor.putBoolean("vibrate", switchButton);
                }

            }
        });
        distub.setImageResource(R.drawable.switch_off, R.drawable.switch_on,
                R.drawable.switch_circle);

        distub.setOnSwitchStateListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                if (isSwitchOn) {
                    switchButton=true;
                    editor.putBoolean("disturb",switchButton);
                    ToastUtils.show(MessageSettingActivity.this, "开启免打扰模式");
                } else {
                    switchButton=true;
                    editor.putBoolean("disturb",switchButton);
                    ToastUtils.show(MessageSettingActivity.this, "关闭免打扰模式");
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }
}
