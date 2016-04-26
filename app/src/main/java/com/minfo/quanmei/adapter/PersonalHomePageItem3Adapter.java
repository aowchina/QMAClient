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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 15/11/3.
 */
public class PersonalHomePageItem3Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GroupArticle> reply = new ArrayList<GroupArticle>();

    public PersonalHomePageItem3Adapter(Context context, List<GroupArticle> reply) {
        this.context = context;
        this.reply = reply;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reply.size();
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
        final GroupArticle groupArticle = reply.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.personal_home_page_item4item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.content.setText(groupArticle.getText());
        viewHolder.from.setText(groupArticle.getText2());
        viewHolder.name.setText(groupArticle.getUsername());
        viewHolder.time.setText(groupArticle.getPubtime());
        UniversalImageUtils.disCircleImage(groupArticle.getUserimg(), viewHolder.useIcon);
        viewHolder.from.setText(Html.fromHtml("<font color='#9b9b9b'>" + groupArticle.getUsername2() + "：" + "</font>" + groupArticle.getText2()));
        return convertView;
    }

    private class ViewHolder {
        private ImageView useIcon;
        private TextView content;
        private TextView time;
        private TextView name;
        private TextView from;

        public ViewHolder(View view) {
            useIcon = (ImageView) view.findViewById(R.id.iv_response_icon);
            content = (TextView) view.findViewById(R.id.tv_response_content);
            from = (TextView) view.findViewById(R.id.tv_response_from);
            name = (TextView) view.findViewById(R.id.tv_response_name);
            time = (TextView) view.findViewById(R.id.tv_response_time);
        }
    }
}
