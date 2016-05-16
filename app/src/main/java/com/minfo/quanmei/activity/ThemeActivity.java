package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.HospitalGridAdapter;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.PullScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主题活动页
 * 2015年08月25日
 * zhang jiachang
 * <p/>
 * 2015-10-15 liujing
 */
public class ThemeActivity extends BaseActivity implements View.OnClickListener {
    //top
    private TextView tvTitle;
    private ImageView ivLeft;

    private GridView gridView;
    private HospitalGridAdapter hospitalGridAdapter;
    private List<Product> list = new ArrayList<>();
    private ImageView ivTheme;
    private PullScrollView pslTheme;

    private View view;

    private String hid;
    private String pid;
    private String bimg;
    private int page = 1;
    private boolean isRefresh;
    private boolean isLoad;
    private List<Product> tempList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
    }

    @Override
    protected void findViews() {
        view = LayoutInflater.from(this).inflate(R.layout.layout_theme, null);
        pslTheme = (PullScrollView) findViewById(R.id.psl_theme);
        pslTheme.addBodyView(view);
        gridView = ((GridView) view.findViewById(R.id.lgr_theme));
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("主题活动");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        ivTheme = (ImageView) view.findViewById(R.id.iv_theme);
    }

    @Override
    protected void initViews() {
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            hid = bundle.getString("hid");
            pid = bundle.getString("pid");
            bimg = bundle.getString("bimg");
            UniversalImageUtils.displayImageUseDefOptions(bimg, ivTheme);
            reqServerInfo();
        }

        pslTheme.setIsCanLoad(true);
        pslTheme.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                page = 1;
                isRefresh = true;
                reqServerInfo();
            }
        });
        pslTheme.setOnLoadListener(new PullScrollView.OnLoadListener() {
            @Override
            public void loadMore() {
                page++;
                isLoad = true;
                reqServerInfo();
            }
        });
        hospitalGridAdapter = new HospitalGridAdapter(this, list);
        gridView.setAdapter(hospitalGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", list.get(position));
                utils.jumpAty(ThemeActivity.this, ProductDetailActivity.class, bundle);
            }
        });
    }

    /**
     * 请求接口数据
     */
    private void reqServerInfo() {
        String url = getResources().getString(R.string.api_baseurl) + "tehui/ActList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + hid + "*" + pid + "*" + page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                tempList = response.getList(Product.class);
                if (isRefresh) {
                    isLoad = false;
                    pslTheme.setheaderViewReset();
                    list.clear();
                }
                if (isLoad) {
                    isLoad = false;
                    pslTheme.setfooterViewGone();
                }
                list.addAll(tempList);
                hospitalGridAdapter.notifyDataSetChanged();
                resetGridviewHeight();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(ThemeActivity.this,response.getErrorcode()+"");
                pslTheme.setheaderViewReset();
                pslTheme.setfooterViewGone();
                ToastUtils.show(ThemeActivity.this, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                pslTheme.setheaderViewReset();
                pslTheme.setfooterViewGone();
                ToastUtils.show(ThemeActivity.this, msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    private void resetGridviewHeight() {
        if (list.size() != 0) {
            //加载itemview计算item的高度
            View view = LayoutInflater.from(this).inflate(R.layout.hosgrid_item, null);
            view.measure(0, 0);
            int itemHeight = view.getMeasuredHeight();
            int verticalSpacing = gridView.getVerticalSpacing();
            int numColumns = 0;
            if (list.size() % 2 == 0) {
                numColumns = list.size() / 2;
            } else {
                numColumns = (list.size() /2) + 1;
            }
            int gridviewheight = itemHeight * numColumns + verticalSpacing * (numColumns - 1) + 10;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) gridView.getLayoutParams();
            layoutParams.height = gridviewheight;
            gridView.setLayoutParams(layoutParams);
        }
    }
}
