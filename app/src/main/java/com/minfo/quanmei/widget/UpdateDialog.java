package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/10/6.
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private DownLoadingListener listener;

    private TextView tvMessage;
    private TextView tvUpdate;
    private TextView tvCancel;

    private boolean isUpdated;
    private String message = "";

    public UpdateDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }

    public void setListener(DownLoadingListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_update);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvMessage.setText(message);
        if(!isUpdated){
            tvUpdate.setVisibility(View.GONE);
        }else{
            tvUpdate.setVisibility(View.VISIBLE);
        }

        tvUpdate.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_update:
                dismiss();
                listener.download();
                break;
        }
    }

    public void setIsUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public interface DownLoadingListener {
        void download();
    }

}
