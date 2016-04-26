package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.minfo.quanmei.R;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.utils.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by min-fo-012 on 15/10/21.
 */
public class ShowHospitalViewPagerAdapter extends PagerAdapter {
    private Context context;
    private View[] views;
    private List<String> list;
    private Utils utils;


    public ShowHospitalViewPagerAdapter(Context context, List<String> list) {
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
            Utils utils = new Utils(context);
            int ScreenWidth = utils.getScreenWidth();
            image.setLayoutParams(new LinearLayout.LayoutParams(ScreenWidth,
                    ScreenWidth));
            views[position] = image;
        }
//        UniversalImageUtils.displayImageUseDefOptions(list.get(position), (ImageView) views[position]);

        UniversalImageUtils.loadDefImage(list.get(position), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                ((ImageView)views[position]).setImageResource(R.mipmap.default_pic);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                ((ImageView)views[position]).setImageResource(R.mipmap.default_pic);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Matrix matrix = new Matrix();
                matrix.postScale(utils.getScreenWidth() / (bitmap.getWidth() * 1.0f), utils.getScreenWidth() / (bitmap.getWidth() * 1.0f)); //长和宽放大缩小的比例
                Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ((ImageView)views[position]).setImageBitmap(resizeBmp);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                ((ImageView)views[position]).setImageResource(R.mipmap.default_pic);
            }
        });

        container.addView(views[position]);
        return views[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
