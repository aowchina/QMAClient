package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.ScreenChooseAdapter;
import com.minfo.quanmei.entity.Special;
import com.minfo.quanmei.utils.Constant;

import java.util.ArrayList;
import java.util.List;
/**
 * 筛选选择页面
 * 2015年8月27日
 * zhang jiachang
 */
public class ScreenChoseActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private String[] screen = {"脸型", "眼部", "鼻部", "胸部", "皮肤", "身材", "牙齿", "毛发", "私密", "微整形", "双眼皮", "开眼角", "隆胸", "整形修复", "激光美肤", "注射美容", "玻尿酸", "BOTOX", "去眼袋"};
    private ScreenChooseAdapter screenChooseAdapter;
    private int temp;
    private int cho;
    private String kind;
    private boolean flag;
    private boolean bodyFlag;
    private boolean proFlag;
    private String bodyOrPro;
    private ImageView back;

    private int provinceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_chose);
        initView();
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViews() {

    }

    public void initView(){
        listView = ((ListView) findViewById(R.id.lv_chose));
        back = ((ImageView) findViewById(R.id.iv_backscreen_chose));
        back.setOnClickListener(this);
        cho=getIntent().getIntExtra("ID",0);
        kind=getIntent().getStringExtra("KIND");
        provinceId = getIntent().getIntExtra("PROVINCE", provinceId);

        final List<Special> list = new ArrayList<Special>();

        //判断已筛选的是部位还是项目
        if(cho==1) {//部位
            for (int i = 0; i < 3; i++) {
                list.add(Constant.specialList.get(i));
            }
            if (!kind.equals("")) {
                bodyFlag = true;
            }
            bodyOrPro="body";
        }else if(cho==2){//项目
            for (int i = 3; i < Constant.specialList.size(); i++) {
                list.add(Constant.specialList.get(i));
            }
            if (!kind.equals("")) {
                proFlag = true;
            }
            bodyOrPro="pro";
        }
        for (int i = 0; i <list.size() ; i++) {
            if(kind.equals(list.get(i).getName())){
                temp=i;
            }
        }


        screenChooseAdapter = new ScreenChooseAdapter(this,list,temp,bodyFlag,proFlag,bodyOrPro);
        listView.setAdapter(screenChooseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScreenChoseActivity.this, SpecialScreenActivity.class);
                String string = ((TextView) view.findViewById(R.id.tv_local_name)).getText().toString();




                intent.putExtra("POSTION", string);
                intent.putExtra("POS", 20);
                if (cho==1){
                    intent.putExtra("BODY",1);
                    intent.putExtra("ID",position);

                }else {
                    intent.putExtra("BODY",2);
                    intent.putExtra("ID",position+3);
                }
                intent.putExtra("TOSS",1);
                intent.putExtra("PROVINCE",provinceId);
                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_backscreen_chose:
                finish();
                //startActivity(new Intent(ScreenChoseActivity.this,SpecialScreenActivity.class));
                break;
        }
    }
}

