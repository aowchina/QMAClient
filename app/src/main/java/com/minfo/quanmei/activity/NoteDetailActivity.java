package com.minfo.quanmei.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.NoteFirstReplyAdapter;
import com.minfo.quanmei.adapter.NoteSecondReplyAdapter;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.NoteFirstReply;
import com.minfo.quanmei.entity.NoteReply;
import com.minfo.quanmei.entity.Person;
import com.minfo.quanmei.entity.SecondReply;
import com.minfo.quanmei.entity.StartDiary;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LLUserTitle;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.LoadingDialog;
import com.minfo.quanmei.widget.MoreDialog;
import com.minfo.quanmei.widget.RefreshListView;
import com.minfo.quanmei.widget.ShareDialog;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帖子详情页面 create by liujing 2015-09-30
 */
public class NoteDetailActivity extends BaseActivity implements View.OnClickListener,
        MoreDialog.MoreDialogClickListener, NoteFirstReplyAdapter.ClickListener {

    private ImageView ivLeft;
    private TextView tvTitle;
    private TextView tvRight;

    private LLUserTitle userTitle;
    private TextView tvNoteTitle;
    private TextView tvNoteContent;
    private TextView tvFollowersCount;

    private RefreshListView llvFirstReply;
    private LimitListView llvSecondReply;

    private MoreDialog moreDialog;
    private ShareDialog shareDialog;
    private LoadingDialog loadingDialog;

    private NoteFirstReplyAdapter firstReplyAdapter;
    private NoteSecondReplyAdapter secondReplyAdapter;


    private List<NoteFirstReply> firstReplies = new ArrayList<>();
    private List<NoteFirstReply> tempList = new ArrayList<>();
    private View view;
    private LinearLayout btnReply;
    private LinearLayout btnReprove;
    private ImageView ivReprove;
    private ImageView ivReply;
    private LinearLayout llEmptyView;

    private StartDiary startDiary;
    private GroupArticle groupArticle;
    private GroupArticle groupArticleDetail;
    private LinearLayout llImgs;
    private int page = 1;
    private List<Person> persons = new ArrayList<Person>();
    private LinearLayout iconComment;
    private LinearLayout llReplyBottom;

    private TextView isVisible;
    private Bundle bundle;
    private int getId = 0;
    private String id = "";
    private String articleId = "";
    private String plTime = "";

    private String ctime = "";
    private String userid = "0";
    private boolean iszan;
    private boolean isUpLoad;
    private boolean isCanScroll = true;//能否上拉加载
    private boolean refresh;//能否下拉刷新
    private boolean isResetPage;//为true表示下拉刷新请求数据为第一页
    private IWXAPI iwxapi;
    private Tencent mTencent;
    private String is_sc="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
    }


    @Override
    protected void findViews() {
        //top
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        btnReply = ((LinearLayout) findViewById(R.id.btn_reply));
        btnReprove = ((LinearLayout) findViewById(R.id.btn_reprove));
        ivReprove = (ImageView) findViewById(R.id.iv_reprove);
        ivReply = (ImageView) findViewById(R.id.iv_reply);




        tvTitle.setText("全部回帖");
        tvRight.setText("更多");
        tvRight.setVisibility(View.VISIBLE);

        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        llvFirstReply = (RefreshListView) findViewById(R.id.llv_first_reply);
        view = LayoutInflater.from(this).inflate(R.layout.layout_notedetail_bodyview, null);
        llvFirstReply.addHeaderView(view);
        llEmptyView = (LinearLayout) view.findViewById(R.id.ll_empty_view);

        btnReprove.setOnClickListener(this);
        btnReply.setOnClickListener(this);

        llImgs = (LinearLayout) view.findViewById(R.id.ll_imgs);

        userTitle = (LLUserTitle) view.findViewById(R.id.ll_user_title);
        tvNoteTitle = (TextView) view.findViewById(R.id.tv_note_title);
        tvNoteContent = (TextView) view.findViewById(R.id.tv_note_content);
        tvFollowersCount = (TextView) view.findViewById(R.id.tv_followers_count);


        llvSecondReply = (LimitListView) view.findViewById(R.id.llv_second_reply);
        iconComment = ((LinearLayout) view.findViewById(R.id.ll_followers));
        isVisible = ((TextView) view.findViewById(R.id.tv_comment_note));


        shareDialog = new ShareDialog(this,shareListener);
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void initViews() {

        if (Constant.user != null) {
            userid = Constant.user.getUserid()+"";
        }
        //接收数据
        setData();

        reqContentDetail();

        llvFirstReply.setIsCanRefresh(true);
        llvFirstReply.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                isUpLoad = true;
                isCanScroll = true;
                page = 1;
                firstReplies.clear();
                reqCommentArticle(id, page);

            }
        });
        llvFirstReply.setIsCanLoad(isCanScroll);

        llvFirstReply.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                if (isCanScroll) {
                    page++;
                    reqCommentArticle(id, page);
                    llvFirstReply.loadComplete();
                } else {
                    llvFirstReply.loadComplete();
                }

            }
        });
        bindData();

    }

    /**
     * 请求帖子详情接口
     */
    private void reqContentDetail() {
        String noteDetailUrl = getResources().getString(R.string.api_baseurl) + "wenzhang/Detail.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + ctime + "*" + utils.getUserid());
        Log.e("errorcode详情", ctime + " " + utils.getUserid() + " " + params);
        httpClient.post(noteDetailUrl, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if (response.getErrorcode() == 0) {
                    Log.e("errorcode详情", response.getData() + " ");
                    groupArticleDetail = response.getObj(GroupArticle.class);
                    showImg(groupArticleDetail);
                    showPlUser();
                    if (groupArticleDetail != null) {
                        is_sc = groupArticleDetail.getIsSc();
                        Log.e("errorcode详情", is_sc + " "+groupArticleDetail.getIsSc());
                        tvNoteContent.setText(groupArticleDetail.getText());
                        iszan = groupArticleDetail.getIszan() == 0 ? false : true;
                        refreshReproveBtn(iszan);
                    }
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                Log.e("errorcode详情", errorcode + " ");
                if (errorcode == 13) {
                    ToastUtils.show(NoteDetailActivity.this, "文章不存在！");
                } else {
                    ToastUtils.show(NoteDetailActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(NoteDetailActivity.this, msg);
            }
        });

    }

    /**
     * 显示点赞的用户的头像
     */
    private void showPlUser() {
        List<View> views = new ArrayList<>();
        List<ImageView> imageViews = new ArrayList<>();
        List<User> plUser = groupArticleDetail.getPluser();
        tvFollowersCount.setText(groupArticleDetail.getZan());
        if (plUser.size() > 0) {

            for (int i = 0; i < plUser.size(); i++) {
                final User user = plUser.get(i);
                View v1 = new View(NoteDetailActivity.this);
                v1.setLayoutParams(new RelativeLayout.LayoutParams(15,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                ImageView image = new ImageView(NoteDetailActivity.this);

                image.setLayoutParams(new RelativeLayout.LayoutParams(utils.dip2px(40),
                        utils.dip2px(40)));
                UniversalImageUtils.disCircleImage(user.getUserimg(), image);
                iconComment.addView(v1);

                iconComment.addView(image);
                views.add(v1);
                imageViews.add(image);

            }
            if (persons.size() >= 9) {
                isVisible.setVisibility(View.VISIBLE);
            } else {
                isVisible.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showImg(GroupArticle groupArticleDetail) {
        if (groupArticleDetail.getImgs() != null) {
            for (int i = 0; i < groupArticleDetail.getImgs().size(); i++) {
                final ImageView imageView = new ImageView(this);

                UniversalImageUtils.loadDefImage(groupArticleDetail.getImgs().get(i), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        imageView.setImageResource(R.mipmap.default_pic);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        imageView.setImageResource(R.mipmap.default_pic);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        Matrix matrix = new Matrix();
                        matrix.postScale(utils.getScreenWidth() / (bitmap.getWidth() * 1.0f), utils.getScreenWidth() / (bitmap.getWidth() * 1.0f)); //长和宽放大缩小的比例
                        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        imageView.setImageBitmap(resizeBmp);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        imageView.setImageResource(R.mipmap.default_pic);
                    }
                });
                llImgs.addView(imageView, i);
            }
        }
    }

    public void setData() {
        bundle = getIntent().getBundleExtra("info");
        getId = getIntent().getIntExtra("ID", 0);
        if (bundle != null) {
            groupArticle = (GroupArticle) bundle.getSerializable("group");
            userTitle.setNickName(groupArticle.getUsername());
            userTitle.setTime(groupArticle.getPubtime());
            userTitle.setUserAvatar(groupArticle.getUserimg());
            userTitle.setUserid(groupArticle.getUserid());


            tvNoteTitle.setText(groupArticle.getTitle());
            tvNoteContent.setText(groupArticle.getText());
            tvFollowersCount.setText(groupArticle.getZan());

            id = groupArticle.getId();

            refreshReproveBtn(iszan);
            ctime = groupArticle.getCtime();

        }

        Log.e("id",id+" "+ctime);
        reqCommentArticle(id, page);

    }

    /**
     * 请求帖子详情文章评论数据
     */
    private void reqCommentArticle(String id, final int page) {
        String url = getResources().getString(R.string.api_baseurl) + "wenzhang/PlList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + id + "*" + page);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                tempList = response.getList(NoteFirstReply.class);

                if (tempList != null && tempList.size() > 0) {
                    firstReplies.addAll(tempList);
                } else {
                    isCanScroll = false;
                }
                if (isUpLoad) {
                    isUpLoad = false;
                    llvFirstReply.refreshComplete();
                }
                firstReplyAdapter.notifyDataSetChanged();
                if (page == 1 && (tempList == null || tempList.size() == 0)) {
                    llEmptyView.setVisibility(View.VISIBLE);
                } else {
                    llEmptyView.setVisibility(View.GONE);
                }

            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                if (isUpLoad) {
                    isUpLoad = false;
                    llvFirstReply.refreshComplete();
                }

                int errorcode = response.getErrorcode();
                Log.e("errorcode评论", errorcode + " ");
                if (errorcode == 14) {
                    ToastUtils.show(NoteDetailActivity.this, "文章不存在或已被删除");
                } else {
                    ToastUtils.show(NoteDetailActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(NoteDetailActivity.this, msg);

            }
        });
    }

    private void bindData() {
        firstReplyAdapter = new NoteFirstReplyAdapter(this, firstReplies, this);
        llvFirstReply.setAdapter(firstReplyAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_right:
                if (is_sc!=null){
                    Log.e("errorcode详情", is_sc + " ");
                    moreDialog = new MoreDialog(this, this,is_sc);
                    moreDialog.show();
                }

                break;
            case R.id.btn_reply:
                if (Constant.user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("group", groupArticle);
                    bundle.putInt("replyType", 1);
                    bundle.putInt("notecom", 8);
                    utils.jumpAty(this, ReplyNoteActivity.class, bundle);
                    finish();
                } else {
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(this, LoginActivity.class, null);
                }
                break;
            case R.id.btn_reprove:
                if (!iszan) {
                    reqReprove(1, id);
                } else {
                    reqUnReprove(1, id);
                }
                break;
        }
    }

    /**
     * 请求取消点赞接口
     *
     * @param type  1：文章  2：评论
     * @param desId 表示点赞目标id，type为1，则为文章id，type为2，则为评论id
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

                ToastUtils.show(NoteDetailActivity.this, "取消成功！");
                if (type == 1) {
                    iszan = false;
                    refreshReproveBtn(iszan);
                } else {

                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();

                if (errorcode == 14) {
                    ToastUtils.show(NoteDetailActivity.this, "您处于未登录状态，请先登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(NoteDetailActivity.this, LoginActivity.class, null);
                } else if (errorcode == 15) {
                    ToastUtils.show(NoteDetailActivity.this, "记录不存在或已被删除");
                } else if (errorcode == 16) {
                    ToastUtils.show(NoteDetailActivity.this, "用户没有点过赞,不能取消");
                } else {
                    ToastUtils.show(NoteDetailActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(NoteDetailActivity.this, msg);
            }
        });

    }

    /**
     * 请求点赞接口
     *
     * @param type  1：文章  2：评论
     * @param desId 表示点赞目标id，type为1，则为文章id，type为2，则为评论id
     */
    private void reqReprove(final int type, String desId) {
        String url = getResources().getString(R.string.api_baseurl) + "wenzhang/AddZan.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + type + "*" + desId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                ToastUtils.show(NoteDetailActivity.this, "成功！");
                if (type == 1) {
                    iszan = true;
                    refreshReproveBtn(iszan);
                } else {

                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 14) {
                    ToastUtils.show(NoteDetailActivity.this, "您处于未登录状态，请先登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(NoteDetailActivity.this, LoginActivity.class, null);
                } else if (errorcode == 16) {
                    ToastUtils.show(NoteDetailActivity.this, "您已赞过，请勿重复操作");
                } else {
                    ToastUtils.show(NoteDetailActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(NoteDetailActivity.this, msg);
            }
        });

    }

    /**
     * 刷新底部点赞按钮状态
     *
     * @param isReproved true 点过赞
     */
    private void refreshReproveBtn(boolean isReproved) {

        if (isReproved) {
            ivReprove.setImageResource(R.mipmap.main_post_liked);
        } else {
            ivReprove.setImageResource(R.mipmap.praise_bottom);
        }
    }

    @Override
    public void moreClick(MoreDialog.Type type) {
        switch (type) {
            case COLLECT:
                moreDialog.dismiss();
                reqCollect();
                break;
            case SHARE:
                moreDialog.dismiss();
                shareDialog.show();
                break;
            case UNCOLLECT:
                moreDialog.dismiss();
                reqUnCollect();
                break;
            case CANCEL:
                moreDialog.dismiss();
        }
    }

    /**
     * 请求收藏的接口
     */
    private void reqCollect() {
        String url = getString(R.string.api_baseurl)+"user/AddSc.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+Constant.user.getUserid()+"*"+"1"+"*"+groupArticle.getId());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                ToastUtils.show(NoteDetailActivity.this, "收藏成功!");
                reqContentDetail();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if(errorcode==12||errorcode==13||errorcode==15){
                    ToastUtils.show(NoteDetailActivity.this, "用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(NoteDetailActivity.this,LoginActivity.class,null);
                }else if(errorcode==16){
                    ToastUtils.show(NoteDetailActivity.this,"内容不存在，可能已被删除");
                }else if(errorcode==17){
                    ToastUtils.show(NoteDetailActivity.this,"收藏失败，不能重复收藏");
                }else{
                    ToastUtils.show(NoteDetailActivity.this,"服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(NoteDetailActivity.this,msg);
            }
        });

    }
    /**
     * 请求取消收藏的接口
     */
    private void reqUnCollect() {
        String url = getString(R.string.api_baseurl)+"user/CancelSC.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+Constant.user.getUserid()+"*"+groupArticle.getId());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                ToastUtils.show(NoteDetailActivity.this,"取消收藏成功!");
                reqContentDetail();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                Log.e("errorcode",+errorcode+"");
                if(errorcode==12){
                    ToastUtils.show(NoteDetailActivity.this, "用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(NoteDetailActivity.this,LoginActivity.class,null);
                }else{
                    ToastUtils.show(NoteDetailActivity.this,"服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(NoteDetailActivity.this,msg);
            }
        });

    }
    @Override
    public void click(NoteReply reply, int type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", groupArticle);
        if (reply instanceof NoteFirstReply) {
            if (type == 1) {//查看全部回复
                bundle.putString("firstReplyId", ((NoteFirstReply) reply).getId());
                bundle.putString("wid",((NoteFirstReply) reply).getWid());
                utils.jumpAty(this, SecondAllReplyActivity.class, bundle);
            } else if (type == 2) {//点击回复按钮回复
                bundle.putInt("replyType", 2);
                bundle.putSerializable("reply", reply);
                bundle.putInt("notecom", 8);
                utils.jumpAty(this, ReplyNoteActivity.class, bundle);
                finish();
            } else if (type == 4) {//点赞或取消点赞

                if (((NoteFirstReply) reply).getStatus() == 0) {
                    reqReprove(2, ((NoteFirstReply) reply).getId());
                } else {
                    reqUnReprove(2, ((NoteFirstReply) reply).getId());

                }
            }
        } else if (reply instanceof SecondReply) {//点击二级评论列表item回复
            bundle.putInt("replyType", 3);
            bundle.putSerializable("reply", reply);
            bundle.putInt("notecom", 8);
            utils.jumpAty(this, ReplyNoteActivity.class, bundle);
            finish();
        }
    }

    ShareDialog.ShareClickListener shareListener  = new ShareDialog.ShareClickListener() {
        @Override
        public void shareClick(ShareDialog.Type type) {
            switch (type) {
                case WECHAT_FRIEND:
                    shareWechat(0);
                    break;
                case WECHAT_CIRCLE:
                    shareWechat(1);
                    break;
                case QQ:
                    shareQq();
                    break;

            }

        }
    };

    /**
     * 分享到微信
     */
    private void shareWechat(int type) {
        iwxapi = WXAPIFactory.createWXAPI(this, getResources().getString(R.string.wxappid), true);
        iwxapi.registerApp(getResources().getString(R.string.wxappid));

        if(iwxapi.isWXAppInstalled()){
            if(utils.isOnLine(this)){
                int wxVersion = iwxapi.getWXAppSupportAPI();
                if(wxVersion >= 21020001){
                    //分享后点击时进入的url
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = getString(R.string.qm_share_url);

                    Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                    WXMediaMessage msg = new WXMediaMessage();
                    msg.title = getString(R.string.qm_share_title);
                    msg.mediaObject = webpage;

                    msg.description = groupArticleDetail.getTitle();
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(img, 150, 150, true);

                    img.recycle();
                    msg.thumbData = utils.bmpToByteArray(thumbBmp, true);
                    //构造一个Req
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    if(type==1) {
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;  //发送至朋友圈
                    }else if(type==0) {
                        req.scene = SendMessageToWX.Req.WXSceneSession; // 默认（发送到消息会话）
                    }
                    // 调用api接口发送到微信
                    iwxapi.sendReq(req);

                }else{
                    ToastUtils.show(NoteDetailActivity.this,"您的微信版本过低,不支持此功能");
                }
            }else{
                ToastUtils.show(NoteDetailActivity.this, "请检查您的网络连接");
            }
        }else{
            ToastUtils.show(NoteDetailActivity.this, "请您先安装微信");
        }
    }
    /**
     * 分享到qq
     */
    private void shareQq(){
        mTencent = Tencent.createInstance(getString(R.string.qq_appid), this.getApplicationContext());
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, getString(R.string.qm_share_title));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  groupArticleDetail.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  getString(R.string.qm_share_url));
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,getString(R.string.qq_img_share_url));
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  getString(R.string.app_name));
        mTencent.shareToQQ(this, params, new IUiListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onComplete(Object o) {
            }

            @Override
            public void onError(UiError uiError) {
            }
        });
    }

}
