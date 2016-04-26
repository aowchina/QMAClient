package com.minfo.quanmei.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Theme;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by zhangjiachang on 15/8/26.
 */
public class SpecialGridAdapter extends BaseAdapter {
    private List<Theme> list;
    private Context context;

    public void addList(Collection<? extends Theme> l) {
        list.addAll(l);
        notifyDataSetChanged();
        Log.d("t", list.toString());
    }

    public void removeAll() {
        list.clear();

        notifyDataSetChanged();
    }

    public SpecialGridAdapter(Context context, List<Theme> list) {
        this.context = context;
        this.list = list;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.special_grid_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        String name = list.get(position).getName();
        String fname = list.get(position).getFname() ;
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.visit1.setText(name);
        viewHolder.visit2.setText(fname);
        int mod = position%4;
        if(mod==0){
            viewHolder.visit1.setTextColor(context.getResources().getColor(R.color.color_1));
        }else if(mod==1){
            viewHolder.visit1.setTextColor(context.getResources().getColor(R.color.color_2));
        }else if(mod==2){
            viewHolder.visit1.setTextColor(context.getResources().getColor(R.color.color_3));
        }else{
            viewHolder.visit1.setTextColor(context.getResources().getColor(R.color.color_4));
        }
        UniversalImageUtils.displayImageUseDefOptions(list.get(position).getSimg(), viewHolder.img);
        return convertView;
    }

    class ViewHolder {
        private TextView visit1;
        private TextView visit2;
        private ImageView img;

        public ViewHolder(View view) {
            visit1 = (TextView) view.findViewById(R.id.tv_visit_special);
            visit2 = (TextView) view.findViewById(R.id.tv_visit2_special);
            img = (ImageView) view.findViewById(R.id.iv_visit_special);
        }
    }

}
