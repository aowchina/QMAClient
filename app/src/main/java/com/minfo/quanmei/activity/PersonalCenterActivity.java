package com.minfo.quanmei.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.Map;

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener{
    //top
    private ImageView ivLeft;
    private TextView tvTitle;
    private TextView tvRight;

    private Button btnExit;

    private ImageView civHeadImage;

    private Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        btnExit = (Button) findViewById(R.id.btn_exit_login);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText("个人中心");
        tvRight.setText("个人资料");
        tvRight.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        civHeadImage = (ImageView) findViewById(R.id.civ_head_image);


    }

    @Override
    protected void initViews() {

        if(Constant.user!=null){
            UniversalImageUtils.disCircleImage(Constant.user.getUserimg(),civHeadImage);
        }
        civHeadImage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.civ_head_image:
                Intent intent = new Intent(this,PersonalHomePageActivity.class);
                intent.putExtra("userid",Constant.user.getUserid());
                startActivity(intent);
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_right:
                utils.jumpAty(this,PersonInfoActivity.class,null);
                break;
            case R.id.rl_diary:
                startActivity(new Intent(PersonalCenterActivity.this,MyDiaryActivity.class));
                break;
            case R.id.rl_note:
                startActivity(new Intent(PersonalCenterActivity.this, MyNoteActivity.class));
                break;
            case R.id.rl_collect:
                startActivity(new Intent(PersonalCenterActivity.this,MyReceiveActivity.class));
                break;

            case R.id.rl_order:
                utils.jumpAty(this, OrderActivity.class, null);
                break;
            case R.id.btn_exit_login:
                reqExitLogin();
                break;
            case R.id.rl_update:
                reqUpdate();
                break;
        }
    }

    /**
     * 请求检查更新接口
     */
    private void reqUpdate() {
        alertDialog =  new  AlertDialog.Builder(this).create();
        alertDialog.setTitle("全美客户端版本更新");
        String url = getResources().getString(R.string.api_baseurl) + "public/Update.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e(TAG,response.getData().toString());

            }

            @Override
            public void onRequestNoData(BaseResponse response) {

            }

            @Override
            public void onRequestError(int code, String msg) {

            }
        });

    }

    /**
     * 退出登录
     */
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
                utils.jumpAty(PersonalCenterActivity.this, InitActivity.class, null);
                appManager.finishAllActivity();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==13){
                    ToastUtils.show(PersonalCenterActivity.this, "用户处于未登录状态");
                }else{
                    ToastUtils.show(PersonalCenterActivity.this,"服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(PersonalCenterActivity.this, msg);
            }
        });
    }
}
