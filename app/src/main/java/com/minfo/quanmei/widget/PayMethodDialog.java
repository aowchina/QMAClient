package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/10/2.
 */
public class PayMethodDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private RadioGroup rgPay;
    private int method = 1;
    private PayCommitListener listener;
    private Button btnYes;


    public PayMethodDialog(Context context, PayCommitListener listener) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_pay);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        rgPay = (RadioGroup) findViewById(R.id.rg_pay);
        btnYes = (Button) findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(this);
        rgPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                method = checkedId == R.id.rb_alipay ? 1 : 2;
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            this.dismiss();
            listener.submit(method);
        }
    }

    public interface PayCommitListener {
        void submit(int method);
    }

}
