package com.minfo.quanmei.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.HospitalCommentAdapter;
import com.minfo.quanmei.entity.HospitalData;
import com.minfo.quanmei.entity.HospitalPJ;
import com.minfo.quanmei.entity.NoteFirstReply;
import com.minfo.quanmei.entity.NoteReply;
import com.minfo.quanmei.entity.SecondReply;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.CircleRateView;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.PullScrollView;
import com.minfo.quanmei.widget.StatscsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 医院评论页
 * 2015年8月25日
 * zhang jiachang
 */
public class HospitalCommentActivity extends BaseActivity implements View.OnClickListener, HospitalCommentAdapter.ClickListener {

    private LimitListView listView;
    private HospitalCommentAdapter hospitalCommentAdapter;

    private PullScrollView scroll;
    private float[] commentData = new float[]{0, 0, 0};
    private StatscsView statscsView;
    private View line;
    private ImageView back;
    private TextView reputation;
    private String[] name = {"冷收场", "浮生戈畔", "余生", "共我童话", "最动心", "最后依归", "转眼多年", "独有你"};

    private CircleRateView cicleRateView;
    private RelativeLayout pRelative;

    private LinearLayout allComment;
    private LinearLayout goodComment;
    private LinearLayout middleComment;
    private LinearLayout badComment;

    private TextView allCommentNum;
    private TextView goodCommentNum;
    private TextView middleCommentNum;
    private TextView badCommentNum;

    private TextView goodCommentTv;
    private TextView allCommentTv;
    private TextView badCommentTv;
    private TextView middleCommentTv;


