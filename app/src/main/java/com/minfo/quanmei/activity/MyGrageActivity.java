package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.MyGradeAdapter;
import com.minfo.quanmei.entity.MyGrade;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitListView;

import java.util.ArrayList;
import java.util.List;

public class MyGrageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView icon;
    private TextView nickname;
    private TextView active;
    private TextView newInvitation;
    private TextView tv_grade;
    private TextView response;
    private ScrollView scroll;
    private LimitListView starListView;
    private LimitListView gradeListView;
    private List<MyGrade> listStar = new ArrayList<MyGrade>();
    private List<MyGrade> listGrade = new ArrayList<MyGrade>();
    private String star[] = {"0-19", "20-199", "200-499", "500-999", "1000-2999", "3000-4999", "5000-9999", "10000-49999", "50000-99999", "1000000-499999", "500000-999999", "1000000+"};
    private String grade[] = {"注册后首次登陆－9次", "10-29次", "30-99次", "100-199次", "200-299次", "300-599次", "600-999次", "1000-1399次", "1400-1999次", "2000次以上"};
    private LinearLayout llContiner;
    private List<ImageView> imageViews = new ArrayList<ImageView>();
    private List<View> views = new ArrayList<View>();




    private User user;
    private TextView tvActivePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_grage);

        if (Constant.user != null) {
            user = Constant.user;
            UniversalImageUtils.disCircleImage(user.getUserimg(), icon);
            nickname.setText(user.getUsername());
            tv_grade.setText("LV" + user.getLevel() + "");
            tvActivePoint.setText(user.getPoint()+"");
        }
    }

    @Override
    protected void findViews() {

        back = ((ImageView) findViewById(R.id.mygrade_back));
        scroll = ((ScrollView) findViewById(R.id.psl_mygrade));
        //View view = LayoutInflater.from(this).inflate(R.layout.layout_mygrade_bodyview, null);
        icon = ((ImageView) findViewById(R.id.iv_icon_mygrade));
        tv_grade = (TextView)findViewById(R.id.tv_grade_mygrade);
        nickname = ((TextView) findViewById(R.id.tv_nickname_mygrade));
        newInvitation = ((TextView) findViewById(R.id.tv_newinvitation_megrade));
        response = ((TextView) findViewById(R.id.tv_response_megrade));
        starListView = ((LimitListView) findViewById(R.id.llv_star_mygrade));
        gradeListView = ((LimitListView) findViewById(R.id.llv_grade_mygrade));
        llContiner = ((LinearLayout) findViewById(R.id.ll_mygrde));
        tvActivePoint = (TextView) findViewById(R.id.tv_active_point);


        //scroll.addBodyView(view);


    }

    @Override
    protected void initViews() {
        scroll.scrollTo(0, 0);
        starListView.setFocusable(false);
        gradeListView.setFocusable(false);

        back.setOnClickListener(this);
        setGradeImg(Constant.user.getStar()+1);
        //if (listStar .size()==0) {
        initStarData(listStar);
        //}
        //if (listGrade .size()==0) {
        initGradeData(listGrade);
        //}
        starListView.setAdapter(new MyGradeAdapter(this, listStar,0));
        gradeListView.setAdapter(new MyGradeAdapter(this, listGrade,1));

    }

    public void initStarData(List<MyGrade> list) {
        for (int i = 0; i < 12; i++) {
            MyGrade myGrade = null;
            if (i <3) {
                myGrade = new MyGrade(i+1+"星", star[i]);
            }
            if (i>2&&i<=10) {
                if((i+1)%4==0){
                    myGrade = new MyGrade((i+1)/4+"钻",star[i]);
                }else{
                    myGrade = new MyGrade((i+1)/4+"钻"+(i+1)%4+"星", star[i]);
                }

            }
           if(i==11){
               myGrade = new MyGrade("皇冠", star[i]);
           }
            list.add(myGrade);
        }
    }
    public void setGradeImg(int temp){
        int mod = temp%4;
        int quo = temp/4;

        for (int i = 0; i < quo&&quo<3; i++) {

            View v1 = new View(this);
            v1.setLayoutParams(new RelativeLayout.LayoutParams(10,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            ImageView tempIv = new ImageView(this);


            tempIv.setImageResource(R.drawable.puss_icon_diamond);

            tempIv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            llContiner.addView(v1);
            views.add(i, v1);

            llContiner.addView(tempIv);


            imageViews.add(i, tempIv);

        }

        if(quo>=3){
            View v1 = new View(this);
            v1.setLayoutParams(new RelativeLayout.LayoutParams(10,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            ImageView tempIv = new ImageView(this);


            tempIv.setImageResource(R.drawable.puss_icon_yellow);

            tempIv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            llContiner.addView(v1);
            views.add(0, v1);

            llContiner.addView(tempIv);


            imageViews.add(0, tempIv);
        }


        for(int i = 0;i<mod&&quo<3;i++){
            View v1 = new View(this);
            v1.setLayoutParams(new RelativeLayout.LayoutParams(10,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            ImageView tempIv = new ImageView(this);


            tempIv.setImageResource(R.drawable.puss_icon_star);

            tempIv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            llContiner.addView(v1);
            views.add(i, v1);

            llContiner.addView(tempIv);


            imageViews.add(i, tempIv);
        }
    }

    public void initGradeData(List<MyGrade> list) {
        for (int i = 0; i < 10; i++) {
            int t = i + 1;
            MyGrade myGrade = new MyGrade("LV" + t, grade[i]);

            list.add(myGrade);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mygrade_back:
                finish();
                break;
        }

    }
}
