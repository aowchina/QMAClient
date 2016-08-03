package com.minfo.quanmei.activity;

import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.ChildCategory;
import com.minfo.quanmei.fragment.RepliedMeFragment;
import com.minfo.quanmei.fragment.ReproveMeFragment;

/**
 * 小组通知
 */
public class GroupNotifyActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvReply;
    private TextView tvReprove;
    private ChildCategory childCategory;

    private ImageView ivLeft;
    private TextView tvTitle;

    private LinearLayout llReply;
    private LinearLayout llReprove;
    private TextView tvReplyNum;
    private TextView tvReproveNum;

    private int replyNum;
    private int reproveNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notify);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);
    }

    @Override
    protected void findViews() {

        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("小组通知");
        tvTitle.setVisibility(View.VISIBLE);

        llReply = (LinearLayout) findViewById(R.id.ll_reply);
        llReprove = (LinearLayout) findViewById(R.id.ll_reprove);
        tvReply = (TextView) findViewById(R.id.tv_reply);
        tvReprove = (TextView) findViewById(R.id.tv_reprove);

        tvReplyNum = (TextView) findViewById(R.id.tv_reply_num);
        tvReproveNum = (TextView) findViewById(R.id.tv_reprove_num);


        llReply.setOnClickListener(this);
        llReprove.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        switchFragment(1);
        replyNum = utils.getReceiveNum("receiveReply");
        reproveNum = utils.getReceiveNum("receiveReprove");
        if (replyNum != 0) {
            tvReplyNum.setVisibility(View.VISIBLE);
            tvReplyNum.setText(replyNum + "");
        }
        if (reproveNum != 0) {
            tvReproveNum.setVisibility(View.VISIBLE);
            tvReproveNum.setText(reproveNum + "");
        }

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_reply:
            case R.id.tv_reply:
                tvReply.setTextColor(Color.BLACK);
                tvReply.setBackgroundColor(Color.WHITE);
                llReply.setBackgroundColor(Color.WHITE);
                tvReprove.setTextColor(Color.WHITE);
                tvReprove.setBackgroundColor(getResources().getColor(R.color.tab_top_bg));
                llReprove.setBackgroundColor(getResources().getColor(R.color.tab_top_bg));
                replyNum = 0;
                utils.setReceiveNum("receiveReply", 0);
                tvReplyNum.setVisibility(View.GONE);
                switchFragment(1);
                break;
            case R.id.ll_reprove:
            case R.id.tv_reprove:
                tvReply.setTextColor(Color.WHITE);
                tvReply.setBackgroundColor(getResources().getColor(R.color.tab_top_bg));
                llReply.setBackgroundColor(getResources().getColor(R.color.tab_top_bg));
                tvReprove.setTextColor(Color.BLACK);
                tvReprove.setBackgroundColor(Color.WHITE);
                llReprove.setBackgroundColor(Color.WHITE);
                reproveNum = 0;
                utils.setReceiveNum("receiveReprove", 0);
                tvReproveNum.setVisibility(View.GONE);
                switchFragment(2);
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    /**
     * 切换fragment
     *
     * @param flag 1代表回复我的 2代表赞我的
     */
    private void switchFragment(int flag) {
        if (flag == 1) {
            RepliedMeFragment repliedMeFragment = new RepliedMeFragment();
            Bundle bundle = new Bundle();
            repliedMeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, repliedMeFragment).commit();
        } else {

            ReproveMeFragment reproveMeFragment = new ReproveMeFragment();
            Bundle bundle = new Bundle();
            reproveMeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, reproveMeFragment).commit();

        }
    }


}
