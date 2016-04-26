package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Group;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created zhangjiachang on 15/8/28.
 */
public class GroupHeadGVAdapter extends BaseAdapter {
    private List<Group> list;
    private Context context;
    public GroupHeadGVAdapter(Context context,List<Group> list){
        this.context=context;
        this.list=list;
    }
    public void addAll(Collection<?extends Group> l){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.group_grid_item,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }

        Group group = list.get(position);
        String name = group.getName();
        String url = group.getIcon();
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.kind.setText(name);
        UniversalImageUtils.displayImageUseDefOptions(url,viewHolder.icon);

        return convertView;
    }
    class ViewHolder{
        private ImageView icon;

        private TextView kind;

        public ViewHolder(View view){
            icon=(ImageView)view.findViewById(R.id.iv_kind_group);
            kind=(TextView)view.findViewById(R.id.tv_kind_group);


        }
    }
}
