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
import com.minfo.quanmei.entity.DoctorData;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.List;

/**
 * Created by min-fo018 on 15/10/21.
 */
public class WholeDoctorPullAdapter extends BaseAdapter{
    private Context context;
    private List<DoctorData> list;
    public WholeDoctorPullAdapter(Context context,List<DoctorData> list){
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



        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_list_wholedoctor,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }
        DoctorData doctorData = list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.itme_doctor_name.setText(doctorData.getName());
        viewHolder.itme_doctor_zhiwu.setText(doctorData.getPos());
        viewHolder.itme_doctor_specialty.setText(doctorData.getDirection());
        viewHolder.itme_doctor_summary.setText(doctorData.getIntro());
        Log.e("T",doctorData.getImg());
        UniversalImageUtils.disCircleImage(doctorData.getImg(), viewHolder.itme_doctor_icon);




        return convertView;


    }
    class ViewHolder{

        private TextView itme_doctor_name,itme_doctor_zhiwu,itme_doctor_specialty,itme_doctor_summary;
        private ImageView itme_doctor_icon;
        public ViewHolder(View view){
            itme_doctor_name=(TextView)view.findViewById(R.id.item_whole_doctor_name_tv);
            itme_doctor_zhiwu=(TextView)view.findViewById(R.id.item_whole_doctor_zhiwu_tv);
            itme_doctor_specialty=(TextView)view.findViewById(R.id.item_whole_doctor__specialty_tv);
            itme_doctor_icon=(ImageView)view.findViewById(R.id.item_whole_doctor_icon_img);
            itme_doctor_summary=(TextView)view.findViewById(R.id.item_whole_doctor_summary_tv);

        }

    }
}
