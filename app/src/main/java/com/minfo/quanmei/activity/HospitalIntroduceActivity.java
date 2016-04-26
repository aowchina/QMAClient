package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.HospitalIntroduce;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.PullScrollView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 医院介绍页
 * 2015年10月21日
 * zhang jiachang
 */
public class HospitalIntroduceActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private PullScrollView scroll;
    private View view;
    private ImageView iconHos;
    private TextView nameHos;
    private TextView introduceHos;
    private LinearLayout continer;
    private String hospitalId;
    private HospitalIntroduce hospitalIntroduce;
    private LinearLayout continerIcon;
    private boolean upLoad;
    List<View> hosViews = new ArrayList<View>();
    List<ImageView> hosImageViews = new ArrayList<ImageView>();
    List<View> caseViews = new ArrayList<View>();
    List<ImageView> caseImageViews = new ArrayList<ImageView>();

    //top
    private ImageView ivLeft;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_introduce);
    }

    @Override
    protected void findViews() {
        //top
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setOnClickListener(this);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("医院介绍");

        scroll = ((PullScrollView) findViewById(R.id.psl_hospital_introduce));
        view = LayoutInflater.from(this).inflate(R.layout.layout_hosintroduce_bodyview, null);
        iconHos = ((ImageView) view.findViewById(R.id.iv_icon_introduce));
        nameHos = ((TextView) view.findViewById(R.id.tv_name_introduce));
        continer = ((LinearLayout) view.findViewById(R.id.ll_continer_hosintroduce));
        continerIcon = ((LinearLayout) view.findViewById(R.id.ll_continericon_introduce));
        introduceHos = ((TextView) view.findViewById(R.id.tv_introduce_hos));
        scroll.addBodyView(view);
        scroll.setfooterViewGone();

    }

    @Override
    protected void initViews() {
        hospitalId = getIntent().getStringExtra("ID");
        if (hospitalId!=null&&!hospitalId.equals("")) {
            reqHospitalIntroduceData();
            scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
                @Override
                public void refresh() {
                    upLoad = true;
                    reqHospitalIntroduceData();
                }
            });
        }



    }

    /**
     * 请求医院详情数据
     */
    private void reqHospitalIntroduceData() {
        String url = getResources().getString(R.string.api_baseurl) + "hospital/Detail.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + hospitalId);


        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {


            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                hospitalIntroduce = response.getObj(HospitalIntroduce.class);
                if (hospitalIntroduce != null) {
                    setHospitalIntroduceData();

                }


            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==11){
                    ToastUtils.show(HospitalIntroduceActivity.this,"医院信息不存在或已被删除");
                }else{
                    ToastUtils.show(HospitalIntroduceActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(HospitalIntroduceActivity.this, msg);

            }
        });
    }

    public void setHospitalIntroduceData() {
        UniversalImageUtils.displayImageUseDefOptions(hospitalIntroduce.getLogo(), iconHos);
        nameHos.setText(hospitalIntroduce.getName());
        introduceHos.setText(hospitalIntroduce.getIntro());

        if (upLoad) {
            upLoad = false;
            for (int i = 0; i < hosImageViews.size(); i++) {
                if (hosImageViews.get(i) != null) {
                    hosImageViews.get(i).setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < hosViews.size(); i++) {
                if (hosViews.get(i) != null) {
                    hosViews.get(i).setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < caseImageViews.size(); i++) {
                if (caseImageViews.get(i) != null) {
                    caseImageViews.get(i).setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < caseViews.size(); i++) {
                if (caseViews.get(i) != null) {
                    caseViews.get(i).setVisibility(View.GONE);
                }
            }
            caseImageViews.clear();
            caseViews.clear();
            hosViews.clear();
            hosImageViews.clear();
            scroll.setheaderViewReset();

            ToastUtils.show(HospitalIntroduceActivity.this, "数据刷新完毕");
        }
        for (int i = 0; i < hospitalIntroduce.getXcimg().size(); i++) {
            View v1 = new View(HospitalIntroduceActivity.this);
            v1.setLayoutParams(new RelativeLayout.LayoutParams(utils.dip2px(10),
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            ImageView image = new ImageView(HospitalIntroduceActivity.this);
            image.setLayoutParams(new LinearLayout.LayoutParams(utils.dip2px(80),
                    utils.dip2px(80)));
            UniversalImageUtils.displayImageUseDefOptions(hospitalIntroduce.getXcimg().get(i), image);
            continer.addView(image);
            continer.addView(v1);
            hosViews.add(v1);
            hosImageViews.add(image);
        }
        for (int i = 0; i < hosImageViews.size(); i++) {
            final int j = i;
            hosImageViews.get(i).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HospitalIntroduceActivity.this, ShowHospitalImgActivity.class);
                    intent.putExtra("ID", j);
                    intent.putStringArrayListExtra("IMG", hospitalIntroduce.getXcimg());
                    startActivity(intent);
                }
            });
        }


        for (int i = 0; i < hospitalIntroduce.getAlimg().size(); i++) {
            View v2 = new View(HospitalIntroduceActivity.this);
            v2.setLayoutParams(new LinearLayout.LayoutParams(15,
                    utils.dip2px(10)));
            final ImageView image = new ImageView(HospitalIntroduceActivity.this);
            image.setLayoutParams(new LinearLayout.LayoutParams(utils.getScreenWidth() - utils.dip2px(10),
                    ViewGroup.LayoutParams.WRAP_CONTENT));

//            UniversalImageUtils.displayImageUseDefOptions(hospitalIntroduce.getAlimg().get(i), image);

            UniversalImageUtils.loadDefImage(hospitalIntroduce.getAlimg().get(i), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    image.setImageResource(R.mipmap.default_pic);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    image.setImageResource(R.mipmap.default_pic);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(utils.getScreenWidth() / (bitmap.getWidth() * 1.0f), utils.getScreenWidth() / (bitmap.getWidth() * 1.0f)); //长和宽放大缩小的比例
                    Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    image.setImageBitmap(resizeBmp);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    image.setImageResource(R.mipmap.default_pic);
                }
            });


            continerIcon.addView(image);
            continerIcon.addView(v2);
            caseViews.add(v2);
            caseImageViews.add(image);
        }
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
