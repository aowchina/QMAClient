package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.GroupTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/3.
 */
public class InvitationDetailGRAdapter extends BaseAdapter {
    private List<GroupTag> list;
    private Context context;
    private List<GroupTag> selectTagList = new ArrayList<>();
    private SelectTagListener listener;
    public InvitationDetailGRAdapter(Context context,List<GroupTag> list,SelectTagListener listener){
        this.context=context;
        this.list=list;
        this.listener = listener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_invitation, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final GroupTag tag = list.get(position);
        String loc = list.get(position).getName();
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.kind.setText(loc);
        viewHolder.kind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectTagList.contains(tag)){
                    viewHolder.kind.setTextColor(context.getResources().getColor(R.color.basic_color));
                    viewHolder.kind.setBackgroundResource(R.drawable.text_screenclick);
                    selectTagList.add(tag);
                }else{
                    viewHolder.kind.setTextColor(Color.DKGRAY);
                    viewHolder.kind.setBackgroundResource(R.drawable.text_screen_shape);
                    selectTagList.remove(tag);
                }
                listener.selectTags(selectTagList);
            }
        });
        return convertView;
    }

    class ViewHolder {

        private TextView kind;

        public ViewHolder(View view) {

            kind = (TextView) view.findViewById(R.id.tv_kind_screen);

        }
    }

    public interface SelectTagListener{
        void selectTags(List<GroupTag> tags);
    }
}
