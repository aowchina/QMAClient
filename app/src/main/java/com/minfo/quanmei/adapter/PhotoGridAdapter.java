package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.config.ImageSelConfig;
import com.minfo.quanmei.entity.Image;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;

import java.util.List;

/**
 * Created by liujing on 15/10/7.
 */
public class PhotoGridAdapter extends CommonAdapter<Image> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public List<String> mSelectedImage;
    private ItemSelectedListener itemSelectedListener;
    private ImageSelConfig config;

    public PhotoGridAdapter(Context context, List<Image> mDatas, int itemLayoutId, ItemSelectedListener itemSelectedListener, List<String> mSelectedImage) {
        super(context, mDatas, itemLayoutId);
        this.itemSelectedListener = itemSelectedListener;
        this.mSelectedImage = mSelectedImage;
        this.config = Constant.imageSelConfig;
    }

    @Override
    public void convert(final BaseViewHolder helper, final Image item, int position) {
        if(config.needCamera&&position==0){
            helper.getView(R.id.id_item_select).setVisibility(View.INVISIBLE);
            helper.getView(R.id.id_item_image).setVisibility(View.INVISIBLE);
            helper.getView(R.id.iv_take_photo).setVisibility(View.VISIBLE);
            return;
        }else{
            helper.getView(R.id.id_item_select).setVisibility(View.VISIBLE);
            helper.getView(R.id.id_item_image).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_take_photo).setVisibility(View.INVISIBLE);
            helper.setImageResource(R.id.id_item_select, R.mipmap.album_photo_select_normal);
            UniversalImageUtils.displayImageUseDefOptions("file://"+item.path, (ImageView) helper.getView(R.id.id_item_image));
        }

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        if(config.multiSelect){
            mSelect.setVisibility(View.VISIBLE);
        }else{
            mSelect.setVisibility(View.INVISIBLE);
        }

        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mSelect.setOnClickListener(new View.OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                if (mSelectedImage.contains(item.path)) {
                    mSelectedImage.remove(item.path);
                    mSelect.setImageResource(R.mipmap.album_photo_select_normal);
                    mImageView.setColorFilter(null);
                } else {
                    if (mSelectedImage.size() == 9) {
                        ToastUtils.show(context, "图片不能超过9张");
                    } else {
                        mSelectedImage.add(item.path);
                        mSelect.setImageResource(R.mipmap.album_photo_select_choose);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                    }
                }
                itemSelectedListener.showSelected(mSelectedImage);

            }
        });
        itemSelectedListener.showSelected(mSelectedImage);

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage != null) {
            if (mSelectedImage.contains(item.path)) {
                mSelect.setImageResource(R.mipmap.album_photo_select_choose);
                mImageView.setColorFilter(Color.parseColor("#77000000"));
            }
        }
    }

    public interface ItemSelectedListener {
        void showSelected(List<String> selectImgUrls);
    }
}
