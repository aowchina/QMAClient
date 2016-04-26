package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.minfo.quanmei.R;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.ToastUtils;

/**
 * Created by liujing on 15/10/10.
 */
public class ModifyPersonInfo extends Dialog implements View.OnClickListener {
    private Context context;
    private ModifyClickListener listener;

    private Button btnCancel;
    private Button btnConfirm;
    private EditText etNickname;
    private String nickname;

    public ModifyPersonInfo(Context context, ModifyClickListener listener) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_modify_nickname);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        etNickname = (EditText) findViewById(R.id.et_nickname);
        nickname = Constant.user.getUsername();


        setCancelable(true);
        setCanceledOnTouchOutside(true);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        etNickname.setText(nickname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                String nickname = etNickname.getText().toString();
                if(TextUtils.isEmpty(nickname)) {
                    ToastUtils.show(context, "昵称不能为空");

                }else if(!MyCheck.isNiCheng(nickname)){
                    ToastUtils.show(context,"昵称必须2到10位大小写字母或汉字");
                }else{
                    listener.moreClick(Type.CONFIRM, nickname);
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;

        }
    }

    public interface ModifyClickListener {
        void moreClick(Type type,String nickname);
    }

    /**
     * 区分按钮点击的枚举类，确定，取消
     */
    public enum Type{
        CONFIRM,CANCEL
    }

    @Override
    public void dismiss() {
        super.dismiss();
        nickname = Constant.user.getUsername();
    }

    @Override
    public void show() {
        super.show();
    }
}