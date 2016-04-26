package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.entity.DiaryUpdateItem;

import java.util.List;

/**
 * Created by liujing on 15/10/5.
 */
public class DiaryFirstReplyAdapter extends BaseAdapter {
    private List<DiaryUpdateItem> list;
    private Context context;
    private LayoutInflater inflater;

    public DiaryFirstReplyAdapter(Context context, List<DiaryUpdateItem> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
        /*
        DiaryUpdateItem updateItem = list.get(position);
        Note updateContent = updateItem.updateItemContent;
        List<NoteFirstReply> noteFirstReplies = updateContent.firstReplies;
       ViewHolder viewHolder;

        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_diary_first_reply,null);
            viewHolder = new ViewHolder();
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tvLikeNum = (TextView) convertView.findViewById(R.id.tv_like_num);
            viewHolder.tvReplyNum = (TextView) convertView.findViewById(R.id.tv_reply_num);
            viewHolder.tvReply1 = (TextView) convertView.findViewById(R.id.tv_reply1);
            viewHolder.tvReply2 = (TextView) convertView.findViewById(R.id.tv_reply2);
            viewHolder.tvMoreReply = (TextView) convertView.findViewById(R.id.tv_more_reply);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvIndex.setText(position+1+"");
        viewHolder.tvTime.setText(updateItem.updateDate+"更新");
        viewHolder.tvContent.setText(updateContent.noteContent);
        viewHolder.tvLikeNum.setText(updateContent.likeNum+"");
        viewHolder.tvReplyNum.setText(updateContent.replyNum+"");
        if(noteFirstReplies.size()>=1) {
            NoteFirstReply firstReply = noteFirstReplies.get(0);
            viewHolder.tvReply1.setText(Html.fromHtml("<font color='#ff4461'>"+firstReply.nickName+":"+"</font>"+firstReply.firstReplyContent));
            if(noteFirstReplies.size()>=2){
                NoteFirstReply firstReply2 = noteFirstReplies.get(1);
                viewHolder.tvReply2.setText(Html.fromHtml("<font color='#ff4461'>"+firstReply2.nickName+":"+"</font>"+firstReply2.firstReplyContent));
                if(noteFirstReplies.size()>2){
                    viewHolder.tvMoreReply.setText("更多"+(noteFirstReplies.size()-2)+"回复");
                }else{
                    viewHolder.tvMoreReply.setVisibility(View.GONE);
                }
            }else{
                viewHolder.tvReply2.setVisibility(View.GONE);
                viewHolder.tvMoreReply.setVisibility(View.GONE);
            }
        }else{
            viewHolder.tvReply1.setVisibility(View.GONE);
            viewHolder.tvReply2.setVisibility(View.GONE);
            viewHolder.tvMoreReply.setVisibility(View.GONE);

        }
*/

        return convertView;
    }
    class ViewHolder{
        TextView tvIndex;//索引
        TextView tvTime;//更新日期
        TextView tvContent;//更新内容
        TextView tvLikeNum;//点赞数
        TextView tvReplyNum;//回复数
        TextView tvReply1;//一级回复1
        TextView tvReply2;//一级回复2
        TextView tvMoreReply;//更多回复
    }
}
