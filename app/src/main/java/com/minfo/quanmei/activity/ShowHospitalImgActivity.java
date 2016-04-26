package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.ShowHospitalViewPagerAdapter;
import com.minfo.quanmei.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ShowHospitalImgActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ViewPager viewPager;
    private int flag;
    private String str;
    List<String> list=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hospital_img);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.show_hospital_back));
        viewPager = ((ViewPager) findViewById(R.id.vp_show_hos));
    }

    @Override
    protected void initViews() {
        back.setOnClickListener(this);
        flag=getIntent().getIntExtra("ID", 0);
        list=getIntent().getStringArrayListExtra("IMG");
        setImage();
    }
    public void setImage(){
        if (list!=null) {
            viewPager.setAdapter(new ShowHospitalViewPagerAdapter(ShowHospitalImgActivity.this, list));
        }else {
            ToastUtils.show(ShowHospitalImgActivity.this, "当前无数据");
        }
        viewPager.setCurrentItem(flag );
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_hospital_back:
                onBackPressed();
                break;
        }
    }
}
