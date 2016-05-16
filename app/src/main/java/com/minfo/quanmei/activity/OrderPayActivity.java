package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.ProductDetail;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.PayResult;
import com.minfo.quanmei.utils.Pay_Utils;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.SelectPayDialog;
import com.minfo.quanmei.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class OrderPayActivity extends BaseActivity implements View.OnClickListener, SelectPayDialog.SelectPayListener {

    private ImageView ivLeft;
    private TextView tvTitle;
    private RelativeLayout rlPayMethod;
    private RelativeLayout rlPayType;

    private SelectPayDialog selectPayDialog;
    private Bundle bundle;
    private Intent intent;
    private ProductDetail productDetail;

    private TextView tvProductName;
    private TextView tvPhone;
    private TextView tvOrderid;
    private TextView tvScore;
    private TextView tvPrice;
    private TextView tvMinusMoney;
    private TextView tvRealMinusMoney;
    private Button btnPay;
    private EditText etPoint;
    private TextView tvPayMethod;
    private TextView tvPayType;
    private TextView tvLeftTime;

    int leftTime;

    private TextView tvPayMoney;

    private int payMethod = 1;//微信2 支付宝1
    private int payType = 1;//定金1 全款2
    //支付宝常量
    private static final int SDK_PAY_FLAG = 1;

    private String phone;
    private String orderid;
    private double score;
    private String point_to_one;
    double minusMoney;
    double realMinusMoney;

    private String productName;

    double payMoney;

    public static final String flag = "ORDER_TO_PAY";

    private String consumePoint;//消耗积分
    private String tel;
    private PayReq wx_payReq;
    private String wx_prepayid;
    private String wx_prepay_nonestr;
    private IWXAPI wxapi;
    public static Handler wxPayHandler;
    private JSONObject jsonObject;
    private String newval;
    private String dj;
    private LinearLayout llLeftTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("订单支付");
        ivLeft.setOnClickListener(this);
        btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(this);
        rlPayMethod = (RelativeLayout) findViewById(R.id.rl_pay_method);
        rlPayType = (RelativeLayout) findViewById(R.id.rl_pay_type);
        etPoint = (EditText) findViewById(R.id.et_score);
        tvPayMethod = (TextView) findViewById(R.id.tv_pay_method);
        tvPayType = (TextView) findViewById(R.id.tv_pay_type);

        tvProductName = (TextView) findViewById(R.id.tv_product_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvOrderid = (TextView) findViewById(R.id.tv_order_id);
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvMinusMoney = (TextView) findViewById(R.id.tv_minus_money);
        tvRealMinusMoney = (TextView) findViewById(R.id.tv_real_minus_money);
        tvPayMoney = (TextView) findViewById(R.id.tv_pay_money);
        tvLeftTime = (TextView) findViewById(R.id.tv_left_time);
        llLeftTime = (LinearLayout) findViewById(R.id.ll_left_time);

        rlPayType.setOnClickListener(this);
        rlPayMethod.setOnClickListener(this);

        selectPayDialog = new SelectPayDialog(this, this);
        wxPayHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    ToastUtils.show(OrderPayActivity.this, "支付成功");
                    wxPayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OrderPayActivity.this.finish();
                        }
                    }, 1000);
                }
            }
        };
    }

    @Override
    protected void initViews() {
        bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            orderid = bundle.getString("orderid");
            getWaitDetail();
        }
    }

    /**
     * 获取实支付价格
     */
    public void getPayMoney() {
        String url = getString(R.string.api_baseurl) + "order/Before_pay.php";
        Log.e(TAG, Constant.user.getUserid() + "*" + orderid + "*" + payType + "*" + payMethod + "*" + consumePoint);
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + orderid + "*" + payType + "*" + payMethod + "*" + consumePoint);
        Log.e("参数", params.toString());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                String str = response.getData().toString();

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    payMoney = jsonObject.getDouble("money");
                    if (payMoney == 0) {
                        ToastUtils.show(OrderPayActivity.this, "支付成功！");
                        startActivity(new Intent(OrderPayActivity.this, OrderListActivity.class));
                        finish();
                    } else {
                        if (payMethod == 1) {
                            pay();
                        } else {

                            wx_prepayid = jsonObject.getString("pid");
                            orderid = jsonObject.getString("orderid");
                            WXPayEntryActivity.orderid = orderid;
                            WXPayEntryActivity.type = 1;
                            wx_prepay_nonestr = jsonObject.getString("nonce_str");
                            wxapi = WXAPIFactory.createWXAPI(OrderPayActivity.this, null);
                            wxapi.registerApp(getString(R.string.wxappid));
                            reqWxPay();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(OrderPayActivity.this, "服务器繁忙" + response.getErrorcode());
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(OrderPayActivity.this, msg);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.rl_pay_method:
                selectPayDialog.show();
                selectPayDialog.setDialogType(2);
                break;
            case R.id.rl_pay_type:
                selectPayDialog.show();
                selectPayDialog.setDialogType(1);
                break;
            case R.id.btn_pay:
                if (checkPoint()) {
                    getPayMoney();
                } else {
                    ToastUtils.show(this, "积分输入不合法");
                }
                break;
        }
    }

    /**
     * 检查所用积分是否合法
     *
     * @return
     */
    private boolean checkPoint() {
        consumePoint = etPoint.getText().toString();
        if (consumePoint.isEmpty()) {
            consumePoint = "0";
            return true;
        }
        if (consumePoint.matches("^[0-9]+$")) {
            if (Double.parseDouble(consumePoint) < score) {
                return true;
            }
        }
        return false;
    }

    private void getWaitDetail() {
        String url = getString(R.string.api_baseurl) + "order/WaitDetail.php";
        Log.e(TAG, Constant.user.getUserid() + "*" + orderid);
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + orderid);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e(TAG,response.toString());
                setView(response.toString());
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(OrderPayActivity.this, "服务器繁忙" + response.getErrorcode());
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(OrderPayActivity.this, msg);
            }
        });
    }

    private void setView(String str) {
        try {
            jsonObject = new JSONObject(str);
            point_to_one = jsonObject.getString("point_to_one");
            tvProductName.setText("【" + jsonObject.getString("fname") + "】" + jsonObject.getString("name"));

            newval = jsonObject.getString("newval");
            dj = jsonObject.getString("dj");
            tvPayMoney.setText(payType == 2 ? newval : dj);
            tvPrice.setText(jsonObject.getString("newval") + "元" + "(定金" + jsonObject.getString("dj") + "元)");
            tel = jsonObject.getString("tel");
            tvPhone.setText(tel);
            orderid = jsonObject.getString("orderid");
            tvOrderid.setText(orderid);
            point_to_one = jsonObject.getString("point_to_one");
            score = jsonObject.getDouble("point");
            productName = jsonObject.getString("name");
            minusMoney = Double.parseDouble(String.format("%.2f", score / Double.parseDouble(point_to_one)));
            tvMinusMoney.setText("" + minusMoney);
            tvScore.setText(score + "");
            leftTime = jsonObject.getInt("chatime");

            if(leftTime>0) {
                new CountDownTimer(leftTime * 1000, 1000) {
                    @Override
                    public void onFinish() {
                        finish();
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        long minutes = millisUntilFinished / 1000 / 60;
                        long seconds = millisUntilFinished / 1000 - minutes * 60;
                        tvLeftTime.setText(minutes + ":" + seconds);
                    }
                }.start();
            }else{
                llLeftTime.setVisibility(View.INVISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void shareClick(int dialogType, SelectPayDialog.Type type) {
        if (dialogType == 1) {
            payType = type == SelectPayDialog.Type.MENU1 ? 2 : 1;
            tvPayType.setText(payType == 2 ? "全款" : "定金");
            tvPayMoney.setText(payType == 2 ? newval : dj);

        } else if (dialogType == 2) {
            payMethod = type == SelectPayDialog.Type.MENU1 ? 2 : 1;
            tvPayMethod.setText(payMethod == 1 ? "支付宝" : "微信");
        }
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {

        //商品名称
        String subject = productName;
        //商品定金
        String price = String.valueOf(payMoney);
        //商品金额
        String deposit = String.valueOf(payMoney);
        //预约电话
        String appointment = tel;

        //商品详情
        String body = appointment + deposit;
        // 订单
        /**
         * @key
         * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
         */
//        key = UUID.randomUUID().toString();
        String orderInfo = Pay_Utils.getOrderInfo(subject, body, price, orderid);
        // 对订单做RSA 签名
        String sign = Pay_Utils.sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + Pay_Utils.getSignType();

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderPayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //支付宝异步返回结果
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtils.show(OrderPayActivity.this, "支付成功");
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态)
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.show(OrderPayActivity.this, "支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.show(OrderPayActivity.this, "支付失败");

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * 调起微信客户端支付
     */
    private void reqWxPay() {
        wx_payReq = new PayReq();

        wx_payReq.appId = getString(R.string.wxappid);
        wx_payReq.partnerId = getString(R.string.wxmchid);
        wx_payReq.prepayId = wx_prepayid;
        wx_payReq.packageValue = "Sign=WXPay";
        wx_payReq.nonceStr = wx_prepay_nonestr;
        wx_payReq.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        wx_payReq.sign = getAppSign();
        wxapi.sendReq(wx_payReq);
    }

    private String getAppSign() {
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(wx_payReq.appId);
        sb.append("&noncestr=").append(wx_payReq.nonceStr);
        sb.append("&package=").append(wx_payReq.packageValue);
        sb.append("&partnerid=").append(wx_payReq.partnerId);
        sb.append("&prepayid=").append(wx_payReq.prepayId);
        sb.append("&timestamp=").append(wx_payReq.timeStamp);
        sb.append("&key=").append(getString(R.string.wxkey));

        return com.minfo.quanmei.utils.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    }
}
