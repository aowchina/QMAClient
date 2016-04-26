package com.minfo.quanmei.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.AccomplishAppointmentActivity;
import com.minfo.quanmei.activity.CourseActivity;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.Utils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Utils utils;
    private VolleyHttpClient httpClient;

    public static String orderid;
    public static int type = 1;//默认为特惠，课程为2

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils(this);
        httpClient = new VolleyHttpClient(this);
        api = WXAPIFactory.createWXAPI(this, getString(R.string.wxappid));
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(final BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //验证支付的errorcode
            Log.e("aaa",resp.errCode+"");
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                //客户端支付成功，请求服务器看是否确实成功
                if (utils.isOnLine(WXPayEntryActivity.this)) {
                    String url = getString(R.string.api_baseurl) + "weixin/PayNotify.php";
                    Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + orderid);
                    httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
                        @Override
                        public void onPreRequest() {

                        }

                        @Override
                        public void onRequestSuccess(BaseResponse response) {
                            if(type==1) {
                                utils.sendMsg(AccomplishAppointmentActivity.wxPayHandler, 0);
                            }else if(type==2){
                                utils.sendMsg(CourseActivity.wxPayHandler, 0);
                            }
                        }

                        @Override
                        public void onRequestNoData(BaseResponse response) {
                            ToastUtils.show(WXPayEntryActivity.this,"服务器繁忙");
                        }

                        @Override
                        public void onRequestError(int code, String msg) {
                            ToastUtils.show(WXPayEntryActivity.this, msg);
                        }
                    });
                } else {
                    ToastUtils.show(WXPayEntryActivity.this, "请检查您的网络连接");
                }
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                ToastUtils.show(WXPayEntryActivity.this, "取消操作");
            } else {
                ToastUtils.show(WXPayEntryActivity.this, "服务器繁忙");
            }

            WXPayEntryActivity.this.finish();
        }
    }
}