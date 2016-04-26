package com.minfo.quanmei.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.SecondReply;

import java.util.List;


/**
 * Created by min-fo-012 on 15/11/2.
 */
public class CommentSecondReplyAdapter  extends BaseAdapter {
    private List<SecondReply> secondReplies;
    private Context context;
    private LayoutInflater inflater;
    private HospitalCommentAdapter.ClickListener operationListener;

    public CommentSecondReplyAdapter(Context context, List<SecondReply> secondReplies,HospitalCommentAdapter.ClickListener operationListener) {
        this.context = context;
        this.secondReplies = secondReplies;
        this.inflater = LayoutInflater.from(context);
        this.operationListener = operationListener;
    }

    @Override
    public int getCount() {
        return secondReplies.size();
    }

    @Override
    public Object getItem(int position) {
        return secondReplies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SecondReply secondReply = secondReplies.get(position);
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_note_second_reply,null);
            viewHolder.tvUserReply = (TextView) convertView.findViewById(R.id.tv_user_reply);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTime.setText(secondReply.getPubtime());
        viewHolder.tvUserReply.setText(Html.fromHtml("<font color='#f94a7a'>" + secondReply.getUsername() + "</font>" + " " + "回复" + " " + "<font color='#f94a7a'>" + secondReply.getUsername2() + ":" + "</font>" + secondReply.getText()));

        viewHolder.tvUserReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationListener.click(secondReply,3);
            }
        });
        return convertView;
    }


    class ViewHolder {

        private TextView tvUserReply;
        private TextView tvTime;
    }
}
