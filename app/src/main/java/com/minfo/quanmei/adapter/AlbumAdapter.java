package com.minfo.quanmei.adapter;

import android.content.Context;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.ImageFloder;

import java.util.List;

/**
 * Created by liujing on 8/19/16.
 */
public class AlbumAdapter  extends CommonAdapter<ImageFloder>{

    public AlbumAdapter(Context context, List<ImageFloder> datas, int layoutItemId) {
        super(context, datas, layoutItemId);
    }

    @Override
    public void convert(BaseViewHolder helper, ImageFloder item, int position) {
        helper.setText(R.id.tv_album_dir, item.getName().substring(1));
        helper.setText(R.id.tv_album_count, "(" + item.getCount() + ")");
        helper.setImageByUrl(R.id.iv_dir_item_image, item.getFirstImagePath());
    }

}
