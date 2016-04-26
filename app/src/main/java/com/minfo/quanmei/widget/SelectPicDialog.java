package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.minfo.quanmei.R;

/**
 * Created by liujing on 15/10/6.
 */
public class SelectPicDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private SelectPicDialogClickListener listener;

    private View layoutView;
    private Button btnCamera;
    private Button btnAlbum;
    private Button btnCancel;

    public SelectPicDialog(Context context, SelectPicDialogClickListener listener) {
        super(context, R.style.dialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select_pic);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnAlbum = (Button) findViewById(R.id.btn_album);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnCamera.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera:
                listener.selectClick(Type.CAMERA);
                dismiss();
                break;
            case R.id.btn_album:
                listener.selectClick(Type.ALBUM);
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;

        }
    }

    public interface SelectPicDialogClickListener {
        void selectClick(Type type);
    }

    /**
     * 区分按钮点击的枚举类，相机，相册
     */
    public enum Type{
       CAMERA,ALBUM
    }
}
