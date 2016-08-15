package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.NoteFirstReplyAdapter;
import com.minfo.quanmei.adapter.NoteSecondReplyAdapter;
import com.minfo.quanmei.entity.NoteFirstReply;
import com.minfo.quanmei.entity.NoteReply;
import com.minfo.quanmei.entity.SecondReply;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LLUserTitle;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全部回复列表 by liujing 2015-10-05
 */
public class SecondAllReplyActivity extends BaseActivity implements View.OnClickListener, NoteFirstReplyAdapter.ClickListener {
    //top
    private TextView tvTitle;
    private ImageView ivLeft;

    private LLUserTitle llUserTitle;
    private TextView tvFirstReplyContent;

    private ListView llvAllReply;

    private NoteFirstReply noteFirstReply;
    private NoteSecondReplyAdapter secondReplyAdapter;
    private List<SecondReply> secondReplies = new ArrayList<>();

    private String firstReplyId = "as";
    private String wid = "";
    private View view;
    private ImageView ivReprove;
    private ImageView ivReply;
    private boolean isUpLoad;
    private boolean iszan;

    public static SecondAllReplyActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_all_reply);
    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setText("全部回复");
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        llvAllReply = (ListView) findViewById(R.id.llv_all_reply);
        view = LayoutInflater.from(this).inflate(R.layout.layout_secondreply_headview, null);
        ivReprove = (ImageView) view.findViewById(R.id.iv_praise);
        ivReply = (ImageView) view.findViewById(R.id.iv_reply);
        ivReprove.setOnClickListener(this);
        ivReply.setOnClickListener(this);
        llvAllReply.addHeaderView(view);

        llUserTitle = (LLUserTitle) view.findViewById(R.id.ll_user_title);
        tvFirstReplyContent = (TextView) view.findViewById(R.id.tv_first_content);

        instance = this;

    }

    @Override
    protected void initViews() {

        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            firstReplyId = bundle.getString("firstReplyId");
            wid = bundle.getString("wid");
        }
        if (firstReplyId != null) {
            reqAllReply();
        }

        secondReplyAdapter = new NoteSecondReplyAdapter(this, secondReplies, this);
        llvAllReply.setAdapter(secondReplyAdapter);
    }

    /**
     * 请求全部回复列表
     */
    private void reqAllReply() {
        String url = getResources().getString(R.string.api_baseurl) + "wenzhang/MorePlList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + firstReplyId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                secondReplies.clear();
                noteFirstReply = response.getObj(NoteFirstReply.class);
                noteFirstReply.setWid(wid);
                bindData();

                secondReplies.addAll(noteFirstReply.getSon());
                for(SecondReply secondReply:secondReplies){
                    secondReply.setParentId(firstReplyId);
                }
                secondReplyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                if (response.getErrorcode() == 13) {
                    ToastUtils.show(SecondAllReplyActivity.this, "记录不存在或已被删除");
                }else{
                    ToastUtils.show(SecondAllReplyActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(SecondAllReplyActivity.this, msg);
            }
        });
    }

    /**
     * 评论 type=1 表示文章 type = 2表示评论
     *
     * @param type
     * @param desId
     */
    private void reqReprove(final int type, String desId) {
        String url = getResources().getString(R.string.api_baseurl) + "wenzhang/AddZan.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + type + "*" + desId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                ToastUtils.show(SecondAllReplyActivity.this, "成功！");
                noteFirstReply.setStatus(1);
                iszan = true;
                refreshReproveBtn(iszan);

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 14) {
                    ToastUtils.show(SecondAllReplyActivity.this, "您处于未登录状态，请先登录");
                } else if (errorcode == 16) {
                    ToastUtils.show(SecondAllReplyActivity.this, "您已赞过，请勿重复操作");
                } else {
                    ToastUtils.show(SecondAllReplyActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(SecondAllReplyActivity.this, msg);
            }
        });

    }

    /**
     * 取消点赞,type=1 表示文章 type=2 表示评论
     */
    private void reqUnReprove(final int type, String desId) {


        String url = getResources().getString(R.string.api_baseurl) + "wenzhang/DelZan.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + type + "*" + desId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                ToastUtils.show(SecondAllReplyActivity.this, "取消成功！");
                noteFirstReply.setStatus(0);
                iszan = false;
                refreshReproveBtn(iszan);

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 14) {
                    ToastUtils.show(SecondAllReplyActivity.this, "您处于未登录状态，请先登录");
                    utils.jumpAty(SecondAllReplyActivity.this, LoginActivity.class, null);
                } else if (errorcode == 15) {
                    ToastUtils.show(SecondAllReplyActivity.this, "记录不存在或已被删除");
                } else if (errorcode == 16) {
                    ToastUtils.show(SecondAllReplyActivity.this, "用户没有点过赞,不能取消");
                } else {
                    ToastUtils.show(SecondAllReplyActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(SecondAllReplyActivity.this, msg);
            }
        });

    }


    private void bindData() {
        llUserTitle.setTime(noteFirstReply.getPubtime());
        llUserTitle.setNickName(noteFirstReply.getUsername());
        llUserTitle.setUserAvatar(noteFirstReply.getUserimg());
        tvFirstReplyContent.setText(noteFirstReply.getText());
        iszan = noteFirstReply.getStatus() == 0 ? false : true;
        refreshReproveBtn(iszan);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.iv_praise:
                if(utils.isLogin()) {
                    if (noteFirstReply.getStatus() == 0) {
                        reqReprove(2, noteFirstReply.getId());
                    } else {
                        reqUnReprove(2, noteFirstReply.getId());
                    }
                }else{
                    LoginActivity.isJumpLogin = true;
                    startActivityForResult(new Intent(SecondAllReplyActivity.this,LoginActivity.class),1);
                }
                break;
            case R.id.iv_reply:
                if(utils.isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("replyType", 2);
                    bundle.putSerializable("reply", noteFirstReply);
                    bundle.putInt("allReply", 1);
                    bundle.putInt("notecom", 8);
                    utils.jumpAty(this, ReplyNoteActivity.class, bundle);
                }else{
                    LoginActivity.isJumpLogin = true;
                    startActivityForResult(new Intent(SecondAllReplyActivity.this,LoginActivity.class),1);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reqAllReply();
    }

    @Override
    public void click(NoteReply reply, int type) {
        Bundle bundle = new Bundle();
        if (reply instanceof SecondReply) {//点击二级评论列表item回复

            if(utils.isLogin()) {
                bundle.putInt("allReply", 1);
                bundle.putInt("replyType", 3);
                bundle.putSerializable("reply", reply);
                bundle.putInt("notecom", 8);
                utils.jumpAty(this, ReplyNoteActivity.class, bundle);
            }else{
                LoginActivity.isJumpLogin = true;
                startActivityForResult(new Intent(SecondAllReplyActivity.this,LoginActivity.class),1);
            }

        }
    }

    /**
     * 刷新底部点赞按钮状态
     *
     * @param isReproved true 点过赞
     */
    private void refreshReproveBtn(boolean isReproved) {

        if (isReproved) {
            ivReprove.setImageResource(R.mipmap.img_praised);
        } else {
            ivReprove.setImageResource(R.mipmap.praise);
        }
    }


}
