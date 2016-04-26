package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;

public class CosmeticActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvTitle;
    private ImageView ivLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosmetic);
    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvTitle.setText("整容宝");

    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_chat_to_expert:
                break;
        }
    }
}
