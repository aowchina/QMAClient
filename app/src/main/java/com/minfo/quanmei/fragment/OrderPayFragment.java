package com.minfo.quanmei.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.activity.OrderPayActivity;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.entity.Order;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LoadingDialog;
import com.minfo.quanmei.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderPayFragment extends BaseFragment {

    private OrderPayAdapter orderPayAdapter;
    private List<Order> orderList = new ArrayList<>();

    private RefreshListView lvOrderPay;
    private boolean isRefresh;
    private LoadingDialog loadingDialog;
    private View pslView;
    private int page = 1;
    private boolean isLoad;
    private List<Order> tempList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(mActivity);

        orderPayAdapter = new OrderPayAdapter(mActivity, orderList, R.layout.item_order_layout);
        lvOrderPay.setAdapter(orderPayAdapter);
        lvOrderPay.setIsCanLoad(true);
        reqMyOrdeList();

        lvOrderPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("orderid", orderList.get(position - 1).getOrderid());
                utils.jumpAty(mActivity, OrderPayActivity.class, bundle);
            }
        });
    }


    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_order_pay, null);
        lvOrderPay = (RefreshListView) view.findViewById(R.id.lv_order);

        lvOrderPay.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                reqMyOrdeList();
            }
        });
        lvOrderPay.setLoadListener(new RefreshListView.ILoadListener() {
            @Override
            public void onLoad() {
                page++;
                isLoad = true;
                reqMyOrdeList();
            }
        });
        return view;
    }

    /**
     * 请求我的订单接口 待支付
     */
    private void reqMyOrdeList() {
        String url = getResources().getString(R.string.api_baseurl) + "order/UserOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + 1 + "*" + page);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                if (!isRefresh && !isLoad) {
                    loadingDialog.show();
                }
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e(TAG, "请求成功" + response.toString());
                loadingDialog.dismiss();
                tempList = response.getList(Order.class);
                Log.e(TAG, tempList.toString());
                if (isRefresh) {
                    isRefresh = false;
                    lvOrderPay.refreshComplete();
                    orderList.clear();
                }
                if (isLoad) {
                    isLoad = false;
                    lvOrderPay.loadComplete();
                }
                orderList.addAll(tempList);
                orderPayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                ToastUtils.show(mActivity, response.getErrorcode() + "");
                lvOrderPay.refreshComplete();
                lvOrderPay.loadComplete();
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                lvOrderPay.refreshComplete();
                lvOrderPay.loadComplete();
                ToastUtils.show(mActivity, msg);

            }
        });
    }

    private void reqDetelete(final Order order){
        String url = getString(R.string.api_baseurl)+"order/DelOrder.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + order.getOrderid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                orderList.remove(order);
                orderPayAdapter.notifyDataSetChanged();
                ToastUtils.show(mActivity, "删除成功");
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if (errorcode == 13) {
                    ToastUtils.show(mActivity, "用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(mActivity, LoginActivity.class, null);
                } else if (errorcode == 15) {
                    ToastUtils.show(mActivity, "您只能删除待支付的订单");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(mActivity, msg);
            }
        });
    }

    class OrderPayAdapter extends CommonAdapter<Order> {

        public OrderPayAdapter(Context context, List<Order> datas, int layoutItemId) {
            super(context, datas, layoutItemId);
        }

        @Override
        public void convert(BaseViewHolder helper, final Order item, int position) {
            helper.setText(R.id.tv_date, item.getCreate_time());
            helper.setText(R.id.tv_orderid, item.getOrderid());
            helper.setText(R.id.tv_name, "【" + item.getFname() + "】" + item.getName());
            helper.setText(R.id.tv_price, "￥" + item.getNewval());
            helper.setText(R.id.tv_hospital_name, item.getHname());

            UniversalImageUtils.displayImageUseDefOptions(item.getSimg(), (ImageView) helper.getView(R.id.iv_product_simg));

            TextView tvCancel = helper.getView(R.id.tv_cancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqDetelete(item);
                }
            });
        }
    }
}
