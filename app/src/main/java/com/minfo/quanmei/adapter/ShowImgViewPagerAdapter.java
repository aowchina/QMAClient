package com.minfo.quanmei.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.utils.Utils;

import java.util.List;

/**
 * Created by min-fo-012 on 15/10/21.
 */
public class ShowImgViewPagerAdapter extends PagerAdapter {
    private Context context;
    private View[] views;
    private List<String> list;
    private Utils utils;


    public ShowImgViewPagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        views = new View[list.size()];
        utils = new Utils(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object.equals(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (views[position] == null) {
            ImageView image = new ImageView(context);

            views[position] = image;
        }
        UniversalImageUtils.displayImageUseDefOptions(list.get(position), (ImageView) views[position]);

        container.addView(views[position]);
        return views[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
