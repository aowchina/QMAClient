package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.List;

/**
 * Created by min-fo-012 on 15/11/9.
 */
public class ProjectDiaryAdapter extends BaseAdapter {


    private List<GroupArticle> list;
    private Context context;

    public ProjectDiaryAdapter(Context context, List<GroupArticle> list) {
        this.context = context;
        this.list = list;
    }

    public void allAll(List<GroupArticle> lis) {
        list.addAll(lis);
        notifyDataSetChanged();
    }

    public void remolveAll() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Log.e("LIST里面的额集合数据",list.toString());
        GroupArticle diary = list.get(position);
        ViewHolderB viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.start_diary_item, parent, false);
            viewHolder = new ViewHolderB();
            viewHolder.iconA = (ImageView) convertView.findViewById(R.id.img_group1);
            viewHolder.iconB = (ImageView) convertView.findViewById(R.id.img_group2);
            viewHolder.imgContiner = ((LinearLayout) convertView.findViewById(R.id.ll_img_group));
            viewHolder.useIcon = (ImageView) convertView.findViewById(R.id.iv_useicon_start0);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_contentdia_start0);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tv_body_start0);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_usename_start0);
            viewHolder.handCount = (TextView) convertView.findViewById(R.id.tv_hand_start0);
            viewHolder.shareCount = (TextView) convertView.findViewById(R.id.tv_share_start0);
            viewHolder.upLoadTime = (TextView) convertView.findViewById(R.id.tv_time_start0);
            viewHolder.diary_text = (TextView) convertView.findViewById(R.id.tv_diary_start0);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderB) convertView.getTag();
        }
        List<String> img = diary.getImgs();
        //Log.e("图片集合有这么多张图片",img.size()+"");
        if (img.size() >= 2) {
            viewHolder.iconA.setVisibility(View.VISIBLE);
            viewHolder.iconB.setVisibility(View.VISIBLE);
            UniversalImageUtils.displayImageUseDefOptions(img.get(0), viewHolder.iconA);
            UniversalImageUtils.displayImageUseDefOptions(img.get(1), viewHolder.iconB);
        } else if (img.size() == 1) {
            viewHolder.iconA.setVisibility(View.VISIBLE);
            UniversalImageUtils.displayImageUseDefOptions(img.get(0), viewHolder.iconA);
            viewHolder.iconB.setVisibility(View.INVISIBLE);
        }
        else {
            viewHolder.iconA.setVisibility(View.GONE);
            viewHolder.iconB.setVisibility(View.GONE);
        }

        viewHolder.content.setText(diary.getTitle());
        viewHolder.diary_text.setVisibility(View.GONE);
        viewHolder.name.setText(diary.getUsername());
        viewHolder.upLoadTime.setText(diary.getPubtime());
        viewHolder.handCount.setText(diary.getZan());
        viewHolder.shareCount.setText(diary.getPl());
        viewHolder.body.setText("[" + diary.getGroupname() + "]");
        //Log.e("start", diary.getImgs().toString());
        UniversalImageUtils.disCircleImage(diary.getUserimg(), viewHolder.useIcon);

        return convertView;
    }

    class ViewHolderB {
        private ImageView iconA;
        private ImageView iconB;
        private ImageView useIcon;
        private TextView content;
        private TextView body;
        private TextView name;
        private TextView handCount;
        private TextView shareCount;
        private TextView upLoadTime;
        private LinearLayout imgContiner;
        private TextView diary_text;

    }
}

