package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.WholeDoctorPullAdapter;
import com.minfo.quanmei.entity.DoctorData;
import com.minfo.quanmei.entity.DoctorIntroduce;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全部医生
 * Created wei min-fo018 on 15/10/21.
 */
public class WholeDoctorActivity extends BaseActivity implements View.OnClickListener {

    //top
    private TextView tvTitle;
    private ImageView ivLeft;
    private List<DoctorData> doctorList = new ArrayList<>();
    private DoctorIntroduce doctorIntroduce;
    private RefreshListView mRefreshListView;
    private WholeDoctorPullAdapter mWholeDoctorPullAdapter;
    private String hospitalId;
    private boolean upLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholedoctor);

    }

    protected void findViews() {
        //设置标题栏
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("全部医生");
        ivLeft.setOnClickListener(this);
        mRefreshListView = (RefreshListView) findViewById(R.id.whole_doctor_RefreshListView);
    }

    @Override
    protected void initViews() {
        hospitalId = getIntent().getStringExtra("ID");
        if (hospitalId != null && !hospitalId.equals("")) {
            reqDoctorData();
        }
        mRefreshListView.setIsCanRefresh(true);
        mRefreshListView.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                upLoad = true;
                reqDoctorData();

            }
        });

    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
        }


    }

    /**
     * 请求医生详情数据
     */
    private void reqDoctorData() {
        String url = getResources().getString(R.string.api_baseurl) + "hospital/Doctor.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + hospitalId);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                doctorIntroduce = response.getObj(DoctorIntroduce.class);
                if (upLoad){
                    upLoad=false;
                    mRefreshListView.refreshComplete();
                    ToastUtils.show(WholeDoctorActivity.this, "刷新成功");
                }
                if (doctorIntroduce != null) {
                    doctorList = doctorIntroduce.getDoctor();
                    mWholeDoctorPullAdapter = new WholeDoctorPullAdapter(WholeDoctorActivity.this, doctorList);
                    mRefreshListView.setAdapter(mWholeDoctorPullAdapter);
                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==11){
                    ToastUtils.show(WholeDoctorActivity.this,"医院信息不存在或已被删除");
                }else{
                    ToastUtils.show(WholeDoctorActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(WholeDoctorActivity.this, msg);

            }
        });
    }


}
