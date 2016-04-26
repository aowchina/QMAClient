package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.ProductListAdapter;
import com.minfo.quanmei.adapter.ScreenLocalAdapter;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.entity.Province;
import com.minfo.quanmei.entity.Special;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 特惠展示面
 * 2015年8月27日
 * zhang jiachang
 */
public class SpecialActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivLeft;
    private TextView tvTitle;

    private TextView local;
    private TextView kind;
    private TextView screen;
    private ScreenLocalAdapter screenLocalAdapter;
    private PopupWindow popupWindow;
    private String[] chose = {"最新上架", "价格最低"};
    private String kindStr = "全美";
    private String locStr = "";
    private int id = 0;


    private List<Product> products = new ArrayList<Product>();

    private List<Product> tempList = new ArrayList<>();

    private RefreshListView lvSpecial;
    private ProductListAdapter adapter;
    private Product product;


    private LinearLayout localLine;
    private LinearLayout screenLine;
    private LinearLayout specLine;
    private boolean localFlag;//为true表示已选定地区
    private boolean specFlag;//为true表示已选定排序顺序
    private int chosePosition = 0;

    private Special special;


    private int provinceID = 0;
    private int kindId = 0;
    private int methodID = 0;
    private int pageID = 1;

    private boolean isCanScroll = true;//能否上拉加载
    private boolean refresh;//能否下拉刷新
    private boolean isResetPage;//为true表示下拉刷新请求数据为第一页
    private boolean temp;//数据较少时的处理

    private boolean isRefreshing;
    private boolean isLoading;

    public static SpecialActivity specialActivityInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        specialActivityInstance = this;
        initData();
    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("特惠");
    }


    public void initViews() {
        local = ((TextView) findViewById(R.id.tv_local_screen));
        kind = ((TextView) findViewById(R.id.tv_kind_screen));
        screen = ((TextView) findViewById(R.id.tv_screen_spec));
        lvSpecial = (RefreshListView) findViewById(R.id.lv_special);
        localLine = ((LinearLayout) findViewById(R.id.ll_local_screen));
        screenLine = ((LinearLayout) findViewById(R.id.ll_kind_screen));
        specLine = ((LinearLayout) findViewById(R.id.ll_screen_spec));


        localLine.setOnClickListener(this);
        screenLine.setOnClickListener(this);
        specLine.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_local_screen://选择地区
                screenLocalAdapter = null;
                showPopWindow(v, Constant.provinces);
                break;
            case R.id.ll_screen_spec://选择排序顺序
                List<Province> cho = new ArrayList<>();
                for (int j = 0; j < 2; j++) {
                    cho.add(new Province(j, chose[j]));
                }
                showPopWindow(v, cho);
                break;
            case R.id.ll_kind_screen:
                choseKind();
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            special = (Special) bundle.getSerializable("special");
        }
        id = getIntent().getIntExtra("ID", 0);

        kindStr = getIntent().getStringExtra("KIND");
        provinceID = getIntent().getIntExtra("PROVINCE", 0);
        kindId = getIntent().getIntExtra("KINDID", 0);

        if (kindStr.equals("更多")) {
            kind.setText("全部项目");
            local.setText("全国");
            kindId = 0;
        } else if (kindStr.equals("全部项目")) {
            kind.setText("全部项目");
        } else {
            kind.setText(special.getName());
        }
        if (provinceID != 0) {
            local.setText(Constant.provinces.get(provinceID).getName());
        }
        readProvinces();
        adapter = new ProductListAdapter(this, products);
        lvSpecial.setAdapter(adapter);
        lvSpecial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toProductDetail = new Intent(SpecialActivity.this, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", products.get(position - 1));
                toProductDetail.putExtra("info", bundle);
                startActivity(toProductDetail);

            }
        });
        reqSpecialListData();
        lvSpecial.setIsCanRefresh(true);
        lvSpecial.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                pageID = 1;

                refresh = true;
                isRefreshing = true;
                reqSpecialListData();
            }
        });
        lvSpecial.setIsCanLoad(true);

        lvSpecial.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                refresh = false;
                isLoading = true;
                pageID++;
                reqSpecialListData();
            }
        });
    }

    /**
     * 请求特惠列表数据
     */
    private void reqSpecialListData() {
        String url = getResources().getString(R.string.api_baseurl) + "tehui/List.php";
        final Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + provinceID + "*" + kindId + "*" + methodID + "*" + pageID);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                lvSpecial.refreshComplete();
                lvSpecial.loadComplete();
                if(isRefreshing){
                    products.clear();
                }


                tempList = response.getList(Product.class);
                if (products != null) {
                    if (tempList != null && tempList.size() > 0) {
                        products.addAll(tempList);//当页码增加时，需要在之前的数据基础上加上现在请求的数据
                    } else {
                        if(pageID!=1&&isLoading){
                            ToastUtils.show(SpecialActivity.this,"数据加载完毕");
                        }
                        if (pageID == 1 && products.size() <= 0) {
                            ToastUtils.show(SpecialActivity.this, "没有相关特惠产品！");
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
                tempList.clear();
                isRefreshing = false;
                isLoading = false;
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                lvSpecial.refreshComplete();
                lvSpecial.loadComplete();
                isRefreshing = false;
                isLoading = false;
                ToastUtils.show(SpecialActivity.this, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                lvSpecial.refreshComplete();
                lvSpecial.loadComplete();
                isRefreshing = false;
                isLoading = false;
                ToastUtils.show(SpecialActivity.this, msg);

            }
        });
    }

    //获取省市数据
    private void readProvinces() {
        Constant.provinces.clear();
        StringBuffer sb = new StringBuffer();
        try {
            InputStream inputStream = getResources().getAssets().open("p.txt");
            byte[] buf = new byte[1024];

            while ((inputStream.read(buf)) != -1) {
                sb.append(new String(buf));
                buf = new byte[1024];//重新生成，避免和上次读取的数据重复
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Province province;
        try {
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                province = new Province(jsonObject.getInt("id"), jsonObject.getString("name"));
                Constant.provinces.add(province);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void choseKind() {
        Intent intent = new Intent(SpecialActivity.this, SpecialScreenActivity.class);
        locStr = local.getText().toString();
        intent.putExtra("LOC", locStr);
        intent.putExtra("POS", 10);
        //2表示接受来自特惠筛选页的信息并反馈信息
        if (id == 2) {
            intent.putExtra("FLAG", true);
        } else if (id == 3) {//3表示接受来自特惠选取页的信息并反馈信息

            intent.putExtra("flag", true);
        }

        intent.putExtra("KIND", kind.getText().toString());
        intent.putExtra("PROVINCE", provinceID);
        startActivity(intent);
//        finish();
    }

    //弹出popWindow

    public void showPopWindow(View view, List<Province> listData) {

        final LinearLayout llPop = (LinearLayout) findViewById(R.id.ll_pop);

        final View layout1 = LayoutInflater.from(this).inflate(
                R.layout.screen_local_pop, null);
        final ListView listView = (ListView) layout1.findViewById(R.id.lv_local_screen);
        final int count = listData.size();
        final List<Province> listD = new ArrayList<>();
        listD.clear();
        for (int i = 0; i < listData.size(); i++) {
            listD.add(listData.get(i));
        }
        String locOrSpec = "";
        if (count == 2) {
            locOrSpec = "spec";
        } else {
            locOrSpec = "loc";
        }
        screenLocalAdapter = new ScreenLocalAdapter(this, listD, chosePosition, localFlag, specFlag, locOrSpec);
        listView.setAdapter(screenLocalAdapter);
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        //设置popupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha));
        popupWindow.showAsDropDown(local);
        //监听
//        llPop.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {


                if (count == 2) {
                    screen.setText(listD.get(arg2).getName());
                    if (methodID != arg2) {
                        isResetPage = true;
                    }
                    methodID = listD.get(arg2).getId();
                    specFlag = true;


                } else {
                    local.setText(listD.get(arg2).getName());
                    localFlag = true;
                    if (provinceID != arg2) {
                        isResetPage = true;
                    }
                    provinceID = listD.get(arg2).getId();

                }
                chosePosition = arg2;

                closePopupWindow();
//                llPop.setVisibility(View.GONE);
                if (isResetPage) {
                    pageID = 1;
                    products.clear();
                    reqSpecialListData();
                    isResetPage = false;
                }

            }
        });
    }

    /**
     * 关闭窗口
     */
    private void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {

            popupWindow.dismiss();
            popupWindow = null;
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