    private List<HospitalPJ> commentList = new ArrayList<>();
    private List<NoteFirstReply> tempList = new ArrayList<>();
    private String userid = "0";
    private String hospitalId;
    private HospitalData hospitalData;
    //top
    private TextView tvTitle;
    private ImageView ivLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setText("评价详情");
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);

        View v = LayoutInflater.from(this).inflate(R.layout.comment_headview, null);
        listView = ((LimitListView) v.findViewById(R.id.lv_comment));
        scroll = ((PullScrollView) findViewById(R.id.psl_hospitalcomment));
        reputation = ((TextView) v.findViewById(R.id.tv_reputation_comment));
        statscsView = (StatscsView) v.findViewById(R.id.statscsView1);
        pRelative = ((RelativeLayout) v.findViewById(R.id.rl_comment));
        cicleRateView = ((CircleRateView) v.findViewById(R.id.crv_reputation));
        allComment = ((LinearLayout) v.findViewById(R.id.ll_all_comment));
        goodComment = ((LinearLayout) v.findViewById(R.id.ll_good_comment));
        middleComment = ((LinearLayout) v.findViewById(R.id.ll_middle_comment));
        badComment = ((LinearLayout) v.findViewById(R.id.ll_bad_comment));
        allCommentNum = ((TextView) v.findViewById(R.id.tv_sumcount_num));
        goodCommentNum = ((TextView) v.findViewById(R.id.tv_goodcount_num));
        middleCommentNum = ((TextView) v.findViewById(R.id.tv_normalcount_num));
        badCommentNum = ((TextView) v.findViewById(R.id.tv_badcount_num));
        goodCommentTv = ((TextView) v.findViewById(R.id.tv_goodcount));
        allCommentTv = ((TextView) v.findViewById(R.id.tv_sumcount));
        badCommentTv = ((TextView) v.findViewById(R.id.tv_badcount));
        middleCommentTv = ((TextView) v.findViewById(R.id.tv_normalcount));
        cicleRateView.setAngle(80);

        statscsView.setData(commentData);

        scroll.addBodyView(v);
        scroll.setfooterViewGone();
    }

    @Override
    protected void initViews() {

        hospitalId = getIntent().getStringExtra("ID");
        hospitalData = (HospitalData) getIntent().getSerializableExtra("hospitalData");
        if (hospitalData != null) {
            setCommentValue();
        }

        allComment.setOnClickListener(this);
        goodComment.setOnClickListener(this);
        middleComment.setOnClickListener(this);
        badComment.setOnClickListener(this);
        allCommentData();
        scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        scroll.setheaderViewReset();


                        //hospitalCommentAdapter.addAll(list);
                        ToastUtils.show(HospitalCommentActivity.this, "刷新成功", Toast.LENGTH_SHORT);
                    }

                }, 2000);
            }
        });


    }

    private void setCommentValue() {
        int ratio = Integer.parseInt(hospitalData.getHp().substring(0, hospitalData.getHp().length() - 1));
        cicleRateView.setAngle(ratio);
        reputation.setText(hospitalData.getHp());
        commentData[0] = Float.parseFloat(hospitalData.getSm())*10;
        commentData[1] = Float.parseFloat(hospitalData.getFw())*10;
        commentData[2] = Float.parseFloat(hospitalData.getHj())*10;
        statscsView.setData(commentData);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.ll_all_comment:
                allCommentData();
                break;
            case R.id.ll_good_comment:
                goodCommentData();
                break;
            case R.id.ll_middle_comment:
                middleCommentData();
                break;
            case R.id.ll_bad_comment:
                badCommentData();
                break;
        }
    }

    private void allCommentData() {
        setCommentData();
        allCommentNum.setTextColor(getResources().getColor(R.color.basic_color));
        allCommentTv.setTextColor(getResources().getColor(R.color.basic_color));
        if (Constant.user != null) {
            userid = Constant.user.getUserid() + "";
        }
        reqHospitalComment(1);


    }

    private void goodCommentData() {
        setCommentData();
        goodCommentNum.setTextColor(getResources().getColor(R.color.basic_color));
        goodCommentTv.setTextColor(getResources().getColor(R.color.basic_color));
        reqHospitalComment(2);

    }

    private void middleCommentData() {
        setCommentData();
        middleCommentNum.setTextColor(getResources().getColor(R.color.basic_color));
        middleCommentTv.setTextColor(getResources().getColor(R.color.basic_color));
        reqHospitalComment(3);

    }

    private void badCommentData() {
        setCommentData();
        badCommentNum.setTextColor(getResources().getColor(R.color.basic_color));
        badCommentTv.setTextColor(getResources().getColor(R.color.basic_color));
        reqHospitalComment(4);

    }

    private void setCommentData() {
        allCommentNum.setTextColor(Color.GRAY);
        allCommentTv.setTextColor(Color.GRAY);
        goodCommentNum.setTextColor(Color.GRAY);
        goodCommentTv.setTextColor(Color.GRAY);
        middleCommentNum.setTextColor(Color.GRAY);
        middleCommentTv.setTextColor(Color.GRAY);

        badCommentNum.setTextColor(Color.GRAY);
        badCommentTv.setTextColor(Color.GRAY);
    }

    @Override
    public void click(NoteReply reply, int type) {
        Bundle bundle = new Bundle();
        //bundle.putSerializable("group", groupArticle);
        if (reply instanceof NoteFirstReply) {
            if (type == 1) {//查看全部回复
                bundle.putSerializable("firstReply", reply);
                bundle.putInt("hoscom", 10);
                utils.jumpAty(this, SecondAllReplyActivity.class, bundle);
                finish();
            } else if (type == 2) {//点击回复按钮回复
                bundle.putInt("replyType", 2);
                bundle.putInt("hoscom", 10);
                bundle.putSerializable("reply", reply);
                utils.jumpAty(this, ReplyNoteActivity.class, bundle);
                finish();

            }
        } else if (reply instanceof SecondReply) {//点击二级评论列表item回复
            bundle.putInt("replyType", 3);
            bundle.putSerializable("reply", reply);
            bundle.putInt("hoscom", 10);
            utils.jumpAty(this, ReplyNoteActivity.class, bundle);
            finish();
        }
    }

    /**
     * 请求帖子详情文章评论数据
     */
    private void reqHospitalComment(int type) {
        String url = getResources().getString(R.string.api_baseurl) + "hospital/PjList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + hospitalId + "*" + type);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                commentList = response.getList(HospitalPJ.class);

                bindData();
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(HospitalCommentActivity.this, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(HospitalCommentActivity.this, msg);

            }
        });
    }

    private void bindData() {
        hospitalCommentAdapter = new HospitalCommentAdapter(this, commentList, this);
        listView.setAdapter(hospitalCommentAdapter);

    }
}
