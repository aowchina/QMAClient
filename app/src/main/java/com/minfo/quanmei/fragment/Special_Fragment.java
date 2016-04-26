package com.minfo.quanmei.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.PersonalHomePageActivity;
import com.minfo.quanmei.activity.SpecialActivity;
import com.minfo.quanmei.activity.ThemeActivity;
import com.minfo.quanmei.adapter.SpecialGVKindAdapter;
import com.minfo.quanmei.adapter.SpecialGridAdapter;
import com.minfo.quanmei.entity.PartUser;
import com.minfo.quanmei.entity.Special;
import com.minfo.quanmei.entity.SpecialPage;
import com.minfo.quanmei.entity.Theme;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitGridView;
import com.minfo.quanmei.widget.PullScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 特惠页面
 * 2015年8月26日
 * zhang jiachang
 *
 * 2015-10-14 liujing
 */

public class Special_Fragment extends BaseFragment implements View.OnClickListener{

    private List<Map<String, Object>> list = new ArrayList<>();

    private SpecialPage specialPage;
    private Theme themeFirst;
    private List<Theme> themeList;
    private List<Special> specialList;
    private SpecialGridAdapter specialGridAdapter;
    private SpecialGVKindAdapter specialGVKindAdapter;
    private String[] screen = {"脸型", "眼部", "鼻部", "胸部", "皮肤", "身材", "整形", "更多"};

    private RelativeLayout headImg;
    private LimitGridView gridViewA;
    private PullScrollView scrollView;
    private LimitGridView gridViewB;

    private LinearLayout llUsers;

    private ImageView ivAct1;
    private TextView tvUserAmount;

    private List<PartUser> partUsers;
    private List<ImageView> imgUsers = new ArrayList<>();


    private LinearLayout head;
    private LimitGridView grid;
    private View conLine;
    private boolean upload;

    public static Special_Fragment newInstance() {
        Special_Fragment fragment = new Special_Fragment();

        return fragment;
    }

    public Special_Fragment() {
    }

    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_special, null);
        scrollView = ((PullScrollView) view.findViewById(R.id.scroll_special));
        scrollView.scrollTo(0, 0);
        scrollView.setfooterViewGone();


        conLine = LayoutInflater.from(getActivity()).inflate(R.layout.layout_continer_special, null);
        gridViewA = ((LimitGridView) conLine.findViewById(R.id.lgr_speciala));
        gridViewB = ((LimitGridView) conLine.findViewById(R.id.lgr_specialb));
        headImg = ((RelativeLayout) conLine.findViewById(R.id.iv_head_special));
        headImg.setOnClickListener(this);
        scrollView.addBodyView(conLine);
        ivAct1 = (ImageView) conLine.findViewById(R.id.iv_act1);

        llUsers = (LinearLayout) conLine.findViewById(R.id.ll_users);
        tvUserAmount = (TextView) conLine.findViewById(R.id.tv_user_amount);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reqSpecialList();

        for (int i = 0; i < 8; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("c", screen[i]);
            list.add(map);
        }
        scrollView.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                upload=true;
                reqSpecialList();

            }
        });



    }

    /**
     * 请求特惠页面数据
     */
    private void reqSpecialList() {
        String url = getResources().getString(R.string.api_baseurl) + "tehui/TypeList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                specialPage = response.getObj(SpecialPage.class);
                if (specialPage != null) {
                    specialList = specialPage.getTypes();
                    Constant.specialList = specialPage.getTypes();
                    themeFirst = specialPage.getAct_first();
                    themeList = specialPage.getAct_list();
                    bindSpecialList();
                    if (upload) {
                        upload = false;

                        scrollView.setheaderViewReset();

                        ToastUtils.show(mActivity, "数据刷新完毕");
                    }

                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                if (upload) {
                    upload = false;
                    scrollView.setheaderViewReset();
                }
                ToastUtils.show(mActivity,"服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity, msg);

            }
        });
    }

    /**
     * 绑定特惠首页数据
     */
    private void bindSpecialList() {


        specialGVKindAdapter = new SpecialGVKindAdapter(mActivity, specialList.size() > 7 ? specialList.subList(0, 8) : specialList);
        gridViewA.setAdapter(specialGVKindAdapter);
        gridViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, SpecialActivity.class);
                Bundle bundle = new Bundle();
                if (position < 7) {
                    bundle.putSerializable("special", specialList.get(position));
                    intent.putExtra("info", bundle);
                    intent.putExtra("KIND", "具体");
                } else {
                    bundle.putSerializable("special", specialList.get(7));
                    intent.putExtra("info", bundle);
                    intent.putExtra("KIND", "更多");
                }
                intent.putExtra("KINDID", specialList.get(position).getId());
                startActivity(intent);

            }
        });

        specialGridAdapter = new SpecialGridAdapter(getActivity(), themeList);
        gridViewB.setAdapter(specialGridAdapter);
        gridViewB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("hid",themeList.get(position).getHid());
                bundle.putString("pid",themeList.get(position).getPid());
                bundle.putString("bimg", themeList.get(position).getBimg());
                utils.jumpAty(mActivity,ThemeActivity.class,bundle);
            }
        });
        bindUser();
        UniversalImageUtils.displayImageUseDefOptions(themeFirst.getBimg(), ivAct1);
    }

    /**
     * 已参加活动用户数据绑定
     */
    private void bindUser() {
        imgUsers.clear();
        llUsers.removeAllViews();
        if(themeFirst.getYyuser().size()!=0) {
            partUsers = themeFirst.getYyuser();
            tvUserAmount.setText("已有"+partUsers.size()+"人参加");
            for(int i = 0;i<partUsers.size();i++){
                ImageView imageView = new ImageView(mActivity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(utils.dip2px(20),utils.dip2px(20));
                params.leftMargin = utils.dip2px(5);
                imageView.setLayoutParams(params);
                UniversalImageUtils.disCircleImage(partUsers.get(i).getUserimg(), imageView);
                llUsers.addView(imageView);
                imgUsers.add(imageView);
            }
            for(int j = 0;j<imgUsers.size();j++){
                final PartUser partUser = partUsers.get(j);
                imgUsers.get(j).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, PersonalHomePageActivity.class);
                        intent.putExtra("userid",partUser.getUserid()+"");
                        mActivity.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_head_special:
                Bundle bundle = new Bundle();
                bundle.putString("hid",themeFirst.getHid());
                bundle.putString("pid",themeFirst.getPid());
                bundle.putString("bimg",themeFirst.getBimg());
                utils.jumpAty(mActivity,ThemeActivity.class,bundle);
                break;
        }
    }
}
