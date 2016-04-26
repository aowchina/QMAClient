package com.minfo.quanmei.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.AppManager;
import com.minfo.quanmei.utils.ImgUtils;
import com.minfo.quanmei.utils.Utils;

/**
 * Created by liujing on 15/9/8.
 */
public abstract class BaseFragment extends Fragment{

    public Activity mActivity;
    public LayoutInflater inflater;
    public VolleyHttpClient httpClient;
    public Utils utils;
    public ImgUtils imgUtils;
    public String TAG;
    public AppManager appManager;

    /**
     * Fragment创建
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        inflater = LayoutInflater.from(mActivity);
        httpClient = new VolleyHttpClient(mActivity);
        TAG = getClass().getSimpleName();
        utils = new Utils(mActivity);
        imgUtils = new ImgUtils(mActivity);
        appManager = AppManager.getAppManager();
    }

    /**
     * 处理activity的布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initViews();
    }

    /**
     * 初始化布局
     * @return
     */
    protected abstract View initViews();

    /**
     * 依附的activity创建完成
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected  void initData(){

    }
}
