package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.GroupActionAdapter;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.PullScrollView;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 群活动页面
 * 2015年10月5日
 * zhang jiachang
 */
public class GroupActionActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private PullScrollView scroll;
    private RefreshListView listView;
    private List<String> list = new ArrayList<String>();
    private String[] hospitalName = {"北京美容医院", "上海美容医院", "天津美容医院", "重庆美容医院"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_action);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.groupaction_back));
       // scroll = ((PullScrollView) findViewById(R.id.psl_groupaction));
        View view = LayoutInflater.from(this).inflate(R.layout.layout_groupaction_bodyview, null);
        listView = ((RefreshListView)findViewById(R.id.llv_groupaction));
        listView.addHeaderView(view);
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        return true;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
        //scroll.addBodyView(view);

    }

    @Override
    protected void initViews() {
        back.setOnClickListener(this);
        for (int i = 0; i < 4; i++) {
            list.add(hospitalName[i]);
        }
        listView.setAdapter(new GroupActionAdapter(this, list));
        listView.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    //
                    @Override
                    public void run() {

                        //specialGridAdapter.addList(list);
                        listView.refreshComplete();
                        ToastUtils.show(GroupActionActivity.this, "数据刷新完毕");
                    }

                }, 2000);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.groupaction_back:
                finish();
                break;
        }
    }
}
