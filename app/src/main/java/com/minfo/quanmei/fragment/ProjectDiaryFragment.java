package com.minfo.quanmei.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.NoteDetailActivity;
import com.minfo.quanmei.adapter.ProjectDiaryAdapter;
import com.minfo.quanmei.entity.ChildCategory;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProjectDiaryFragment extends BaseFragment {


    private View view;
    private RefreshListView listView;
    private int page = 1;
    private int id = 0;
    private List<GroupArticle> dataList = new ArrayList<GroupArticle>();
    private List<GroupArticle> tempList = new ArrayList<GroupArticle>();
    private ChildCategory childCategory;
    private ProjectDiaryAdapter startDiaryLVAdapter;
    private boolean downLoad;
    private boolean upload = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View initViews() {
        view = View.inflate(mActivity, R.layout.fragment_project_diary, null);
        listView = ((RefreshListView) view.findViewById(R.id.rlv_list_pro));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initData();
        reqArticleInfo(id);


    }

    public void initData() {
        childCategory = (ChildCategory) getActivity().getIntent().getSerializableExtra("childCategory");
        id = childCategory.getId();
        startDiaryLVAdapter = new ProjectDiaryAdapter(getActivity(), dataList);

        listView.setAdapter(startDiaryLVAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDiaryDetail = new Intent(getActivity(), NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", dataList.get(position - 1));
                intentToDiaryDetail.putExtra("info", bundle);
                startActivity(intentToDiaryDetail);
            }
        });
        listView.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                downLoad = true;
                page = 1;
                reqArticleInfo(id);

            }
        });
        listView.setIsCanLoad(upload);
        listView.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                upload = true;
                page++;
                reqArticleInfo(id);
                listView.loadComplete();

            }
        });

    }


    //项目相关日记接口
    private void reqArticleInfo(int i) {

        String articleInfoUrl = mActivity.getResources().getString(R.string.api_baseurl) + "project/Riji.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + i + "*" + page);

        httpClient.post(articleInfoUrl, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if(downLoad){
                    listView.refreshComplete();
                    downLoad = false;
                    dataList.clear();
                }
                if(upload){
                    listView.loadComplete();
                    upload = false;
                }
                tempList = response.getList(GroupArticle.class);

                dataList.addAll(tempList);
                startDiaryLVAdapter.notifyDataSetChanged();
                tempList.clear();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                listView.loadComplete();
                listView.refreshComplete();
                upload = false;
                downLoad = false;
            }

            @Override
            public void onRequestError(int code, String msg) {
                listView.loadComplete();
                listView.refreshComplete();
                upload = false;
                downLoad = false;
                ToastUtils.show(mActivity, msg);
            }
        });
    }


}
