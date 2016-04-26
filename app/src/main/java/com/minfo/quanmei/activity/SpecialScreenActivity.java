package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.ScreenGridAdapter;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.widget.LimitGridView;

/**
 * 特惠筛选页面
 * 2015年8月28日
 * zhang jiachang
 */
public class SpecialScreenActivity extends BaseActivity implements View.OnClickListener {

    private LimitGridView gridView;
    private TextView chose;
    private TextView unChose;
    private TextView kindBody;
    private TextView kindPro;
    private ScreenGridAdapter screenGridAdapter;
    private ScrollView scrollView;
    private ImageView back;
    private boolean flag = false;
    private boolean flag2 = false;
    private String kindString = "";
    private int temp = 0;
    private LinearLayout lineBody;
    private LinearLayout linePro;
    private static int bodyOrPro;

    private int provinceId;
    private int formSc;
    private Intent toSpecial;

    private ImageView ivLeft;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        initView();
    }

    public void initView() {
        //top
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("筛选");
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);

        gridView = ((LimitGridView) findViewById(R.id.lgr_screen));
        scrollView = ((ScrollView) findViewById(R.id.scroll_screen));
        back = ((ImageView) findViewById(R.id.iv_left));
        scrollView.scrollTo(0, 0);
        gridView.setFocusable(false);

        chose = ((TextView) findViewById(R.id.tv_chose_screen));
        unChose = ((TextView) findViewById(R.id.tv_unchose_screen));
        kindBody = ((TextView) findViewById(R.id.tv_kind_scr));
        kindPro = ((TextView) findViewById(R.id.tv_kind_pro));
        lineBody = ((LinearLayout) findViewById(R.id.ll_body_screen));
        linePro = ((LinearLayout) findViewById(R.id.ll_body_screen));
        lineBody.setOnClickListener(this);
        unChose.setOnClickListener(this);
        linePro.setOnClickListener(this);
        back.setOnClickListener(this);
        setData();



        screenGridAdapter = new ScreenGridAdapter(this, Constant.specialList, temp, flag);
        gridView.setAdapter(screenGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tv_kind_screen);
                tv.setTextColor(Color.RED);

                tv.setBackgroundResource(R.drawable.text_screenclick);
                String str = Constant.specialList.get(position).getName();
                if (flag) {

                    TextView tvTemp = (TextView) gridView.getChildAt(temp).findViewById(R.id.tv_kind_screen);
                    tvTemp.setTextColor(Color.BLACK);
                    tvTemp.setBackgroundResource(R.drawable.text_screen_shape);
                }
                SpecialActivity.specialActivityInstance.finish();
                Intent strIntent = new Intent(SpecialScreenActivity.this, SpecialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("special",Constant.specialList.get(position));
                strIntent.putExtra("info", bundle);
                strIntent.putExtra("KIND", str);
                strIntent.putExtra("ID", 2);
                strIntent.putExtra("PROVINCE",provinceId);
                strIntent.putExtra("KINDID",Constant.specialList.get(position).getId());
                startActivity(strIntent);
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (formSc==1){
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    SpecialActivity.specialActivityInstance.finish();
                    startActivity(toSpecial);
                    finish();
                }

            }, 1000);
        }
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViews() {

    }

    public void setData() {
        //判断来自特惠筛选页的数据还是来自筛选选取页的数据
        int pos = getIntent().getIntExtra("POS", 0);

        if (pos == 20) {//来自筛选选取页的数据
            String cho = getIntent().getStringExtra("POSTION").toString();
            bodyOrPro = getIntent().getIntExtra("BODY", 0);
            provinceId = getIntent().getIntExtra("PROVINCE",0);
            int position =getIntent().getIntExtra("ID",0);
            formSc=getIntent().getIntExtra("TOSS",0);
            toSpecial = new Intent(SpecialScreenActivity.this, SpecialActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("special", Constant.specialList.get(position));
            toSpecial.putExtra("info", bundle);
            toSpecial.putExtra("KIND", cho);
            toSpecial.putExtra("KINDID",Constant.specialList.get(position).getId());
            toSpecial.putExtra("ID", 3);
            toSpecial.putExtra("PROVINCE", provinceId);



        } else if (pos == 10) {//来自特惠页的数据
            //flag=true大于一次来自特惠页（其数据来自特惠筛选页）的数据
            //flag2=true大于一次来自特惠页(其数据来自筛选选取页)的数据
            flag = getIntent().getBooleanExtra("FLAG", false);
            flag2 = getIntent().getBooleanExtra("flag", false);
            kindString = getIntent().getStringExtra("KIND");
            provinceId = getIntent().getIntExtra("PROVINCE",0);
            if (flag) {
                for (int i = 0; i < Constant.specialList.size(); i++) {
                    if (kindString.equals(Constant.specialList.get(i).getName())) {
                        chose.setText(getIntent().getStringExtra("LOC") + "," + kindString);
                        kindBody.setText("");
                        kindPro.setText("");

                        temp = i;

                    }
                }
            } else if (flag2) {
                for (int i = 0; i < Constant.specialList.size(); i++) {
                    if (kindString.equals(Constant.specialList.get(i).getName())) {
                        chose.setText(getIntent().getStringExtra("LOC") + "," + kindString);
                        if (bodyOrPro == 1) {//=1 选择部位
                            kindBody.setText(kindString);
                        } else if (bodyOrPro == 2) {//=2 选择项目

                            kindPro.setText(kindString);
                        }

                        temp = i;

                    }
                }
            } else {

                //判断是否取消筛选
                if (!kindString.equals("全部项目")) {
                    chose.setText(getIntent().getStringExtra("LOC") + "," + kindString);
                    //判断已筛选的是部位还是项目
                    if (!kindString.equals("微整形")) {
                        kindBody.setText(kindString);
                    } else {
                        kindPro.setText(kindString);
                    }
                } else {
                    chose.setText(kindString);
                    kindBody.setText("");
                    kindPro.setText("");
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_unchose_screen:
                Intent stIntent = new Intent(SpecialScreenActivity.this, SpecialActivity.class);
                //stIntent.putExtra("ID", 2);
                stIntent.putExtra("KIND", "全部项目");
                startActivity(stIntent);
                finish();
                break;
            case R.id.ll_body_screen:
                Intent bIntent = new Intent(SpecialScreenActivity.this, ScreenChoseActivity.class);
                bIntent.putExtra("ID", 1);
                bIntent.putExtra("KIND", kindBody.getText().toString());
                bIntent.putExtra("PROVINCE",provinceId);
                startActivity(bIntent);
                finish();
                break;
            case R.id.ll_pro_screen:
                Intent pIntent = new Intent(SpecialScreenActivity.this, ScreenChoseActivity.class);
                pIntent.putExtra("ID", 2);
                pIntent.putExtra("KIND", kindPro.getText().toString());
                pIntent.putExtra("PROVINCE",provinceId);
                startActivity(pIntent);
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }
}
