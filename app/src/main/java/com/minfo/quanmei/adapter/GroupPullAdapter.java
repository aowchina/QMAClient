package com.minfo.quanmei.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.Collection;
import java.util.List;


/**
 * Created by zhangjiachang on 15/8/28.
 */
public class GroupPullAdapter extends BaseAdapter {


    private List<GroupArticle> list;
    private Context context;

    public GroupPullAdapter(Context context, List<GroupArticle> list) {
        this.context = context;
        this.list = list;
    }

    public void addAll(Collection<? extends GroupArticle> l) {
        list.addAll(l);
        notifyDataSetChanged();
    }

    public void removeAll() {
        list.clear();
        notifyDataSetChanged();
    }

//    @Override
//    public int getItemViewType(int position) {
//        int type = 0;
//        if (position % 2 == 0 & position != 0) {
//            type = 1;
//        } else if (position % 2 != 0) {
//            type = 2;
//        }
//        return type;
//    }

//    @Override
//    public int getViewTypeCount() {
//        return 3;
//    }

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
        ViewHolderA viewHolderA;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.start_diary_item, parent, false);
            viewHolderA = new ViewHolderA(convertView);
            convertView.setTag(viewHolderA);
        } else {
            viewHolderA = (ViewHolderA) convertView.getTag();
        }
        List<String> img = list.get(position).getImgs();
        viewHolderA.img_group1.setVisibility(View.VISIBLE);
        viewHolderA.img_group2.setVisibility(View.VISIBLE);
        if (Integer.parseInt(list.get(position).getType()) == 1) {
            if (img.size() >= 2) {
                UniversalImageUtils.displayImageUseDefOptions(img.get(0), viewHolderA.img_group1);
                UniversalImageUtils.displayImageUseDefOptions(img.get(1), viewHolderA.img_group2);
            } else if (img.size() == 1) {
                UniversalImageUtils.displayImageUseDefOptions(img.get(0), viewHolderA.img_group1);
                viewHolderA.img_group2.setVisibility(View.INVISIBLE);
            } else {
                viewHolderA.img_group1.setVisibility(View.GONE);
                viewHolderA.img_group2.setVisibility(View.GONE);
            }
        } else {
            viewHolderA.img_group1.setVisibility(View.GONE);
            viewHolderA.img_group2.setVisibility(View.GONE);
            if(img.size()>0){
                viewHolderA.ivIsHaveImage.setVisibility(View.VISIBLE);
            }else{
                viewHolderA.ivIsHaveImage.setVisibility(View.GONE);
            }
        }
        if(list.get(position).getIsjing()==2){
            viewHolderA.ivJingHua.setVisibility(View.VISIBLE);
        }else{
            viewHolderA.ivJingHua.setVisibility(View.GONE);
        }
        viewHolderA.content0.setText(list.get(position).getTitle());
        viewHolderA.tv_diary_start0.setText(list.get(position).getText());
        viewHolderA.name0.setText(list.get(position).getUsername());
        viewHolderA.tv_time_start0.setText(list.get(position).getPubtime());
        viewHolderA.handCount0.setText(list.get(position).getZan());
        viewHolderA.shareCount0.setText(list.get(position).getPl());
        UniversalImageUtils.disCircleImage(list.get(position).getUserimg(), viewHolderA.useIcon0);
        viewHolderA.tv_body_start0.setText("[" + list.get(position).getGroupname() + "]");

        return convertView;
    }

    class ViewHolderA {
        private ImageView img_group1;
        private ImageView img_group2;
        private ImageView useIcon0;
        private TextView content0;
        private TextView name0;
        private TextView handCount0;
        private TextView shareCount0;
        private TextView tv_diary_start0;
        private TextView tv_time_start0;
        private TextView tv_body_start0;
        private ImageView ivIsHaveImage;
        private ImageView ivJingHua;

        public ViewHolderA(View view) {
            useIcon0 = (ImageView) view.findViewById(R.id.iv_useicon_start0);
            content0 = (TextView) view.findViewById(R.id.tv_contentdia_start0);
            name0 = (TextView) view.findViewById(R.id.tv_usename_start0);
            handCount0 = (TextView) view.findViewById(R.id.tv_hand_start0);
            shareCount0 = (TextView) view.findViewById(R.id.tv_share_start0);
            tv_diary_start0 = (TextView) view.findViewById(R.id.tv_diary_start0);
            tv_time_start0 = (TextView) view.findViewById(R.id.tv_time_start0);
            tv_body_start0 = (TextView) view.findViewById(R.id.tv_body_start0);
            img_group1 = (ImageView) view.findViewById(R.id.img_group1);
            img_group2 = (ImageView) view.findViewById(R.id.img_group2);
            ivIsHaveImage = (ImageView) view.findViewById(R.id.iv_ishave_image);
            ivJingHua = (ImageView) view.findViewById(R.id.iv_jing);
        }
    }
}


