package com.minfo.quanmei.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.entity.MyPocket;
import com.minfo.quanmei.entity.Record;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PocketActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvTitle;
    private ImageView ivLeft;

    private TextView tvPoint;
    private View headerView;
    private RefreshListView lvPoint;

    private List<Record> list = new ArrayList<>();
    private List<Record> tempList = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;
    private boolean isLoad;
    private RecordAdapter recordAdapter;
    private MyPocket myPocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket);
    }

    @Override
    protected void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setText("我的钱包");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_point_head,null);
        tvPoint = (TextView) headerView.findViewById(R.id.tv_point);
        lvPoint = (RefreshListView) findViewById(R.id.lv_point);
        lvPoint.addHeaderView(headerView);
        recordAdapter = new RecordAdapter(this,list,R.layout.item_grade_record);
        lvPoint.setAdapter(recordAdapter);
        lvPoint.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                isLoad = true;
                page++;
                reqRecord();
            }
        });
        lvPoint.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                reqRecord();
            }
        });

    }

    @Override
    protected void initViews() {

        reqRecord();
    }

    /**
     * 获取积分记录
     */
    private void reqRecord() {
        String url = getString(R.string.api_baseurl)+"user/PointList.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+utils.getUserid()+"*"+page);
        httpClient.post(url,params,R.string.loading_msg,new RequestListener(){
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                if(isRefresh){
                    lvPoint.refreshComplete();
                    isRefresh = false;
                    list.clear();
                }
                if(isLoad){
                    lvPoint.loadComplete();
                    isLoad = false;
                }
                myPocket = response.getObj(MyPocket.class);
                tvPoint.setText(myPocket.getPoint());
                tempList = myPocket.getList();
                list.addAll(tempList);
                tempList.clear();
                recordAdapter.notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==11||errorcode==12){
                    ToastUtils.show(PocketActivity.this,"用户未登录");
                }else{
                    ToastUtils.show(PocketActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(PocketActivity.this,msg);
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

    class RecordAdapter extends CommonAdapter<Record> {
        public RecordAdapter(Context context, List<Record> datas, int layoutItemId) {
            super(context, datas, layoutItemId);
        }

        @Override
        public void convert(BaseViewHolder helper, Record item, int position) {
            int type = Integer.parseInt(item.getType());
            if(type==1){
                helper.setText(R.id.tv_content,""+item.getYear()+"-"+item.getMonth()+"-"+item.getDay()+" "+"签到");
                helper.setImageResource(R.id.iv_type,R.mipmap.item_sign);
            }else if(type==2){
                helper.setText(R.id.tv_content,item.getOrder_num());
                helper.setImageResource(R.id.iv_type,R.mipmap.item_pocket);
            }
            if(item.getStatus().equals("1")) {
                helper.setText(R.id.tv_item_grade, "+" + item.getPoint());
            }else{
                helper.setText(R.id.tv_item_grade, "-" + item.getPoint());
            }

            helper.setText(R.id.tv_week,item.getWeek());
            helper.setText(R.id.tv_date,item.getMonth()+"-"+item.getDay());

        }
    }


}
