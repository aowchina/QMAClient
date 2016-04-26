package com.minfo.quanmei.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minfo.quanmei.activity.CourseActivity;
import com.minfo.quanmei.activity.SecureActivity;
import com.minfo.quanmei.activity.ThemeActivity;
import com.minfo.quanmei.entity.CycleImg;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 15/12/28.
 */
public class StartBannerAdapter extends PagerAdapter {
    private Context mContext;
    private List<CycleImg> lunbo;// 滑动的图片url集合
    private Utils utils;
    private List<ImageView> imageViews = new ArrayList<>();


    public StartBannerAdapter(Context context, List<CycleImg> lunbo) {
        this.lunbo = lunbo;
        mContext = context;
        utils = new Utils(mContext);
        addDynamicView();
    }

    @Override
    public int getCount() {
        return lunbo.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView iv = imageViews.get(position);
        container.addView(iv);
        final CycleImg cycleImg = lunbo.get(position);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (cycleImg.getType() == 1) {
                    bundle.putString("hid", cycleImg.getHid());
                    bundle.putString("pid", cycleImg.getPid());
                    bundle.putString("bimg", cycleImg.getImg());
                    utils.jumpAty(mContext, ThemeActivity.class, bundle);
                } else if (cycleImg.getType() == 2) {
                    bundle.putString("teacherid",cycleImg.getId());
                    utils.jumpAty(mContext,CourseActivity.class,bundle);
                } else if (cycleImg.getType() == 3) {
                    bundle.putString("secureId",cycleImg.getId());
                    utils.jumpAty(mContext,SecureActivity.class,bundle);
                }
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    private void addDynamicView() {

        for (int i = 0; i < lunbo.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            UniversalImageUtils.displayImageUseDefOptions(lunbo.get(i).getImg(), imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
    }


}
