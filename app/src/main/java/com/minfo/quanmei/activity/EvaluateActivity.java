package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;

import java.util.Map;

public class EvaluateActivity extends BaseActivity implements View.OnClickListener {

    private EditText evaluateEt;
    private ImageView goodIv;
    private ImageView middleIv;
    private ImageView badIv;
    private ImageView back;
    private LinearLayout goodLl;
    private LinearLayout middleLl;
    private LinearLayout badLl;
    private TextView evaluateTv;
    private RatingBar asethetic;
    private RatingBar environment;
    private RatingBar service;

    private String ordeId;
    private int hID;
    private int flowerLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
    }

    @Override
    protected void findViews() {
        evaluateEt = ((EditText) findViewById(R.id.et_content_evaluate));
        back = ((ImageView) findViewById(R.id.iv_back_evaluate));
        goodIv = ((ImageView) findViewById(R.id.iv_good_evaluate));
        middleIv = ((ImageView) findViewById(R.id.iv_middle_evaluate));
        badIv = ((ImageView) findViewById(R.id.iv_bad_evaluate));
        goodLl = ((LinearLayout) findViewById(R.id.ll_good_evaluate));
        middleLl = ((LinearLayout) findViewById(R.id.ll_middle_evaluate));
        badLl = ((LinearLayout) findViewById(R.id.ll_bad_evaluate));
        evaluateTv = ((TextView) findViewById(R.id.tv_evaluate));
        asethetic = ((RatingBar) findViewById(R.id.rb_aesthetic_evaluate));
        environment = ((RatingBar) findViewById(R.id.rb_environment_evaluate));
        service = ((RatingBar) findViewById(R.id.rb_service_evaluate));


    }

    @Override
    protected void initViews() {
        Bundle bundle = getIntent().getBundleExtra("info");
        ordeId = bundle.getString("orderid");
        hID = bundle.getInt("hid");

        back.setOnClickListener(this);
        goodLl.setOnClickListener(this);
        middleLl.setOnClickListener(this);
        badLl.setOnClickListener(this);
        evaluateTv.setOnClickListener(this);

        asethetic.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        environment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        service.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_evaluate:
                onBackPressed();
                break;
            case R.id.ll_good_evaluate:
                flowerLevel = 3;
                setEvaluateImg();
                goodIv.setImageResource(R.mipmap.evaluate_good);
                break;
            case R.id.ll_middle_evaluate:
                flowerLevel = 2;
                setEvaluateImg();
                middleIv.setImageResource(R.mipmap.evaluate_middle);
                break;
            case R.id.ll_bad_evaluate:
                flowerLevel = 1;
                setEvaluateImg();
                badIv.setImageResource(R.mipmap.evaluate_bad);
                break;
            case R.id.tv_evaluate:
                reqEvaluate();
                break;
        }

    }

    private void setEvaluateImg() {
        goodIv.setImageResource(R.mipmap.unevaluate_good);
        middleIv.setImageResource(R.mipmap.unevaluate_middle);
        badIv.setImageResource(R.mipmap.unevaluate_bad);
    }

    /**
     * 调用评论接口
     */
    private void reqEvaluate() {
        int asetheticNum = asethetic.getNumStars();//审美
        int environmentNum = environment.getNumStars();//环境
        int serviceNum = service.getNumStars();//服务
        String context = evaluateEt.getText().toString().trim();

        String url = getResources().getString(R.string.api_baseurl) + "order/AddPj.php";
        Log.e(TAG,Constant.user.getUserid() + "*" + ordeId + "*" + hID + "*" + flowerLevel + "*" + asetheticNum + "*" + environmentNum + "*" + serviceNum);
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + ordeId + "*" + hID + "*" + flowerLevel + "*" + asetheticNum + "*" + environmentNum + "*" + serviceNum + "*" +utils.convertChinese(context));
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }
            @Override
            public void onRequestSuccess(BaseResponse response) {
                EvaluateActivity.this.finish();
            }
            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==16){
                    ToastUtils.show(EvaluateActivity.this,"文字内容不能为空");
                }else if(errorcode==19){
                    ToastUtils.show(EvaluateActivity.this,"用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(EvaluateActivity.this, LoginActivity.class, null);
                }else{
                    ToastUtils.show(EvaluateActivity.this,"服务器繁忙"+errorcode);
                }
            }
            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(EvaluateActivity.this,msg);
            }
        });
    }
}
