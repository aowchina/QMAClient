package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LoadingDialog;

import java.util.Map;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    //top
    private ImageView ivLeft;
    private TextView tvTitle;

    private LoadingDialog mLoadingDialog;
    private PopupWindow mPopupWindow;
    private DisplayMetrics mDisplayMetrics;
    private TextView upDate;
    private TextView cancelUpDate;
    private TextView upDateContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("设置");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message_setting:
                utils.jumpAty(this, MessageSettingActivity.class, null);
                break;
            case R.id.rl_drawback:
                utils.jumpAty(this, FeedBackActivity.class, null);
                break;
            case R.id.rl_contact_us:
                utils.jumpAty(this, ContactUsActivity.class, null);
                break;
            case R.id.rl_check_update:
                // checkUpdate();

//                getPopupWindow(v);
                break;
            case R.id.rl_introduce_friend:
                break;
            case R.id.rl_protocol:
                utils.jumpAty(this, UserProtocolActivity.class, null);
                break;
            case R.id.btn_exit_login:
                reqExitLogin();
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }
    //退出登录接口
    private void reqExitLogin() {
            String url = getResources().getString(R.string.api_baseurl) + "public/GetOut.php";
            Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid());
            httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
                @Override
                public void onPreRequest() {

                }

                @Override
                public void onRequestSuccess(BaseResponse response) {
                    Constant.user = null;
                    utils.setCUserid("");
                    utils.setUserid(0);
                    utils.jumpAty(SettingActivity.this, InitActivity.class, null);
                    appManager.finishAllActivity();
                }

                @Override
                public void onRequestNoData(BaseResponse response) {
                    int errorcode = response.getErrorcode();
                    if(errorcode==13){
                        ToastUtils.show(SettingActivity.this,"用户处于未登录状态");
                    }else{
                        ToastUtils.show(SettingActivity.this,"服务器繁忙");
                    }

                }

                @Override
                public void onRequestError(int code, String msg) {
                    ToastUtils.show(SettingActivity.this, msg);
                }
            });
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();


    }

    /**
     * 加载PopupWindow布局
     */
    public void createPopupWindow(View v) {


        View layouty = LayoutInflater.from(this).inflate(R.layout.version_update, null);

        upDate = (TextView) layouty.findViewById(R.id.version_update);
        cancelUpDate = (TextView) layouty.findViewById(R.id.version_cancel_update);
        upDateContent = (TextView) layouty.findViewById(R.id.version_update_contnet);

        //更新的监听
        cancelUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();

            }
        });
        upDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateContent.setText("最新更新NB功能,请及时更新!");
            }

        });
        //调用父类构造 传入 ：布局 ，高度， 宽度 ，是否有焦点
        mPopupWindow = new PopupWindow(layouty, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setTouchable(true);
        //通过获取屏幕管理者来设置poppupwindow之外的屏幕背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        //设置mPopupWindow拦截事件
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //设置mPopupWindow显示的宽高
        mDisplayMetrics = new DisplayMetrics();
        //mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha));

        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mPopupWindow.setWidth(mDisplayMetrics.widthPixels * 3 / 4);
        mPopupWindow.setHeight(mDisplayMetrics.heightPixels * 1 / 2);
        mPopupWindow.showAtLocation(ivLeft, Gravity.LEFT, mDisplayMetrics.widthPixels * 1 / 8, mDisplayMetrics.heightPixels * 1 /16);

    }

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindow(View v) {

        if (null != mPopupWindow) {
            closePopupWindow();
            return;
        } else {
            createPopupWindow(v);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.groupDetail = null;
    }

    /**
     * 关闭窗口
     */
    private void closePopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }

}
