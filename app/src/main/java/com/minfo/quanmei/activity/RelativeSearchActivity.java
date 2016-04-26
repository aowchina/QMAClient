package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.adapter.ProjectDiaryAdapter;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.Product;
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

public class RelativeSearchActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvRelative;
    private ImageView ivBack;
    private LimitGridView lgvProject;
    private LimitListView diaryListView;
    private LimitListView tieZiListView;
    private String searchStr = "";
    private String searchData = "";
    private View view;
    private PullScrollView scroll;
    private int diaryPage = 1;
    private int tieZiPage = 1;
    private int teHuiPage = 1;
    private boolean diaryDown;
    private boolean tieZiDown;
    private boolean teHuiDown;
    private List<GroupArticle> diaryList = new ArrayList<GroupArticle>();
    private List<GroupArticle> diaryTempList = new ArrayList<GroupArticle>();
    private ProjectDiaryAdapter startDiaryLVAdapter;

    private List<GroupArticle> tieZiList = new ArrayList<GroupArticle>();
    private List<GroupArticle> tieZiTempList = new ArrayList<GroupArticle>();
    private ProjectDiaryAdapter startTieZiLVAdapter;

    private CommonAdapter<Product> projectAdapter;

    private List<Product> projectList = new ArrayList<>();
    private List<Product> projectTempList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_search);
    }

    @Override
    protected void findViews() {

        ivBack = (ImageView) findViewById(R.id.iv_left);

        tvRelative = (TextView) findViewById(R.id.tv_title);
        ivBack.setVisibility(View.VISIBLE);
        tvRelative.setVisibility(View.VISIBLE);


        scroll = ((PullScrollView) findViewById(R.id.psl_relative));
        view = LayoutInflater.from(this).inflate(R.layout.layout_relative_bodyview, null);
        lgvProject = (LimitGridView) view.findViewById(R.id.grid_project_relative);
        diaryListView = (LimitListView) view.findViewById(R.id.rsl_diary);
        tieZiListView = (LimitListView) view.findViewById(R.id.rsl_tiezi);
        scroll.addBodyView(view);

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        String searchType = getIntent().getStringExtra("search");
        if (searchType != null && !searchType.equals("")) {
            searchStr = searchType;
        }
        String searchContent = getIntent().getStringExtra("searchStr");
        if (searchContent != null && !searchContent.equals("")) {
            searchData = searchContent;
        }
        if (searchStr.equals("project")) {
            setTeHui();
        } else if (searchStr.equals("note")) {

            setTieZi();
        } else if (searchStr.equals("diary")) {
            setDiary();
        }

    }
    //日记相关操作

    public void setDiary() {

        diaryListView.setVisibility(View.VISIBLE);

        tvRelative.setText("相关日记");
        startDiaryLVAdapter = new ProjectDiaryAdapter(this, diaryList);
        diaryListView.setAdapter(startDiaryLVAdapter);
        reqDiary(diaryPage);
        scroll.setIsCanLoad(true);
        scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                diaryDown = true;
                diaryPage = 1;
                diaryList.clear();
                reqDiary(diaryPage);
            }
        });
        scroll.setOnLoadListener(new PullScrollView.OnLoadListener() {
            @Override
            public void loadMore() {
                diaryPage++;
                reqDiary(diaryPage);
                scroll.setfooterViewGone();
            }
        });

        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDiaryDetail = new Intent(RelativeSearchActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", diaryList.get(position));
                intentToDiaryDetail.putExtra("info", bundle);
                startActivity(intentToDiaryDetail);
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

    /**
     * 请求日记数据
     */
    private void reqDiary(final int page) {
        String url = getResources().getString(R.string.api_baseurl) + "wenzhang/ChoiceWz.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + searchData + "*" + 1 + "*" + page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                diaryTempList = response.getList(GroupArticle.class);
                if (diaryPage >= 1 && diaryTempList != null && diaryTempList.size() > 0) {
                    diaryList.addAll(diaryTempList);
                } else {
                    if (page > 1) {
                        ToastUtils.show(RelativeSearchActivity.this, "数据加载完毕");
                    }
                }
                if (diaryDown) {
                    diaryDown = false;
                    scroll.setheaderViewReset();
                    ToastUtils.show(RelativeSearchActivity.this, "数据刷新完毕");
                }
                startDiaryLVAdapter.notifyDataSetChanged();
                diaryTempList.clear();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (diaryDown) {
                    diaryDown = false;
                    scroll.setheaderViewReset();
                }
                if (errorcode == 10) {
                    ToastUtils.show(RelativeSearchActivity.this, "搜索条件为空");
                } else {
                    ToastUtils.show(RelativeSearchActivity.this, "服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(RelativeSearchActivity.this, msg);
            }
        });
    }


    //帖子相关操作
    public void setTieZi() {
        tieZiListView.setVisibility(View.VISIBLE);
        tvRelative.setText("相关帖子");
        startTieZiLVAdapter = new ProjectDiaryAdapter(this, tieZiList);

        tieZiListView.setAdapter(startTieZiLVAdapter);
        reqTieZi(tieZiPage);
        tieZiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDiaryDetail = new Intent(RelativeSearchActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", tieZiList.get(position));
                intentToDiaryDetail.putExtra("info", bundle);
                startActivity(intentToDiaryDetail);
            }
        });
        scroll.setIsCanLoad(true);
        scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                Log.e(TAG, "日记下拉刷新");
                tieZiDown = true;
                tieZiPage = 1;
                tieZiList.clear();
                reqTieZi(tieZiPage);
            }
        });
        scroll.setOnLoadListener(new PullScrollView.OnLoadListener() {
            @Override
            public void loadMore() {
                tieZiPage++;
                reqTieZi(tieZiPage);
                scroll.setfooterViewGone();
            }
        });
    }


    /**
     * 请求帖子数据
     */
    private void reqTieZi(final int page) {
        String url = getResources().getString(R.string.api_baseurl) + "wenzhang/ChoiceWz.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + searchData + "*" + 2 + "*" + page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                tieZiTempList = response.getList(GroupArticle.class);
                if (tieZiPage >= 1 && tieZiTempList != null && tieZiTempList.size() > 0) {
                    tieZiList.addAll(tieZiTempList);
                } else {
                    if(page>1) {
                        ToastUtils.show(RelativeSearchActivity.this, "数据加载完毕");
                    }
                }
                if (tieZiDown) {
                    tieZiDown = false;
                    scroll.setheaderViewReset();
                    ToastUtils.show(RelativeSearchActivity.this, "数据刷新完毕");
                }
                startTieZiLVAdapter.notifyDataSetChanged();
                tieZiTempList.clear();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (tieZiDown) {
                    tieZiDown = false;
                    scroll.setheaderViewReset();
                }
                if (errorcode == 10) {
                    ToastUtils.show(RelativeSearchActivity.this, "搜索条件为空", Toast.LENGTH_SHORT);
                } else {
                    ToastUtils.show(RelativeSearchActivity.this, "服务器繁忙", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onRequestError(int code, String msg) {

            }
        });
    }


    //特惠相关操作
    public void setTeHui() {
        tvRelative.setText("相关项目");
        scroll.setVisibility(View.VISIBLE);
        lgvProject.setVisibility(View.VISIBLE);
        projectAdapter = new CommonAdapter<Product>(this, projectList.size() > 4 ? projectList.subList(0, 4) : projectList, R.layout.hosgrid_item) {
            @Override
            public void convert(BaseViewHolder helper, Product item, int position) {


                helper.setText(R.id.hosgrid_np, item.getNewval() + "");
                helper.setText(R.id.hosgrid_op, item.getOldval() + "");
                helper.setText(R.id.hg_content, item.getFname());
                helper.setText(R.id.hg_hos, item.getHname());
                TextView tvOld = helper.getView(R.id.hosgrid_op);
                tvOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView img = helper.getView(R.id.hosgrid_img);
                int ScreenWidth = utils.getScreenWidth();
                ViewGroup.LayoutParams params = img.getLayoutParams();
                params.width = ScreenWidth / 2;
                params.height = ScreenWidth / 2;

                img.setLayoutParams(params);
                UniversalImageUtils.displayImageUseDefOptions(item.getSimg(), img);
            }
        };
        lgvProject.setAdapter(projectAdapter);
        reqTeHui(teHuiPage);
        scroll.setIsCanLoad(true);
        lgvProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", projectList.get(position));
                utils.jumpAty(RelativeSearchActivity.this, ProductDetailActivity.class, bundle);
            }
        });
        scroll.setOnRefreshListener(new PullScrollView.OnRefreshListener() {
            @Override
            public void refresh() {
                teHuiDown = true;
                teHuiPage = 1;
                projectList.clear();
                reqTeHui(teHuiPage);
            }
        });
        scroll.setOnLoadListener(new PullScrollView.OnLoadListener() {
            @Override
            public void loadMore() {
                teHuiPage++;
                reqTeHui(teHuiPage);
                scroll.setfooterViewGone();
            }
        });

    }

    /**
     * 请求特惠页面数据
     */
    private void reqTeHui(final int page) {
        String url = getResources().getString(R.string.api_baseurl) + "tehui/ChoiceTh.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + searchData + "*" + page);
        Log.e("首页数据", params + "");
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                projectTempList = response.getList(Product.class);
                if (teHuiPage >= 1 && projectTempList != null && projectTempList.size() > 0) {
                    projectList.addAll(projectTempList);
                } else {
                    if(page>1) {
                        ToastUtils.show(RelativeSearchActivity.this, "数据加载完毕");
                    }
                }
                if (teHuiDown) {
                    teHuiDown = false;
                    scroll.setheaderViewReset();
                    ToastUtils.show(RelativeSearchActivity.this, "数据刷新完毕");
                }
                projectAdapter.notifyDataSetChanged();
                projectTempList.clear();

            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                Log.e("特惠请求无数据", response.getErrorcode() + "");
                int errorcode = response.getErrorcode();
                if (teHuiDown) {
                    teHuiDown = false;
                    scroll.setheaderViewReset();
                }
                if (errorcode == 10) {
                    ToastUtils.show(RelativeSearchActivity.this, "搜索条件为空");
                } else {
                    ToastUtils.show(RelativeSearchActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(RelativeSearchActivity.this, msg);
            }
        });
    }


}
