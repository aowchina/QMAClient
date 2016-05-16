package com.minfo.quanmei.activity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Order;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.SquareImage;

import java.util.Hashtable;
import java.util.Map;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivLeft;
    private TextView tvTitle;

    private LinearLayout llGenerateCode;

    private AlertDialog dialog;
    private Bitmap mBitmap;
    private Order order;
    private String orderid;

    SquareImage ivProductImg;
    TextView tvProductName;
    TextView tvHospitalName;
    TextView tvTotalPrice;
    TextView tvPayMoney;
    TextView tvMinusMoney;
    TextView tvRealMoney;
    TextView tvPoint;
    TextView tvTime;
    TextView tvOrderid;
    TextView tvPhone;
    TextView tvPayType;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111) {
                if (mBitmap != null) {
                    initDialog();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("订单详情");
        ivLeft.setOnClickListener(this);
        llGenerateCode = (LinearLayout) findViewById(R.id.ll_generate_code);
        llGenerateCode.setOnClickListener(this);

        ivProductImg = (SquareImage) findViewById(R.id.iv_product_simg);
        tvProductName = (TextView) findViewById(R.id.tv_product_name);
        tvHospitalName = (TextView) findViewById(R.id.tv_hospital_name);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvMinusMoney = (TextView) findViewById(R.id.tv_minus_money);
        tvRealMoney = (TextView) findViewById(R.id.tv_real_money);
        tvPoint = (TextView) findViewById(R.id.tv_point);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvOrderid = (TextView) findViewById(R.id.tv_orderid);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvPayType = (TextView) findViewById(R.id.tv_pay_type);
        tvPayMoney = (TextView) findViewById(R.id.tv_pay_money);

    }

    @Override
    protected void initViews() {
        Bundle bundle = getIntent().getBundleExtra("info");
        if(bundle!=null){
            orderid = bundle.getString("orderid");
            reqOrderDetail();
        }
    }

    /**
     * 请求订单详情
     */
    private void reqOrderDetail() {
        String url = getResources().getString(R.string.api_baseurl) + "order/OrderDetail.php";
        Log.e(TAG,Constant.user.getUserid() + "*" + orderid);
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + orderid);
        Log.e(TAG,params.toString());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e(TAG, response.toString());

                order = response.getObj(Order.class);
                setOrderView();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(OrderDetailActivity.this, "服务器繁忙"+response.getErrorcode());
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(OrderDetailActivity.this, msg);
            }
        });
    }

    private void setOrderView() {
        UniversalImageUtils.displayImageUseDefOptions(order.getSimg(), ivProductImg);
        tvHospitalName.setText(order.getHname());
        tvOrderid.setText(orderid);
        tvProductName.setText("【" + order.getFname() + "】" + order.getName());
        tvTime.setText(order.getCtime());
        tvTotalPrice.setText(order.getNewval()+"元");
        tvRealMoney.setText("￥"+order.getTrue_pay()+"");
        tvPoint.setText(order.getGet_point());
        tvMinusMoney.setText("(积分抵" + order.getPoint_money() + "元)");
        tvPhone.setText(order.getTel());
        if(order.getWk_money().equals("0.00")){
            tvPayType.setText("定金支付");
            tvPayMoney.setText("￥"+order.getDj());
        }else{
            tvPayType.setText("全款支付");
            tvPayMoney.setText("￥"+order.getNewval());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.ll_generate_code:
                generateBarCode();
                break;
        }
    }

    private void initDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialog = new AlertDialog.Builder(this,R.style.dialog).create();
        }
        dialog.show();
        dialog.getWindow().setContentView(R.layout.layout_bar_code);
        ImageView ivCode = (ImageView) dialog.getWindow().findViewById(R.id.iv_bar_code);
        ivCode.setImageBitmap(mBitmap);
    }

    private void generateBarCode() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mBitmap = Create2DCode(orderid);
                    utils.sendMsg(mHandler, 111);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public Bitmap Create2DCode(String str) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, utils.dip2px(300), utils.dip2px(300), hints);
        int width = matrix.getWidth();


        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
