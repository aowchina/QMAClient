package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;


public class PolicyActivity extends BaseActivity implements View.OnClickListener {

    public static int CATEGORY_TAG = 0;

    private ImageView ivLeft;
    private TextView tvTitle;

    private WebView webContent;

    private String user_category_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);


    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        if(CATEGORY_TAG==0){
            tvTitle.setText("用户协议");
            user_category_url = getString(R.string.user_policy_url);
        }else{
            tvTitle.setText("法律声明");
            user_category_url = getString(R.string.user_security_url);
        }

        tvTitle.setVisibility(View.VISIBLE);

        webContent = (WebView) findViewById(R.id.web_content);
    }

    @Override
    protected void initViews() {
        webContent.loadUrl(user_category_url);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_left:
                break;
        }
    }
}
