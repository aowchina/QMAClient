
package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.minfo.quanmei.R;


public class CustomDialog extends Dialog {


    private Context context;

    private View layoutView;

    public CustomDialog(Context context,View view) {
        super(context, R.style.dialog);
        this.context = context;
        this.layoutView = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(layoutView);
    }
}
