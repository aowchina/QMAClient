package com.minfo.quanmei.widget;

/**
 * Created by zhangjiachang on 2015/9/2.
 * 医院好评柱状图
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class StatscsView extends View {


    private int height;

    public StatscsView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

        init(context, null);
    }

    public StatscsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    //  坐标轴 轴线 画笔：
    private Paint axisLinePaint;
    //  坐标轴水平内部 虚线画笔
    private Paint hLinePaint;
    //  绘制文本的画笔
    private Paint titlePaint;
    //  矩形画笔 柱状图的样式信息
    private Paint recPaint;
    private int width;
    private int xSLine;
    private int ySLine;
    private int xELine;
    private int yELine;
    private int minddle;

    private void init(Context context, AttributeSet attrs) {

        axisLinePaint = new Paint();
        titlePaint = new Paint();
        recPaint = new Paint();
        hLinePaint=new Paint();

        axisLinePaint.setColor(Color.RED);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(20);
        hLinePaint.setColor(Color.GRAY);

//        hLinePaint.setStyle(Paint.Style.STROKE);
//        hLinePaint.setStrokeWidth(1);
//        PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);
//        hLinePaint.setPathEffect(effects);

        //画笔画虚线

    }
    public void getViewWidth(){

    }


    //4 条
    private float[] commentData;


    public void setData(float[] commentData) {
        this.commentData = commentData;
    }

    private String[] yTitlesStrings =
            new String[]{"100","80", "60", "40", "20", "0"};

    private String[] xTitles =
            new String[]{"审美", "服务", "环境"};

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        xSLine=0;
        ySLine=height*1/10;
        xELine=width;
        yELine=height*9/10;

        // 1 绘制坐标线：

        canvas.drawLine(xSLine, 0, xSLine, yELine, axisLinePaint);

        canvas.drawLine(xSLine, yELine, xELine, yELine, axisLinePaint);
        int leftHeight = height*8/10;// 左侧外周的 需要划分的高度：
        minddle=ySLine+leftHeight/2;
        //canvas.drawLine(xSLine, minddle, xELine, minddle, hLinePaint);
        //canvas.drawText("平均水平", xSLine+xELine+20, minddle, titlePaint);
        // 2 绘制坐标内部的水平线


        //int hLineHeight = leftHeight / 2;


        // 3 绘制 Y 周坐标

        int hPerHeight = leftHeight / 5;


        // 3 绘制 Y 周坐标

        FontMetrics metrics = titlePaint.getFontMetrics();
        int descent = (int) metrics.descent;
        titlePaint.setTextAlign(Align.RIGHT);
        for (int i = 0; i < yTitlesStrings.length; i++) {
            //canvas.drawText(yTitlesStrings[i], 100, 20 + i * hPerHeight + descent, titlePaint);
        }

        // 4  绘制 X 周 做坐标

        int xAxisLength = xELine;
        int columCount = xTitles.length + 1;
        int step = xAxisLength / columCount;

        for (int i = 0; i < columCount - 1; i++) {
            //canvas.drawText(xTitles[i], xSLine*2/3 + step * (i + 1), yELine+ySLine*3/2, titlePaint);
        }
        int temp=width/10;
        if (commentData != null && commentData.length > 0) {
            int thisCount = commentData.length;

            for (int i = 0; i < thisCount; i++) {
                int value = (int)commentData[i];
                int num = 10 - value / 10;
                recPaint.setColor(Color.RED);
                Rect rect = new Rect();

                rect.left = temp  ;
                rect.right = temp + width/5;
                temp=temp + width/5;
                temp+=width/10;
//              当前的相对高度：
                int rh = (leftHeight * num) / 10;
                rect.top = rh;
                rect.bottom = yELine;
                canvas.drawRect(rect, recPaint);

            }
        }
    }
}
