package com.minfo.quanmei.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.ChildCategory;
import com.minfo.quanmei.fragment.ProjectDiaryFragment;
import com.minfo.quanmei.fragment.ProjectInfoFragment;

/**
 * 项目详情页
 * liujing 2015-08-26
 */
public class ProjectItemInfoActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvProInfo;
    private TextView tvDiary;
    private ChildCategory childCategory;

    private ImageView ivLeft;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
    }

    @Override
    protected void findViews() {

        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("项目详情");
        tvTitle.setVisibility(View.VISIBLE);

        tvProInfo = (TextView) findViewById(R.id.tv_pro_info);
        tvDiary = (TextView) findViewById(R.id.tv_diary);
        tvProInfo.setOnClickListener(this);
        tvDiary.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        childCategory = (ChildCategory) getIntent().getSerializableExtra("childCategory");
        switchFragment(1);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_pro_info:
                tvProInfo.setTextColor(Color.BLACK);
                tvProInfo.setBackgroundColor(Color.WHITE);
                tvDiary.setTextColor(Color.WHITE);
                tvDiary.setBackgroundColor(getResources().getColor(R.color.tab_top_bg));
                switchFragment(1);
                break;
            case R.id.tv_diary:
                tvProInfo.setTextColor(Color.WHITE);
                tvProInfo.setBackgroundColor(getResources().getColor(R.color.tab_top_bg));
                tvDiary.setTextColor(Color.BLACK);
                tvDiary.setBackgroundColor(Color.WHITE);
                switchFragment(2);
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    /**
     * 切换fragment
     * @param flag 1代表项目简介 2代表相关日记
     */
    private void switchFragment(int flag){
        if(flag==1){
            ProjectInfoFragment projectInfoFragment = new ProjectInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("childCategory",childCategory);
            projectInfoFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, projectInfoFragment).commit();
        }else{

            ProjectDiaryFragment projectDiaryFragment = new ProjectDiaryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("childCategory",childCategory);
            projectDiaryFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, projectDiaryFragment).commit();

        }
    }
}
