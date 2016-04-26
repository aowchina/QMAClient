package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;

/**
 * 意见反馈 页面
 * Created by min-fo018 on 15/11/3.
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;

    private ImageView ivLeft;
    private TextView tv_right;
    private EditText Contact;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void findViews() {
        //设置标题栏
        Contact = (EditText) findViewById(R.id.feedback_Contact_et);
        content = (EditText) findViewById(R.id.feedback_content_et);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("提交反馈");
        tvTitle.setText("意见反馈");
        tv_right.setOnClickListener(this);
    }


    protected void initViews() {

    }

    @Override
    public void onClick(View v) {
        String inPutContact = Contact.getText().toString();
        String inPutConrent = content.getText().toString();
    }
}
