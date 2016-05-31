package com.minfo.quanmei.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;


public class MySwitchButton extends View implements OnTouchListener {

    private Bitmap switchOnBkg;
    //private Bitmap switchOffBkg;
    private Bitmap slipSwitchButton;
    //private Rect onRect;
    private Rect offRect;

    private boolean isSlipping = false;
    private boolean isSwitchOn = false;
    private float previousX;
    private float currentX;
    private int switchBkg;
    private int switchBkg2;
    private int count = 0;

    private ArrayList<OnSwitchListener> onSwitchListenerList;

    public MySwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        this.setOnTouchListener(this);
        onSwitchListenerList = new ArrayList<OnSwitchListener>();
    }

    public void setImageResource(int switchBkg, int switchBkg2, int slipBtn) {
        this.switchBkg = switchBkg;
        this.switchBkg2 = switchBkg2;
        switchOnBkg = BitmapFactory.decodeResource(this.getResources(),
                switchBkg);

//		switchOffBkg = BitmapFactory.decodeResource(this.getResources(),
//				switchBkg);
        slipSwitchButton = BitmapFactory.decodeResource(this.getResources(),
                slipBtn);

        //onRect = new Rect(0,
        //	0, switchOnBkg.getWidth(), slipSwitchButton.getHeight());
        //offRect = new Rect(0, 0, slipSwitchButton.getWidth(),
        //		slipSwitchButton.getHeight());
    }

    public void setSwitchState(boolean switchState) {
        this.isSwitchOn = switchState;
        this.invalidate();
    }

    public boolean getSwitchState() {
        return this.isSwitchOn;
    }

    public void setOnSwitchStateListener(OnSwitchListener listener) {
        onSwitchListenerList.add(listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        Paint paint = new Paint();

        float leftSlipBtnX;

        System.out.println("currentX=" + currentX + " switchOnBkg.width="
                + switchOnBkg.getWidth());


        if (isSlipping) {
            if (currentX > switchOnBkg.getWidth()) {
                leftSlipBtnX = switchOnBkg.getWidth()
                        - slipSwitchButton.getWidth();
            } else {
                leftSlipBtnX = currentX - slipSwitchButton.getWidth();
            }
        } else {
            if (isSwitchOn) {
                leftSlipBtnX = switchOnBkg.getWidth()
                        - slipSwitchButton.getWidth();
                switchOnBkg = BitmapFactory.decodeResource(this.getResources(),
                        switchBkg2);
            } else {
                leftSlipBtnX = 0;

                switchOnBkg = BitmapFactory.decodeResource(this.getResources(),
                        switchBkg);


            }
        }
        canvas.drawBitmap(switchOnBkg, matrix, paint);
        if (leftSlipBtnX < 0) {
            leftSlipBtnX = 0;
        } else if (leftSlipBtnX > switchOnBkg.getWidth()
                - slipSwitchButton.getWidth()) {
            leftSlipBtnX = switchOnBkg.getWidth() - slipSwitchButton.getWidth();
        }
        canvas.drawBitmap(slipSwitchButton, leftSlipBtnX, 0, paint);

        count++;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchOnBkg.getWidth(), switchOnBkg.getHeight());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_DOWN:
                isSlipping = true;
                break;
            case MotionEvent.ACTION_UP:
                isSlipping = false;
                boolean previousState = isSwitchOn;
                if (event.getX() > (switchOnBkg.getWidth() / 2)) {
                    isSwitchOn = true;
                } else {
                    isSwitchOn = false;
                }

                if (previousState != isSwitchOn) {
                    if (onSwitchListenerList.size() > 0) {
                        for (OnSwitchListener listener : onSwitchListenerList) {
                            listener.onSwitched(isSwitchOn);
                        }
                    }
                }
                break;

            default:
                break;
        }

        this.invalidate();
        return true;
    }

}
