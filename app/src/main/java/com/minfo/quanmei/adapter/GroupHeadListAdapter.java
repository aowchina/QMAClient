package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.GroupArticle;

import java.util.Collection;
import java.util.List;

/**
 * Created zhangjiachang on 15/8/28.
 */
public class GroupHeadListAdapter extends BaseAdapter {
    private List<GroupArticle> list;
    private Context context;
    public GroupHeadListAdapter(Context context,List<GroupArticle> list){
        this.context=context;
        this.list=list;
    }
    public void addAll(Collection<?extends GroupArticle> l){
        list.addAll(l);
        notifyDataSetChanged();
    }
    public void removeAll(){
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 2;
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
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.group_list_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.subject.setText("那些年我们一起战痘的日子");
        if (position == 1) {
            viewHolder.group_list_line.setVisibility(View.GONE);
            viewHolder.group_list_bline.setVisibility(View.VISIBLE);
        } else {
            viewHolder.group_list_line.setVisibility(View.VISIBLE);
            viewHolder.group_list_bline.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder{

        private TextView subject;
        private View group_list_line,group_list_bline;
        public ViewHolder(View view){

            subject=(TextView)view.findViewById(R.id.tv_subject_group);
            group_list_line = view.findViewById(R.id.group_list_line);
            group_list_bline = view.findViewById(R.id.group_list_bline);
        }
    }
}
