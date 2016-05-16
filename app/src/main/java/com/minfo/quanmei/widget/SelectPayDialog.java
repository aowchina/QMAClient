package com.minfo.quanmei.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/10/2.
 */
public class SelectPayDialog extends AlertDialog implements View.OnClickListener {
    private Context context;
    private SelectPayListener listener;

    private View layoutView;
    private TextView tvMenu1;
    private TextView tvMenu2;

    private int dialogType;

    public SelectPayDialog(Context context, SelectPayListener listener) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select_pay);

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
        tvMenu1 = (TextView) findViewById(R.id.tv_menu1);
        tvMenu2 = (TextView) findViewById(R.id.tv_menu2);
        tvMenu1.setOnClickListener(this);
        tvMenu2.setOnClickListener(this);
    }

    /**
     * 1 支付类型
     * 2 支付方式
     * @param dialogType
     */
    public void setDialogType(int dialogType){
        this.dialogType = dialogType;
        if(dialogType==1){
            tvMenu1.setText("全款");
            tvMenu2.setText("定金");
        }else if(dialogType==2){
            tvMenu1.setText("微信");
            tvMenu2.setText("支付宝");
        }
    }

    public int getDialogType(){
        return dialogType;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_menu1:
                dismiss();
                listener.shareClick(dialogType,Type.MENU1);
                break;
            case R.id.tv_menu2:
                dismiss();
                listener.shareClick(dialogType,Type.MENU2);
                break;
        }
    }

    public interface SelectPayListener {

        void shareClick(int dialogType,Type type);
    }

    /**
     * 区分分享的类别，全款，定金
     */
    public enum Type {
        MENU1,MENU2
    }
}
