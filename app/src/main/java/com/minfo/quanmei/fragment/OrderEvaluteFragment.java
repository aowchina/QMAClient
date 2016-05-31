package com.minfo.quanmei.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.EvaluateActivity;
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

public class OrderEvaluteFragment extends BaseFragment {


    private OrderEvaluteAdapter orderEvaluteAdapter;
    private List<Order> orderList = new ArrayList<>();

    private RefreshListView lvOrderEvalute;
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

        orderEvaluteAdapter = new OrderEvaluteAdapter(mActivity, orderList, R.layout.item_order_evalute_layout);
        lvOrderEvalute.setAdapter(orderEvaluteAdapter);
        lvOrderEvalute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        View view = View.inflate(mActivity, R.layout.fragment_order_evalute, null);
        lvOrderEvalute = (RefreshListView) view.findViewById(R.id.lv_order);
        lvOrderEvalute.setRefreshListener(new RefreshListView.IrefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                reqMyOrdeList();
            }
        });
        lvOrderEvalute.setLoadListener(new RefreshListView.ILoadListener() {
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
     * 请求我的订单接口 评价
     */
    private void reqMyOrdeList() {
        String url = getResources().getString(R.string.api_baseurl) + "order/UserOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + 3 + "*" + page);
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
                    lvOrderEvalute.refreshComplete();
                    orderList.clear();
                }
                if (isLoad) {
                    isLoad = false;
                    lvOrderEvalute.loadComplete();
                }
                orderList.addAll(tempList);
                orderEvaluteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                lvOrderEvalute.refreshComplete();
                lvOrderEvalute.loadComplete();
                int errorcode = response.getErrorcode();
                if(errorcode==11||errorcode==12){
                    utils.jumpAty(mActivity,LoginActivity.class,null);
                    LoginActivity.isJumpLogin = true;
                }else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                lvOrderEvalute.refreshComplete();
                lvOrderEvalute.loadComplete();
                ToastUtils.show(mActivity, msg);

            }
        });
    }

    class OrderEvaluteAdapter extends CommonAdapter<Order> {

        public OrderEvaluteAdapter(Context context, List<Order> datas, int layoutItemId) {
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

            TextView tvEvalute = helper.getView(R.id.tv_evalute);
            tvEvalute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderid", item.getOrderid());
                    bundle.putInt("hid", item.getHid());
                    utils.jumpAty(mActivity, EvaluateActivity.class, bundle);
                }
            });

        }
    }


}
