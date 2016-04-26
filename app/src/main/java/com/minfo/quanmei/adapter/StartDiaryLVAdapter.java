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
 * Created by zhangjiachang on 15/8/28.
 */
public class StartDiaryLVAdapter extends BaseAdapter {
    private List<GroupArticle> list;
    private Context context;

    public StartDiaryLVAdapter(Context context, List<GroupArticle> list) {
        this.context = context;
        this.list = list;
    }
    public void allAll(List<GroupArticle> lis){
        list.addAll(lis);
        notifyDataSetChanged();
    }
    public void remolveAll(){
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

        GroupArticle diary = (GroupArticle) list.get(position);
        ViewHolderB viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.start_diary_item, parent, false);
            viewHolder = new ViewHolderB(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolderB) convertView.getTag();
        }
        viewHolder.content.setText(diary.getTitle());
        viewHolder.diary_text.setVisibility(View.GONE);
        viewHolder.name.setText(diary.getUsername());
        viewHolder.upLoadTime.setText(diary.getPubtime());
        viewHolder.handCount.setText(diary.getZan());
        viewHolder.shareCount.setText(diary.getPl());
        viewHolder.body.setText("[" + diary.getGroupname() + "]");

        if (diary.getImgs()!=null) {
            if (diary.getImgs().size() >= 2) {
                UniversalImageUtils.displayImageUseDefOptions(diary.getImgs().get(0), viewHolder.iconA);
                UniversalImageUtils.displayImageUseDefOptions(diary.getImgs().get(1), viewHolder.iconB);
            } else if (diary.getImgs().size() == 1) {
                UniversalImageUtils.displayImageUseDefOptions(diary.getImgs().get(0), viewHolder.iconA);
                viewHolder.iconB.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.iconA.setVisibility(View.GONE);
                viewHolder.iconB.setVisibility(View.GONE);
            }
        }else{
            viewHolder.iconA.setVisibility(View.GONE);
            viewHolder.iconB.setVisibility(View.GONE);
        }
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

        public ViewHolderB(View view) {
            iconA = (ImageView) view.findViewById(R.id.img_group1);
            iconB = (ImageView) view.findViewById(R.id.img_group2);
            imgContiner=((LinearLayout) view.findViewById(R.id.ll_img_group));
            useIcon = (ImageView) view.findViewById(R.id.iv_useicon_start0);
            content = (TextView) view.findViewById(R.id.tv_contentdia_start0);
            body = (TextView) view.findViewById(R.id.tv_body_start0);
            name = (TextView) view.findViewById(R.id.tv_usename_start0);
            handCount = (TextView) view.findViewById(R.id.tv_hand_start0);
            shareCount = (TextView) view.findViewById(R.id.tv_share_start0);
            upLoadTime = (TextView) view.findViewById(R.id.tv_time_start0);
            diary_text = (TextView) view.findViewById(R.id.tv_diary_start0);

        }
    }


}
