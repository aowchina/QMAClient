package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.HospitalData;
import com.minfo.quanmei.entity.HospitalPJ;

import java.util.List;

/**
 * Created zhangjiachang on 15/8/24.
 */
public class HosComListAdapter extends BaseAdapter {
    private List<HospitalPJ> list;
    private HospitalData hospitalData;
    private Context context;
    private int num=0;
    public HosComListAdapter(Context context,HospitalData hospitalData){
        this.context=context;
        this.hospitalData = hospitalData;
        list = hospitalData.getPl();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.hoscomlist_item,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }
        if(position<3) {
            HospitalPJ comment = list.get(position);

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.textView.setText(comment.getText());
            viewHolder.hoscom_author.setText(comment.getUsername());
            viewHolder.hoscom_time.setText(comment.getPubtime());
            viewHolder.ratingBar.setMax(5);
            viewHolder.ratingBar.setRating(Float.parseFloat(comment.getStars()));
            viewHolder.ratingBar.setIsIndicator(true);
        }
        return convertView;
    }
    class ViewHolder{
        private final RatingBar ratingBar;
        private TextView textView;
        private TextView hoscom_author;
        private TextView hoscom_time;
        public ViewHolder(View view){
            textView=(TextView)view.findViewById(R.id.com_content);
            ratingBar = ((RatingBar) view.findViewById(R.id.rb_hoscomment));
            hoscom_author = (TextView)view.findViewById(R.id.hoscom_author);
            hoscom_time = (TextView)view.findViewById(R.id.hoscom_time);
        }
    }
}
