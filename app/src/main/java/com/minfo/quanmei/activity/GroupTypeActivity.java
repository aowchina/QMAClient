package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.GroupHeadListAdapter;
import com.minfo.quanmei.adapter.GroupPullAdapter;
import com.minfo.quanmei.entity.Group;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.GroupTag;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.PullScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 小组分类页面
 * 2015年9月29日
 * zhang jiachang
 */
public class GroupTypeActivity extends BaseActivity implements View.OnClickListener, PullScrollView.OnRefreshListener {

    private ImageView back;
    private TextView recent;
    private TextView cream;
    private RelativeLayout send;
    private PullScrollView scroll;
    private View inflate;
    private LimitListView listViewA;//置顶
    private ListView listViewB;//日记，帖子列表
    private GroupHeadListAdapter groupHeadListAdapter;
    private GroupPullAdapter groupPullAdapter;
    private Group groupDetail;
    private List<GroupArticle> GAdataList = new ArrayList<>();
    private List<GroupArticle> tempList = new ArrayList<>();
    private LinearLayout listAContiner;
    private TextView join;
    private LinearLayout manger;
    private LinearLayout toChat;
    private LinearLayout lable;
    private boolean temp;//加入或退出小组的标志
    private PopupWindow popupWindow;
    private DisplayMetrics outMetrics;
    private LinearLayout llCategory;
    private HorizontalScrollView scrollView;
    private boolean t1;//manger开启或关闭horizontalScrollView的标志
    private boolean t2;//lable开启或关闭horizontalScrollView的标志
    private boolean create;//lable显示或关闭LinearLayout llCategory的标志
    private boolean mangImg; //manger显示或关闭LinearLayout llCategory的标志
    private ImageView img;
    private int pos;
    private int gid;
    private int page = 1;
    private int tid = 0;
    private int type = 1;
    private List<TextView> textViews = new ArrayList<TextView>();
    private List<View> views = new ArrayList<View>();

    private Group group;
    private List<GroupTag> tags;//标签
    private TextView tvGroupIntro;//小组介绍
    private TextView tvGroupName;//小组名字
    private TextView tvGroupAmount;//小组成员数
    private ImageView grouptype_img_icon;

    private LinearLayout llMoreDiary;
    private LinearLayout llMoreDiaryLoading;
    private boolean upload;

