package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;

/**
 * Created by min-fo018 on 15/11/3.
 */
public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
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
        tv_right.setText("发送");
        tvTitle.setText("联系我们");
        tv_right.setOnClickListener(this);
        ivLeft.setOnClickListener(this);

    }

    @Override
    protected void initViews() {

    }


    @Override

    public void onClick(View v) {
        String inPutContact = Contact.getText().toString();
        String inPutConrent = content.getText().toString();
        switch (v.getId()){
            case R.id.iv_left:
                finish();
                break;
        }
    }
}
