package com.minfo.quanmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/8/27.
 */
public class ProjectInfoLabel1 extends LinearLayout {
    private Context context;
    private TextView tvIndex;
    private TextView tvContent;
    public ProjectInfoLabel1(Context context) {
        this(context, null);
    }

    public ProjectInfoLabel1(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_project_info_content,null);
        tvIndex = (TextView) findViewById(R.id.tv_index);
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

    private void setIndex(String index) {
        tvIndex.setText(index);
    }

    public void setContent(String  content) {
        tvContent.setText(content);
    }
}
