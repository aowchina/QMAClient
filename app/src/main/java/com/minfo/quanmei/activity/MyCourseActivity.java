package com.minfo.quanmei.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.entity.Course;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCourseActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivLeft;
    private RefreshListView rflCourse;
    private List<Course> list = new ArrayList<>();
    private List<Course> tempList = new ArrayList<>();
    private CourseAdapter courseAdapter;

    private int page = 1;
    private boolean isFreshing;
    private boolean isLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_course);
    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvTitle.setText("我的课程");
        rflCourse = (RefreshListView) findViewById(R.id.rfl_course);
        rflCourse.setIsCanLoad(true);
        rflCourse.setIsCanRefresh(true);
        rflCourse.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isFreshing = true;
                reqData();
            }
        });
        rflCourse.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                page++;
                isLoading = true;
                reqData();
            }
        });
        courseAdapter = new CourseAdapter(this,list,R.layout.item_course);
        rflCourse.setAdapter(courseAdapter);
        rflCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("teacherid",list.get(position-1).getTeacherid());
                utils.jumpAty(MyCourseActivity.this,CourseActivity.class,bundle);
            }
        });


    }

    @Override
    protected void initViews() {
        if(utils.isOnLine(this)) {
            reqData();
        }else{
            ToastUtils.show(this,"暂时无网络");
        }
    }

    /**
     * 请求我的订单列表数据
     */
    private void reqData() {
        String url = getString(R.string.api_baseurl)+"user/CourseList.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.user.getUserid()+"*"+page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                if(isFreshing){
                    rflCourse.refreshComplete();
                    isFreshing = false;
                    list.clear();
                }
                if(isLoading){
                    rflCourse.loadComplete();
                    isLoading = false;
                }
                tempList = response.getList(Course.class);
                list.addAll(tempList);
                tempList.clear();
                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==11||errorcode==12){
                    ToastUtils.show(MyCourseActivity.this,"用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(MyCourseActivity.this,LoginActivity.class,null);
                }else{
                    ToastUtils.show(MyCourseActivity.this,"服务器繁忙");
                }
                rflCourse.loadComplete();
                rflCourse.refreshComplete();
                isFreshing = false;
                isLoading = false;
            }

            @Override
            public void onRequestError(int code, String msg) {
                rflCourse.loadComplete();
                rflCourse.refreshComplete();
                isFreshing = false;
                isLoading = false;
                ToastUtils.show(MyCourseActivity.this,msg);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_left){
            onBackPressed();
        }
    }

    class CourseAdapter extends CommonAdapter<Course>{

        public CourseAdapter(Context context, List<Course> datas, int layoutItemId) {
            super(context, datas, layoutItemId);
        }

        @Override
        public void convert(BaseViewHolder helper, Course item, int position) {
            helper.setText(R.id.tv_course_name,item.getName()+"×"+item.getAmount());
            helper.setText(R.id.tv_order_num,item.getOrderid());
            helper.setText(R.id.tv_count,item.getAmount());
        }
    }

}
