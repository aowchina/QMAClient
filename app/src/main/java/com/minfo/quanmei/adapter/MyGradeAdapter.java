package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.MyGrade;

import java.util.List;

/**
 * Created by min-fo-012 on 15/10/12.
 */
public class MyGradeAdapter extends BaseAdapter {
    private List<MyGrade> list;
    private Context context;

    public MyGradeAdapter(Context context, List<MyGrade> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_mygrade, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        MyGrade myGrade = (MyGrade) list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.grade.setText(myGrade.getGrade());
        viewHolder.numerical.setText(myGrade.getNumerical());
        if (position == 0) {
            viewHolder.grade.setBackgroundResource(R.color.basic_color);
            //(TextView)(viewHolder.numerical).setTextColor(R.color.basic_color);
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