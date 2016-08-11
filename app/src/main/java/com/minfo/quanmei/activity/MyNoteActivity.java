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
import com.minfo.quanmei.adapter.MyPostAdapter;
import com.minfo.quanmei.adapter.StartDiaryLVAdapter;
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
 */
public class MyNoteActivity extends BaseActivity implements View.OnClickListener {
    private TextView mypost;
    private TextView myreply;
    private LinearLayout nothing_layout;
    private RefreshListView post_listview;
    private MyPostAdapter myPostAdapter;
    private StartDiaryLVAdapter startDiaryLVAdapter;
    private List<GroupArticle> diarys = new ArrayList<GroupArticle>();
    private List<GroupArticle> diary = new ArrayList<GroupArticle>();
    //下拉刷新变量
    private boolean load;
    private boolean refresh;
    private int page = 1;
    //我回复的
    private int responsePage = 1;
    private boolean responseRefresh;
    private boolean responseLoad;
    private List<GroupArticle> responseList = new ArrayList<GroupArticle>();
    private List<GroupArticle> responseTempList = new ArrayList<GroupArticle>();
    private boolean downLoadResponse;
    private boolean upLoadResponse;
    private MyNoteResponseAdapter myNoteResponseAdapter;
    private MyPostActivityAdapter myPostActivityAdapter;

    private boolean isResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post);
        //视图一进页面进行我的帖子和回复进行调接口,然后获取数据,
    }

    @Override
    protected void findViews() {
        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setVisibility(View.VISIBLE);
        tvtitle.setText("我的帖子");
        ImageView tvleftbtn = (ImageView) findViewById(R.id.iv_left);
        tvleftbtn.setVisibility(View.VISIBLE);
        tvleftbtn.setOnClickListener(this);

        mypost = (TextView) findViewById(R.id.tv_my_post);
        myreply = (TextView) findViewById(R.id.tv_my_reply);
        nothing_layout = (LinearLayout) findViewById(R.id.nothing_layout);
        post_listview = (RefreshListView) findViewById(R.id.my_post_listview);
    }

    @Override
    protected void initViews() {
        mypost.setOnClickListener(this);
        myreply.setOnClickListener(this);

        refreshview1();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
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
        mypost.setBackgroundColor(getResources().getColor(R.color.basic_color));
        mypost.setTextColor(getResources().getColor(R.color.white));
        myreply.setBackgroundColor(getResources().getColor(R.color.white));
        myreply.setTextColor(getResources().getColor(R.color.black));
        nothing_layout.setVisibility(View.GONE);
        myPostActivityAdapter = new MyPostActivityAdapter(this, diarys, "note");
        post_listview.setAdapter(myPostActivityAdapter);
        reqMyNote();

        post_listview.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                page = 1;
                reqMyNote();
            }
        });
        post_listview.setIsCanLoad(true);
        post_listview.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                load = true;
                page++;
                reqMyNote();
                post_listview.loadComplete();
            }
        });


        post_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isResponse) {
                    Intent intentToDiaryDetail = new Intent(MyNoteActivity.this, NoteDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("group", diarys.get(position - 1));
                    intentToDiaryDetail.putExtra("info", bundle);
                    startActivity(intentToDiaryDetail);
                }
            }
        });
    }

    private void refreshview2() {
        myreply.setBackgroundColor(getResources().getColor(R.color.basic_color));
        myreply.setTextColor(getResources().getColor(R.color.white));
        mypost.setBackgroundColor(getResources().getColor(R.color.white));
        mypost.setTextColor(getResources().getColor(R.color.black));
        myNoteResponseAdapter = new MyNoteResponseAdapter(MyNoteActivity.this, responseList, "note");
        post_listview.setAdapter(myNoteResponseAdapter);
        reqMyresponseData();

        post_listview.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                responseRefresh = true;
                responsePage = 1;
                reqMyresponseData();
            }
        });
        post_listview.setIsCanLoad(true);
        post_listview.setLoadListener(new RefreshListView.ILoadListener() {
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
                    post_listview.refreshComplete();
                    responseRefresh = false;
                    responseList.clear();
                }
                if (responseLoad) {
                    post_listview.loadComplete();
                    responseLoad = false;
                }
                responseTempList = response.getList(GroupArticle.class);
                if (responsePage == 1 && (responseTempList == null || responseTempList.size() == 0)) {
                    nothing_layout.setVisibility(View.VISIBLE);
                }
                responseList.addAll(responseTempList);
                myNoteResponseAdapter.notifyDataSetChanged();
                responseTempList.clear();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                post_listview.loadComplete();
                post_listview.refreshComplete();
                responseLoad = false;
                responseRefresh = false;
            }

            @Override
            public void onRequestError(int code, String msg) {
                post_listview.loadComplete();
                post_listview.refreshComplete();
                responseLoad = false;
                responseRefresh = false;
                ToastUtils.show(MyNoteActivity.this, msg);

            }
        });
    }

    /**
     * 我的帖子接口
     */
    private void reqMyNote() {
        String url = getResources().getString(R.string.api_baseurl) + "user/WzList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + 2 + "*" + page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                if (refresh) {
                    post_listview.refreshComplete();
                    refresh = false;
                    diarys.clear();
                }
                if (load) {
                    post_listview.loadComplete();
                    load = false;
                }
                diary = response.getList(GroupArticle.class);
                if (page == 1 && (diary == null || diary.size() == 0)) {
                    nothing_layout.setVisibility(View.VISIBLE);
                }
                diarys.addAll(diary);
                myPostActivityAdapter.notifyDataSetChanged();
                diary.clear();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                post_listview.loadComplete();
                post_listview.refreshComplete();
                refresh = false;
                load = false;
            }

            @Override
            public void onRequestError(int code, String msg) {
                post_listview.loadComplete();
                post_listview.refreshComplete();
                refresh = false;
                load = false;
                ToastUtils.show(MyNoteActivity.this, msg);
            }
        });
    }
}
