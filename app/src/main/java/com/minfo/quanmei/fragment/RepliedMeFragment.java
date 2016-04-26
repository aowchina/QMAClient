package com.minfo.quanmei.fragment;

import android.os.Bundle;
import android.view.View;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.ReplyMeAdapter;
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
 * 回复我的
 */
public class RepliedMeFragment extends BaseFragment {

    private RefreshListView rflReplyMe;

    private List<GroupArticle> dataList = new ArrayList<>();
    private List<GroupArticle> tempList = new ArrayList<>();
    private ReplyMeAdapter replyMeAdapter;
    private int page = 1;
    private boolean isRefreshing;
    private boolean isLoading;


    public RepliedMeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_reply_me, null);
        rflReplyMe = (RefreshListView) view.findViewById(R.id.rlv_reply_me);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        reqServer();
    }


    public void initData() {
        replyMeAdapter = new ReplyMeAdapter(mActivity,dataList);
        rflReplyMe.setIsCanRefresh(true);
        rflReplyMe.setIsCanLoad(true);
        rflReplyMe.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefreshing = true;
                reqServer();
            }
        });
        rflReplyMe.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                isLoading = true;
                page++;
                reqServer();
            }
        });
        rflReplyMe.setAdapter(replyMeAdapter);
    }


    private void reqServer() {

        String url = getString(R.string.api_baseurl)+"user/BhfList.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.user.getUserid()+"*"+page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                rflReplyMe.refreshComplete();
                rflReplyMe.loadComplete();
                if(isRefreshing){
                    dataList.clear();
                }


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
                    replyMeAdapter.notifyDataSetChanged();

                }
                tempList.clear();
                isRefreshing = false;
                isLoading = false;
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                rflReplyMe.refreshComplete();
                rflReplyMe.loadComplete();
                isRefreshing = false;
                isLoading = false;
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                rflReplyMe.refreshComplete();
                rflReplyMe.loadComplete();
                isRefreshing = false;
                isLoading = false;
                ToastUtils.show(mActivity,msg);
            }
        });

    }


}
