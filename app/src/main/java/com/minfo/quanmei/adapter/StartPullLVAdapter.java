package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.List;

/**
 * Created by zhangjiachang on 15/8/28.
 */
public class StartPullLVAdapter extends BaseAdapter{
    private List<Product> list;
    private Context context;
    private ListView listView;

    public StartPullLVAdapter(Context context,List<Product> list,ListView listView){
        this.context=context;
        this.list=list;
        this.listView = listView;
        listView.setOnScrollListener(new PauseOnScrollListener(UniversalImageUtils.getInstance().getImgLoader(),true,false));
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
            convertView= LayoutInflater.from(context).inflate(R.layout.start_ptlv_item,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }

        Product product = list.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.content.setText("【"+product.getFname()+"】"+product.getName());
        viewHolder.icon.setTag(product.getSimg());
        UniversalImageUtils.displayImageUseDefOptions( product.getSimg(), viewHolder.icon);
        viewHolder.hos.setText(product.getHname());
        viewHolder.sale.setText("已售:" + product.getSellout());
        viewHolder.nowP.setText("¥" + product.getNewval());
        viewHolder.oldP.setText("¥" +product.getOldval());
        viewHolder.oldP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;
    }

    class ViewHolder{
        private ImageView icon;
        private TextView hos;
        private TextView content;
        private TextView nowP;
        private TextView oldP;
        private TextView sale;
        public ViewHolder(View view){
            icon=(ImageView)view.findViewById(R.id.iv_iconpl_start);
            content=(TextView)view.findViewById(R.id.tv_cont_start);
            hos=(TextView)view.findViewById(R.id.tv_hosp_start);
            nowP=(TextView)view.findViewById(R.id.tv_nprice_start);
            oldP=(TextView)view.findViewById(R.id.tv_oprice_start);
            sale=(TextView)view.findViewById(R.id.tv_sal_start);

        }
    }
}

