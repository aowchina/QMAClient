package com.minfo.quanmei.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.List;

/**
 * Created by liujing on 15/12/10.
 */
public class ReplyMeAdapter extends BaseAdapter {
    private Context context;
    private List<GroupArticle> list;
    private LayoutInflater inflater;

    public ReplyMeAdapter(Context context, List<GroupArticle> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        ViewHolder viewHolder;
        GroupArticle groupArticle = list.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_reply_me,null);
            viewHolder.ivUserAvatar = (ImageView) convertView.findViewById(R.id.iv_user_avatar);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvReplyText = (TextView) convertView.findViewById(R.id.tv_reply_text);
            viewHolder.tvMyText = (TextView) convertView.findViewById(R.id.tv_my_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UniversalImageUtils.disCircleImage(groupArticle.getUserimg(), viewHolder.ivUserAvatar);
        viewHolder.tvUsername.setText(groupArticle.getUsername());
        viewHolder.tvTime.setText(groupArticle.getPubtime());
        viewHolder.tvReplyText.setText(groupArticle.getText());
        viewHolder.tvMyText.setText(Html.fromHtml("<font color='#fa4288'>"+"回复了我的帖子: "+"</font>"+groupArticle.getText2()));

        return convertView;
    }
    class ViewHolder{
        ImageView ivUserAvatar;
        TextView tvUsername;
        TextView tvTime;
        TextView tvReplyText;
        TextView tvMyText;

    }
}
