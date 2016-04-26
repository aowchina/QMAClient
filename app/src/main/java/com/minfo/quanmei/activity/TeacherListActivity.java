package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.entity.Teacher;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeacherListActivity extends BaseActivity implements View.OnClickListener{

    //top
    private TextView tvTitle;
    private ImageView ivLeft;

    private GridView gvTeacher;
    private List<Teacher> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

    }

    @Override
    protected void findViews() {
        gvTeacher = (GridView) findViewById(R.id.gv_teacher);
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("百万名师");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initViews() {
        initData();
        if (utils.isOnLine(this)) {
            reqData();
        }else{
            ToastUtils.show(this,"暂时无网络");
        }


    }

    private void reqData() {
        String url = getResources().getString(R.string.api_baseurl)+"course/TeacherList.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                list = response.getList(Teacher.class);
                bindData();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(TeacherListActivity.this,"服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(TeacherListActivity.this, msg);
            }
        });
    }

    private void bindData() {
        gvTeacher.setAdapter(new CommonAdapter<Teacher>(this, list, R.layout.item_teacher) {
            @Override
            public void convert(BaseViewHolder helper, final Teacher item, int position) {
                helper.setText(R.id.tv_teacher, item.getName());
                ImageView ivHead = helper.getView(R.id.civ_teacher);
                UniversalImageUtils.disCircleImage(item.getLogo(),ivHead);
                ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("teacherid",item.getId());
                        utils.jumpAty(TeacherListActivity.this, CourseActivity.class, bundle);
                    }
                });
            }
        });
    }

    private void initData() {

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
