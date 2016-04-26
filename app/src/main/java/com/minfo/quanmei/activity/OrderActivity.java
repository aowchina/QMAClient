package com.minfo.quanmei.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.entity.OrdeList;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.entity.ProductDetail;
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

/**
 * @author jiachang
 * @funcation 我的订单列表
 */
public class OrderActivity extends BaseActivity implements RefreshListView.IrefreshListener, View.OnClickListener {

    private ImageView back;
    private RefreshListView listView;
    private List<OrdeList> list = new ArrayList<OrdeList>();
    private ProductDetail productDetail;
    private LoadingDialog loadingDialog;
    private OrderListAdapter orderListAdapter;
    private boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.order_back));
        listView = ((RefreshListView) findViewById(R.id.rl_order));
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void initViews() {
        reqMyOrdeList();
        back.setOnClickListener(this);

        listView.setRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OrdeList order = list.get(position - 1);
                TextView payOrder = (TextView) view.findViewById(R.id.tv_pay_order);
                String payStr = payOrder.getText().toString();
                if (!payStr.equals("") && payStr != null) {
                    if (payStr.equals("待支付")) {
                        productDetail = new ProductDetail();

                        productDetail.setId(order.getTid());
                        productDetail.setNewval(order.getDj());
                        productDetail.setName(order.getName());
                        productDetail.setNewval(order.getNewval());
                        String tel = order.getTel();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("productdetail", productDetail);
                        bundle.putString("orderid", order.getOrderid());
                        bundle.putString("tel", tel);
                        utils.jumpAty(OrderActivity.this, AccomplishAppointmentActivity.class, bundle);
                    } else if (payStr.equals("待评价")) {
                        Intent intent = new Intent(OrderActivity.this, EvaluateActivity.class);
                        intent.putExtra("OrdeID", order.getOrderid());
                        intent.putExtra("HID", order.getHid());
                        startActivity(intent);
                    }else{
                        Bundle bundle = new Bundle();
                        Product product = new Product();
                        product.setId(Integer.parseInt(order.getTid()));
                        bundle.putSerializable("product", product);
                        utils.jumpAty(OrderActivity.this,ProductDetailActivity.class,bundle);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        reqMyOrdeList();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        reqMyOrdeList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_back:
                onBackPressed();
                break;
        }
    }

    /**
     * 请求我的订单接口
     */
    private void reqMyOrdeList() {
        String url = getResources().getString(R.string.api_baseurl) + "order/UserOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                if(!isRefresh){
                    loadingDialog.show();
                }
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                listView.refreshComplete();
                list = response.getList(OrdeList.class);
                orderListAdapter = new OrderListAdapter(OrderActivity.this,list,R.layout.item_list_order);
                listView.setAdapter(orderListAdapter);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                listView.refreshComplete();
                ToastUtils.show(OrderActivity.this, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                listView.refreshComplete();
                ToastUtils.show(OrderActivity.this, msg);

            }
        });
    }

    private class OrderListAdapter extends CommonAdapter<OrdeList>{
        private TextView payTv;
        private TextView tvDelete;
        private ImageView payIv;

        public OrderListAdapter(Context context, List<OrdeList> datas, int layoutItemId) {
            super(context, datas, layoutItemId);
        }

        @Override
        public void convert(BaseViewHolder helper, final OrdeList item, int position) {
            helper.setText(R.id.tv_name_order, item.getHname());
            helper.setText(R.id.tv_pro_order, item.getName());
            helper.setText(R.id.tv_price_order, "定金：" + item.getDj());

            payIv = helper.getView(R.id.iv_order_icon);
            tvDelete = helper.getView(R.id.tv_delete);
            UniversalImageUtils.displayImageUseDefOptions(item.getSimg(), payIv);
            payTv = helper.getView(R.id.tv_pay_order);
            if (Integer.parseInt(item.getStatus()) == 1) {
                payTv.setText("待支付");
                tvDelete.setVisibility(View.VISIBLE);
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqDeleteOrder(item);
                    }
                });
            } else if (Integer.parseInt(item.getStatus()) == 2) {
                payTv.setBackgroundResource(R.drawable.shape_product_order);
                payTv.setText("已预订");
            } else if (Integer.parseInt(item.getStatus()) == 3) {
                payTv.setText("待评价");
            } else if (Integer.parseInt(item.getStatus()) == 4) {
                payTv.setText("已结束");
            }
        }
    }

    private void reqDeleteOrder(final OrdeList order){
        String url = getString(R.string.api_baseurl)+"order/DelOrder.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+Constant.user.getUserid()+"*"+order.getOrderid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                list.remove(order);
                orderListAdapter.notifyDataSetChanged();
                ToastUtils.show(OrderActivity.this,"删除成功");
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if(errorcode==13){
                    ToastUtils.show(OrderActivity.this,"用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(OrderActivity.this, LoginActivity.class, null);
                }else if(errorcode==15){
                    ToastUtils.show(OrderActivity.this,"您只能删除待支付的订单");
                }else{
                    ToastUtils.show(OrderActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(OrderActivity.this,msg);

            }
        });

    }
}
