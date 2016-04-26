package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天群页面
 * 2015年10月5日
 * zhang jiachang
 */
public class ChatGroupActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView message;
    private RelativeLayout moreGroup;

    private RefreshListView listView;
    private TextView moreTV;
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.chatgroup_back));
        message = ((ImageView) findViewById(R.id.iv_message));
        moreGroup = ((RelativeLayout) findViewById(R.id.rl_more_chatgroup));
        View view = LayoutInflater.from(this).inflate(R.layout.layout_chatgroup_bodyview, null);

        listView = ((RefreshListView) findViewById(R.id.llv_chatgroup));
        //moreTV = ((TextView) findViewById(R.id.tv_more_chatgroup));
        listView.addHeaderView(view);
    }

    @Override
    protected void initViews() {
        back.setOnClickListener(this);
        message.setOnClickListener(this);
        moreGroup.setOnClickListener(this);
        //moreTV.setOnClickListener(this);
        for (int i = 0; i < 2; i++) {
            list.add("赚钻钻");
        }

        listView.setAdapter(new CommonAdapter<String>(this, list, R.layout.item_list_chatgroup) {
            @Override
            public void convert(BaseViewHolder helper, String item, int position) {
                helper.setText(R.id.tv_name_groupitem,item);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(ChatGroupActivity.this, GroupMessageActivity.class));

            }
        });
        listView.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        listView.refreshComplete();

                        ToastUtils.show(ChatGroupActivity.this, "刷新成功");
                    }

                }, 2000);
            }
        });




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatgroup_back:
                finish();
                break;
            case R.id.iv_message:
                break;
            case R.id.rl_more_chatgroup:
                startActivity(new Intent(ChatGroupActivity.this,JoinGroupActivity.class));
                break;


        }

    }
}
