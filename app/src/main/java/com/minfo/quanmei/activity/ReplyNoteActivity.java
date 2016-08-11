package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.NoteFirstReply;
import com.minfo.quanmei.entity.SecondReply;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LoadingDialog;

import java.util.Map;

public class ReplyNoteActivity extends BaseActivity implements View.OnClickListener {

    //top
    private TextView tvTitle;
    private TextView tvRight;
    private ImageView ivLeft;

    private EditText etReply;
    private String fromClass;
    private String  publishUserid;//文章发布者userid
    private String repliedUserid = "0";//被评论者userid
    private String articleId;//文章id
    private String parentCommentId;//父级评论id

    private GroupArticle groupArticle;

    private NoteFirstReply noteFirstReply;
    private SecondReply secondReply;

    private int replyType;
    private int allReply = 0;
    private LoadingDialog loadingDialog;


    private String strReply;//评论内容
    private int hoscom;
    private int commentRes;
    private int notecomres;
    private int hoscomres;
    private int notecom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_note);
    }

    @Override
    protected void findViews() {
        //top
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setText("回复");
        tvRight.setVisibility(View.VISIBLE);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        etReply = (EditText) findViewById(R.id.et_reply);
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void initViews() {
        publishUserid = utils.getUserid()+"";
        Bundle bundle = getIntent().getBundleExtra("info");
        if(bundle!=null) {
            groupArticle = (GroupArticle) bundle.getSerializable("group");
            replyType = bundle.getInt("replyType");
            hoscom = bundle.getInt("hoscom");
            notecomres = bundle.getInt("notecomres");
            hoscomres = bundle.getInt("hoscomres");
            notecom = bundle.getInt("notecom");
            allReply = bundle.getInt("allReply",0);
            if(replyType==1) {//一级回复
                articleId = groupArticle.getId();
//                repliedUserid = groupArticle.getUserid();
            }else if(replyType==2){//点击回复按钮回复
                noteFirstReply = (NoteFirstReply) bundle.getSerializable("reply");
                repliedUserid = noteFirstReply.getUserid();
                articleId = noteFirstReply.getWid();//文章id
                parentCommentId = noteFirstReply.getId();//父级评论id

            }else if(replyType==3){//点击二级评论回复
                secondReply = (SecondReply) bundle.getSerializable("reply");
                repliedUserid = secondReply.getUserid();
                articleId = secondReply.getWid();
                parentCommentId = secondReply.getParentId();
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_right:
                if(checkInput()){
                    reqReply();
                }
                break;
        }
    }

    /**
     * 请求回复接口
     */
    private void reqReply() {

        String url = getResources().getString(R.string.api_baseurl)+"wenzhang/AddPl.php";
        strReply = utils.convertChinese(strReply);
        if(publishUserid!=null&&publishUserid.equals(repliedUserid)){
            repliedUserid="0";
        }
        String strParams = utils.getBasePostStr()+"*"+publishUserid+"*"+repliedUserid+"*"+articleId+"*"+parentCommentId+"*"+strReply;
        Map<String,String> params = utils.getParams(strParams);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", groupArticle);
                bundle.putInt("ID", 1);
                if (hoscom == 10) {
                    utils.jumpAty(ReplyNoteActivity.this, HospitalCommentActivity.class, bundle);
                }
                if (notecom == 8) {
                    if(allReply==1){//表示从二级全部回复列表跳转过来的
                        if(replyType==3) {
                            bundle.putString("firstReplyId", secondReply.getParentId());
                        }else if(replyType==2){
                            bundle.putString("firstReplyId", noteFirstReply.getId());
                            bundle.putString("wid",noteFirstReply.getWid());
                        }
                        SecondAllReplyActivity.instance.finish();
                        utils.jumpAty(ReplyNoteActivity.this, SecondAllReplyActivity.class, bundle);
                    }else {
                        NoteDetailActivity.instance.finish();
                        utils.jumpAty(ReplyNoteActivity.this, NoteDetailActivity.class, bundle);
                    }
                }
                finish();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if(errorcode==16){
                    ToastUtils.show(ReplyNoteActivity.this,"用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(ReplyNoteActivity.this, LoginActivity.class, null);
                }else if(errorcode==17){
                    ToastUtils.show(ReplyNoteActivity.this,"文章不存在或已被删除");
                }else{
                    ToastUtils.show(ReplyNoteActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(ReplyNoteActivity.this, msg);
            }
        });

    }

    private boolean checkInput() {
        strReply = etReply.getText().toString();
        if(TextUtils.isEmpty(strReply)){
            ToastUtils.show(this,"回复内容不能为空");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Bundle bundle = new Bundle();
        bundle.putSerializable("group", groupArticle);
        bundle.putInt("ID", 1);
        if (hoscom==10){
            utils.jumpAty(ReplyNoteActivity.this, HospitalCommentActivity.class, bundle);
        }

        if (notecom==8){
            if(allReply==1){
                bundle.putString("firstReplyId",noteFirstReply.getId());
                utils.jumpAty(ReplyNoteActivity.this, SecondAllReplyActivity.class, bundle);
            } else {
                utils.jumpAty(ReplyNoteActivity.this, NoteDetailActivity.class, bundle);
            }
        }
        finish();*/
    }
}
