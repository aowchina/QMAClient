package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Theme;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.utils.Utils;

import java.util.List;

/**
 * Created by zhangjiachang on 15/8/28.
 */
public class StartActionLVAdapter extends BaseAdapter {
    private List<Theme> list;
    private Context context;
    private Utils utils;

    public StartActionLVAdapter(Context context, List<Theme> list) {
        this.context = context;
        this.list = list;
        utils = new Utils(context);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.start_action_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        Theme theme = (Theme) list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        for (int i = 0; i <theme.getYyuser().size() ; i++) {

            View v1 = new View(context);
            v1.setLayoutParams(new RelativeLayout.LayoutParams(20,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            ImageView tempIv = new ImageView(context);
            viewHolder.continer.addView(v1);
            viewHolder.continer.addView(tempIv, utils.dip2px(20),utils.dip2px(20));
            UniversalImageUtils.disCircleImage(theme.getYyuser().get(i).getUserimg(), tempIv);


        }


        UniversalImageUtils.displayImageUseDefOptions(theme.getBimg(), viewHolder.icon);

        int count = theme.getAmount()==null?0:Integer.parseInt(theme.getAmount().toString());

        viewHolder.count.setText("已有"+count+"人参加");
        viewHolder.subject.setText("");

        return convertView;
    }

    class ViewHolder {
        private final TextView subject;
        private  ImageView icon;
        private LinearLayout continer;

        private TextView count;

        public ViewHolder(View view) {
            continer = (LinearLayout) view.findViewById(R.id.ll_icon_con);
            icon = (ImageView)view.findViewById(R.id.iv_icon_action);

            count = (TextView) view.findViewById(R.id.tv_count_start);
            subject = ((TextView) view.findViewById(R.id.tv_action_start));


        }
    }
}
