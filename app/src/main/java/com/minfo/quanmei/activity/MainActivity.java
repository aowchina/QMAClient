package com.minfo.quanmei.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.fragment.Group_Fragment;
import com.minfo.quanmei.fragment.My_Fragment;
import com.minfo.quanmei.fragment.Special_Fragment;
import com.minfo.quanmei.fragment.Start_Fragment;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.lang.ref.WeakReference;

import cn.jpush.android.api.JPushInterface;

/**
 * 主界页面
 * 2015年8月24日
 * zhang jiachang
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, Start_Fragment.JumpFragmentListener {

    private ImageView ivMain;
    private ImageView ivGroup;
    private ImageView ivSpecial;
    private ImageView ivMy;
    private long time;
    private Start_Fragment start_fragment;
    private Group_Fragment group_fragment;
    private Special_Fragment special_fragment;
    private My_Fragment my_fragment;
    private String message = "";
    private TextView tv_right;

    private LinearLayout llMyNickname;
    private TextView tvLevel;
    private TextView tvNickname;
    public static MyHandler myHandler;


    //标题栏
    private ImageView civAvatar;//头像
    private RelativeLayout rlUserAvatar;//头像相对布局
    private LinearLayout llScanner;//扫码
    private RelativeLayout rlSearch;//搜索
    private TextView tvTitle;//标题
    private TextView tvLeft;//左边tv
    private EditText edSearch;
    private RelativeLayout mainRelative;
    private View topLine;

    private ImageView ivMsg;

    private int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        myHandler = new MyHandler(this);
    }


    @Override
    protected void findViews() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.minfo.quanmei.load.head.image");
        registerReceiver(loadHeadReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void initViews() {
        //top
        civAvatar = (ImageView) findViewById(R.id.civ_user_avatar);
        rlUserAvatar = (RelativeLayout) findViewById(R.id.rl_user_avatar);
        llScanner = (LinearLayout) findViewById(R.id.ll_scanner);
        llScanner.setOnClickListener(this);
        tv_right = (TextView) findViewById(R.id.tv_right);
        ivMsg = (ImageView) findViewById(R.id.iv_message);

        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        llMyNickname = (LinearLayout) findViewById(R.id.ll_my_nickname);


        tvTitle = (TextView) findViewById(R.id.tv_title);
        rlSearch = (RelativeLayout) findViewById(R.id.rl_search);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        edSearch = (EditText) findViewById(R.id.ed_search);
        mainRelative = ((RelativeLayout) findViewById(R.id.common_top));
        topLine = findViewById(R.id.top_line);

        rlSearch.setOnClickListener(this);
        edSearch.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        rlUserAvatar.setOnClickListener(this);
        tvLeft.setOnClickListener(this);

        ivMain = (ImageView) findViewById(R.id.iv_main);
        ivGroup = (ImageView) findViewById(R.id.iv_group);
        ivSpecial = (ImageView) findViewById(R.id.iv_special);
        ivMy = (ImageView) findViewById(R.id.iv_my);

        String str = getIntent().getStringExtra("ID");
        if (str != null) {
            message = str;
        }
        if (message.equals("THEME") || message.equals("SPECIAL")) {
            setSelect(2);
        } else if (message.equals("InFo")) {
            setSelect(3);
        } else {
            setSelect(0);
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> activityWeakReference;

        public MyHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (message.what == 1) {
                    User user = (User) message.obj;
                    activity.tvNickname.setText(user.getUsername());
                    activity.tvLevel.setText("LV" + user.getLevel());
                }
            }
        }
    }

    private BroadcastReceiver loadHeadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (utils.isLogin()) {
                tvLeft.setVisibility(View.GONE);
                rlUserAvatar.setVisibility(View.VISIBLE);
                UniversalImageUtils.disCircleImage(utils.getUserimg(), civAvatar);
            }
        }
    };

    /**
     * 切换fragmen时改变标题栏状态 2015-09-01
     */
    private void refreshTop(Fragment fragment) {
        if (utils.isLogin() && Constant.user != null) {
            tvLeft.setVisibility(View.GONE);
            rlUserAvatar.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(utils.getUserimg())) {
                UniversalImageUtils.disCircleImage(Constant.user.getUserimg(), civAvatar);
            }
        } else {
            tvLeft.setVisibility(View.VISIBLE);
            rlUserAvatar.setVisibility(View.GONE);
        }

        if (fragment instanceof Start_Fragment) {
            rlSearch.setVisibility(View.VISIBLE);
//            mainRelative.setBackgroundColor(getResources().getColor(R.color.start_title_color));
            llScanner.setVisibility(View.VISIBLE);
            tv_right.setVisibility(View.GONE);
            civAvatar.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
            edSearch.setInputType(InputType.TYPE_NULL);
            topLine.setVisibility(View.GONE);

            refreshMsg();
            llMyNickname.setVisibility(View.INVISIBLE);


        } else if (fragment instanceof Group_Fragment) {
            rlSearch.setVisibility(View.GONE);
            mainRelative.setBackgroundColor(getResources().getColor(R.color.white));
            llScanner.setVisibility(View.GONE);
            tv_right.setVisibility(View.GONE);
            civAvatar.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("美人圈");
            topLine.setVisibility(View.VISIBLE);
            llMyNickname.setVisibility(View.INVISIBLE);
        } else if (fragment instanceof Special_Fragment) {
            mainRelative.setBackgroundResource(R.mipmap.start_title2);
            rlSearch.setVisibility(View.GONE);
            llScanner.setVisibility(View.GONE);
            civAvatar.setVisibility(View.VISIBLE);
            tv_right.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("特惠");
            topLine.setVisibility(View.VISIBLE);
            llMyNickname.setVisibility(View.INVISIBLE);
        } else if (fragment instanceof My_Fragment) {
            mainRelative.setBackgroundResource(R.mipmap.start_title2);
            rlSearch.setVisibility(View.GONE);
            llScanner.setVisibility(View.GONE);
            civAvatar.setVisibility(View.GONE);
            llMyNickname.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText("个人资料");
            tvTitle.setText("我的");
            topLine.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新消息图标
     */
    private void refreshMsg() {
        int replyNum = 0;
        int reproveNum = 0;
        replyNum = utils.getReceiveNum("receiveReply");
        reproveNum = utils.getReceiveNum("receiveReprove");
        if ((replyNum + reproveNum) == 0) {
            ivMsg.setImageResource(R.mipmap.start_no_message);
        } else {
            ivMsg.setImageResource(R.mipmap.start_message);
        }
    }

    //两次返回，退出

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (System.currentTimeMillis() - time > 2000) {
                ToastUtils.show(this, "再按一次退出程序");
                time = System.currentTimeMillis();
            } else {
                JPushInterface.stopPush(getApplicationContext());
                appManager.finishAllActivity();
//                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        return true;
    }

    @Override
    public void jumpFragment(int i) {
        if (i == 4) {
            setSelect(3);
        } else if (i == 3) {
            setSelect(2);
        } else if (i == 2) {
            setSelect(1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                LoginActivity.isJumpLogin = true;
                utils.jumpAty(this, LoginActivity.class, null);
                break;
            case R.id.btn_right:
                break;
            case R.id.tv_right:
                startActivity(new Intent(this, PersonInfoActivity.class));
                break;
            case R.id.ll_main:
                currentIndex = 0;
                setSelect(0);
                break;
            case R.id.ll_group:
                currentIndex = 1;
                setSelect(1);
                break;
            case R.id.ll_special:
                currentIndex = 2;
                setSelect(2);
                break;
            case R.id.ll_my:
                if (Constant.user != null) {
                    currentIndex = 3;
                    setSelect(3);
                } else {
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(this, LoginActivity.class, null);
                }
                break;
            case R.id.rl_search:

                break;
            case R.id.ed_search:
                utils.jumpAty(this, SearchActivity.class, null);
                break;
            case R.id.rl_user_avatar:
                if (currentIndex != 3) {
                    setSelect(3);
                }
                break;
            case R.id.ll_scanner:
                if (Constant.user != null) {
                    utils.jumpAty(this, MessageActivity.class, null);
                } else {
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(this, LoginActivity.class, null);
                }
                break;
        }
    }

    private void setSelect(int i) {
        resetBottom();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (start_fragment == null) {
                    start_fragment = new Start_Fragment();
                    transaction.add(R.id.perfect_continer, start_fragment);
                } else {
                    transaction.show(start_fragment);
                }
                ivMain.setImageResource(R.mipmap.tab_main_p);
                refreshTop(start_fragment);
                break;
            case 1:
                if (group_fragment == null) {
                    group_fragment = new Group_Fragment();
                    transaction.add(R.id.perfect_continer, group_fragment);
                } else {
                    transaction.show(group_fragment);

                }
                ivGroup.setImageResource(R.mipmap.tab_group_p);
                refreshTop(group_fragment);
                break;
            case 2:
                if (special_fragment == null) {
                    special_fragment = new Special_Fragment();
                    transaction.add(R.id.perfect_continer, special_fragment);
                } else {
                    transaction.show(special_fragment);
                }
                ivSpecial.setImageResource(R.mipmap.tab_special_p);
                refreshTop(special_fragment);
                break;
            case 3:
                if (my_fragment == null) {
                    my_fragment = new My_Fragment();
                    transaction.add(R.id.perfect_continer, my_fragment);
                } else {
                    transaction.show(my_fragment);
                }
                ivMy.setImageResource(R.mipmap.tab_me_p);
                refreshTop(my_fragment);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (start_fragment != null) {
            transaction.hide(start_fragment);
        }
        if (group_fragment != null) {
            transaction.hide(group_fragment);
        }
        if (special_fragment != null) {
            transaction.hide(special_fragment);
        }
        if (my_fragment != null) {
            transaction.hide(my_fragment);
        }
    }

    private void resetBottom() {
        ivMain.setImageResource(R.mipmap.tab_main_n);
        ivGroup.setImageResource(R.mipmap.tab_group_n);
        ivSpecial.setImageResource(R.mipmap.tab_special_n);
        ivMy.setImageResource(R.mipmap.tab_me_n);
    }

    public void updateUnreadLabel() {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loadHeadReceiver);
    }
}
