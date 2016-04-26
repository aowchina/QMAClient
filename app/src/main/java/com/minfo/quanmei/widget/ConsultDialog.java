package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.minfo.quanmei.R;
import com.minfo.quanmei.utils.ToastUtils;

/**
 * Created by liujing on 15/8/31.
 * 在线咨询对话框
 */
public class ConsultDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private RatingBar ratingBar;
    private CircleImageView civHead;//医生头像
    private ImageButton ibtnClose;//对话框关闭按钮
    private Button btnWordConsult;//文字咨询
    private Button btnAudioConsult;//语音咨询


    public ConsultDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_online_consult,null);
        civHead = (CircleImageView) view.findViewById(R.id.civ_doctor_head);
        ibtnClose = (ImageButton) view.findViewById(R.id.ibtn_dialog_close);
        btnWordConsult = (Button) view.findViewById(R.id.btn_word_consult);
        btnAudioConsult = (Button) view.findViewById(R.id.btn_audio_consult);
        ibtnClose.setOnClickListener(this);
        btnWordConsult.setOnClickListener(this);
        btnAudioConsult.setOnClickListener(this);

        this.setCancelable(false);

        ratingBar = (RatingBar) view.findViewById(R.id.rb_good_comment);
        ratingBar.setNumStars(5);
        ratingBar.setRating(1);

        ratingBar.setIsIndicator(false);


        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(ibtnClose)){
            this.dismiss();
        }else if(v.equals(btnWordConsult)){
            ToastUtils.show(context,"文字咨询");
        }else if(v.equals(btnAudioConsult)){
            ToastUtils.show(context,"语音咨询");
        }
    }
}
