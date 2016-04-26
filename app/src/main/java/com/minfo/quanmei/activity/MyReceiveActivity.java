package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
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

public class MyReceiveActivity extends BaseActivity implements View.OnClickListener {

    private RefreshListView listView;
    private StartDiaryLVAdapter startDiaryLVAdapter;
    private List<GroupArticle> diarys = new ArrayList<>();
    private List<GroupArticle> totalDiarys = new ArrayList<GroupArticle>();
    private TextView countRe;

    private int page = 1;
    private boolean download = false;
    private boolean refresh = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_receive);
    }

    @Override
    protected void findViews() {
        TextView tvtitle = (TextView) findViewById(R.id.tv_title);

        ImageView tvleftbtn = (ImageView) findViewById(R.id.iv_left);
        listView = ((RefreshListView) findViewById(R.id.lv_receive_listview));
        countRe = ((TextView) findViewById(R.id.tv_receive));
        tvtitle.setVisibility(View.VISIBLE);
        tvtitle.setText("我的收藏");
        tvleftbtn.setVisibility(View.VISIBLE);
        tvleftbtn.setOnClickListener(this);
    }

    @Override
    protected void initViews() {

        startDiaryLVAdapter = new StartDiaryLVAdapter(this, totalDiarys);
        listView.setAdapter(startDiaryLVAdapter);
        listView.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                page = 1;
                reqMyCollect();
            }
        });
        listView.setIsCanLoad(true);
        listView.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                download = true;
                page++;
                reqMyCollect();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentToDiaryDetail = new Intent(MyReceiveActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", totalDiarys.get(position - 1));
                intentToDiaryDetail.putExtra("info", bundle);
                startActivity(intentToDiaryDetail);
            }
        });
        reqMyCollect();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
        }

    }

    public void reqMyCollect(){
        String url = getString(R.string.api_baseurl)+"user/ScList.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.user.getUserid()+"*"+page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if(refresh){
                    listView.refreshComplete();
                    refresh = false;
                    totalDiarys.clear();
                }
                if(download){
                    listView.loadComplete();
                    download = false;
                }
                diarys = response.getList(GroupArticle.class);
                totalDiarys.addAll(diarys);
                diarys.clear();
                startDiaryLVAdapter.notifyDataSetChanged();
                bindData();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {

                int errorcode = response.getErrorcode();
                if(errorcode==11||errorcode==12){
                    ToastUtils.show(MyReceiveActivity.this,"您还未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(MyReceiveActivity.this, LoginActivity.class, null);
                }else if(errorcode==16){
                    ToastUtils.show(MyReceiveActivity.this,"您收藏的信息不存在，可能已被删除");
                }else{
                    ToastUtils.show(MyReceiveActivity.this,"服务器繁忙");
                }
                listView.refreshComplete();
                listView.loadComplete();
            }

            @Override
            public void onRequestError(int code, String msg) {
                listView.refreshComplete();
                listView.loadComplete();
                ToastUtils.show(MyReceiveActivity.this,msg);
            }
        });
    }


    private void bindData() {
        countRe.setText("共" + totalDiarys.size() + "条收藏");


    }
}
