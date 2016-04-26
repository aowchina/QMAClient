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
public class ProjectInfoLabel2 extends LinearLayout {
    private Context context;
    private LayoutInflater inflater;

    private TextView tvDuration;//治疗时长
    private TextView tvNums;//治疗次数
    private TextView tvMethod;//麻醉方法
    private TextView tvHos;//是否住院
    private TextView tvHftime;//恢复时间
    private TextView tvCxtime;//拆线时间
    public ProjectInfoLabel2(Context context) {
        this(context, null);
    }

    public ProjectInfoLabel2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        inflater.inflate(R.layout.layout_project_info_label2,null);
        tvDuration = (TextView) findViewById(R.id.tv_pro_info_duration);
        tvNums = (TextView) findViewById(R.id.tv_pro_info_nums);
        tvMethod = (TextView) findViewById(R.id.tv_pro_info_method);
        tvHos = (TextView) findViewById(R.id.tv_pro_info_hos);
        tvHftime = (TextView) findViewById(R.id.tv_pro_info_hftime);
        tvCxtime = (TextView) findViewById(R.id.tv_pro_info_cxtime);
    }

}
