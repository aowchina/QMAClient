
package com.minfo.quanmei.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.utils.AppManager;
import com.minfo.quanmei.utils.Constant;


public class ForceExitDialog extends Dialog implements View.OnClickListener{

    Button btnYes;

    private Context context;

    public ForceExitDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_force_exit);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        btnYes = (Button) findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        Constant.user = null;
        SharedPreferences sp = context.getSharedPreferences("SpQm", Activity.MODE_PRIVATE);
        sp.edit().putInt("userid",0).commit();
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
