package com.minfo.quanmei.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.ProductDetailActivity;
import com.minfo.quanmei.adapter.ProductListAdapter;
import com.minfo.quanmei.adapter.StartPullLVAdapter;
import com.minfo.quanmei.entity.Baseinfo;
import com.minfo.quanmei.entity.ChildCategory;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.entity.ProjectInfo;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LimitListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProjectInfoFragment extends BaseFragment {


    private ScrollView svProInfo;

    private LinearLayout llIntro;//简介
    private TextView tvIntroTitle;//标题
    private TextView tvIntroContent;//简介内容
    private TextView tvPriceRange;//价格范围
    private RatingBar rbRiskIndex;//风险指数
    private TextView tvInsistTime;//持续时间

    private LinearLayout llAdv;//优点
    private TextView tvAdvTitle;//优点标题
    private TextView tvAdvContent;//优点内容

    private LinearLayout llDis;//缺点
    private TextView tvDisTitle;//优点内容
    private TextView tvDisContent;//优点内容

    private LinearLayout llFitCrowd;//适宜人群
    private TextView tvFitCrowdTitle;//适宜人群内容标题
    private TextView tvFitCrowdContent;//适宜人群内容
    private TextView tvZlDuration;//治疗时长
    private TextView tvZlTimes;//治疗次数
    private TextView tvMethod;//麻醉方法
    private TextView tvHos;//是否住院
    private TextView tvhFtime;//恢复时间
    private TextView tvCxtime;//拆线时间

    private TextView tvZysx;//注意事项
    private TextView tvFxts;//风险提示
    private TextView tvJjrq;//禁忌人群
    private ListView lvProduct;//项目列表


    private Baseinfo baseinfo;

    private LayoutInflater inflater;

    private ChildCategory childCategory;
    private ProjectInfo proInfo;

    private Map<String, String> params;

    private ProductListAdapter adapter;
    private List<Product> goodsList= new ArrayList<>();
    private StartPullLVAdapter startPullLVAdapter;

    public ProjectInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_project_info, null);

        svProInfo = (ScrollView) view.findViewById(R.id.sv_project_intro);

        llIntro = (LinearLayout) view.findViewById(R.id.ll_pro_intro);
        llAdv = (LinearLayout) view.findViewById(R.id.ll_pro_adv);
        llDis = (LinearLayout) view.findViewById(R.id.ll_pro_dis);
        llFitCrowd = (LinearLayout) view.findViewById(R.id.ll_pro_fitcrowd);

        tvPriceRange = (TextView) view.findViewById(R.id.tv_price_range);//
        rbRiskIndex = (RatingBar) view.findViewById(R.id.rb_risk_index);
        rbRiskIndex.setIsIndicator(true);
        tvInsistTime = (TextView) view.findViewById(R.id.tv_insist_time);
        tvZlDuration = (TextView) view.findViewById(R.id.tv_pro_info_duration);
        tvZlTimes = (TextView) view.findViewById(R.id.tv_pro_info_nums);
        tvMethod = (TextView) view.findViewById(R.id.tv_pro_info_method);
        tvHos = (TextView) view.findViewById(R.id.tv_pro_info_hos);
        tvhFtime = (TextView) view.findViewById(R.id.tv_pro_info_hftime);
        tvCxtime = (TextView) view.findViewById(R.id.tv_pro_info_cxtime);
        tvZysx = (TextView) view.findViewById(R.id.tv_zysx);
        tvFxts = (TextView) view.findViewById(R.id.tv_fxts);
        tvJjrq = (TextView) view.findViewById(R.id.tv_jjrq);

        lvProduct = (LimitListView) view.findViewById(R.id.lv_product);
        lvProduct.setFocusable(false);
        svProInfo.scrollTo(0, 0);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reqServer();
    }

    private void initViews1() {
        searchTextView(llIntro, 1);
        searchTextView(llAdv, 2);
        searchTextView(llDis, 3);
        searchTextView(llFitCrowd, 4);
        tvPriceRange.setText(proInfo.getBaseinfo().getJgfw());
        if (!proInfo.getBaseinfo().getFxzs().equals("")) {
            rbRiskIndex.setRating(Float.parseFloat(proInfo.getBaseinfo().getFxzs()));
        } else {
            rbRiskIndex.setRating(2);
        }
        tvInsistTime.setText(proInfo.getBaseinfo().getCxsj());
        tvZlDuration.setText(proInfo.getBaseinfo().getZlsc());
        tvZlTimes.setText(proInfo.getBaseinfo().getZlcs());
        tvMethod.setText(proInfo.getBaseinfo().getMzff());

        tvHos.setText(Integer.parseInt(proInfo.getBaseinfo().getSfzy())==1?"否":"是");

        tvCxtime.setText(proInfo.getBaseinfo().getCxsj2());
        tvhFtime.setText(proInfo.getBaseinfo().getHfsj());


        initData();

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", goodsList.get(position));
                utils.jumpAty(mActivity, ProductDetailActivity.class, bundle);
            }
        });





    }

    public void initData() {


        startPullLVAdapter = new StartPullLVAdapter(getActivity(), goodsList,lvProduct);
        lvProduct.setAdapter(startPullLVAdapter);

    }

    private void searchTextView(LinearLayout ll, int flag) {
        inflater = LayoutInflater.from(mActivity);
        View v1 = inflater.inflate(R.layout.layout_project_info_label1, null);
        switch (flag) {
            case 1:
                tvIntroTitle = (TextView) v1.findViewById(R.id.tv_pro_info_label1_title);
                tvIntroContent = (TextView) v1.findViewById(R.id.tv_pro_info_label1_content);
                tvIntroTitle.setText("简介");
                tvIntroContent.setText(proInfo.getBaseinfo().getJj());
                ll.addView(v1);
                break;
            case 2:
                tvAdvTitle = (TextView) v1.findViewById(R.id.tv_pro_info_label1_title);
                tvAdvContent = (TextView) v1.findViewById(R.id.tv_pro_info_label1_content);
                tvAdvTitle.setText("优点");
                tvAdvContent.setText(proInfo.getBaseinfo().getYd());
                ll.addView(v1);
                break;
            case 3:
                tvDisTitle = (TextView) v1.findViewById(R.id.tv_pro_info_label1_title);
                tvDisContent = (TextView) v1.findViewById(R.id.tv_pro_info_label1_content);
                tvDisTitle.setText("缺点");
                tvDisContent.setText(proInfo.getBaseinfo().getQd());
                ll.addView(v1);
                break;
            case 4:
                tvFitCrowdTitle = (TextView) v1.findViewById(R.id.tv_pro_info_label1_title);
                tvFitCrowdContent = (TextView) v1.findViewById(R.id.tv_pro_info_label1_content);
                tvFitCrowdTitle.setText("适宜人群");
                tvFitCrowdContent.setText(proInfo.getBaseinfo().getSyrq());
                ll.addView(v1);
                break;
        }
        tvZysx.setText(proInfo.getBaseinfo().getZysx());
        tvFxts.setText(proInfo.getBaseinfo().getFxts());
        tvJjrq.setText(proInfo.getBaseinfo().getJjrq());
    }

    private void reqServer() {
        childCategory = (ChildCategory) this.getArguments().getSerializable("childCategory");
        String url = getResources().getString(R.string.api_baseurl) + "project/Detail.php";
        params = utils.getParams(utils.getBasePostStr() + "*" + childCategory.getId());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                proInfo = response.getObj(ProjectInfo.class);
                goodsList = proInfo.getGoods();
                initViews1();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity, msg);
            }

        });

    }



}
