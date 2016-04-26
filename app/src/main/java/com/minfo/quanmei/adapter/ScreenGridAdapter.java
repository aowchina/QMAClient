package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Special;

import java.util.List;

/**
 * Created by zhangjiachang on 15/8/27.
 */
public class ScreenGridAdapter extends BaseAdapter {
    private List<Special> list;
    private Context context;
    private int temp;
    private boolean flag;

    public ScreenGridAdapter(Context context, List<Special> list,int temp,boolean flag) {
        this.context = context;
        this.list = list;
        this.temp=temp;
        this.flag=flag;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.screen_grid_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        String loc = list.get(position).getName();


        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.kind.setText(loc);
        if(temp==position&flag){
            viewHolder.kind.setTextColor(Color.RED);
            viewHolder.kind.setBackground(context.getResources().getDrawable(R.drawable.text_screenclick));
        }




        return convertView;
    }

    class ViewHolder {

        private TextView kind;

        public ViewHolder(View view) {

            kind = (TextView) view.findViewById(R.id.tv_kind_screen);

        }
    }
}


