package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.entity.SearchData;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitGridView;
import com.minfo.quanmei.widget.LimitListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llRelateTo;
    private TextView tvSearchKeyword;
    private TextView thCount;
    private TextView rjCount;
    private TextView tzCount;
    private ImageView ivBack;
    private RelativeLayout rlSearch;
    private EditText etSearch;

    private LinearLayout llRelateProject;//相关项目
    private LinearLayout llRelateDiary;//相关日记
    private LinearLayout llRelateNote;//相关帖子

    private LinearLayout llMoreProject;//查看全部项目
    private LinearLayout llMoreDiary;//查看全部日记
    private LinearLayout llMoreNote;//查看全部帖子

    private LimitGridView lgvProject;
    private LimitListView llvDiary;
    private LimitListView llvNote;


    private List<Product> projectList = new ArrayList<>();
    private List<GroupArticle> diarys = new ArrayList<GroupArticle>();
    private List<GroupArticle> tieZis = new ArrayList<GroupArticle>();

    private CommonAdapter<GroupArticle> diaryAdapter;
    private CommonAdapter<GroupArticle> tieZiAdapter;
    private CommonAdapter<Product> projectAdapter;

    private ImageView ivSearch;
    private String byteSearch = "";
    private int tz_amount = 0;
    private int rj_amount = 0;
    private int th_amount = 0;
    private SearchData searchData = new SearchData();
    private boolean initAdapter;
    private String content = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//设置弹出软键盘
    }

    @Override
    protected void findViews() {
        //top
        ivBack = (ImageView) findViewById(R.id.iv_left);
        ivSearch = ((ImageView) findViewById(R.id.iv_search));
        rlSearch = (RelativeLayout) findViewById(R.id.rl_search);
        etSearch = (EditText) findViewById(R.id.ed_search);
        tvSearchKeyword = (TextView) findViewById(R.id.tv_search_keyword);
        thCount = (TextView) findViewById(R.id.tv_th_count);
        rjCount = (TextView) findViewById(R.id.tv_rj_count);
        tzCount = (TextView) findViewById(R.id.tv_tz_count);
        llRelateTo = (LinearLayout) findViewById(R.id.ll_relate_to);
        ivBack.setVisibility(View.VISIBLE);
        rlSearch.setVisibility(View.VISIBLE);
        ivSearch.setImageResource(R.mipmap.main_icon_search);
        rlSearch.setBackgroundResource(R.mipmap.actionbar_search_ll_bg);
        etSearch.setTextColor(Color.GRAY);
        etSearch.setHintTextColor(Color.GRAY);

        llRelateProject = (LinearLayout) findViewById(R.id.ll_relate_project);
        llRelateDiary = (LinearLayout) findViewById(R.id.ll_relate_diary);
        llRelateNote = (LinearLayout) findViewById(R.id.ll_relate_note);
        llMoreProject = (LinearLayout) findViewById(R.id.ll_load_project);
        llMoreDiary = (LinearLayout) findViewById(R.id.ll_load_diary);
        llMoreNote = (LinearLayout) findViewById(R.id.ll_load_note);
        lgvProject = (LimitGridView) findViewById(R.id.grid_project);
        llvDiary = (LimitListView) findViewById(R.id.llv_diary);
        llvNote = (LimitListView) findViewById(R.id.llv_note);


    }


    @Override
    protected void initViews() {
        llMoreProject.setOnClickListener(this);
        llMoreNote.setOnClickListener(this);
        llMoreDiary.setOnClickListener(this);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    content = etSearch.getText().toString();
                    if (content.equals("") || content == null) {
                        ToastUtils.show(SearchActivity.this, "请输入内容！");
                    } else {
                        tvSearchKeyword.setText(content);
                        content = utils.convertChinese(content);
                        reqSearchList(content);
                    }
                }
                return false;
            }
        });
    }


    private void initAdapter() {
        initAdapter = true;
        llRelateTo.setVisibility(View.VISIBLE);
        //设置相关项目显示
        if (th_amount > 0) {
            lgvProject.setVisibility(View.VISIBLE);
            llRelateProject.setVisibility(View.VISIBLE);
            if (th_amount > 4) {
                llMoreProject.setVisibility(View.VISIBLE);
            } else {
                llMoreProject.setVisibility(View.GONE);
            }


        } else {
            lgvProject.setVisibility(View.GONE);
            llRelateProject.setVisibility(View.GONE);
            llMoreProject.setVisibility(View.GONE);
        }
        projectAdapter = new CommonAdapter<Product>(this, projectList.size() > 4 ? projectList.subList(0, 4) : projectList, R.layout.hosgrid_item) {
            @Override
            public void convert(BaseViewHolder helper, Product item, int position) {


                helper.setText(R.id.hosgrid_np, item.getNewval() + "");
                helper.setText(R.id.hosgrid_op, item.getOldval() + "");
                helper.setText(R.id.hg_content, "【" + item.getFname() + "】" + item.getName());
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
        lgvProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", projectList.get(position));
                utils.jumpAty(SearchActivity.this, ProductDetailActivity.class, bundle);
            }
        });

        //设置相关日记
        if (rj_amount > 0) {
            llvDiary.setVisibility(View.VISIBLE);
            llRelateDiary.setVisibility(View.VISIBLE);
            if (rj_amount > 5) {
                llMoreDiary.setVisibility(View.VISIBLE);
            } else {
                llMoreDiary.setVisibility(View.GONE);
            }


        } else {
            llvDiary.setVisibility(View.GONE);
            llRelateDiary.setVisibility(View.GONE);
            llMoreDiary.setVisibility(View.GONE);
        }
        diaryAdapter = new CommonAdapter<GroupArticle>(this, diarys.size() > 5 ? diarys.subList(0, 5) : diarys, R.layout.start_diary_item) {
            @Override
            public void convert(BaseViewHolder helper, GroupArticle item, int position) {
                helper.setText(R.id.tv_contentdia_start0, "【日记】" + item.getTitle());
                helper.setText(R.id.tv_body_start0, "[" + item.getGroupname() + "]");
                helper.setText(R.id.tv_usename_start0, item.getUsername());
                helper.setText(R.id.tv_time_start0, item.getPltime());
                helper.setText(R.id.tv_hand_start0, item.getZan() + "");
                helper.setText(R.id.tv_share_start0, item.getPl() + "");

                ImageView img1 = (ImageView) helper.getView(R.id.img_group1);
                ImageView img2 = (ImageView) helper.getView(R.id.img_group2);
                ImageView useIcon = (ImageView) helper.getView(R.id.iv_useicon_start0);
                if (item.getImgs() != null) {
                    if (item.getImgs().size() >= 2) {

                        UniversalImageUtils.displayImageUseDefOptions(item.getImgs().get(0), img1);
                        UniversalImageUtils.displayImageUseDefOptions(item.getImgs().get(1), img2);
                    } else if (item.getImgs().size() == 1) {
                        UniversalImageUtils.displayImageUseDefOptions(item.getImgs().get(0), img1);
                        img2.setVisibility(View.INVISIBLE);
                    } else {
                        img1.setVisibility(View.GONE);
                        img2.setVisibility(View.GONE);
                    }
                } else {
                    img1.setVisibility(View.GONE);
                    img2.setVisibility(View.GONE);
                }
                UniversalImageUtils.disCircleImage(item.getPluserimg(), useIcon);


            }
        };
        llvDiary.setAdapter(diaryAdapter);
        llvDiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDiaryDetail = new Intent(SearchActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", diarys.get(position));
                intentToDiaryDetail.putExtra("info", bundle);
                startActivity(intentToDiaryDetail);
            }
        });
        //设置相关帖子显示
        if (tz_amount > 0) {
            llvNote.setVisibility(View.VISIBLE);
            llRelateNote.setVisibility(View.VISIBLE);
            if (tz_amount > 5) {
                llMoreNote.setVisibility(View.VISIBLE);
            } else {
                llMoreNote.setVisibility(View.GONE);
            }


        } else {
            llvNote.setVisibility(View.GONE);
            llRelateNote.setVisibility(View.GONE);
            llMoreNote.setVisibility(View.GONE);
        }

        tieZiAdapter = new CommonAdapter<GroupArticle>(this, tieZis.size() > 5 ? tieZis.subList(0, 5) : tieZis, R.layout.start_diary_item) {
            @Override
            public void convert(BaseViewHolder helper, GroupArticle item, int position) {
                helper.setText(R.id.tv_contentdia_start0, item.getTitle());
                helper.setText(R.id.tv_body_start0, "[" + item.getGroupname() + "]");
                helper.setText(R.id.tv_usename_start0, item.getUsername());
                helper.setText(R.id.tv_time_start0, item.getPubtime());
                helper.setText(R.id.tv_hand_start0, item.getZan() + "");
                helper.setText(R.id.tv_share_start0, item.getPl() + "");

                ImageView img1 = helper.getView(R.id.img_group1);
                ImageView img2 = helper.getView(R.id.img_group2);
                ImageView useIcon = helper.getView(R.id.iv_useicon_start0);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                UniversalImageUtils.disCircleImage(item.getUserimg(), useIcon);
            }
        };
        llvNote.setAdapter(tieZiAdapter);
        llvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDiaryDetail = new Intent(SearchActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", tieZis.get(position));
                intentToDiaryDetail.putExtra("info", bundle);
                startActivity(intentToDiaryDetail);
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SearchActivity.this, RelativeSearchActivity.class);
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.ll_load_project:
                intent.putExtra("search", "project");
                intent.putExtra("searchStr", content);
                startActivity(intent);
                break;
            case R.id.ll_load_note:
                intent.putExtra("search", "note");
                intent.putExtra("searchStr", content);
                startActivity(intent);
                break;
            case R.id.ll_load_diary:
                intent.putExtra("search", "diary");
                intent.putExtra("searchStr", content);
                startActivity(intent);
                break;
        }
    }

    /**
     * 请求搜索数据
     */
    private void reqSearchList(String bytes) {
        String url = getResources().getString(R.string.api_baseurl) + "public/Choice.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + bytes);
        Log.e(TAG, params + "");

        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {


            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                searchData = response.getObj(SearchData.class);
                Log.e(TAG, response + "");
                if (searchData != null) {
                    diarys = searchData.getRj();
                    projectList = searchData.getTh();
                    tieZis = searchData.getTz();
                    th_amount = Integer.parseInt(searchData.getTh_amount());
                    rj_amount = Integer.parseInt(searchData.getRj_amount());
                    tz_amount = Integer.parseInt(searchData.getTz_amount());
                }
                thCount.setText("(" + th_amount + ")");
                rjCount.setText("(" + rj_amount + ")");
                tzCount.setText("(" + tz_amount + ")");

                initAdapter();
                projectAdapter.notifyDataSetChanged();
                diaryAdapter.notifyDataSetChanged();
                tieZiAdapter.notifyDataSetChanged();


            }

            @Override
            public void onRequestNoData(BaseResponse response) {

                int errorcode = response.getErrorcode();
                if (errorcode == 10) {
                    ToastUtils.show(SearchActivity.this, "搜索条件为空");
                } else {
                    ToastUtils.show(SearchActivity.this, "服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(SearchActivity.this, msg);
            }
        });
    }

}
