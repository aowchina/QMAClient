package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.HosComListAdapter;
import com.minfo.quanmei.adapter.HospitalGridAdapter;
import com.minfo.quanmei.entity.HospitalData;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.PullScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 医院主页面
 * 2015年8月24日
 * zhang jiachang
 */
public class HospitalActivity extends BaseActivity implements View.OnClickListener {

    private HospitalGridAdapter hospitalGridAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<String> l = new ArrayList<String>();
    private HosComListAdapter adapter;

    private LimitListView listView;
    private PullScrollView scroll;
    private GridView gridView;

    private TextView tvComNums;
    private View view;
    private TextView moreLoad;
    private RatingBar ratingBar;
    private LinearLayout toDoctor;
    private LinearLayout toHospital;
    private ImageView back;
    private String hospitalId;
    private ImageView hospitalIcon;
    private HospitalData hospitalData;
    private TextView hosName;
    private TextView goodComment;
    private TextView beauty;
    private TextView environment;
    private TextView service;
    private TextView commentCount;

    private ImageView doctorIconOne;
    private ImageView doctorIconTwo;
    private TextView doctorNameOne;
    private TextView doctorNameTwo;
    private TextView doctorVisitOne;
    private TextView doctorVisitTwo;
    private LinearLayout docBLl;
    private boolean isRefresh;
    //top
    private TextView tvTitle;
    private ImageView ivLeft;
    private int page = 1;
    private List<Product> tempList = new ArrayList<>();
    private boolean isLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setText("医院主页");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);

        view = ((View) LayoutInflater.from(this).inflate(R.layout.layout_hospital, null));

        scroll = ((PullScrollView) findViewById(R.id.scroll_hos));
        gridView = (GridView) view.findViewById(R.id.lgr_hos);
        listView = ((LimitListView) view.findViewById(R.id.llv_hos));
        tvComNums = (TextView) view.findViewById(R.id.hos_comcount);
        //moreLoad = ((TextView) view.findViewById(R.id.tv_more_hospital));
        ratingBar = ((RatingBar) view.findViewById(R.id.hospital_comment));
        toDoctor = ((LinearLayout) view.findViewById(R.id.goto_doctor));
        toHospital = ((LinearLayout) view.findViewById(R.id.goto_hospital_introduce));
        docBLl = ((LinearLayout) view.findViewById(R.id.ll_docb));
        hospitalIcon = ((ImageView) view.findViewById(R.id.hospital_img));
        hosName = ((TextView) view.findViewById(R.id.hos_name));
        goodComment = ((TextView) view.findViewById(R.id.tv_good_value));
        beauty = ((TextView) view.findViewById(R.id.hos_beauty));
        environment = ((TextView) view.findViewById(R.id.hos_envir));
        service = ((TextView) view.findViewById(R.id.hos_service));
        commentCount = ((TextView) view.findViewById(R.id.com_count));
        doctorIconOne = ((ImageView) view.findViewById(R.id.hos_doca));
        doctorIconTwo = ((ImageView) view.findViewById(R.id.hos_docb));
        doctorNameOne = ((TextView) view.findViewById(R.id.hos_docaname));
        doctorNameTwo = ((TextView) view.findViewById(R.id.hos_docbname));
        doctorVisitOne = ((TextView) view.findViewById(R.id.hos_docavisit));
        doctorVisitTwo = ((TextView) view.findViewById(R.id.hos_docbvisit));
        gridView.setFocusable(false);

        scroll.addBodyView(view);
    }

    @Override
    protected void initViews() {
        tvComNums.setOnClickListener(this);
        //moreLoad.setOnClickListener(this);
        toDoctor.setOnClickListener(this);
        toHospital.setOnClickListener(this);


        hospitalGridAdapter = new HospitalGridAdapter(this, productList);
        gridView.setAdapter(hospitalGridAdapter);
        hospitalId = getIntent().getStringExtra("ID");
        if (hospitalId!=null&&!hospitalId.equals("")) {
            reqHospitalData();
            reqHospitalProductData();
        }

        ratingBar.setIsIndicator(true);
        scroll.setfooterViewGone();




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                page = 1;
                isRefresh = true;
                Log.e(TAG,"走没走");
                reqHospitalData();
                reqHospitalProductData();
            }
        });
        scroll.setIsCanLoad(true);
        scroll.setOnLoadListener(new PullScrollView.OnLoadListener() {
            @Override
            public void loadMore() {
                page++;
                isLoad = true;
                reqHospitalProductData();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", productList.get(position));
                utils.jumpAty(HospitalActivity.this, ProductDetailActivity.class, bundle);
            }
        });

    }
    /**
     * 请求医院首页数据
     */
    private void reqHospitalData() {
        String url = getResources().getString(R.string.api_baseurl) + "hospital/Main.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + hospitalId);

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }
            @Override
            public void onRequestSuccess(BaseResponse response) {
                hospitalData = response.getObj(HospitalData.class);
                if (hospitalData!=null){
                    setHospitalData(hospitalData);

                    adapter = new HosComListAdapter(HospitalActivity.this, hospitalData);
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==11){
                    ToastUtils.show(HospitalActivity.this,"医院不存在或已被删除");
                }else{
                    ToastUtils.show(HospitalActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(HospitalActivity.this, msg);
            }
        });
    }
    /**
     * 请求医院产品详情数据
     */
    private void reqHospitalProductData( ) {
        String url = getResources().getString(R.string.api_baseurl) + "tehui/ActList.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + hospitalId+"*"+0+"*"+page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                tempList = response.getList(Product.class);
                if (isRefresh) {
                    isRefresh = false;
                    productList.clear();
                    scroll.setheaderViewReset();
                    ToastUtils.show(HospitalActivity.this, "数据刷新完毕");
                }
                if (isLoad) {
                    isLoad = false;
                    scroll.setfooterViewGone();
                }
                productList.addAll(tempList);
                tempList.clear();
                hospitalGridAdapter.notifyDataSetChanged();
                resetGridviewHeight();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(HospitalActivity.this,"服务器繁忙");
                scroll.setheaderViewReset();
                scroll.setfooterViewGone();
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(HospitalActivity.this, msg);
                scroll.setheaderViewReset();
                scroll.setfooterViewGone();
            }
        });
    }

    public void setHospitalData(HospitalData hospitalData){
        UniversalImageUtils.displayImageUseDefOptions(hospitalData.getLogo(), hospitalIcon);
        hosName.setText(hospitalData.getName());
        ratingBar.setMax(5);
        ratingBar.setRating(hospitalData.getStars());
        goodComment.setText(hospitalData.getHp());
        beauty.setText(hospitalData.getSm());
        environment.setText(hospitalData.getHj());
        service.setText(hospitalData.getFw());
        commentCount.setText("（ "+hospitalData.getPlamount()+"人 ）");
        tvComNums.setText("共" + hospitalData.getPlamount() + "条评价 >>");

        if (hospitalData.getDoctor().size()>1){
            docBLl.setVisibility(View.VISIBLE);
            doctorIconTwo.setVisibility(View.VISIBLE);
        }else if (hospitalData.getDoctor().size()==1){
            docBLl.setVisibility(View.GONE);
            doctorIconTwo.setVisibility(View.GONE);
        }else {
            toDoctor.setVisibility(View.GONE);
        }
        for (int i = 0; i <hospitalData.getDoctor().size() ; i++) {
            if (i==0){
                UniversalImageUtils.disCircleImage(hospitalData.getDoctor().get(i).getImg(), doctorIconOne);
                doctorNameOne.setText(hospitalData.getDoctor().get(i).getName());
                doctorVisitOne.setText(hospitalData.getDoctor().get(i).getDirection());
            }
            else if (i==1){
                UniversalImageUtils.disCircleImage(hospitalData.getDoctor().get(i).getImg(), doctorIconTwo);
                doctorNameTwo.setText(hospitalData.getDoctor().get(i).getName());
                doctorVisitTwo.setText(hospitalData.getDoctor().get(i).getDirection());
            }
        }
    }

    /**
     * TextView在xml中加点击事件有时不好用 by liujing
     *
     * @param v
     */

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.hos_comcount:
                Intent toPlList=new Intent(HospitalActivity.this, HospitalCommentActivity.class);
                toPlList.putExtra("ID",hospitalId);
                toPlList.putExtra("hospitalData",hospitalData);
                startActivity(toPlList);
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            //case R.id.tv_more_hospital:

            //break;
            case R.id.goto_doctor:
                Intent toDoctor=new Intent(HospitalActivity.this, WholeDoctorActivity.class);
                toDoctor.putExtra("ID",hospitalId);
                startActivity(toDoctor);
                break;
            case R.id.goto_hospital_introduce:
                Intent intent=new Intent(HospitalActivity.this,HospitalIntroduceActivity.class);
                intent.putExtra("ID",hospitalId);
                startActivity(intent);
                break;

        }
    }

    /**
     * 加载数据后，计算listview高度
     *
     */
    private void resetGridviewHeight() {
        if (productList.size() != 0) {
            //加载itemview计算item的高度
            View view = LayoutInflater.from(this).inflate(R.layout.hosgrid_item, null);
            view.measure(0, 0);
            int itemHeight = view.getMeasuredHeight();
            int verticalSpacing = gridView.getVerticalSpacing();
            int numColumns = 0;
            if (productList.size() % 2 == 0) {
                numColumns = productList.size() / 2;
            } else {
                numColumns = (productList.size() /2) + 1;
            }
            int gridviewheight = itemHeight * numColumns + verticalSpacing * (numColumns - 1) + 10;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) gridView.getLayoutParams();
            layoutParams.height = gridviewheight;
            gridView.setLayoutParams(layoutParams);
        }
    }
}
