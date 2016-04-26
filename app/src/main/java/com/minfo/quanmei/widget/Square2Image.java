package com.minfo.quanmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by liujing on 15/12/8.
 */
public class Square2Image extends ImageView {
    public Square2Image(Context context) {
        super(context);
    }

    public Square2Image(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Square2Image(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),((int)(getMeasuredWidth()/1.9)));
    }
}
