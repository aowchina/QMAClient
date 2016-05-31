package com.minfo.quanmei.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.activity.OrderDetailActivity;
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

public class OrderServiceFragment extends BaseFragment {

    private OrderServiceAdapter orderServiceAdapter;
    private List<Order> orderList = new ArrayList<>();

    private RefreshListView lvOrderService;
    private boolean isRefresh;
    private boolean isLoad;
    private int page = 1;

    private LoadingDialog loadingDialog;
    private List<Order> tempList = new ArrayList<>();

    public static Handler handler;
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

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    page = 1;
                    orderList.clear();
                    tempList.clear();
                    reqMyOrdeList();
                }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();

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
                loadingDialog.dismiss();
                tempList = response.getList(Order.class);
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
                lvOrderService.refreshComplete();
                lvOrderService.loadComplete();
                int errorcode = response.getErrorcode();
                if (errorcode == 11 || errorcode == 12) {
                    utils.jumpAty(mActivity, LoginActivity.class, null);
                    LoginActivity.isJumpLogin = true;
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
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

            TextView tvRemainBalance = helper.getView(R.id.tv_remain_balance);

            UniversalImageUtils.displayImageUseDefOptions(item.getSimg(), (ImageView) helper.getView(R.id.iv_product_simg));

            if (item.getStatus().equals("2")) {
                helper.setText(R.id.tv_pay_status, "定金支付");
                tvRemainBalance.setVisibility(View.VISIBLE);

                tvRemainBalance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderid", item.getOrderid());
                        bundle.putInt("payType", 3);
                        bundle.putString("from","OrderServiceFragment");
                        utils.jumpAty(mActivity, OrderPayActivity.class,bundle);
                    }
                });

            } else if (item.getStatus().equals("5")) {
                helper.setText(R.id.tv_pay_status, "全款支付");
                tvRemainBalance.setVisibility(View.GONE);
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
