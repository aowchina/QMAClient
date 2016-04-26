package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.List;

/**
 * Created by liujing on 15/8/24.
 */
public class ProductListAdapter extends BaseAdapter {
    private List<Product> list;
    private Context context;
    private LayoutInflater inflater;

    public ProductListAdapter(Context context,List<Product> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        Product product = list.get(position);
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_product,null);
            viewHolder = new ViewHolder();
            viewHolder.ivProLogo = (ImageView) convertView.findViewById(R.id.iv_pro_logo);
            viewHolder.tvProDescription = (TextView) convertView.findViewById(R.id.tv_pro_description);
            viewHolder.tvHosName = (TextView) convertView.findViewById(R.id.tv_hos_name);
            viewHolder.tvNewPrice = (TextView) convertView.findViewById(R.id.tv_new_price);
            viewHolder.tvOldPrice = (TextView) convertView.findViewById(R.id.tv_old_price);
            viewHolder.tvSaleNums = (TextView) convertView.findViewById(R.id.tv_sale_nums);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UniversalImageUtils.displayImageUseDefOptions(product.getSimg(),viewHolder.ivProLogo);
        viewHolder.tvProDescription.setText("【" + product.getFname() + "】" + product.getName());
        viewHolder.tvHosName.setText(product.getHname());
        viewHolder.tvNewPrice.setText("¥"+product.getNewval()+ "");
        viewHolder.tvOldPrice.setText("¥"+product.getOldval()+"");
        viewHolder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tvSaleNums.setText("已售："+product.getSellout()+"");
        return convertView;
    }
    private class ViewHolder{
        ImageView ivProLogo;
        TextView tvProDescription;
        TextView tvHosName;
        TextView tvNewPrice;
        TextView tvOldPrice;
        TextView tvSaleNums;
    }
}
