package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.ParentCategory;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LimitGridView;

import java.util.List;

/**
 * Created by liujing on 15/8/25.
 * 一级分类列表adapter
 */
public class ParentCategoryAdapter extends BaseAdapter {

    private List<ParentCategory> list;
    private Context context;
    private LayoutInflater inflater;
    private ChildCategoryAdapter.ExpandListListener listener;

    public ParentCategoryAdapter(Context context, List<ParentCategory> list,ChildCategoryAdapter.ExpandListListener listener) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        ParentCategory pCategory = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_parent_category, null);
            viewHolder = new ViewHolder();
            viewHolder.ivPCatLogo = (ImageView) convertView.findViewById(R.id.iv_category_logo);
            viewHolder.tvPCategoryName = (TextView) convertView.findViewById(R.id.tv_parent_category_name);
            viewHolder.limitGridView = (LimitGridView) convertView.findViewById(R.id.lgv_child_category);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UniversalImageUtils.displayImageUseDefOptions(pCategory.getIcon(), viewHolder.ivPCatLogo);
        viewHolder.tvPCategoryName.setText(pCategory.getName());
        if (pCategory.getList().size() >9 && !pCategory.isExpanded()) {
            viewHolder.limitGridView.setAdapter(new ChildCategoryAdapter(context, pCategory.getList().subList(0, 9), pCategory,listener));
        } else {
            viewHolder.limitGridView.setAdapter(new ChildCategoryAdapter(context, pCategory.getList(), pCategory,listener));
        }


        return convertView;
    }

    private class ViewHolder {
        ImageView ivPCatLogo;
        TextView tvPCategoryName;
        LimitGridView limitGridView;

    }
}
