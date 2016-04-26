package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.Product;
import com.minfo.quanmei.entity.ProductDetail;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.ConsultDialog;
import com.minfo.quanmei.widget.LoadingDialog;
import com.minfo.quanmei.widget.ShareDialog;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 产品详情页
 * liujing 2015-08-28
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    //top

    private TextView tvTitle;
    private ImageView ivLeft;
    private TextView tvRight;


    private Button tvConsult;
    private Button tvOrder;
    private RelativeLayout rlConsult;

    private ImageView ivBig;
    private TextView tvNewPrice;
    private TextView tvOldPrice;
    private TextView tvIntro;
    private ImageView ivOrder;
    private TextView tvDescription;
    private TextView tvDetailDescription;
    private ImageView ivHosLogo;
    private TextView tvHosName;
    private ImageView tvDetail;

    private Product product;
    private ProductDetail productDetail;
    private String hosId;
    private LoadingDialog loadingDialog;

    private IWXAPI iwxapi;
    private static Tencent mTencent;

    /**
     * 底部在线咨询是否需要闪烁
     */
    private boolean isNeedSplash = true;
    private int clo = 0;
    private MyHandler handler = new MyHandler(this);
    private ConsultDialog consultDialog;
    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        public void run() {
            /*runOnUiThread(new Runnable() {
                public void run() {
                    if (clo == 0) {
                        clo = 1;
                        tvConsult.setTextColor(getResources().getColor(R.color.basic_color));
                    } else {
                        clo = 0;
                        tvConsult.setTextColor(Color.WHITE);
                    }
                }
            });*/
        }
    };
    private Button appoint;
    private int id;
    private IUiListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
    }

    @Override
    protected void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setText("分享");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(this);


        ivBig = (ImageView) findViewById(R.id.iv_big);
        tvNewPrice = (TextView) findViewById(R.id.tv_new_price);
        tvOldPrice = (TextView) findViewById(R.id.tv_old_price);
        ivOrder = (ImageView) findViewById(R.id.iv_order);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvDetailDescription = (TextView) findViewById(R.id.tv_detail_description);
        ivHosLogo = (ImageView) findViewById(R.id.iv_hos_logo);
        tvHosName = (TextView) findViewById(R.id.tv_hos_name);
        tvDetail = (ImageView) findViewById(R.id.wv_detail);

        tvConsult = (Button) findViewById(R.id.btn_consult);
        tvOrder = (Button) findViewById(R.id.btn_order);
        rlConsult = (RelativeLayout) findViewById(R.id.rl_bottom_consult);
        appoint = ((Button) findViewById(R.id.btn_to_appiont));
        rlConsult.setVisibility(View.INVISIBLE);


        consultDialog = new ConsultDialog(this);
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 1000 * 5);*/
    }

    @Override
    protected void initViews() {

        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            product = (Product) bundle.getSerializable("product");
            if (product != null) {
                id = product.getId();
                reqProductdeiailData();


            }
        }
        ivLeft.setOnClickListener(this);
        loadingDialog = new LoadingDialog(this);

        mTencent = Tencent.createInstance(getString(R.string.qq_appid), this);
        listener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };
    }

    private class MyHandler extends Handler {
        private WeakReference<ProductDetailActivity> mActivity;

        public MyHandler(ProductDetailActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity != null) {
                if (msg.what == 1) {
                    if (!consultDialog.isShowing()) {
                        spark();
                        rlConsult.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    /**
     * 请求产品详情数据
     */
    private void reqProductdeiailData() {
        String url = getResources().getString(R.string.api_baseurl) + "tehui/Detail.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + id);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                productDetail = response.getObj(ProductDetail.class);

                Log.e(TAG,productDetail.toString());
                if (productDetail != null) {
                    setDetailData();
                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==11){
                    ToastUtils.show(ProductDetailActivity.this,"特惠不存在或已被删除");
                }else{
                    ToastUtils.show(ProductDetailActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(ProductDetailActivity.this, msg);

            }
        });
    }

    public void setDetailData() {
        UniversalImageUtils.displayImageUseDefOptions(productDetail.getBimg(), ivBig);



        tvNewPrice.setText(productDetail.getNewval() + "");
        tvOldPrice.setText("¥" + productDetail.getOldval() + "");
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        appoint.setText("预约(定金" + productDetail.getDj() + ")");
        tvOrder.setText("预约(定金" + productDetail.getDj() + ")");
        tvIntro.setText(productDetail.getIntro());
        tvDescription.setText(productDetail.getLc());
        tvDetailDescription.setText(productDetail.getLcnote());
        UniversalImageUtils.displayImageUseDefOptions(productDetail.getHimg(), ivHosLogo);
        tvHosName.setText(productDetail.getHname());
        hosId = productDetail.getHid();

        UniversalImageUtils.loadDefImage(productDetail.getDetail(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                tvDetail.setImageResource(R.mipmap.default_pic);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                tvDetail.setImageResource(R.mipmap.default_pic);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Matrix matrix = new Matrix();
                matrix.postScale(utils.getScreenWidth() / (bitmap.getWidth() * 1.0f), utils.getScreenWidth() / (bitmap.getWidth() * 1.0f)); //长和宽放大缩小的比例
                Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                tvDetail.setImageBitmap(resizeBmp);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                tvDetail.setImageResource(R.mipmap.default_pic);
            }
        });


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bottom_consult:
            case R.id.btn_consult:
                /*if (!consultDialog.isShowing()) {
                    consultDialog.show();
                    rlConsult.setVisibility(View.INVISIBLE);
                    timer.cancel();
                }
                handler.removeMessages(1);*/
                break;
            case R.id.btn_order:
                if (productDetail != null) {
                    reqMyServer();
                }
                break;
            case R.id.btn_to_appiont:
                if (productDetail != null) {
                    reqMyServer();
                }
                break;
            case R.id.btn_go_hospital:
                Intent intent = new Intent(this, HospitalActivity.class);
                intent.putExtra("ID", hosId);
                startActivity(intent);
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_right:
                ShareDialog shareDialog = new ShareDialog(this, shareListener);
                shareDialog.show();
                break;
        }
    }

    /**
     * 底部字体闪烁
     */
    private void spark() {
        timer.schedule(task, 1, 500);
    }


    private void reqMyServer() {
        //用户ID
        String userid = utils.getUserid() + "";
        //特惠ID
        String thid = productDetail.getId() + "";
        String url = getResources().getString(R.string.api_baseurl) + "order/AddOrder.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + userid + "*" + thid);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {

            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                jumpCompleteOrder(response.toString());
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if (errorcode == 15) {
                    ToastUtils.show(ProductDetailActivity.this, "您处于未登录状态");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(ProductDetailActivity.this, LoginActivity.class, null);
                } else if (errorcode == 17) {
                    Intent intent = new Intent(ProductDetailActivity.this, BindPhoneNumActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    ToastUtils.show(ProductDetailActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(ProductDetailActivity.this, msg);
            }
        });
    }

    /**
     * 跳转到完成预约界面
     *
     * @param responseStr
     */
    private void jumpCompleteOrder(String responseStr) {
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            String orderid = jsonObject.getString("orderid");
            String tel = jsonObject.getString("tel");
            Bundle bundle = new Bundle();
            bundle.putSerializable("productdetail", productDetail);
            bundle.putString("tel", tel);
            bundle.putString("orderid", orderid);
            utils.jumpAty(ProductDetailActivity.this, AccomplishAppointmentActivity.class, bundle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode,resultCode,data,listener);
            Log.e(TAG,"qq分享");
        }
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    reqMyServer();
                }
                break;
            default:
        }
    }

    ShareDialog.ShareClickListener shareListener = new ShareDialog.ShareClickListener() {
        @Override
        public void shareClick(ShareDialog.Type type) {
            switch (type) {
                case WECHAT_FRIEND:
                    shareWechat(0);
                    break;
                case WECHAT_CIRCLE:
                    shareWechat(1);
                    break;
                case QQ:
                    shareQq();
                    break;

            }

        }
    };

    /**
     * 分享到微信
     */
    private void shareWechat(int type) {
        iwxapi = WXAPIFactory.createWXAPI(this, getResources().getString(R.string.wxappid), true);
        iwxapi.registerApp(getResources().getString(R.string.wxappid));

        if (iwxapi.isWXAppInstalled()) {
            if (utils.isOnLine(this)) {
                int wxVersion = iwxapi.getWXAppSupportAPI();
                if (wxVersion >= 21020001) {
                    //分享后点击时进入的url
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = getString(R.string.qm_share_url);

                    Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                    WXMediaMessage msg = new WXMediaMessage();
                    msg.title = getString(R.string.qm_share_title);
                    msg.mediaObject = webpage;

                    msg.description = productDetail.getHname()+productDetail.getName();
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(img, 150, 150, true);

                    img.recycle();
                    msg.thumbData = utils.bmpToByteArray(thumbBmp, true);
                    //构造一个Req
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    if (type == 1) {
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;  //发送至朋友圈
                    } else if (type == 0) {
                        req.scene = SendMessageToWX.Req.WXSceneSession; // 默认（发送到消息会话）
                    }
                    // 调用api接口发送到微信
                    iwxapi.sendReq(req);

                } else {
                    ToastUtils.show(ProductDetailActivity.this, "您的微信版本过低,不支持此功能");
                }
            } else {
                ToastUtils.show(ProductDetailActivity.this, "请检查您的网络连接");
            }
        } else {
            ToastUtils.show(ProductDetailActivity.this, "请您先安装微信");
        }
    }

    /**
     * 分享到qq
     */
    private void shareQq() {

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, getString(R.string.qm_share_title));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, productDetail.getHname()+productDetail.getName());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, getString(R.string.qm_share_url));
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, getString(R.string.qq_img_share_url));
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
        mTencent.shareToQQ(this, params, listener);

    }
}
