package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.PullScrollView;

/**
 * 群信息页面
 * 2015年10月5日
 * zhang jiachang
 */
public class GroupMessageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView message;
    private RelativeLayout joinGroup;
    private PullScrollView scroll;

    private LimitListView listView;
    private TextView moreTV;
    private TextView name;
    private TextView number;
    private TextView joinTV;
    private LinearLayout newAction;
    private TextView introduce;
    private TextView subject;
    private TextView qqNumber;
    private ImageView icon;
    private PopupWindow popupWindow;
    private DisplayMetrics outMetrics;
    private boolean temp;//加入或退出群的标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.groupmessage_back));
        message = ((ImageView) findViewById(R.id.iv_message));
        joinGroup = ((RelativeLayout) findViewById(R.id.rl_join_groupmessage));
        scroll = ((PullScrollView) findViewById(R.id.psl_groupmessage));
        View view = LayoutInflater.from(this).inflate(R.layout.layout_groupmessage_bodyview, null);

        //moreTV = ((TextView) view.findViewById(R.id.tv_more_groupmessage));

        joinTV = ((TextView) view.findViewById(R.id.tv_join_groupmessage));
        newAction = ((LinearLayout) view.findViewById(R.id.ll_action_groupmessage));

        name = ((TextView) view.findViewById(R.id.tv_name_groupmessage));
        number = ((TextView) view.findViewById(R.id.tv_number_groupmessage));
        introduce = ((TextView) view.findViewById(R.id.tv_simple_groupmessage));
        subject = ((TextView) view.findViewById(R.id.tv_subject_groupmessage));
        qqNumber = ((TextView) view.findViewById(R.id.tv_qqnumber_groupmessage));
        icon = ((ImageView) view.findViewById(R.id.iv_icon_groupmessage));


        scroll.addBodyView(view);
    }

    @Override
    protected void initViews() {
        back.setOnClickListener(this);
        message.setOnClickListener(this);
        joinGroup.setOnClickListener(this);
        //moreTV.setOnClickListener(this);
        joinTV.setOnClickListener(this);
        newAction.setOnClickListener(this);
        scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        scroll.setheaderViewReset();
                        for (int i = 0; i < 5; i++) {
                            //list.add(i + "目");
                        }
                        ToastUtils.show(GroupMessageActivity.this, "刷新成功");
                    }

                }, 2000);
            }
        });



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.groupmessage_back:
                finish();
                break;
            case R.id.iv_message:
                break;
            case R.id.tv_join_groupmessage:

                joinOrQuitGroup(v);

                break;
            case R.id.ll_action_groupmessage:
                startActivity(new Intent(GroupMessageActivity.this,GroupActionActivity.class));
                break;
            case R.id.rl_join_groupmessage:
                joinOrQuitGroup(v);
                break;


        }

    }

    public void joinOrQuitGroup(View v) {
        if (!temp) {
            joinTV.setTextColor(Color.GRAY);
            joinTV.setBackgroundResource(R.drawable.text_group_unchose);
            joinTV.setText("已申请");
            ToastUtils.show(GroupMessageActivity.this, "加入成功");
            temp = true;

        } else {

            getPopupWindow(v);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    temp = false;
                    closePopupWindow();
                    joinTV.setTextColor(getResources().getColor(R.color.basic_color));
                    joinTV.setBackgroundResource(R.drawable.text_group_chose);
                    joinTV.setText("加入");
                    ToastUtils.show(GroupMessageActivity.this, "退出成功");
                }

            }, 2000);


        }
    }

    /**
     * 创建PopupWindow
     */

    public void initPopuptWindow(View v) {
        final View layout1 = LayoutInflater.from(this).inflate(
                R.layout.layout_groupmessage_pop, null);


        popupWindow = new PopupWindow(layout1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;

        getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //设置popupWindow弹出窗体的背景

        outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        popupWindow.setWidth(outMetrics.widthPixels * 3 / 4);
        popupWindow.setHeight(outMetrics.heightPixels * 1 / 5);
        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha));
        popupWindow.showAtLocation(back, Gravity.LEFT, outMetrics.widthPixels * 1 / 8, outMetrics.heightPixels * 1 / 20);


    }


    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindow(View v) {

        if (null != popupWindow) {
            closePopupWindow();
            return;
        } else {
            initPopuptWindow(v);
        }
    }

    /**
     * 关闭窗口
     */
    private void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }


}
