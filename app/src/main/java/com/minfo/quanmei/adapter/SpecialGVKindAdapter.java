package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Special;

import java.util.Collection;
import java.util.List;

/**
 * Created by zhangjiachang on 15/8/26.
 */
public class SpecialGVKindAdapter extends BaseAdapter {
    private List<Special> list;
    private Context context;
    public void addList(Collection<?extends Special> l){
        list.addAll(l);
        notifyDataSetChanged();
    }
    public void removeAll(){
        list.clear();
        notifyDataSetChanged();
    }
    public SpecialGVKindAdapter(Context context,List<Special> list){
        this.context=context;
        this.list=list;
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
        Special special = list.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.special_gvkind_item,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }
        String str=special.getName();
        ViewHolder viewHolder=(ViewHolder)convertView.getTag();
        viewHolder.text.setText(str);
        if(position==7){
            viewHolder.text.setText("更多");
        }else{
            viewHolder.text.setText(str);
        }
        return convertView;
    }

    class ViewHolder{
        private TextView text;

        public ViewHolder(View view){
            text=(TextView)view.findViewById(R.id.tv_kind_special);

        }
    }
}
