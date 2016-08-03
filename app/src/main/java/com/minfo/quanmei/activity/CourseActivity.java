package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.CourseDetail;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.PayResult;
import com.minfo.quanmei.utils.Pay_Utils;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.LoadingDialog;
import com.minfo.quanmei.widget.PayMethodDialog;
import com.minfo.quanmei.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class CourseActivity extends BaseActivity implements View.OnClickListener, PayMethodDialog.PayCommitListener {

    private TextView tvTitle;
    private ImageView ivLeft;

    private ImageView ivCourse;

    private TextView tvMinus;
    private TextView tvAdd;
    private TextView tvCount;

    private TextView tvCourseName;
    private TextView tvTeacherName;
    private TextView tvCourseDetail;
    private TextView tvTeacherDetail;
    private TextView tvCoursePrice;

    private PayMethodDialog payMethodDialog;

    private int count;
    private String teacherid = "";
    private CourseDetail courseDetail;
    private LoadingDialog loadingDialog;
    private int method = 1;
    //支付宝常量
    private static final int SDK_PAY_FLAG = 1;
    private String orderid;

    private String courseId;
    private String coursePrice;

    //微信支付
    private IWXAPI wxapi;

    private String wx_prepayid = "";
    private String wx_prepay_nonestr = "";
    private PayReq wx_payReq;

    public static Handler wxPayHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        wxPayHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    ToastUtils.show(CourseActivity.this, "支付成功");
                }
            }
        };
    }

    @Override
    protected void findViews() {
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("王牌课程：《由内内外的富足》");

        tvCoursePrice = (TextView) findViewById(R.id.tv_course_price);
        tvCourseName = (TextView) findViewById(R.id.tv_course_name);
        tvTeacherName = (TextView) findViewById(R.id.tv_teacher_name);
        ivCourse = (ImageView) findViewById(R.id.iv_course);
        tvMinus = (TextView) findViewById(R.id.tv_minus);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvTeacherDetail = (TextView) findViewById(R.id.tv_teacher_detail);
        tvCourseDetail = (TextView) findViewById(R.id.tv_course_detail);
        tvMinus.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        loadingDialog = new LoadingDialog(this);
        payMethodDialog = new PayMethodDialog(this, this);


    }

    @Override
    protected void initViews() {
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null && bundle.getString("teacherid") != null) {
            teacherid = bundle.getString("teacherid");
            if (utils.isOnLine(this)) {
                reqCourseData();
            } else {
                ToastUtils.show(this, "暂时无网络");
            }
        }
    }

    /**
     * 请求课程详情数据
     */
    private void reqCourseData() {
        String url = getResources().getString(R.string.api_baseurl) + "course/CourseDetail.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + teacherid);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e(TAG,response.toString());
                loadingDialog.dismiss();
                courseDetail = response.getObj(CourseDetail.class);
                if (courseDetail != null) {
                    bindData();
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if (errorcode == 11) {
                    ToastUtils.show(CourseActivity.this, "教师不存在或已被删除");
                } else if (errorcode == 12) {
                    ToastUtils.show(CourseActivity.this, "课程不存在或已被删除");
                } else {
                    ToastUtils.show(CourseActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(CourseActivity.this, msg);
            }

        });
    }

    /**
     * 显示课程详情数据
     */
    private void bindData() {
        if (!TextUtils.isEmpty(courseDetail.getCourse_name())) {
            tvTitle.setText(courseDetail.getCourse_name());
            tvCourseName.setText(courseDetail.getCourse_name());
        }
        tvTeacherName.setText(courseDetail.getTeacher_name());
        tvCoursePrice.setText(courseDetail.getCourse_price());
        UniversalImageUtils.displayImageUseBigOptions(courseDetail.getCourse_banner(),ivCourse);
        tvTeacherDetail.setText(courseDetail.getTeacher_intro());
        tvCourseDetail.setText(courseDetail.getCourse_intro());

    }

    @Override
    public void onClick(View v) {
        count = Integer.parseInt(tvCount.getText().toString());
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_minus:
                if (count > 1) {
                    count--;
                }
                tvCount.setText(count + "");
                break;
            case R.id.tv_add:
                count++;
                tvCount.setText(count + "");
                break;
            case R.id.btn_buy:
                courseId = courseDetail.getCourse_id();
                coursePrice = courseDetail.getCourse_price();
                payMethodDialog.show();

                break;

        }
    }

    /**
     * 请求订单接口
     */
    private void reqAddOrder() {
        String url = getResources().getString(R.string.api_baseurl) + "course/AddOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" +
                courseId + "*" + count + "*" + coursePrice);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.getData().toString());
                    orderid = jsonObject.getString("orderid");
                    if (!TextUtils.isEmpty(orderid)) {
                        pay();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 14 || errorcode == 15 || errorcode == 17) {
                    ToastUtils.show(CourseActivity.this, "用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(CourseActivity.this, LoginActivity.class, null);
                } else if (errorcode == 18) {
                    Intent intent = new Intent(CourseActivity.this, BindPhoneNumActivity.class);
                    startActivityForResult(intent, 1);
                } else if (errorcode == 19) {
                    ToastUtils.show(CourseActivity.this, "该课程已被删除");
                } else {
                    ToastUtils.show(CourseActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(CourseActivity.this, msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    reqAddOrder();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    reqWxAddOrder();
                }
        }
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
                        ToastUtils.show(CourseActivity.this, "支付成功");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态)
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.show(CourseActivity.this, "支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.show(CourseActivity.this, "支付失败");

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
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {

        //商品名称
        String subject = courseDetail.getCourse_name();
        //商品金额
        String deposit = String.valueOf(Float.parseFloat(courseDetail.getCourse_price())*count);
        //有效期
        String period_validity = "20170101";
        //商品详情
        String body = period_validity + deposit;
        // 订单
        /**
         * @key
         * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
         */
//        key = UUID.randomUUID().toString();
        String orderInfo = Pay_Utils.getOrderInfo(subject, body, deposit, orderid);
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
                PayTask alipay = new PayTask(CourseActivity.this);
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

    @Override
    public void submit(int method) {
        this.method = method;
        if (method == 1) {
            reqAddOrder();
        } else {
            wxapi = WXAPIFactory.createWXAPI(this, null);
            wxapi.registerApp(getString(R.string.wxappid));
            reqWxAddOrder();
        }
    }

    private void reqWxAddOrder() {
        String url = getString(R.string.api_baseurl) + "course/PayReq.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + courseId + "*" + count + "*" + coursePrice);
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
                    wx_prepay_nonestr = jsonObject1.getString("nonce_str");
                    WXPayEntryActivity.orderid = orderid;
                    WXPayEntryActivity.type = 2;
                    reqWxPay();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 14 || errorcode == 15 || errorcode == 17) {
                    ToastUtils.show(CourseActivity.this, "用户未登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(CourseActivity.this, LoginActivity.class, null);
                } else if (errorcode == 18) {
                    Intent intent = new Intent(CourseActivity.this, BindPhoneNumActivity.class);
                    startActivityForResult(intent, 2);
                } else if (errorcode == 19) {
                    ToastUtils.show(CourseActivity.this, "该课程已被删除");
                } else {
                    ToastUtils.show(CourseActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(CourseActivity.this, msg);
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
}
