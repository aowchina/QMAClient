package com.minfo.quanmei.config;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by liujing on 8/21/16.
 */
public interface ImageLoader extends Serializable{
    void displayImage(Context context, String path, ImageView imageView);
}
