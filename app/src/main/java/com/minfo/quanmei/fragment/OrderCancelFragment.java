package com.minfo.quanmei.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.minfo.quanmei.R;
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

public class OrderCancelFragment extends BaseFragment {

    private OrderCanceledAdapter orderCancelAdapter;
    private List<Order> orderList = new ArrayList<>();

    private RefreshListView lvOrderCancel;
    private boolean isRefresh;
    private LoadingDialog loadingDialog;
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

        orderCancelAdapter = new OrderCanceledAdapter(mActivity,orderList,R.layout.item_order_cancel_layout);
        lvOrderCancel.setAdapter(orderCancelAdapter);
        lvOrderCancel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        View view = View.inflate(mActivity, R.layout.fragment_order_cancel,null);
        lvOrderCancel = (RefreshListView) view.findViewById(R.id.lv_order);

        lvOrderCancel.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                reqMyOrdeList();
            }
        });
        lvOrderCancel.setLoadListener(new RefreshListView.ILoadListener() {
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
     * 请求我的订单接口 已取消
     */
    private void reqMyOrdeList() {
        String url = getResources().getString(R.string.api_baseurl) + "order/UserOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + 4 + "*" + page);
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
                    lvOrderCancel.refreshComplete();
                    orderList.clear();
                }
                if (isLoad) {
                    isLoad = false;
                    lvOrderCancel.loadComplete();
                }
                orderList.addAll(tempList);
                orderCancelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                ToastUtils.show(mActivity, response.getErrorcode() + "");
                lvOrderCancel.refreshComplete();
                lvOrderCancel.loadComplete();
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                lvOrderCancel.refreshComplete();
                lvOrderCancel.loadComplete();
                ToastUtils.show(mActivity, msg);

            }
        });
    }

    class OrderCanceledAdapter extends CommonAdapter<Order> {

        public OrderCanceledAdapter(Context context, List<Order> datas, int layoutItemId) {
            super(context, datas, layoutItemId);
        }

        @Override
        public void convert(BaseViewHolder helper, Order item, int position) {

            helper.setText(R.id.tv_date, item.getCreate_time());
            helper.setText(R.id.tv_orderid, item.getOrderid());
            helper.setText(R.id.tv_name, "【" + item.getFname() + "】" + item.getName());
            helper.setText(R.id.tv_price, "￥" + item.getNewval());
            helper.setText(R.id.tv_hospital_name, item.getHname());

            UniversalImageUtils.displayImageUseDefOptions(item.getSimg(), (ImageView) helper.getView(R.id.iv_product_simg));

            if(item.getStatus().equals("7")||item.getStatus().equals("9")){
                helper.setText(R.id.tv_refund_status,"退款中");
            }else if(item.getStatus().equals("8")) {
                helper.setText(R.id.tv_refund_status,"已退款");
            }
        }
    }

}
