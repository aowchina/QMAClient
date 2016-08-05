package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/10/2.
 */
public class MoreDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private MoreDialogClickListener listener;

    private Button btnCollect;
    private Button btnShare;
    private Button btnReport;
    private Button btnCancel;

    public MoreDialog(Context context, MoreDialogClickListener listener) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_more);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        btnCollect = (Button) findViewById(R.id.btn_collect);
        btnShare = (Button) findViewById(R.id.btn_share);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnReport = (Button) findViewById(R.id.btn_report);
        btnCollect.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_collect:
                listener.moreClick(Type.COLLECT);
                break;
            case R.id.btn_share:
                listener.moreClick(Type.SHARE);
                break;
            case R.id.btn_cancel:
                listener.moreClick(Type.CANCEL);
                break;
            case R.id.btn_report:
                listener.moreClick(Type.REPORT);
                break;

        }
    }

    public void setCollectText(String collectText){
        btnCollect.setText(collectText);

    }

    public interface MoreDialogClickListener {
        void moreClick(Type type);
    }

    /**
     * 区分按钮点击的枚举类，收藏，分享，举报，取消
     */
    public enum Type{
       COLLECT,SHARE,REPORT,CANCEL
    }
}
