package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.ChildCategoryAdapter;
import com.minfo.quanmei.adapter.ParentCategoryAdapter;
import com.minfo.quanmei.entity.ChildCategory;
import com.minfo.quanmei.entity.ParentCategory;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目大全页
 * liujing 2015-08-26
 */
public class ProjectAllActivity extends BaseActivity implements ChildCategoryAdapter.ExpandListListener, View.OnClickListener {
    private ListView lvProjectAll;
    private ParentCategoryAdapter pAdapter;
    private List<ParentCategory> pCatList = new ArrayList<>();
    private Map<String, String> params;
    private ImageView ivLeft;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_all);
        initData();
    }

    @Override
    protected void findViews() {

        lvProjectAll = (ListView) findViewById(R.id.lv_p_category);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("项目大全");
        tvTitle.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initViews() {

    }

    private void bindData() {
        pAdapter = new ParentCategoryAdapter(this, pCatList, this);
        lvProjectAll.setAdapter(pAdapter);
    }

    /**
     * 请求数据
     */
    private void initData() {
        String url = getResources().getString(R.string.api_baseurl) + "project/List.php";
        params = utils.getParams(utils.getBasePostStr());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                pCatList = response.getList(ParentCategory.class);
                bindData();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(ProjectAllActivity.this, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(ProjectAllActivity.this, msg);
            }


        });

    }


    /**
     * 点击列表展开按钮时调用
     */
    @Override
    public void onExpand() {
        pAdapter.notifyDataSetChanged();
    }

    /**
     * 点击子分类列表item时调用
     *
     * @param category
     */
    @Override
    public void onStartProItem(ChildCategory category) {
        Intent intent = new Intent(this, ProjectItemInfoActivity.class);
        intent.putExtra("childCategory", category);

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }
}
