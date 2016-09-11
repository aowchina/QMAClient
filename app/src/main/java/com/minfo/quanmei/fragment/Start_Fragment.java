package com.minfo.quanmei.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.CosmeticActivity;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.activity.NoteDetailActivity;
import com.minfo.quanmei.activity.PocketActivity;
import com.minfo.quanmei.activity.ProductDetailActivity;
import com.minfo.quanmei.activity.ProjectAllActivity;
import com.minfo.quanmei.activity.SpecialDiaryActivity;
import com.minfo.quanmei.activity.TeacherListActivity;
import com.minfo.quanmei.activity.ThemeActivity;
import com.minfo.quanmei.adapter.StartActionLVAdapter;
import com.minfo.quanmei.adapter.StartBannerAdapter;
import com.minfo.quanmei.adapter.StartDiaryLVAdapter;
import com.minfo.quanmei.adapter.StartPullLVAdapter;
import com.minfo.quanmei.adapter.StartSaleGVAdapter;
import com.minfo.quanmei.entity.CycleImg;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.entity.ProjectProduct;
import com.minfo.quanmei.entity.Start;
import com.minfo.quanmei.entity.Theme;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.AutoScrollViewPager;
import com.minfo.quanmei.widget.LimitGridView;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.PullScrollView;
import com.minfo.quanmei.widget.SignDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页面
 * 2015年8月28日
 * zhang jiachang
 */
public class Start_Fragment extends BaseFragment implements View.OnClickListener,SignDialog.SignSuccessListener {
    private View v;
    private Button goToHospital;
    private AutoScrollViewPager vpstartpage;
    private View view;
    private LimitListView pullToListView;
    private StartPullLVAdapter startPullLVAdapter;
    private LimitGridView gridView;
    private StartSaleGVAdapter startSaleGVAdapter;
    private ListView listView;
    private StartActionLVAdapter startActionLVAdapter;
    private ListView listViewDiary;
    private StartDiaryLVAdapter startDiaryLVAdapter;
    private HorizontalScrollView scrollView;
    private FragmentManager supportFragmentManager;
    private FragmentTransaction transaction;
    private LinearLayout ins;
    private LinearLayout ll;
    private StartBannerAdapter startBannerAdapter;

    private TextView tvConsult;//内涵美
    private TextView tvSpecialDiary;//日记精选
    private TextView tvPlastic;//整容宝
    private TextView tvQueryPro;//查项目
    private ImageView tvSign;//签到
    private TextView tvMoreStart;//限时特惠查看更多
    private LinearLayout llDiaryMore;//日记精选查看更多
    private LinearLayout llSpecialMore;//查看全部特惠项目
    private LinearLayout llSpecialMore2;//水平滑动布局里的线性布局，跳转到特惠

    private JumpFragmentListener jumpFragmentListener;
    private PullScrollView scroll;
    private LinearLayout startToSpecial;

    private Start start;
    private List<CycleImg> lunbo = new ArrayList<>();
    private List<Theme> acts = new ArrayList<>();
    private List<Product> tehuis = new ArrayList<>();
    private List<Product> goods = new ArrayList<>();
    private List<ProjectProduct> projects = new ArrayList<>();
    private List<GroupArticle> diarys = new ArrayList<GroupArticle>();
    private boolean upLoad;
    private boolean build;
    private List<View> indicatorsList = new ArrayList<>();
    private LinearLayout llIndicator;

    private SignDialog signDialog;

    private LinearLayout llVisual;
    private TextView tvDepositMoney;

