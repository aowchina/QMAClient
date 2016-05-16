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
import com.minfo.quanmei.activity.OrderDetailActivity;
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

public class OrderServiceFragment extends BaseFragment {

    private OrderServiceAdapter orderServiceAdapter;
    private List<Order> orderList = new ArrayList<>();

    private RefreshListView lvOrderService;
    private boolean isRefresh;
    private boolean isLoad;
    private int page = 1;

    private LoadingDialog loadingDialog;
    private List<Order> tempList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingDialog = new LoadingDialog(mActivity);

        orderServiceAdapter = new OrderServiceAdapter(mActivity, orderList, R.layout.item_order_service_layout);
        lvOrderService.setAdapter(orderServiceAdapter);
        lvOrderService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("orderid", orderList.get(position - 1).getOrderid());
                utils.jumpAty(mActivity, OrderDetailActivity.class, bundle);
            }
        });

        reqMyOrdeList();
    }

    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_order_service, null);
        lvOrderService = (RefreshListView) view.findViewById(R.id.lv_order);
        lvOrderService.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                reqMyOrdeList();
            }
        });
        lvOrderService.setLoadListener(new RefreshListView.ILoadListener() {
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
     * 请求我的订单接口 待服务
     */
    private void reqMyOrdeList() {
        String url = getResources().getString(R.string.api_baseurl) + "order/UserOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + 2 + "*" + page);
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
                    lvOrderService.refreshComplete();
                    orderList.clear();
                }
                if (isLoad) {
                    isLoad = false;
                    lvOrderService.loadComplete();
                }
                orderList.addAll(tempList);
                orderServiceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                ToastUtils.show(mActivity, response.getErrorcode() + "");
                lvOrderService.refreshComplete();
                lvOrderService.loadComplete();
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                lvOrderService.refreshComplete();
                lvOrderService.loadComplete();
                ToastUtils.show(mActivity, msg);

            }
        });
    }

    private void reqCancel(String orderid, final Order order) {
        String url = getString(R.string.api_baseurl) + "order/BackOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + orderid);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                ToastUtils.show(mActivity, "操作成功");
                orderList.remove(order);
                orderServiceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if (errorcode == 13 || errorcode == 18) {
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(mActivity, LoginActivity.class, null);
                } else if (errorcode == 20) {
                    ToastUtils.show(mActivity, "订单记录不存在");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙" + errorcode);
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(mActivity, msg);
            }
        });

    }

    class OrderServiceAdapter extends CommonAdapter<Order> {

        public OrderServiceAdapter(Context context, List<Order> datas, int layoutItemId) {
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

            if(item.getStatus().equals("2")) {
                helper.setText(R.id.tv_pay_status, "定金支付");
            }else if(item.getStatus().equals("5")){
                helper.setText(R.id.tv_pay_status, "全款支付");
            }

            TextView tvCancel = helper.getView(R.id.tv_cancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqCancel(item.getOrderid(), item);
                }
            });

        }
    }

}
