package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.minfo.quanmei.R;


/**
 * Created by liujing on 15/10/10.
 */
public class ModifyGender extends Dialog implements View.OnClickListener {
    private Context context;
    private String nickname;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;

    private ModifyGenderListener listener;

    private int type = 1;


    public ModifyGender(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }



    public interface ModifyGenderListener{
        void onChang(int type);
    }

    public void setListener(ModifyGenderListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_modify_gender);

        rgGender = (RadioGroup) findViewById(R.id.rg_gender);
        rgGender.check(type==1?R.id.rb_male:R.id.rb_female);
        rbFemale = (RadioButton) findViewById(R.id.rb_female);
        rbMale = (RadioButton) findViewById(R.id.rb_male);
        rbFemale.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        if(rbMale.isChecked()){
            setType(1);
        }else{
            setType(2);
        }
        dismiss();
        listener.onChang(type);
    }

    @Override
    public void show() {
        super.show();
    }


}