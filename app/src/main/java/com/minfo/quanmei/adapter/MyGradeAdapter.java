package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.MyGrade;
import com.minfo.quanmei.utils.Constant;

import java.util.List;

/**
 * Created by min-fo-012 on 15/10/12.
 */
public class MyGradeAdapter extends BaseAdapter {
    private List<MyGrade> list;
    private Context context;
    private int type;

    public MyGradeAdapter(Context context, List<MyGrade> list,int type) {
        this.context = context;
        this.list = list;
        this.type = type;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_mygrade, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        MyGrade myGrade = list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.grade.setText(myGrade.getGrade());
        viewHolder.numerical.setText(myGrade.getNumerical());
        if (type == 0&&position== Constant.user.getStar()) {
            viewHolder.grade.setBackgroundResource(R.color.basic_color);
        }else if(type==1&&position==(Constant.user.getLevel()-1)){
            viewHolder.grade.setBackgroundResource(R.color.basic_color);
        }else{
            viewHolder.grade.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        return convertView;
    }

    class ViewHolder {

        private TextView grade;
        private TextView numerical;

        public ViewHolder(View view) {

            grade = (TextView) view.findViewById(R.id.tv_item_mygrade);
            numerical = (TextView) view.findViewById(R.id.tv_numerical_mygrade);

        }
    }
}