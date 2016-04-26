package com.minfo.quanmei.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.minfo.quanmei.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 完成预约页面
 * 2015年10月3日
 * zhang jiachang
 */
public class AccomplishAppointmentActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private TextView productName;
    private TextView productPrice;
    private TextView phone;
    private TextView date;
    private TextView productMoney;
    private TextView sure;
    private RadioGroup rgPay;

    private String tel;
    public static  String orderid;

    private ProductDetail productDetail;
    private Bundle bundle;

    private int method = 2;
    //支付宝常量
    private static final int SDK_PAY_FLAG = 1;

    //微信支付
    private IWXAPI wxapi;

    private String wx_prepayid = "";
    private String wx_prepay_nonestr = "";
    private PayReq wx_payReq;

    public static Handler wxPayHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomplish_appointment);
        wxPayHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    ToastUtils.show(AccomplishAppointmentActivity.this, "支付成功");
                    wxPayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccomplishAppointmentActivity.this.finish();
                        }
                    },1000);
                }
            }
        };
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.appointment_back));
        productName = ((TextView) findViewById(R.id.tv_productname_appoint));
        productPrice = ((TextView) findViewById(R.id.tv_productprice_appoint));
        phone = ((TextView) findViewById(R.id.tv_phone_appoint));
        date = ((TextView) findViewById(R.id.tv_date_appoint));
        productMoney = ((TextView) findViewById(R.id.tv_productmoney_appoint));
        sure = ((TextView) findViewById(R.id.tv_sure_appiontment));
        rgPay = (RadioGroup) findViewById(R.id.rg_pay);
        rgPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                method = checkedId == R.id.rb_wxpay ? 2 : 1;
            }
        });

    }

    @Override
    protected void initViews() {
        bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            productDetail = (ProductDetail) bundle.getSerializable("productdetail");

            productName.setText(productDetail.getName());
            productPrice.setText(productDetail.getNewval());
            productMoney.setText(productDetail.getDj());

            tel = bundle.getString("tel");
            orderid = bundle.getString("orderid");

            phone.setText(tel);
        }

        back.setOnClickListener(this);
        productName.setOnClickListener(this);
        productPrice.setOnClickListener(this);
        phone.setOnClickListener(this);
        date.setOnClickListener(this);
        productMoney.setOnClickListener(this);
        sure.setOnClickListener(this);


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
                        ToastUtils.show(AccomplishAppointmentActivity.this, "支付成功");
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态)
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.show(AccomplishAppointmentActivity.this, "支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.show(AccomplishAppointmentActivity.this, "支付失败");

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appointment_back:
                finish();
                break;
            case R.id.tv_productname_appoint:
                break;
            case R.id.tv_productprice_appoint:
                break;
            case R.id.tv_phone_appoint:
                break;
            case R.id.tv_date_appoint:
                break;
            case R.id.tv_productmoney_appoint:
                break;
            case R.id.tv_sure_appiontment:
                if (method == 1) {
                    pay();
                } else {
                    wxpay();
                }
                break;
        }

    }

    private void wxpay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp(getString(R.string.wxappid));

        reqWxParams();
    }

    /**
     * 微信支付所需参数
     */
    private void reqWxParams() {
        //TODO 初始化微信支付所需参数
        String url = getString(R.string.api_baseurl) + "weixin/PayReq.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + orderid);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("wxinfo");
                    wx_prepayid = jsonObject1.getString("pid");
                    orderid = jsonObject1.getString("orderid");
                    WXPayEntryActivity.orderid = orderid;
                    WXPayEntryActivity.type = 1;
                    wx_prepay_nonestr = jsonObject1.getString("nonce_str");
                    reqWxPay();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                Log.e("微信支付",response.getErrorcode()+"");
                ToastUtils.show(AccomplishAppointmentActivity.this, "服务器繁忙"+response);
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(AccomplishAppointmentActivity.this, msg);
            }
        });
    }

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

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {

        //商品名称
        String subject = productName.getText().toString();
        //商品定金
        String price = productDetail.getDj();
        //商品金额
        String deposit = productPrice.getText().toString();
        //预约电话
        String appointment = phone.getText().toString();
        //有效期
        String period_validity = date.getText().toString();
        //商品详情
        String body = appointment + period_validity + deposit;
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
                PayTask alipay = new PayTask(AccomplishAppointmentActivity.this);
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


}