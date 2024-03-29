package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.minfo.quanmei.R;
import com.minfo.quanmei.chat.Constant;
import com.minfo.quanmei.chat.ui.ChatActivity;
import com.minfo.quanmei.utils.ToastUtils;

public class MessageActivity extends BaseActivity implements View.OnClickListener {

    //top
    private ImageView ivLeft;
    private TextView tvTitle;

    private TextView tvMessage;
    private TextView tvMessageNum;
    private TextView tvMessageTime;

    private TextView tvAsk;

    private LinearLayout llGroupNotify;
    private int replyNum;
    private int reproveNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


    }

    @Override
    protected void findViews() {

        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("消息");

        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvMessageNum  = (TextView) findViewById(R.id.tv_message_num);
        tvMessageTime = (TextView) findViewById(R.id.tv_last_message_time);

        tvAsk = (TextView) findViewById(R.id.tv_ask);
        tvAsk.setOnClickListener(this);


        llGroupNotify = (LinearLayout) findViewById(R.id.ll_group_notify);
        llGroupNotify.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
    }



    @Override
    protected void initViews() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        replyNum = utils.getReceiveNum("receiveReply");
        reproveNum = utils.getReceiveNum("receiveReprove");
        tvMessageTime.setText(utils.getLastMessageTime());
        tvMessage.setText("你收到" + replyNum + "个回复" + reproveNum + "个有用");
        if((replyNum+reproveNum)!=0){
            tvMessageNum.setVisibility(View.VISIBLE);
            tvMessageNum.setText((replyNum+reproveNum)+"");
        }else{
            tvMessageNum.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_group_notify:
                utils.jumpAty(this,GroupNotifyActivity.class,null);
                break;
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_ask:
//                String toUserName = "594111915";//默认用户
                String toUserName = "785251622";//默认用户
                if(toUserName.equals(EMClient.getInstance().getCurrentUser())){
                    ToastUtils.show(this,R.string.Cant_chat_with_yourself);
                }else {
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(Constant.EXTRA_USER_ID,toUserName);
                    startActivity(intent);
                }
                break;
        }
    }
}
