package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/10/2.
 */
public class SignDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private ImageView ivClose;
    private TextView tvSignPoint;
    private SignSuccessListener listener;
    private Button btnCheck;

    public SignDialog(Context context,SignSuccessListener listener) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_sign);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvSignPoint = (TextView) findViewById(R.id.tv_sign_point);
        ivClose.setOnClickListener(this);
        btnCheck = (Button) findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(this);
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_check:
                listener.onSignSuccess();
                dismiss();
                break;

        }
    }

    public void setPoint(String strPoint){
        tvSignPoint.setText(strPoint);
    }

    public interface SignSuccessListener{
        void onSignSuccess();
    }

}
