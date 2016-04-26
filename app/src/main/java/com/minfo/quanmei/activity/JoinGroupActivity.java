package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.GroupActionAdapter;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LimitListView;
import com.minfo.quanmei.widget.PullScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 群类别页面
 * 2015年10月6日
 * zhang jiachang
 */
public class JoinGroupActivity extends BaseActivity implements View.OnClickListener, PullScrollView.OnRefreshListener {

    private ImageView back;
    private EditText searchET;
    private PullScrollView scroll;
    private ImageView message;
    private LimitListView listView;
    private LinearLayout lableContiner;
    private TextView relativeGroup;
    private List<TextView> textViews = new ArrayList<TextView>();
    private List<View> views = new ArrayList<View>();
    private String[] lable = {"双眼皮", "单眼皮", "二次精装", "美型整修", "塌塌鼻", "驼峰鼻", "鼻部矫正", "韩式芭比", "大脸猫", "巴掌脸", "明星脸", "深V", "飞机场", "郑多燕", "瘦腰提臀", "细长直美腿", "辣妈帮", "美白白", "干皮肤", "大油皮", "敏感肌", "逆龄生长", "龅牙妹", "牙齿美白", "嘟嘟唇", "赴韩思密达", "运势面相", "星座情感", "美容美妆"};
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.joingroup_back));
        searchET = ((EditText) findViewById(R.id.et_search_joingroup));
        scroll = ((PullScrollView) findViewById(R.id.psl_joingroup));
        message = ((ImageView) findViewById(R.id.iv_message));
        View view = ((View) LayoutInflater.from(this).inflate(R.layout.layout_joingroup_bodyview, null));
        listView = ((LimitListView) view.findViewById(R.id.llv_joingroup));
        lableContiner = ((LinearLayout) view.findViewById(R.id.ll_lable_joingroup));
        relativeGroup = ((TextView) view.findViewById(R.id.tv_relative_joingroup));
        scroll.addBodyView(view);
        back.setOnClickListener(this);
        scroll.setOnRefreshListener(this);
        message.setOnClickListener(this);
        scroll.setfooterViewGone();
    }

    @Override
    protected void initViews() {
        getGroupLable();
        for (int i = 0; i < 4; i++) {
            list.add("赚钻钻");
        }
        listView.setAdapter(new GroupActionAdapter(this, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(JoinGroupActivity.this, GroupMessageActivity.class));

            }
        });

    }

    //获取群标签HorizontalScrollView布局
    public void getGroupLable() {
        for (int i = 0; i < lable.length; i++) {
            View v1 = new View(this);
            v1.setLayoutParams(new RelativeLayout.LayoutParams(40,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            View v2 = new View(this);
            v2.setLayoutParams(new RelativeLayout.LayoutParams(40,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextView tempTv = new TextView(this);
            tempTv.setText(lable[i]);
            tempTv.setGravity(Gravity.CENTER);
            tempTv.setTextColor(Color.GRAY);
            tempTv.setBackgroundResource(R.drawable.text_group_unchose);
            tempTv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));
            lableContiner.addView(v1);
            views.add(i, v1);
            lableContiner.addView(tempTv);
            if (i == lable.length - 1) {
                lableContiner.addView(v2);
                views.add(i + 1, v2);
            }
            textViews.add(i, tempTv);
        }
        for (int i = 0; i < textViews.size(); i++) {
            final int j = i;
            textViews.get(i).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    textViews.get(j).setTextColor(Color.WHITE);
                    textViews.get(j).setBackgroundResource(R.drawable.text_joingroup_chose);
                    for (int k = 0; k < textViews.size(); k++) {
                        if (k != j) {
                            textViews.get(k).setTextColor(Color.GRAY);
                            textViews.get(k).setBackgroundResource(R.drawable.text_group_unchose);
                        }
                    }
                    //更新数据
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joingroup_back:
                finish();
                break;
            case R.id.iv_message:
                break;
        }
    }

    @Override
    public void refresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                scroll.setheaderViewReset();
                List<String> listD = new ArrayList<String>();
                for (int i = 0; i < 2; i++) {
                    listD.add(i + "目");
                }
                //groupPullAdapter.addAll(listD);
                ToastUtils.show(JoinGroupActivity.this, "刷新成功");
            }

        }, 2000);
    }


}
