package com.minfo.quanmei.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liujing on 15/8/25.
 * 医院好评率环形图
 */
public class CircleRateView extends View {
    private final Paint paint;
    private final Context context;
    private static  int DEFAULT_ROUND_WIDTH = 8;//默认环的宽度
    private static  int DEFAULT_RADIUS = 50;//默认半径
    private static int DEFAULT_START_ANGLE = -90;//默认起始角度

    private int angle = 70;//进度的角度

    private int roundWidth = DEFAULT_ROUND_WIDTH;//环的宽度
    private int radius = DEFAULT_RADIUS;//半径
    private int startAngle = DEFAULT_START_ANGLE;
    private int pHeigh;



    public CircleRateView(Context context) {
        this(context, null);
    }

    public CircleRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true); //消除锯齿
        this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆



    }


    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
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

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int wCenter = getWidth() / 2;
        int hCenter = getHeight() / 2;
        radius=wCenter*4/5;


        /**
         * 画圆环
         */
        this.paint.setColor(Color.GREEN);
        this.paint.setStrokeWidth(roundWidth);
        canvas.drawCircle(wCenter, hCenter, radius, this.paint);


        /**
         * 画进度
         */
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(Color.RED);
        Log.e("angle",angle+"");
        //paint.setColor(getResources().getColor(R.color.basic_color));  //设置进度的颜色
        RectF oval = new RectF(wCenter - radius, hCenter - radius, wCenter + radius, hCenter + radius);  //用于定义的圆弧的形状和大小的界限

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, startAngle, 360 * angle / 100, false, paint);  //根据进度画圆弧

//        //TODO 画好评率百分比
//        paint.setStrokeWidth(0);
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(12);
//        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
//        int percent = (int)(((float)angle / (float)100) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
//        float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
//
//        if(percent != 0){
//            canvas.drawText(percent + "%", center - textWidth / 2, center + 12/2, paint); //画出进度百分比
//        }


        super.onDraw(canvas);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
