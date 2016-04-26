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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 15/11/3.
 */
public class PersonalHomePageItem2Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GroupArticle> diary = new ArrayList<GroupArticle>();

    public PersonalHomePageItem2Adapter(Context context, List<GroupArticle> diary) {
        this.context = context;
        this.diary = diary;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return diary.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.personal_home_page_item3item, null);
            viewHolder.tv_contentdia_start0 = (TextView) convertView.findViewById(R.id.tv_contentdia_start0);
            viewHolder.tv_diary_start0 = (TextView) convertView.findViewById(R.id.tv_diary_start0);
            viewHolder.tv_usename_start0 = (TextView) convertView.findViewById(R.id.tv_usename_start0);
            viewHolder.tv_time_start0 = (TextView) convertView.findViewById(R.id.tv_time_start0);
            viewHolder.home_diary1 = (ImageView) convertView.findViewById(R.id.home_diary1);
            viewHolder.home_diary2 = (ImageView) convertView.findViewById(R.id.home_diary2);
            viewHolder.iv_useicon_start0 = (ImageView) convertView.findViewById(R.id.iv_useicon_start0);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_contentdia_start0.setText(diary.get(position).getTitle());
        viewHolder.tv_diary_start0.setText(diary.get(position).getText());
        viewHolder.tv_usename_start0.setText(diary.get(position).getUsername());
        viewHolder.tv_time_start0.setText(diary.get(position).getPubtime());
        UniversalImageUtils.disCircleImage(diary.get(position).getUserimg(), viewHolder.iv_useicon_start0);
        List<String> imgs = diary.get(position).getImgs();
        if (imgs.size()>=2){
            viewHolder.home_diary1.setVisibility(View.VISIBLE);
            viewHolder.home_diary2.setVisibility(View.VISIBLE);
            UniversalImageUtils.displayImageUseDefOptions(diary.get(position).getImgs().get(0), viewHolder.home_diary1);
            UniversalImageUtils.displayImageUseDefOptions(diary.get(position).getImgs().get(1), viewHolder.home_diary2);
        }else if(imgs.size()==1){
            viewHolder.home_diary1.setVisibility(View.VISIBLE);
            UniversalImageUtils.displayImageUseDefOptions(diary.get(position).getImgs().get(0), viewHolder.home_diary1);
            viewHolder.home_diary2.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.home_diary1.setVisibility(View.GONE);
            viewHolder.home_diary2.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_contentdia_start0;//标题
        private TextView tv_diary_start0;//内容
        private ImageView home_diary1, home_diary2;//图片1,图片2
        private ImageView iv_useicon_start0;//头像
        private TextView tv_usename_start0;//名字
        private TextView tv_time_start0;//时间
    }
}
