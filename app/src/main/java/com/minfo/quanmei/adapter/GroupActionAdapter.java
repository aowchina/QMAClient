package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;

import java.util.Collection;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/6.
 */
public class GroupActionAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    public GroupActionAdapter(Context context,List<String> list){
        this.context=context;
        this.list=list;
    }
    public void addAll(Collection<?extends String> l){
        list.addAll(l);
        notifyDataSetChanged();
    }
    public void removeAll(){
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

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_list_groupaction,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }

        String str = (String) list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(str);
        viewHolder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;
    }
    class ViewHolder{

        private TextView name;
        private TextView oldPrice;
        public ViewHolder(View view){

            name=(TextView)view.findViewById(R.id.tv_name_groupactionitem);
            oldPrice=(TextView)view.findViewById(R.id.tv_op__groupactionitem);

        }
    }
}
