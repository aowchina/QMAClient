package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.ProductListAdapter;
import com.minfo.quanmei.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品列表
 * liujing
 * 2015-08-25
 */
public class ProlistActivity extends BaseActivity {
    private List<Product> list = new ArrayList<>();
    private ListView listView;
    private ProductListAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prolist);
        initData();
    }

    @Override
    protected void findViews() {
        listView = (ListView) findViewById(R.id.lv_product);
    }

    @Override
    protected void initViews() {

    }

    private void initData() {

    }

}
