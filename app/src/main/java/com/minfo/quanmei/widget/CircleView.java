package com.minfo.quanmei.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangjiachang on 15/8/27.
 */
public class CircleView extends View {
    private final Paint paint;
    private final Context context;
    private static final int DEFAULT_ROUND_WIDTH = 5;//默认环的宽度
    private static final int DEFAULT_RADIUS = 70;//默认半径

    private int roundWidth = DEFAULT_ROUND_WIDTH;//环的宽度
    private int radius = DEFAULT_RADIUS;//半径

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true); //消除锯齿
        this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆
    }


    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(int roundWidth) {
        this.roundWidth = roundWidth;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;

        /**
         * 画圆环
         */
        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(roundWidth);
        canvas.drawCircle(center, center, radius, this.paint);

        super.onDraw(canvas);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
