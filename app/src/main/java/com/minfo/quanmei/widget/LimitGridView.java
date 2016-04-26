package com.minfo.quanmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by liujing on 15/8/25.
 */
public class LimitGridView extends GridView{
    public LimitGridView(Context context) {
        super(context);
    }

    public LimitGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LimitGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}