    private int currentListViewIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_type);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.grouptype_back));
        recent = ((TextView) findViewById(R.id.tv_recent_gtype));
        cream = ((TextView) findViewById(R.id.tv_cream_gtype));
        scroll = ((PullScrollView) findViewById(R.id.psl_grouptype));
        send = ((RelativeLayout) findViewById(R.id.tv_send_gtype));
        inflate = LayoutInflater.from(this).inflate(R.layout.layout_grouptype_bodyview, null);

        grouptype_img_icon = (ImageView) inflate.findViewById(R.id.grouptype_img_icon);
        listViewA = ((LimitListView) inflate.findViewById(R.id.llv_lista));
        listViewB = ((ListView) inflate.findViewById(R.id.llv_listb));
        listAContiner = ((LinearLayout) inflate.findViewById(R.id.ll_continer_group));
        toChat = ((LinearLayout) inflate.findViewById(R.id.ll_tochat_group));
        join = ((TextView) inflate.findViewById(R.id.tv_join_group));
        manger = ((LinearLayout) inflate.findViewById(R.id.ll_manger_group));
        lable = ((LinearLayout) inflate.findViewById(R.id.ll_lable_group));
        llCategory = ((LinearLayout) inflate.findViewById(R.id.ll_categroy_group));
        scrollView = ((HorizontalScrollView) inflate.findViewById(R.id.hs_group));

        tvGroupIntro = (TextView) inflate.findViewById(R.id.tv_group_intro);
        tvGroupName = (TextView) inflate.findViewById(R.id.tv_group_name);
        tvGroupAmount = (TextView) inflate.findViewById(R.id.tv_group_amount);
        scroll.setfooterViewGone();
        llMoreDiary = (LinearLayout) inflate.findViewById(R.id.ll_more_diary);
        llMoreDiary.setOnClickListener(this);

        llMoreDiaryLoading = (LinearLayout) inflate.findViewById(R.id.ll_more_diary_loading);


        scroll.addBodyView(inflate);

    }

    @Override
    protected void initViews() {
        listViewB.setFocusable(false);
        group = (Group) getIntent().getBundleExtra("info").getSerializable("group");

        UniversalImageUtils.displayImageUseDefOptions(group.getIcon(), grouptype_img_icon);
        tvGroupIntro.setText(group.getIntro());
        tvGroupName.setText(group.getName());
        tvGroupAmount.setText(group.getAmount());

        reqGroupdetail(Integer.parseInt(group.getId()), utils.getUserid());
        cream.setTextColor(Color.GRAY);
        recent.setTextColor(getResources().getColor(R.color.basic_color));
        gid = Integer.parseInt(group.getId());
        reqArticleInfo(type, gid, 0, page);


        recent.setOnClickListener(this);
        cream.setOnClickListener(this);
        scroll.setOnRefreshListener(this);
        join.setOnClickListener(this);
        lable.setOnClickListener(this);
        manger.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        toChat.setOnClickListener(this);

        listViewB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToNoteDetail = new Intent(GroupTypeActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", GAdataList.get(position));
                intentToNoteDetail.putExtra("ID", 1);
                intentToNoteDetail.putExtra("info", bundle);
                startActivity(intentToNoteDetail);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        gid = 0;
        page = 1;
        tid = 0;
        type = 1;
        GAdataList.clear();
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gid = 0;
        page = 1;
        tid = 0;
        type = 1;
        GAdataList.clear();
        initViews();
    }

    /**
     * 显示小组详情内容
     */
    private void refreshGroupDetail() {
        tvGroupIntro.setText(groupDetail.getIntro());
        tvGroupName.setText(groupDetail.getName());
        tvGroupAmount.setText(groupDetail.getAmount());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grouptype_back:// 返回键
                finish();
                break;
            case R.id.tv_join_group://加入或退出小组
                if (!utils.isLogin()&&utils.getUserid() == 0) {
                    LoginActivity.isJumpLogin = true;
                    startActivityForResult(new Intent(GroupTypeActivity.this,LoginActivity.class),1);
                } else {
                    if (!temp) {
                        reqJoinGroup();
                    } else {
                        getPopupWindow(recent);
                    }
                }
                break;
            case R.id.ll_manger_group://点击管理员
                mangerControlHorizontalScrollView();
                break;
            case R.id.ll_lable_group://点击标签
                lableControlHorizontalScrollView();
                break;
            case R.id.tv_cream_gtype://精华
                cream.setTextColor(getResources().getColor(R.color.basic_color));
                cream.setBackgroundResource(R.drawable.text_group_right);
                recent.setTextColor(Color.GRAY);
                recent.setBackgroundResource(R.drawable.text_group_left_un);
                listAContiner.setVisibility(View.GONE);
                page = 1;
                type = 2;
                GAdataList.clear();
                reqArticleInfo(type, gid, tid, page);
                break;
            case R.id.tv_recent_gtype://最新
                cream.setTextColor(Color.GRAY);
                cream.setBackgroundResource(R.drawable.text_group_right_un);
                recent.setTextColor(getResources().getColor(R.color.basic_color));
                recent.setBackgroundResource(R.drawable.text_group_left);
                listAContiner.setVisibility(View.VISIBLE);
                page = 1;
                type = 1;
                GAdataList.clear();
                reqArticleInfo(type, gid, tid, page);
                break;
            case R.id.tv_send_gtype://发帖
                if (Constant.user != null && utils.getUserid() != 0) {
                    Intent intent = new Intent(this, InvitationDetailActivity.class);
                    Constant.groupDetail = groupDetail;
                    currentListViewIndex = listViewB.getFirstVisiblePosition();
                    startActivity(intent);
                } else {
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(this, LoginActivity.class, null);
                }
                break;
            case R.id.ll_tochat_group://聊天群列表
                startActivity(new Intent(GroupTypeActivity.this, ChatGroupActivity.class));
                break;

            case R.id.ll_more_diary:
                llMoreDiary.setVisibility(View.GONE);
                llMoreDiaryLoading.setVisibility(View.VISIBLE);
                reqArticleInfo(type, gid, tid, ++page);
                break;
        }

    }

    /**
     * 请求加入小组接口
     */
    private void reqJoinGroup() {
        if (groupDetail != null) {

            String url = getResources().getString(R.string.api_baseurl) + "group/In.php";
            Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + groupDetail.getId());
            httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
                @Override
                public void onPreRequest() {

                }

                @Override
                public void onRequestSuccess(BaseResponse response) {
                    temp = true;
                    refreshGroupBtn(temp);
                    ToastUtils.show(GroupTypeActivity.this, "加入成功");
                }

                @Override
                public void onRequestNoData(BaseResponse response) {
                    int errorcode = response.getErrorcode();
                    if (errorcode == 10) {
                        LoginActivity.isJumpLogin = true;
                        utils.jumpAty(GroupTypeActivity.this, LoginActivity.class, null);
                    } else if (errorcode == 15) {
                        ToastUtils.show(GroupTypeActivity.this, "您还未登录,请先登录");
                        LoginActivity.isJumpLogin = true;
                        utils.jumpAty(GroupTypeActivity.this, LoginActivity.class, null);
                    } else if (errorcode == 16) {
                        ToastUtils.show(GroupTypeActivity.this, "您已加入小组,请勿重复操作！");
                    } else {
                        ToastUtils.show(GroupTypeActivity.this, "服务器繁忙");
                    }
                }

                @Override
                public void onRequestError(int code, String msg) {
                    ToastUtils.show(GroupTypeActivity.this, msg);
                }
            });
        }

    }

    /**
     * 刷新加入，退出 小组按钮
     *
     * @param temp
     */
    private void refreshGroupBtn(boolean temp) {
        if (temp) {
            join.setTextColor(Color.GRAY);
            join.setBackgroundResource(R.drawable.text_group_unchose);
            join.setText("退出");
        } else {
            join.setTextColor(Color.RED);
            join.setBackgroundResource(R.drawable.text_group_chose);
            join.setText("加入");
        }
    }

    /**
     * 请求退出小组接口
     */
    private void reqExitGroup() {
        if (groupDetail != null) {
            String url = getResources().getString(R.string.api_baseurl) + "group/Out.php";
            Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + groupDetail.getId());
            httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
                @Override
                public void onPreRequest() {

                }

                @Override
                public void onRequestSuccess(BaseResponse response) {
                    temp = false;
                    refreshGroupBtn(temp);
                    ToastUtils.show(GroupTypeActivity.this, "退出成功");
                }

                @Override
                public void onRequestNoData(BaseResponse response) {
                    int errorcode = response.getErrorcode();
                    if (errorcode == 10) {
                        LoginActivity.isJumpLogin = true;
                        utils.jumpAty(GroupTypeActivity.this, LoginActivity.class, null);
                    } else if (errorcode == 15) {
                        ToastUtils.show(GroupTypeActivity.this, "您还未登录,请先登录");
                        LoginActivity.isJumpLogin = true;
                        utils.jumpAty(GroupTypeActivity.this, LoginActivity.class, null);
                    } else if (errorcode == 16) {
                        ToastUtils.show(GroupTypeActivity.this, "您已退出小组,请勿重复操作！");
                    } else {
                        ToastUtils.show(GroupTypeActivity.this, "服务器繁忙");
                    }
                }

                @Override
                public void onRequestError(int code, String msg) {
                    ToastUtils.show(GroupTypeActivity.this, msg);
                }
            });
        }

    }

    //小组详情接口
    private void reqGroupdetail(int gid, int userid) {

        String articleInfoUrl = this.getResources().getString(R.string.api_baseurl) + "group/Detail.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + gid + "*" + userid);

        httpClient.post(articleInfoUrl, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                groupDetail = response.getObj(Group.class);
                temp = groupDetail.getIsin() == 1 ? true : false;
                refreshGroupDetail();
                refreshGroupBtn(temp);
                tags = groupDetail.getTag();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(GroupTypeActivity.this, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(GroupTypeActivity.this, msg);
            }
        });
    }

    //文章接口
    private void reqArticleInfo(int i, int gid, int tid, int page) {

        String articleInfoUrl = this.getResources().getString(R.string.api_baseurl) + "wenzhang/List.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + i + "*" + gid + "*" + tid + "*" + page);
        httpClient.post(articleInfoUrl, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                llMoreDiary.setVisibility(View.VISIBLE);
                llMoreDiaryLoading.setVisibility(View.GONE);
                tempList = response.getList(GroupArticle.class);

                if (tempList != null && tempList.size() > 0) {
                    GAdataList.addAll(tempList);
//                    groupHeadListAdapter = new GroupHeadListAdapter(GroupTypeActivity.this, GAdataList);
//                    listViewA.setAdapter(groupHeadListAdapter);
                } else {
                    ToastUtils.show(GroupTypeActivity.this, "没有更多数据了");
                }
                groupPullAdapter = new GroupPullAdapter(GroupTypeActivity.this, GAdataList);
                listViewB.setAdapter(groupPullAdapter);
                listViewB.setSelection(Constant.currentGroupIndex);
                setListViewHeightBasedOnChildren(listViewB);
                if (upload) {
                    upload = false;
                    groupPullAdapter.notifyDataSetChanged();
                    scroll.setheaderViewReset();
                    ToastUtils.show(GroupTypeActivity.this, "刷新成功");
                }
                tempList.clear();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                llMoreDiary.setVisibility(View.VISIBLE);
                llMoreDiaryLoading.setVisibility(View.GONE);
                ToastUtils.show(GroupTypeActivity.this, "服务器繁忙");
                if (upload) {
                    upload = false;
                    scroll.setheaderViewReset();
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                llMoreDiary.setVisibility(View.VISIBLE);
                llMoreDiaryLoading.setVisibility(View.GONE);
                ToastUtils.show(GroupTypeActivity.this, msg);
            }
        });
    }

    /**
     * 控制管理员HorizontalScrollView布局
     */
    public void mangerControlHorizontalScrollView() {
        if (!t1) {
            scrollView.setVisibility(View.VISIBLE);
            if (img != null) {
                img.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < textViews.size(); i++) {
                if (textViews.get(i) != null) {
                    textViews.get(i).setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < views.size(); i++) {
                if (views.get(i) != null) {
                    views.get(i).setVisibility(View.GONE);
                }
            }
            if (!mangImg) {
                img = new ImageView(this);
                img.setImageResource(R.mipmap.ic_launcher);
                img.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                llCategory.addView(img);
                mangImg = true;
            }
            t1 = true;
            t2 = false;
        } else {
            scrollView.setVisibility(View.GONE);
            t1 = false;
        }
    }

    //控制标签HorizontalScrollView布局
    public void lableControlHorizontalScrollView() {
        if (!t2) {
            scrollView.setVisibility(View.VISIBLE);
            if (img != null) {
                img.setVisibility(View.GONE);
            }
            for (int i = 0; i < textViews.size(); i++) {
                if (textViews.get(i) != null) {
                    textViews.get(i).setVisibility(View.VISIBLE);
                }
            }
            for (int i = 0; i < views.size(); i++) {
                if (views.get(i) != null) {
                    views.get(i).setVisibility(View.VISIBLE);
                }
            }

            if (!create) {
                create = true;
                if (tags != null) {
                    for (int i = 0; i < tags.size() + 1; i++) {

                        View v1 = new View(this);
                        v1.setLayoutParams(new RelativeLayout.LayoutParams(40,
                                RelativeLayout.LayoutParams.WRAP_CONTENT));

                        View v2 = new View(this);
                        v2.setLayoutParams(new RelativeLayout.LayoutParams(40,
                                RelativeLayout.LayoutParams.WRAP_CONTENT));
                        TextView tempTv = new TextView(this);
                        if (i == 0) {
                            tempTv.setText("全部");
                        } else {

                            tempTv.setText(tags.get(i - 1).getName());
                        }
                        tempTv.setGravity(Gravity.CENTER);
                        if (i != 0) {
                            tempTv.setTextColor(Color.BLACK);
                            tempTv.setBackgroundResource(R.drawable.text_group_unchose);
                        } else {
                            tempTv.setTextColor(getResources().getColor(R.color.basic_color));
                            tempTv.setBackgroundResource(R.drawable.text_group_chose);
                        }
                        tempTv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT));

                        llCategory.addView(v1);
                        views.add(i, v1);

                        llCategory.addView(tempTv);

                        if (i == tags.size()) {
                            llCategory.addView(v2);
                            views.add(i + 1, v2);
                        }
                        textViews.add(i, tempTv);
                    }
                }
            }
            for (int i = 0; i < textViews.size(); i++) {
                final int j = i;
                final int l = i - 1;
                textViews.get(i).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        textViews.get(j).setTextColor(getResources().getColor(R.color.basic_color));
                        textViews.get(j).setBackgroundResource(R.drawable.text_group_chose);
                        for (int k = 0; k < textViews.size(); k++) {
                            if (k != j) {
                                textViews.get(k).setTextColor(Color.BLACK);
                                textViews.get(k).setBackgroundResource(R.drawable.text_group_unchose);
                            }
                        }
                        page = 1;
                        if (j == 0) {
                            tid = 0;
                        } else {
                            tid = Integer.parseInt(groupDetail.getTag().get(l).getId());
                        }
                        GAdataList.clear();
                        reqArticleInfo(type, gid, tid, page);
                    }
                });
            }
            t2 = true;
            t1 = false;
        } else {
            scrollView.setVisibility(View.GONE);
            t2 = false;
        }
    }

    @Override
    public void refresh() {
        GAdataList.clear();
        page = 1;
        upload = true;
        reqGroupdetail(gid, utils.getUserid());
        reqArticleInfo(type, gid, tid, page);
    }

    /**
     * 创建PopupWindow
     */

    public void initPopuptWindow(View v) {
        final View layout1 = LayoutInflater.from(this).inflate(
                R.layout.layout_group_pop, null);

        TextView sure = (TextView) layout1.findViewById(R.id.tv_sure_group);
        TextView cancle = (TextView) layout1.findViewById(R.id.tv_cancle_group);
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
        popupWindow.setWidth(outMetrics.widthPixels * 4 / 5);
        popupWindow.setHeight(outMetrics.heightPixels * 1 / 4);
        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha));
        popupWindow.showAtLocation(recent, Gravity.LEFT, outMetrics.widthPixels * 1 / 10, outMetrics.heightPixels * 1 / 20);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqExitGroup();
                closePopupWindow();


            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.groupDetail = null;
    }

    /**
     * 加载数据后，计算listview高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