    private int visualIndex = 0;
    private Timer time = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mActivity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    if(visualIndex%2==0){
                        llVisual.animate().alpha(1).setDuration(1000);
                    }else{
                        llVisual.animate().alpha(0).setDuration(1000);
                        if(Start_Fragment.this.isAdded()) {
                            reqSecurityDeposit();
                        }
                    }
                    visualIndex++;
                }
            });
        }
    };

    /**
     * 是否已签到，默认为false
     */
    private boolean isSigned;


    public static Start_Fragment newInstance() {
        Start_Fragment fragment = new Start_Fragment();

        return fragment;
    }

    public Start_Fragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        jumpFragmentListener = (JumpFragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportFragmentManager = getFragmentManager();




    }




    @Override
    protected View initViews() {
        view = View.inflate(mActivity, R.layout.fragment_start, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scroll = ((PullScrollView) view.findViewById(R.id.psl_startfragment));
        signDialog = new SignDialog(mActivity,this);

        /**
         * headview
         */
        v = LayoutInflater.from(getActivity()).inflate(R.layout.start_headview, null);
        vpstartpage = (AutoScrollViewPager)v.findViewById(R.id.vp_startpage);
        llVisual = (LinearLayout) v.findViewById(R.id.ll_visual);
        tvDepositMoney = (TextView) v.findViewById(R.id.tv_deposit_money);

        setViewPager();
        pullToListView = ((LimitListView) v.findViewById(R.id.lv_startfragment));
        tvConsult = (TextView) v.findViewById(R.id.tv_consult);
        tvSpecialDiary = (TextView) v.findViewById(R.id.tv_special_diary);
        tvSign = (ImageView) v.findViewById(R.id.tv_sign);
        tvPlastic = (TextView) v.findViewById(R.id.tv_plastic);
        tvQueryPro = (TextView) v.findViewById(R.id.tv_query_pro);
        tvMoreStart = (TextView) v.findViewById(R.id.tv_more_start);
        llDiaryMore = (LinearLayout) v.findViewById(R.id.ll_more_diary);
        scrollView = (HorizontalScrollView) v.findViewById(R.id.hs_start);
        llSpecialMore2 = (LinearLayout) v.findViewById(R.id.ll_special_more);
        startToSpecial = ((LinearLayout) v.findViewById(R.id.ll_more_starttospecial));
        scroll.addBodyView(v);
        llIndicator = (LinearLayout) v.findViewById(R.id.ll_indicators);

        tvQueryPro.setOnClickListener(this);
        tvConsult.setOnClickListener(this);
        tvSpecialDiary.setOnClickListener(this);
        tvPlastic.setOnClickListener(this);
        tvMoreStart.setOnClickListener(this);
        llDiaryMore.setOnClickListener(this);
        llSpecialMore2.setOnClickListener(this);
        startToSpecial.setOnClickListener(this);
        tvSign.setOnClickListener(this);
        scroll.setfooterViewGone();

        //限时特惠
        gridView = ((LimitGridView) v.findViewById(R.id.gv_sale_start));

        //热门活动
        listView = ((ListView) v.findViewById(R.id.lv_action_start));

        //日记精选
        listViewDiary = ((ListView) v.findViewById(R.id.lv_diary));

//        reqSignState();
        reqStartList();
        reqSecurityDeposit();
        if(!isSigned){
//            reqSign();
        }
        HorizontalScrollView scrollView = ((HorizontalScrollView) v.findViewById(R.id.hs_start));
        scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                upLoad = true;
                reqStartList();
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toProductDetail=new Intent(getActivity(), ProductDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("product", tehuis.get(position));
                toProductDetail.putExtra("info",bundle);
                startActivity(toProductDetail);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("hid",acts.get(position).getHid());
                bundle.putString("pid",acts.get(position).getPid());
                bundle.putString("bimg",acts.get(position).getBimg());
                utils.jumpAty(mActivity,ThemeActivity.class,bundle);
            }
        });
        pullToListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toProductDetail=new Intent(getActivity(), ProductDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("product", goods.get(position));
                toProductDetail.putExtra("info",bundle);
                startActivity(toProductDetail);
            }
        });
        listViewDiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDiaryDetail = new Intent(getActivity(), NoteDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("group", diarys.get(position));
                intentToDiaryDetail.putExtra("info",bundle);
                startActivity(intentToDiaryDetail);
            }
        });


        time.schedule(timerTask,1,3000);


    }

    /**
     * 请求今天签到状态
     */
    private void reqSignState() {
        String url = getString(R.string.api_baseurl)+"user/QdStatus.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+utils.getUserid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                tvSign.setImageResource(R.mipmap.sign_normal);
                isSigned = false;

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                tvSign.setImageResource(R.mipmap.signed);
                isSigned = true;
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity,msg);
            }
        });

    }

    private void setViewPager() {
        vpstartpage.setInterval(3500);
        vpstartpage.setCycle(true);
        vpstartpage.startAutoScroll();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_query_pro://查项目
                Intent intent = new Intent(getActivity(), ProjectAllActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_consult://内涵美
                utils.jumpAty(mActivity, TeacherListActivity.class,null);
                break;
            case R.id.tv_sign:
                if(!isSigned){
//                    reqSign();
                }
                break;
            case R.id.tv_special_diary://日记精选
                utils.jumpAty(mActivity, SpecialDiaryActivity.class,null);
                break;
            case R.id.tv_plastic://整容宝
                utils.jumpAty(mActivity, CosmeticActivity.class, null);
                break;
            case R.id.ll_special_more:
            case R.id.ll_more_start:
                jumpFragmentListener.jumpFragment(3);
                break;
            case R.id.tv_more_start://跳转到特惠
                jumpFragmentListener.jumpFragment(3);
                break;
            case R.id.ll_more_diary://跳转到小组
                jumpFragmentListener.jumpFragment(2);
                break;
            case R.id.ll_more_starttospecial://跳转到小组
                jumpFragmentListener.jumpFragment(3);
                break;
        }
    }

    /**
     * 请求签到接口
     */
    private void reqSign() {
        String url = getString(R.string.api_baseurl)+"user/Qd.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+utils.getUserid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                isSigned = true;
                tvSign.setImageResource(R.mipmap.signed);
                signDialog.show();
                tvSign.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signDialog.dismiss();
                    }
                },1500);

                signDialog.setPoint(response.toString());
            }

            @Override
            public void onRequestNoData(BaseResponse response) {

                int errorcode = response.getErrorcode();
                if (errorcode == 13) {
//                    ToastUtils.show(mActivity, "今日已签到");
                } else if (errorcode == 15) {
//                    ToastUtils.show(mActivity, "签到失败");
                } else if (errorcode == 11 || errorcode == 12) {
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(mActivity, LoginActivity.class, null);
                } else {
                    ToastUtils.show(mActivity,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity, msg);
            }
        });

    }

    @Override
    public void onSignSuccess() {
        utils.jumpAty(mActivity, PocketActivity.class,null);
    }

    /**
     * 往小组特惠跳转的监听接口，MainActivity来实现它 2015-09-01 liujing
     */
    public interface JumpFragmentListener {
        void jumpFragment(int i);
    }

    /**
     * 请求保证金接口
     */
    private void reqSecurityDeposit(){
        String url = getResources().getString(R.string.api_baseurl) +"public/getSecurityDeposit.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr());

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    tvDepositMoney.setText(" ￥"+jsonObject.getString("securityDeposit"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(mActivity,"no data"+response.toString()+response.getErrorcode());
            }

            @Override
            public void onRequestError(int code, String msg) {
//                ToastUtils.show(mActivity,msg);
            }
        });
    }


    /**
     * 请求特惠页面数据
     */
    private void reqStartList() {
        String url = getResources().getString(R.string.api_baseurl) + "public/Main.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                start = response.getObj(Start.class);


                if (start != null) {
                    acts = start.getAct();
                    tehuis = start.getTehui();
                    goods = start.getGoods();
                    projects = start.getProject();
                    diarys = start.getDiary();
                    lunbo = start.getLunbo();
                    initIndicators();

                    startSaleGVAdapter = new StartSaleGVAdapter(mActivity, tehuis);
                    startActionLVAdapter = new StartActionLVAdapter(getActivity(), acts);
                    startDiaryLVAdapter = new StartDiaryLVAdapter(getActivity(), diarys);
                    startPullLVAdapter = new StartPullLVAdapter(getActivity(), goods,pullToListView);
                    startBannerAdapter = new StartBannerAdapter(mActivity,lunbo);

                    vpstartpage.setAdapter(startBannerAdapter);
                    vpstartpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            refreshIndicators(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    listViewDiary.setAdapter(startDiaryLVAdapter);
                    setListViewHeightBasedOnChildren(listViewDiary);
                    gridView.setAdapter(startSaleGVAdapter);
                    listView.setAdapter(startActionLVAdapter);
                    setListViewHeightBasedOnChildren(listView);
                    pullToListView.setAdapter(startPullLVAdapter);
                    llSpecialMore2.removeAllViews();
                    for (int i = 0; i < projects.size(); i++) {
                        TextView tempTv = new TextView(getActivity());
                        View v1 = new View(getActivity());
                        v1.setLayoutParams(new RelativeLayout.LayoutParams(40,
                                RelativeLayout.LayoutParams.WRAP_CONTENT));
                        tempTv.setPadding(10, 3, 10, 3);
                        tempTv.setGravity(Gravity.LEFT);
                        tempTv.setTextColor(Color.WHITE);
                        tempTv.setText(projects.get(i).getName());
                        tempTv.setBackgroundResource(R.color.project_background);
                        tempTv.setGravity(Gravity.CENTER);
                        llSpecialMore2.addView(v1);
                        llSpecialMore2.addView(tempTv, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        if(i==projects.size()-1){
                            View v2 = new View(getActivity());
                            v2.setLayoutParams(new RelativeLayout.LayoutParams(40,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT));
                            llSpecialMore2.addView(v2);
                        }
                    }

                    if (upLoad) {
                        upLoad = false;

                        scroll.setheaderViewReset();

                        ToastUtils.show(getActivity(), "数据刷新完毕");
                    }
                } else {
                    ToastUtils.show(getActivity(), "当前无数据");
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                if (upLoad) {
                    upLoad = false;

                    scroll.setheaderViewReset();
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity, msg);

            }
        });
    }

    private void initIndicators() {

        if(indicatorsList.size()>0){
            indicatorsList.clear();
            llIndicator.removeAllViews();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(utils.dip2px(5), utils.dip2px(5));
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = utils.dip2px(5);
        for (int i = 0; i < lunbo.size(); i++) {
            View indicator = new View(mActivity);
            indicator.setBackgroundResource(R.drawable.dot_normal);
            llIndicator.addView(indicator, params);
            indicatorsList.add(indicator);
        }
        if(lunbo!=null&&lunbo.size()>0) {
            indicatorsList.get(0).setBackgroundResource(R.drawable.dot_focused);
        }
    }

    protected void refreshIndicators(int position) {

        for (int i = 0; i < indicatorsList.size(); i++) {
            if (i == position) {
                indicatorsList.get(i).setBackgroundResource(R.drawable.dot_focused);
            } else {
                indicatorsList.get(i).setBackgroundResource(R.drawable.dot_normal);
            }
        }
    }

    /**
     * 加载数据后，计算listview高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
