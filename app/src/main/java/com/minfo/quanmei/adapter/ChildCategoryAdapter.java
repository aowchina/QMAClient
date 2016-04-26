package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.ChildCategory;
import com.minfo.quanmei.entity.ParentCategory;

import java.util.List;

/**
 * Created by liujing on 15/8/25.
 */
public class ChildCategoryAdapter extends BaseAdapter{
    private List<ChildCategory> list;
    private Context context;
    private LayoutInflater inflater;
    private ParentCategory pCategory;
    private ExpandListListener listener;

    public ChildCategoryAdapter(Context context,List<ChildCategory> list,ParentCategory pCategory,ExpandListListener listener) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pCategory = pCategory;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChildCategory cCategory = list.get(position);
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_child_category,null);
            viewHolder = new ViewHolder();
            viewHolder.tvcCategoryName = (TextView) convertView.findViewById(R.id.tv_child_category_name);
            viewHolder.llMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
            viewHolder.llChild = (LinearLayout) convertView.findViewById(R.id.ll_child);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvcCategoryName.setVisibility(View.VISIBLE);
        viewHolder.llMore.setVisibility(View.GONE);
        viewHolder.tvcCategoryName.setText(cCategory.getName());
        if(!pCategory.isExpanded()){
            if(position==8){
                viewHolder.tvcCategoryName.setVisibility(View.GONE);
                viewHolder.llChild.setClickable(false);
                viewHolder.llMore.setVisibility(View.VISIBLE);
                viewHolder.llMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pCategory.setIsExpanded(true);
                        listener.onExpand();
                    }
                });
            }else{
                viewHolder.tvcCategoryName.setVisibility(View.VISIBLE);
                viewHolder.llMore.setVisibility(View.GONE);
                viewHolder.tvcCategoryName.setText(cCategory.getName());
            }
        }else{
            viewHolder.tvcCategoryName.setVisibility(View.VISIBLE);
            viewHolder.llMore.setVisibility(View.GONE);
            viewHolder.tvcCategoryName.setText(cCategory.getName());
        }
        viewHolder.llChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStartProItem(cCategory);
            }
        });

        return convertView;
    }
    private class ViewHolder{
        TextView tvcCategoryName;
        LinearLayout llMore;
        LinearLayout llChild;

    }

    /**
     * 点击列表展开按钮及子分类列表item时的回调接口
     */
    public interface ExpandListListener{
        public void onExpand();
        public void onStartProItem(ChildCategory category);
    }
}
