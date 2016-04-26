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
 * Created by zhangjiachang on 15/8/28.
 */
public class StartSaleGVAdapter extends BaseAdapter {
    private List<Product> list;
    private Context context;
    public StartSaleGVAdapter(Context context,List<Product> list){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.start_gvsale_item,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }

        Product product = (Product) list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.kind.setText(product.getFname());
        UniversalImageUtils.displayImageUseDefOptions(product.getSimg(), viewHolder.icon);
        viewHolder.sale.setText("已售" + product.getSellout());
        viewHolder.nowP.setText("¥" + product.getNewval());
        viewHolder.oldP.setText("¥" +product.getOldval());
        viewHolder.oldP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;
    }
    class ViewHolder{
        private ImageView icon;
        private TextView kind;
        private TextView nowP;
        private TextView oldP;
        private TextView sale;
        public ViewHolder(View view){
            icon=(ImageView)view.findViewById(R.id.iv_a_start);
            sale=(TextView)view.findViewById(R.id.tv_sale_start);
            kind=(TextView)view.findViewById(R.id.tv_kind_start);
            nowP=(TextView)view.findViewById(R.id.tv_np_start);
            oldP=(TextView)view.findViewById(R.id.tv_op_start);

        }
    }
}
