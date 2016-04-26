package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by zhangjiachang on 15/8/24.
 */
public class HospitalGridAdapter extends BaseAdapter {
    private List<Product> list;
    private Context context;

    public void addList(Collection<? extends Product> l) {
        list.addAll(l);
        notifyDataSetChanged();
        Log.d("t", list.toString());
    }

    public void removeAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public HospitalGridAdapter(Context context, List<Product> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.hosgrid_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        Product product = list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tvOldPrice.setText(product.getOldval() + "");
        viewHolder.tvOldPrice.getPaint().setAntiAlias(true);
        viewHolder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tvContent.setText("【" + product.getFname() + "】" + product.getName());
        viewHolder.tvNewPrice.setText(product.getNewval() + "");
        viewHolder.tvHosName.setText(product.getHname());
        /*Utils utils = new Utils(context);
        int ScreenWidth = utils.getScreenWidth();
        ViewGroup.LayoutParams params = viewHolder.ivImg.getLayoutParams();
        params.width = ScreenWidth/2;
        params.height = ScreenWidth/2;

        viewHolder.ivImg.setLayoutParams(params);*/
        UniversalImageUtils.displayImageUseDefOptions(product.getSimg(),viewHolder.ivImg);
        return convertView;
    }

    class ViewHolder {
        private TextView tvNewPrice;
        private TextView tvOldPrice;
        private TextView tvContent;
        private TextView tvHosName;
        private ImageView ivImg;

        public ViewHolder(View view) {
            tvNewPrice = (TextView) view.findViewById(R.id.hosgrid_np);
            tvOldPrice = (TextView) view.findViewById(R.id.hosgrid_op);
            tvContent = (TextView) view.findViewById(R.id.hg_content);
            tvHosName = (TextView) view.findViewById(R.id.hg_hos);
            ivImg = (ImageView) view.findViewById(R.id.hosgrid_img);
        }
    }

}
