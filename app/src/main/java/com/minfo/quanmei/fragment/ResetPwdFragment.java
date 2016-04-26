package com.minfo.quanmei.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.ResetPwdActivity;
import com.minfo.quanmei.utils.ToastUtils;

import java.lang.ref.WeakReference;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ResetPwdFragment extends BaseFragment implements View.OnClickListener{



    private String phoneNum;
    private String verifyCode;



    public static ResetPwdFragment newInstance() {
        ResetPwdFragment fragment = new ResetPwdFragment();
        return fragment;
    }

    public ResetPwdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_reset_pwd,null);
        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


}
