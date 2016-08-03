package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
    private Button btnUnCollect;
    private Button btnShare;
    private Button btnCancel;
    private String is_sc;

    public MoreDialog(Context context, MoreDialogClickListener listener,String is_sc) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
        this.is_sc=is_sc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_more);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        btnCollect = (Button) findViewById(R.id.btn_collect);
        btnUnCollect = (Button) findViewById(R.id.btn_uncollect);
        btnShare = (Button) findViewById(R.id.btn_share);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        Log.e("",is_sc);
        if (is_sc!=null){
            if (is_sc.equals("0")){
                btnCollect.setVisibility(View.VISIBLE);
                btnUnCollect.setVisibility(View.GONE);
            }
            if (is_sc.equals("1")){
                btnCollect.setVisibility(View.GONE);
                btnUnCollect.setVisibility(View.VISIBLE);
            }
        }else {
            btnCollect.setVisibility(View.VISIBLE);
            btnUnCollect.setVisibility(View.GONE);
        }
        btnUnCollect.setOnClickListener(this);
        btnCollect.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_collect:
                listener.moreClick(Type.COLLECT);
                break;
            case R.id.btn_uncollect:
                listener.moreClick(Type.UNCOLLECT);
                break;
            case R.id.btn_share:
                listener.moreClick(Type.SHARE);
                break;
            case R.id.btn_cancel:
                listener.moreClick(Type.CANCEL);
                break;

        }
    }

    public interface MoreDialogClickListener {
        void moreClick(Type type);
    }

    /**
     * 区分按钮点击的枚举类，收藏，分享，举报，取消
     */
    public enum Type{
        COLLECT,UNCOLLECT,SHARE,CANCEL
    }
}
