package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.MyNoteResponseAdapter;
import com.minfo.quanmei.adapter.MyPostActivityAdapter;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fei on 15/11/3.
 *
 * @funcation 我的日记
 */
public class MyDiaryActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_left_btn;
    private ImageView mydiary_invitation;
    private boolean isWriteDiary = true;
    private ArrayList<String> imgPaths = new ArrayList<>();//从相册选择的照片的路径
    private LinearLayout diaryLl;
    private LinearLayout nothingLi;
    private RefreshListView listView;
    private TextView mypost;
    private TextView myreply;
    private LinearLayout myDiary;
    private List<GroupArticle> diarys = new ArrayList<GroupArticle>();
    private List<GroupArticle> diary = new ArrayList<GroupArticle>();
    //下拉刷新变量
    private boolean load;
    private boolean refresh;
    private int page = 1;
    //我回复的
    private int responsePage = 1;
    private List<GroupArticle> responseList = new ArrayList<GroupArticle>();
    private List<GroupArticle> responseTempList = new ArrayList<GroupArticle>();
    private boolean responseLoad;
    private boolean responseRefresh;
    private MyNoteResponseAdapter myNoteResponseAdapter;
    private MyPostActivityAdapter myPostActivityAdapter;

    private boolean isResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_diary);
        inittitle();
    }

    private void inittitle() {
        View title = findViewById(R.id.my_diary_title);
        tv_title = (TextView) title.findViewById(R.id.tv_title);
        iv_left_btn = (ImageView) title.findViewById(R.id.iv_left);

        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("我的日记");
        iv_left_btn.setVisibility(View.VISIBLE);
        iv_left_btn.setOnClickListener(this);
    }

    @Override
    protected void findViews() {
        mydiary_invitation = (ImageView) findViewById(R.id.iv_mydiary_invitation);
        diaryLl = ((LinearLayout) findViewById(R.id.ll_diary));
        nothingLi = ((LinearLayout) findViewById(R.id.nothing_diary_layout));
        listView = ((RefreshListView) findViewById(R.id.my_diary_listview));
        mypost = (TextView) findViewById(R.id.tv_my_post);
        myreply = (TextView) findViewById(R.id.tv_my_reply);

    }

    @Override
    protected void initViews() {
        diaryLl.setOnClickListener(this);
        myreply.setOnClickListener(this);
        mypost.setOnClickListener(this);
        refreshview1();
        //reqMyDiary();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.ll_diary:
                //跳转到相册
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWriteDiary", isWriteDiary);
                bundle.putStringArrayList("imgPaths", imgPaths);
                utils.jumpAty(this, AlbumActivity.class, bundle);
                appManager.finishActivity();
                break;
            case R.id.tv_my_post:
                page = 1;
                diarys.clear();
                refreshview1();
                isResponse = false;
                break;
            case R.id.tv_my_reply:
                responsePage = 1;
                page = 1;
                responseList.clear();
                refreshview2();
                isResponse = true;
                break;
        }
    }

    private void refreshview1() {
        diaryLl.setVisibility(View.GONE);
        nothingLi.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        mypost.setBackgroundColor(getResources().getColor(R.color.basic_color));
        mypost.setTextColor(getResources().getColor(R.color.white));
        myreply.setBackgroundColor(getResources().getColor(R.color.white));
        myreply.setTextColor(getResources().getColor(R.color.black));

        myPostActivityAdapter = new MyPostActivityAdapter(this, diarys, "diary");
        listView.setAdapter(myPostActivityAdapter);
        reqMyDiary();
        listView.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                page = 1;
                reqMyDiary();
            }
        });
        listView.setIsCanLoad(true);
        listView.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                load = true;
                page++;
                reqMyDiary();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isResponse) {
                    Intent intentToDiaryDetail = new Intent(MyDiaryActivity.this, NoteDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("group", diarys.get(position - 1));
                    intentToDiaryDetail.putExtra("info", bundle);
                    startActivity(intentToDiaryDetail);
                }
            }
        });
    }

    private void refreshview2() {
        diaryLl.setVisibility(View.GONE);
        myreply.setBackgroundColor(getResources().getColor(R.color.basic_color));
        myreply.setTextColor(getResources().getColor(R.color.white));
        mypost.setBackgroundColor(getResources().getColor(R.color.white));
        mypost.setTextColor(getResources().getColor(R.color.black));
        myNoteResponseAdapter = new MyNoteResponseAdapter(MyDiaryActivity.this, responseList, "diary");
        listView.setAdapter(myNoteResponseAdapter);
        reqMyresponseData();

        listView.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                responseRefresh = true;
                responsePage = 1;
                reqMyresponseData();
            }
        });
        listView.setIsCanLoad(true);
        listView.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                responseLoad = true;
                responsePage++;
                reqMyresponseData();
            }
        });
    }

    /**
     * 请求我的回复数据
     */
    private void reqMyresponseData() {
        String url = getResources().getString(R.string.api_baseurl) + "user/HfList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + responsePage);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if (responseRefresh) {
                    listView.refreshComplete();
                    responseRefresh = false;
                    responseList.clear();
                }
                if (responseLoad) {
                    listView.loadComplete();
                    responseLoad = false;
                }
                responseTempList = response.getList(GroupArticle.class);
                if (responsePage == 1 && (responseTempList == null || responseTempList.size() == 0)) {
                    nothingLi.setVisibility(View.VISIBLE);
                }
                responseList.addAll(responseTempList);
                myNoteResponseAdapter.notifyDataSetChanged();
                responseTempList.clear();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                listView.loadComplete();
                listView.refreshComplete();
                responseLoad = false;
                responseRefresh = false;
            }

            @Override
            public void onRequestError(int code, String msg) {
                listView.loadComplete();
                listView.refreshComplete();
                responseLoad = false;
                responseRefresh = false;
                ToastUtils.show(MyDiaryActivity.this, msg);

            }
        });
    }

    /**
     * 我的日记接口
     */
    private void reqMyDiary() {
        String url = getResources().getString(R.string.api_baseurl) + "user/WzList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + 1 + "*" + page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {


                if (refresh) {
                    listView.refreshComplete();
                    refresh = false;
                    diarys.clear();
                }
                if (load) {
                    listView.loadComplete();
                    load = false;
                }
                diary = response.getList(GroupArticle.class);
                if (page == 1 && (diary == null || diary.size() == 0)) {
                    nothingLi.setVisibility(View.VISIBLE);
                }
                diarys.addAll(diary);
                myPostActivityAdapter.notifyDataSetChanged();
                diary.clear();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                listView.loadComplete();
                listView.refreshComplete();
                refresh = false;
                load = false;
            }

            @Override
            public void onRequestError(int code, String msg) {
                listView.loadComplete();
                listView.refreshComplete();
                refresh = false;
                load = false;
                ToastUtils.show(MyDiaryActivity.this, msg);
            }
        });
    }
}
