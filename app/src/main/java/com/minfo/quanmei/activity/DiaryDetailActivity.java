package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.DiaryFirstReplyAdapter;
import com.minfo.quanmei.adapter.NoteFirstReplyAdapter;
import com.minfo.quanmei.entity.Diary;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.NoteFirstReply;
import com.minfo.quanmei.entity.NoteReply;
import com.minfo.quanmei.entity.StartDiary;
import com.minfo.quanmei.widget.LLUserTitle;
import com.minfo.quanmei.widget.LimitListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 日记详情页 by liujing
 */
public class DiaryDetailActivity extends BaseActivity implements View.OnClickListener, NoteFirstReplyAdapter.ClickListener {

    //top
    private TextView tvTitle;
    private ImageView ivLeft;
    private TextView tvRight;

    private LLUserTitle userTitle;//作者
    private TextView tvDiaryTitle;//日记标题
    private TextView tvHosName;//医院名称
    private TextView tvPrice;//花费
    private TextView tvLikeNum;//点赞数
    private TextView tvReplyNum;//回复数


    private LimitListView llvFirstReply;
    private LimitListView llvAllReply;

    private DiaryFirstReplyAdapter diaryFirstReplyAdapter;//更新列表

    private NoteFirstReplyAdapter allFirstReplyAdapter;

    private Diary diary;
    private StartDiary startDiary;
    private GroupArticle groupArticle;
    private List<NoteFirstReply> allFirstReplies = new ArrayList<>();
    private Bundle bundle;
    private int getId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvRight.setText("更多");
        tvTitle.setText("详情");
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        userTitle = (LLUserTitle) findViewById(R.id.ll_user_title);
        tvDiaryTitle = (TextView) findViewById(R.id.tv_diary_title);
        tvHosName = (TextView) findViewById(R.id.tv_hos_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvLikeNum = (TextView) findViewById(R.id.tv_like_num);
        tvReplyNum = (TextView) findViewById(R.id.tv_all_reply);
        llvFirstReply = (LimitListView) findViewById(R.id.llv_first_reply);
        llvAllReply = (LimitListView) findViewById(R.id.llv_all_reply);
    }

    @Override
    protected void initViews() {
//        initDiary();
        bundle = getIntent().getBundleExtra("info");
        getId=getIntent().getIntExtra("ID", 0);
        String username = "";
        String time = "";
        String usericon = "";
        String title = "";
        String text = "";


        if (getId==1){
            if (bundle != null) {
                groupArticle = (GroupArticle)bundle.getSerializable("GROUP");
                userTitle.setNickName(groupArticle.getPlusername());
                userTitle.setTime(groupArticle.getCtime() + "更新");
                userTitle.setUserAvatar(groupArticle.getUserimg());
                tvDiaryTitle.setText(groupArticle.getTitle());
                tvHosName.setText(diary.hosName);
                tvPrice.setText(diary.price);
                tvLikeNum.setText(diary.likeNum + "人觉得有用");
                tvReplyNum.setText("全部回帖(" + diary.replyNum + ")");
            }
        }else if (getId==2){
            if (bundle != null) {
                startDiary = (StartDiary) bundle.getSerializable("START");
                userTitle.setNickName(startDiary.getPlusername());
                userTitle.setTime(startDiary.getCtime() + "更新");
                userTitle.setUserAvatar(startDiary.getPluserimg());
                tvDiaryTitle.setText(startDiary.getTitle());
                //tvHosName.setText(startDiary.getPlusername());
                //tvPrice.setText(diary.price);
                //tvLikeNum.setText(diary.likeNum + "人觉得有用");
                //tvReplyNum.setText("全部回帖(" + diary.replyNum + ")");
            }
        }




        bindData();


    }

    private void bindData() {
        //更新列表
        diaryFirstReplyAdapter = new DiaryFirstReplyAdapter(this, diary.updateList);
        llvFirstReply.setAdapter(diaryFirstReplyAdapter);
        llvFirstReply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(DiaryDetailActivity.this, NoteDetailActivity.class);
                Bundle bundleTo=new Bundle();
                if (getId==1){
                    bundleTo.putSerializable("group",groupArticle);
                    intent.putExtra("ID",1);


                }else if (getId==2){
                    bundleTo.putSerializable("start",startDiary);
                    intent.putExtra("ID",2);

                }
                intent.putExtra("info",bundleTo);
                startActivity(intent);
            }
        });

        //全部回帖列表
        for (int i = 0; i < diary.updateList.size(); i++) {
            allFirstReplies.addAll(diary.updateList.get(i).updateItemContent.firstReplies);
        }
        allFirstReplyAdapter = new NoteFirstReplyAdapter(this, allFirstReplies, this);
        llvAllReply.setAdapter(allFirstReplyAdapter);
    }

    /*
    private void initDiary() {
        diary = new Diary();
        diary.title = "玻尿酸越来越自然了";
        diary.nickName = "小小怪";
        diary.replyNum = 125;
        diary.likeNum = 271;
        diary.time = "05月27日";
        diary.hosName = "北京精艺医院";
        diary.price = "6000-1万";
        List<DiaryUpdateItem> updateItemList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DiaryUpdateItem updateItem = new DiaryUpdateItem();
            updateItem.updateDate = "5月24日";

            Note updateContent = new Note();
            updateContent.noteContent = "从小到大，鼻子都是一个不满意的地方，很短，没鼻梁，没鼻尖，鼻头圆，一直想能有所改变";
            updateContent.likeNum = 149;
            updateContent.replyNum = 30;
            List<NoteFirstReply> firstReplies = new ArrayList<>();

            for (int j = 0; j < 5; j++) {
                NoteFirstReply firstReply = new NoteFirstReply();
                firstReply.nickName = "顾装快乐";
                firstReply.firstReplyContent = "术前就这么漂亮，那做完以后会是什么样子";
                firstReply.time = "3小时以前";
                List<SecondReply> secondReplies = new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    SecondReply secondReply = new SecondReply();
                    secondReply.replyContent = "我也不想熬夜啊我也不想熬夜啊我也不想熬夜啊我也不想熬夜啊我也不想熬夜啊";
                    secondReply.replyer = "爱吃鱼的喵";
                    secondReply.replyeder = "高冷逗霸";
                    secondReply.time = "2小时以前";
                    secondReplies.add(secondReply);
                }
                firstReply.secondReplies = secondReplies;
                firstReplies.add(firstReply);
            }
            updateContent.firstReplies = firstReplies;
            updateItem.updateItemContent = updateContent;
            updateItemList.add(updateItem);
        }
        diary.updateList = updateItemList;

    }
    */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                onBackPressed();
                break;
            case R.id.iv_left:
                break;
        }
    }

    @Override
    public void click(NoteReply noteFirstReply,int type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("firstReply",noteFirstReply);

        utils.jumpAty(this, SecondAllReplyActivity.class, bundle);
    }
}
