package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.PersonalHomePageItem1Adapter;
import com.minfo.quanmei.adapter.PersonalHomePageItem2Adapter;
import com.minfo.quanmei.adapter.PersonalHomePageItem3Adapter;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fei on 15/11/3.
 *
 * @funcation 个人中心页面
 */
public class PersonalHomePageActivity extends BaseActivity implements View.OnClickListener {

    private List<GroupArticle> diary = new ArrayList<GroupArticle>();
    private List<GroupArticle> note = new ArrayList<GroupArticle>();
    private List<GroupArticle> reply = new ArrayList<GroupArticle>();
    private User currentUser;
    private String userid = "";
    //top
    private ImageView ivLeft;
    private TextView tvTitle;

    private ImageView head_image;
    private ImageView ivInfobg;
    private TextView personal_code, personal_level, personal_age, personal_address;
    private TextView tvjoingroup, tvdiary, tvnote, tvreply;
    private HorizontalScrollView personal_join_group;
    //listview
    private LimitListView lvdiary, lvNote, lvReply;
    private PersonalHomePageItem1Adapter adapter1;
    private PersonalHomePageItem2Adapter adapter2;
    private PersonalHomePageItem3Adapter adapter3;

    private LinearLayout ll_joingroup;
    private List<View> ll_joingroups = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_home_page);

    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("个人中心");
        ivLeft.setOnClickListener(this);


        head_image = (ImageView) findViewById(R.id.civ_head_image);
        personal_code = (TextView) findViewById(R.id.personal_code);
        personal_level = (TextView) findViewById(R.id.personal_level);
        personal_age = (TextView) findViewById(R.id.personal_age);
        personal_address = (TextView) findViewById(R.id.personal_address);
        ivInfobg = (ImageView) findViewById(R.id.iv_info_bg);


        tvjoingroup = (TextView) findViewById(R.id.tv_join_group);
        tvdiary = (TextView) findViewById(R.id.tv_diary);
        tvnote = (TextView) findViewById(R.id.tv_note);
        tvreply = (TextView) findViewById(R.id.tv_reply);

        personal_join_group = (HorizontalScrollView) findViewById(R.id.personal_join_group);
        ll_joingroup = (LinearLayout) findViewById(R.id.ll_joingroup);

        lvdiary = (LimitListView) findViewById(R.id.lv_personalhome_diary);
        lvNote = (LimitListView) findViewById(R.id.lv_personalhome_note);
        lvReply = (LimitListView) findViewById(R.id.lv_personalhome_reply);
        lvdiary.setFocusable(false);
        lvNote.setFocusable(false);
        lvReply.setFocusable(false);
    }

    @Override
    protected void initViews() {
        userid = getIntent().getStringExtra("userid");

        reqPersonalHome();
        reqPersonalDiary();
        reqPersonalnote();
        reqPersonalreply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
        }
    }

    /**
     * 请求个人主页（带加入小组）
     */
    private void reqPersonalHome() {
        String url = getResources().getString(R.string.api_baseurl) + "user/MainMore.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + userid);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                currentUser = response.getObj(User.class);
                UniversalImageUtils.disCircleImage(currentUser.getUserimg(), head_image);
                if(currentUser.getBgimg()!=null&&!"".equals(currentUser.getBgimg())) {
                    UniversalImageUtils.displayImageUseDefOptions(currentUser.getBgimg(), ivInfobg);
                }
                personal_code.setText(currentUser.getUsername());
                personal_age.setText(currentUser.getAge() + "");
                personal_level.setText("LV" + currentUser.getLevel());
                personal_address.setText(currentUser.getCity());


                for (int i = 0; i < currentUser.getGroup().size(); i++) {
                    View view = LayoutInflater.from(PersonalHomePageActivity.this).inflate(R.layout.personalhome_joingroup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_joingroup);
                    TextView textView = (TextView) view.findViewById(R.id.tv_joingroup);
                    UniversalImageUtils.displayImageUseDefOptions(currentUser.getGroup().get(i).getIcon(), imageView);
                    textView.setText(currentUser.getGroup().get(i).getName());
                    ll_joingroups.add(view);
                    ll_joingroup.addView(view);
                }
                for (int i = 0; i < currentUser.getGroup().size(); i++) {
                    final int j = i;
                    ll_joingroups.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PersonalHomePageActivity.this, GroupTypeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("group",currentUser.getGroup().get(j));
                            intent.putExtra("info",bundle);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(PersonalHomePageActivity.this,"服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(PersonalHomePageActivity.this,msg);
            }
        });
    }

    /**
     * 请求个人主页（日记）
     */
    private void reqPersonalDiary() {
        String url = getResources().getString(R.string.api_baseurl) + "user/WzList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + userid + "*" + 1 + "*" + 1);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                diary = response.getList(GroupArticle.class);
                if (diary.size() == 0) {
                    tvdiary.setVisibility(View.GONE);
                }
                adapter2 = new PersonalHomePageItem2Adapter(PersonalHomePageActivity.this, diary);
                lvdiary.setAdapter(adapter2);
                lvdiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intentToNoteDetail = new Intent(PersonalHomePageActivity.this, NoteDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("group", diary.get(position));
                        intentToNoteDetail.putExtra("ID", 1);
                        intentToNoteDetail.putExtra("info", bundle);
                        startActivity(intentToNoteDetail);
                    }
                });

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
            }

            @Override
            public void onRequestError(int code, String msg) {

            }
        });
    }

    /**
     * 请求个人主页（帖子）
     */
    private void reqPersonalnote() {
        String url = getResources().getString(R.string.api_baseurl) + "user/WzList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + userid + "*" + 2 + "*" + 1);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                note = response.getList(GroupArticle.class);
                if (note.size() == 0) {
                    tvnote.setVisibility(View.GONE);
                }
                adapter1 = new PersonalHomePageItem1Adapter(PersonalHomePageActivity.this, note);
                lvNote.setAdapter(adapter1);
                lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intentToNoteDetail = new Intent(PersonalHomePageActivity.this, NoteDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("group", note.get(position));
                        intentToNoteDetail.putExtra("ID", 1);
                        intentToNoteDetail.putExtra("info", bundle);
                        startActivity(intentToNoteDetail);
                    }
                });
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
            }

            @Override
            public void onRequestError(int code, String msg) {

            }
        });
    }

    /**
     * 请求个人主页（回复）
     */
    private void reqPersonalreply() {
        String url = getResources().getString(R.string.api_baseurl) + "user/HfList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + userid + "*" + 1);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                reply = response.getList(GroupArticle.class);
                if (reply.size() == 0) {
                    tvreply.setVisibility(View.GONE);
                }
                adapter3 = new PersonalHomePageItem3Adapter(PersonalHomePageActivity.this, reply);
                lvReply.setAdapter(adapter3);
                lvReply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intentToNoteDetail = new Intent(PersonalHomePageActivity.this, NoteDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("group", reply.get(position));
                        intentToNoteDetail.putExtra("ID", 1);
                        intentToNoteDetail.putExtra("info", bundle);
                        startActivity(intentToNoteDetail);
                    }
                });
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(PersonalHomePageActivity.this,"服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(PersonalHomePageActivity.this,msg);
            }
        });
    }
}
