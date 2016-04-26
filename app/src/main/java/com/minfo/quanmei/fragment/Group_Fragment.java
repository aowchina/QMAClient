package com.minfo.quanmei.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.GroupTypeActivity;
import com.minfo.quanmei.activity.NoteDetailActivity;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.adapter.GroupHeadGVAdapter;
import com.minfo.quanmei.adapter.GroupHeadListAdapter;
import com.minfo.quanmei.adapter.GroupPullAdapter;
import com.minfo.quanmei.entity.Group;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitGridView;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.PullScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 小组面
 * 2015年8月28日
 * zhang jiachang
 */
public class Group_Fragment extends BaseFragment implements View.OnClickListener {

    private ListView pullToListView;
    private ListView lView;
    private LimitGridView headGridView;
    private LimitListView headListView;
    private List<GroupArticle> dataList = new ArrayList<GroupArticle>();
    private GroupHeadGVAdapter groupHeadGVAdapter;
    private GroupHeadListAdapter groupHeadListAdapter;
    private GroupPullAdapter groupPullAdapter;
    private PullScrollView pullScrollView;

    private LinearLayout llLoadMore;
    private LinearLayout llLoadMoreLoading;

    private List<Group> groupList = new ArrayList<>();
    private CommonAdapter<Group> groupGridAdapter;

    private int page = 1;

    private List<GroupArticle> tempList = new ArrayList<GroupArticle>();

    private View footView;

    private boolean isFirstIn = true;
    //文章参数
    private int gid;
    private int tid;

    public static Group_Fragment newInstance() {


        Group_Fragment fragment = new Group_Fragment();

        return fragment;
    }

    public Group_Fragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_group, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullScrollView = ((PullScrollView) view.findViewById(R.id.psl_group));

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.group_headview, null);
//        footView = LayoutInflater.from(getActivity()).inflate(R.layout.group_footview, null);

        llLoadMore = (LinearLayout) headView.findViewById(R.id.ll_more_group);
        llLoadMore = (LinearLayout) headView.findViewById(R.id.ll_more_group);
        llLoadMoreLoading = (LinearLayout) headView.findViewById(R.id.ll_more_diary_loading);
        llLoadMore.setOnClickListener(this);
        headGridView = ((LimitGridView) headView.findViewById(R.id.gv_group));
        headListView = ((LimitListView) headView.findViewById(R.id.lv_group));
        pullToListView = ((ListView) headView.findViewById(R.id.lv_groupfragment));
        pullScrollView.addBodyView(headView);
        //pullScrollView.addBodyView(footView);
        reqGroupInfo();
        reqArticleInfo(1, 0, 0, page);

        pullScrollView.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                page = 1;
                reqArticleInfo(1, 0, 0, page);
            }
        });


        pullToListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToNoteDetail = new Intent(getActivity(), NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", dataList.get(position));
                intentToNoteDetail.putExtra("ID", 1);
                intentToNoteDetail.putExtra("info", bundle);
                startActivity(intentToNoteDetail);
            }
        });

    }
    //小组列表请求
    private void reqGroupInfo() {
        String groupInfoUrl = mActivity.getResources().getString(R.string.api_baseurl) + "group/List.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr());
        httpClient.post(groupInfoUrl, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                //GroupList groups = response.getObj(GroupList.class);
                groupList = response.getList(Group.class);


                groupGridAdapter = new CommonAdapter<Group>(mActivity, groupList, R.layout.group_grid_item) {
                    @Override
                    public void convert(BaseViewHolder helper, Group item, int position) {
                        Group temp = groupList.get(position);
                        helper.setText(R.id.tv_kind_group, temp.getName());
                        UniversalImageUtils.displayImageUseDefOptions(temp.getIcon(), (ImageView) helper.getView(R.id.iv_kind_group));
                    }
                };

                headGridView.setAdapter(groupGridAdapter);
                headGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("group", groupList.get(position));
                        utils.jumpAty(mActivity, GroupTypeActivity.class, bundle);
                    }
                });
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(mActivity,"服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity,msg);
            }
        });
    }

    //文章接口
    private void reqArticleInfo(int i, int gid, int tid, final int page) {

        String articleInfoUrl = mActivity.getResources().getString(R.string.api_baseurl) + "wenzhang/List.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + i + "*" + gid + "*" + tid + "*" + page);

        httpClient.post(articleInfoUrl, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                llLoadMore.setVisibility(View.VISIBLE);
                llLoadMoreLoading.setVisibility(View.GONE);
                tempList = response.getList(GroupArticle.class);
                if (page == 1 && dataList != null && dataList.size() > 0) {
                    dataList.clear();
                }
                if (tempList != null && tempList.size() > 0) {
                    dataList.addAll(tempList);
                    if (dataList != null && dataList.size() > 0) {
                        groupHeadListAdapter = new GroupHeadListAdapter(getActivity(), dataList);
                        headListView.setAdapter(groupHeadListAdapter);
                    }
                } else {
                    ToastUtils.show(mActivity, "数据加载完毕");
                }
                groupPullAdapter = new GroupPullAdapter(getActivity(), dataList);
                pullToListView.setAdapter(groupPullAdapter);
                setListViewHeightBasedOnChildren(pullToListView);

                tempList.clear();
                pullScrollView.setheaderViewReset();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                llLoadMore.setVisibility(View.VISIBLE);
                llLoadMoreLoading.setVisibility(View.GONE);
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                llLoadMore.setVisibility(View.VISIBLE);
                llLoadMoreLoading.setVisibility(View.GONE);
                ToastUtils.show(mActivity, msg);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_more_group:
                llLoadMoreLoading.setVisibility(View.VISIBLE);
                llLoadMore.setVisibility(View.GONE);
                reqArticleInfo(1, 0, 0, ++page);
                break;
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
