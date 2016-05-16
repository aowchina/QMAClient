package com.minfo.quanmei.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/10/2.
 */
public class ShareDialog extends AlertDialog implements View.OnClickListener {
    private Context context;
    private ShareClickListener listener;

    private View layoutView;
    private Button btnWechat;
    private Button btnWechatCircle;
    private Button btnQQ;
    private Button btnCancel;

    public ShareDialog(Context context, ShareClickListener listener) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_share);

        Window win = getWindow();
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.dialog_anim);
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        btnWechat = (Button) findViewById(R.id.btn_wechat_share);
        btnWechatCircle = (Button) findViewById(R.id.btn_wechat_circle);
        btnQQ = (Button) findViewById(R.id.btn_qq_share);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnWechat.setOnClickListener(this);
        btnWechatCircle.setOnClickListener(this);
        btnQQ.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_wechat_share:
                dismiss();
                listener.shareClick(Type.WECHAT_FRIEND);
                break;
            case R.id.btn_wechat_circle:
                dismiss();
                listener.shareClick(Type.WECHAT_CIRCLE);
                break;
            case R.id.btn_qq_share:
                dismiss();
                listener.shareClick(Type.QQ);
                break;
            case R.id.btn_cancel:
                dismiss();
                break;

        }
    }

    public interface ShareClickListener {
        void shareClick(Type type);
    }

    /**
     * 区分分享的类别，微信好友，朋友圈，qq好友
     */
    public enum Type{
       WECHAT_FRIEND,WECHAT_CIRCLE,QQ
    }
}
