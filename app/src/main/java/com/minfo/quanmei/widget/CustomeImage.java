package com.minfo.quanmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by liujing on 15/12/8.
 */
public class CustomeImage extends ImageView {
    private double ratio;
    public CustomeImage(Context context) {
        super(context);
    }

    public CustomeImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomeImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setRatio(double ratio){
        this.ratio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
