package com.minfo.quanmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.quanmei.adapter.HosComListAdapter;


/**
 * Created by zhangjiachang on 15/8/24.
 */
public class HeadView extends LinearLayout {

    private ImageView hos_img;
    private TextView hos_name;
    private LinearLayout goToHos;
    private TextView commet_pro;
    private TextView hos_beauty;
    private TextView hos_envir;

    private TextView hos_service;
    private TextView com_count;
    private TextView hos_comcount;


    private TextView hos_docaname;
    private TextView hos_docavisit;
    private TextView hos_docbname;
    private TextView hos_docbvisit;
    private ImageView docA;
    private ImageView docB;
    private ListView listView;


    public HeadView(Context context) {
        super(context);
        initView(context);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private Context con;
    private HosComListAdapter adapter;
    public void initView(Context context){
        con=context;
//        View v= LayoutInflater.from(context).inflate(R.layout.hos_headview,this);
//        hos_img = ((ImageView) v.findViewById(R.id.hospital_img));
//        hos_name = ((TextView) v.findViewById(R.id.hos_name));
//        goToHos = ((LinearLayout) v.findViewById(R.id.goto_hospital));
//        commet_pro = ((TextView) v.findViewById(R.id.hoscom_pro));
//        hos_beauty = ((TextView) v.findViewById(R.id.hos_beauty));
//        hos_envir = ((TextView) v.findViewById(R.id.hos_envir));
//        hos_service = ((TextView) v.findViewById(R.id.hos_service));
//        com_count = ((TextView) v.findViewById(R.id.com_count));
//        hos_comcount = ((TextView) v.findViewById(R.id.hos_comcount));
//
//        hos_docaname = ((TextView) v.findViewById(R.id.hos_docaname));
//        hos_docavisit = ((TextView) v.findViewById(R.id.hos_docavisit));
//        hos_docbname = ((TextView) v.findViewById(R.id.hos_docbname));
//        hos_docbvisit = ((TextView) v.findViewById(R.id.hos_docbvisit));
//        docA = ((ImageView) v.findViewById(R.id.hos_doca));
//        docB = ((ImageView) v.findViewById(R.id.hos_docb));
//
//        listView = ((ListView) v.findViewById(R.id.hospital_commentlist));


    }
//    public void setData(List<String> l){
//        adapter=new HosComListAdapter(con,l);
//        listView.setAdapter(adapter);
//
//    }
}
