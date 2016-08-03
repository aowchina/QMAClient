package com.minfo.quanmei.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.NoteDetailActivity;
import com.minfo.quanmei.adapter.ReproveMeAdapter;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 赞我的
 */
public class ReproveMeFragment extends BaseFragment {

    private RefreshListView lvReplyMeList;

    private List<GroupArticle> dataList = new ArrayList<>();
    private List<GroupArticle> tempList = new ArrayList<>();
    private ReproveMeAdapter reproveMeAdapter;
    private int page = 1;
    private boolean isRefreshing;
    private boolean isLoading;

    public ReproveMeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_reprove_me, null);
        lvReplyMeList = (RefreshListView) view.findViewById(R.id.rlv_reprove_me);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        reqServer();
    }


    public void initData() {

        reproveMeAdapter = new ReproveMeAdapter(mActivity,dataList);
        lvReplyMeList.setIsCanRefresh(true);
        lvReplyMeList.setIsCanLoad(true);
        lvReplyMeList.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefreshing = true;
                reqServer();
            }
        });
        lvReplyMeList.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                isLoading = true;
                page++;
                reqServer();
            }
        });
        lvReplyMeList.setAdapter(reproveMeAdapter);
        lvReplyMeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDiaryDetail = new Intent(getActivity(), NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", dataList.get(position-1));
                intentToDiaryDetail.putExtra("info", bundle);
                startActivity(intentToDiaryDetail);

            }
        });
    }


    private void reqServer() {

        String url = getString(R.string.api_baseurl)+"user/BzanList.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.user.getUserid()+"*"+page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                lvReplyMeList.refreshComplete();
                lvReplyMeList.loadComplete();
                if(isRefreshing){
                    dataList.clear();
                }

                Log.e("", response.getData());
                tempList = response.getList(GroupArticle.class);
                if (dataList != null) {
                    if (tempList != null && tempList.size() > 0) {
                        dataList.addAll(tempList);//当页码增加时，需要在之前的数据基础上加上现在请求的数据
                    } else {
                        if(page!=1&&isLoading){
                            ToastUtils.show(mActivity,"数据加载完毕");
                        }
                        if (page == 1 && dataList.size() <= 0) {
                            ToastUtils.show(mActivity, "没有相关数据");
                        }
                    }
                    reproveMeAdapter.notifyDataSetChanged();

                }
                tempList.clear();
                isRefreshing = false;
                isLoading = false;
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                lvReplyMeList.refreshComplete();
                lvReplyMeList.loadComplete();
                isRefreshing = false;
                isLoading = false;
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                lvReplyMeList.refreshComplete();
                lvReplyMeList.loadComplete();
                isRefreshing = false;
                isLoading = false;
                ToastUtils.show(mActivity,msg);
            }
        });

    }


}
