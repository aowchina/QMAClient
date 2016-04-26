package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.GroupPullAdapter;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LoadingDialog;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecialDiaryActivity extends BaseActivity implements View.OnClickListener {

    //top
    private TextView tvTitle;
    private ImageView ivLeft;

    private RefreshListView rflDiary;
    private List<GroupArticle> list = new ArrayList<>();
    private List<GroupArticle> tempList = new ArrayList<>();
    private boolean isLoading;
    private boolean isRefreshing;
    private GroupPullAdapter diaryAdapter;
    private LoadingDialog loadingDialog;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_special_diary);
    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setText("日记精选");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        rflDiary = (RefreshListView) findViewById(R.id.rfl_special_diary);
        rflDiary.setIsCanLoad(true);
        rflDiary.setIsCanRefresh(true);
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void initViews() {
        diaryAdapter = new GroupPullAdapter(this,list);
        rflDiary.setAdapter(diaryAdapter);
        rflDiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToNoteDetail = new Intent(SpecialDiaryActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", list.get(position - 1));
                intentToNoteDetail.putExtra("ID", 1);
                intentToNoteDetail.putExtra("info", bundle);
                startActivity(intentToNoteDetail);
            }
        });
        reqData();
        rflDiary.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                page++;
                isLoading = true;
                reqData();
            }
        });
        rflDiary.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefreshing = true;
                reqData();

            }
        });
    }

    private void reqData() {
        String url = getString(R.string.api_baseurl)+ "wenzhang/List.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr() + "*" + 2 + "*" + 0 + "*" + 0 + "*" + page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                if(!isRefreshing&&page==1){
                    loadingDialog.show();
                }
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                if (isRefreshing) {
                    list.clear();
                }
                tempList = response.getList(GroupArticle.class);
                if (tempList != null && tempList.size() > 0) {
                    list.addAll(tempList);
                    diaryAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        ToastUtils.show(SpecialDiaryActivity.this, "无相关数据");
                        if (isRefreshing) {
                            ToastUtils.show(SpecialDiaryActivity.this, "数据刷新完毕");
                        }
                    } else {
                        if (isLoading) {
                            ToastUtils.show(SpecialDiaryActivity.this, "数据加载完毕");
                        }
                    }
                }
                isRefreshing = false;
                isLoading = false;
                rflDiary.loadComplete();
                rflDiary.refreshComplete();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                isLoading = false;
                isRefreshing = false;
                rflDiary.loadComplete();
                rflDiary.refreshComplete();
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                isLoading = false;
                isRefreshing = false;
                rflDiary.loadComplete();
                rflDiary.refreshComplete();
                ToastUtils.show(SpecialDiaryActivity.this, msg);
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }
}
