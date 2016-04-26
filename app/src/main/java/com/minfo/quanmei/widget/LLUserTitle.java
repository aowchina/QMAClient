package com.minfo.quanmei.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.PersonalHomePageActivity;
import com.minfo.quanmei.utils.UniversalImageUtils;

/**
 * Created by liujing on 15/9/30.
 * 头像，昵称，回复时间
 */
public class LLUserTitle extends LinearLayout implements View.OnClickListener{

    private Context context;
    private ImageView ivUserAvatar;
    private TextView tvNickName;
    private TextView tvTime;
    private String userid;
    public LLUserTitle(Context context) {
        this(context, null);

    }

    public LLUserTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.user_title,this,true);
        this.context = context;

        ivUserAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvNickName = (TextView) findViewById(R.id.tv_nickname);
        tvTime = (TextView) findViewById(R.id.tv_time);
        ivUserAvatar.setOnClickListener(this);
    }


    public void setNickName(String nickname){
        tvNickName.setText(nickname);
    }

    public void setTime(String time){
        tvTime.setText(time);
    }
    public void setUserAvatar(String url){
        UniversalImageUtils.disCircleImage(url, ivUserAvatar);
    }

    public void setUserid(String userid){
        this.userid = userid;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, PersonalHomePageActivity.class);
        intent.putExtra("userid",userid);
        context.startActivity(intent);
    }
}
