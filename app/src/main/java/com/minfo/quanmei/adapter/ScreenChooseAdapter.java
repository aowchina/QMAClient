package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Special;

import java.util.List;

/**
 * Created by zhangjiachang on 15/8/26.
 */
public class ScreenChooseAdapter extends BaseAdapter {
    private List<Special> list;
    private Context context;
    private int temp;
    private boolean flag1;
    private String str;
    private boolean flag2;

    public ScreenChooseAdapter(Context context, List<Special> list, int temp, boolean flag1, boolean flag2, String str) {
        this.context = context;
        this.list = list;
        this.temp=temp;
        this.flag1=flag1;
        this.flag2=flag2;
        this.str=str;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.local_pop_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        String loc = list.get(position).getName();


        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.locName.setText(loc);
        if (str.equals("loc")){
            if(flag1&temp==position){
                viewHolder.check.setVisibility(View.VISIBLE);
            }else if(!flag1){
                if (position==0){
                    viewHolder.check.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.check.setVisibility(View.INVISIBLE);
                }
            }
        }else if (str.equals("spec")){
            if(flag2&temp==position){
                viewHolder.check.setVisibility(View.VISIBLE);
            }else if(!flag2){
                if (position==0){
                    viewHolder.check.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.check.setVisibility(View.INVISIBLE);
                }
            }
        }else if (str.equals("body")){
            if(flag1&temp==position){
                viewHolder.check.setVisibility(View.VISIBLE);
            }
        }else if (str.equals("pro")){
            if(flag2&temp==position){
                viewHolder.check.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    class ViewHolder {

        private TextView locName;
        private ImageView check;

        public ViewHolder(View view) {
            check = (ImageView) view.findViewById(R.id.iv_local_chose);

            locName = (TextView) view.findViewById(R.id.tv_local_name);

        }
    }
}
