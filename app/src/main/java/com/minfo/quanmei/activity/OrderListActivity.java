package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.fragment.OrderCancelFragment;
import com.minfo.quanmei.fragment.OrderEvaluteFragment;
import com.minfo.quanmei.fragment.OrderMyFragment;
import com.minfo.quanmei.fragment.OrderPayFragment;
import com.minfo.quanmei.fragment.OrderServiceFragment;

/**
 * 订单列表
 */
public class OrderListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private ImageButton ivPay;
    private ImageButton ivService;
    private ImageButton ivEvalute;
    private ImageButton ivCancel;
    private ImageButton ivMyOrder;

    private OrderPayFragment orderPayFragment;
    private OrderServiceFragment orderServiceFragment;
    private OrderEvaluteFragment orderEvaluteFragment;
    private OrderCancelFragment orderCancelFragment;
    private OrderMyFragment orderMyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("订单列表");
        tvTitle.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this);

        ivPay = (ImageButton) findViewById(R.id.iv_pay);
        ivService = (ImageButton) findViewById(R.id.iv_service);
        ivEvalute = (ImageButton) findViewById(R.id.iv_evalute);
        ivCancel = (ImageButton) findViewById(R.id.iv_cancel);
        ivMyOrder = (ImageButton) findViewById(R.id.iv_my_order);
        ivPay.setOnClickListener(this);
        ivService.setOnClickListener(this);
        ivEvalute.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivMyOrder.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        setSelect(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.iv_pay:
                setSelect(0);
                break;
            case R.id.iv_service:
                setSelect(1);
                break;
            case R.id.iv_evalute:
                setSelect(2);
                break;
            case R.id.iv_cancel:
                setSelect(3);
                break;
            case R.id.iv_my_order:
                setSelect(4);
                break;
        }
    }

    private void setSelect(int i) {
        resetTab();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (orderPayFragment == null) {
                    orderPayFragment = new OrderPayFragment();
                    transaction.add(R.id.fl_order_container, orderPayFragment);
                } else {
                    transaction.show(orderPayFragment);
                }
                ivPay.setImageResource(R.mipmap.order_to_pay_c);
                break;
            case 1:
                if (orderServiceFragment == null) {
                    orderServiceFragment = new OrderServiceFragment();
                    transaction.add(R.id.fl_order_container, orderServiceFragment);
                } else {
                    transaction.show(orderServiceFragment);
                }
                ivService.setImageResource(R.mipmap.order_to_service_c);
                break;
            case 2:
                if (orderEvaluteFragment == null) {
                    orderEvaluteFragment = new OrderEvaluteFragment();
                    transaction.add(R.id.fl_order_container, orderEvaluteFragment);
                } else {
                    transaction.show(orderEvaluteFragment);
                }
                ivEvalute.setImageResource(R.mipmap.order_to_evalute_c);
                break;
            case 3:
                if (orderCancelFragment == null) {
                    orderCancelFragment = new OrderCancelFragment();
                    transaction.add(R.id.fl_order_container, orderCancelFragment);
                } else {
                    transaction.show(orderCancelFragment);
                }
                ivCancel.setImageResource(R.mipmap.order_have_cancel_c);
                break;
            case 4:
                if (orderMyFragment == null) {
                    orderMyFragment = new OrderMyFragment();
                    transaction.add(R.id.fl_order_container, orderMyFragment);
                } else {
                    transaction.show(orderMyFragment);
                }
                ivMyOrder.setImageResource(R.mipmap.order_my_order_c);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (orderPayFragment != null) {
            transaction.hide(orderPayFragment);
        }
        if (orderServiceFragment != null) {
            transaction.hide(orderServiceFragment);
        }
        if (orderEvaluteFragment != null) {
            transaction.hide(orderEvaluteFragment);
        }
        if (orderCancelFragment != null) {
            transaction.hide(orderCancelFragment);
        }
        if (orderMyFragment != null) {
            transaction.hide(orderMyFragment);
        }
    }

    private void resetTab() {
        ivPay.setImageResource(R.mipmap.order_to_pay_n);
        ivService.setImageResource(R.mipmap.order_to_service_n);
        ivEvalute.setImageResource(R.mipmap.order_to_evalute_n);
        ivCancel.setImageResource(R.mipmap.order_have_cancel_n);
        ivMyOrder.setImageResource(R.mipmap.order_my_order_n);
    }
}
