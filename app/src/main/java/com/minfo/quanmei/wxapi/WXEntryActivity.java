package com.minfo.quanmei.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.Utils;
import com.minfo.quanmei.widget.LoadingDialog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by liujing on 15/9/29.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI iw;
    private String openid;
    private final static String wxAppid = "wx719c1e4eebfb3fb7";
    private final static String wxSecret = "b931d772cbf4ad7d1330779dc054108d";
    private String wx_openid = "";
    private VolleyHttpClient httpClient;
    private Utils utils;
    private LoadingDialog loadingDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iw = WXAPIFactory.createWXAPI(this, wxAppid, true);
        iw.registerApp(wxAppid);
        iw.handleIntent(getIntent(), this);
        utils = new Utils(this);


    }

    private void reqThirdLoginServer(int type, String thirdId, String nickname, String imgUrl) {
        String url = getResources().getString(R.string.api_baseurl) + "public/Login_dsf.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + type + "*" + thirdId + "*" + nickname + "*" + imgUrl);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                User user = response.getObj(User.class);

                utils.sendMsg(LoginActivity.liujing,0,user);
                WXEntryActivity.this.finish();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
               int errorcode = response.getErrorcode();
                if(errorcode==9){
                    ToastUtils.show(WXEntryActivity.this, "昵称过长");
                }else if(errorcode==23){
                    ToastUtils.show(WXEntryActivity.this,"用户已被封");
                }else{
                    ToastUtils.show(WXEntryActivity.this,"服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(WXEntryActivity.this,"服务器繁忙");
            }


        });
    }

    private void getUserInfo(String access_token, final String openid) {
        final String reqUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String img = jsonObject.getString("headimgurl");
                    String name = jsonObject.getString("nickname");
                    reqThirdLoginServer(1, openid, utils.convertNickname(name), img);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("", error.getMessage());
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(
                            new String(response.data, "UTF-8"));
                    return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
        httpClient.jsonReq(request);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResp(BaseResp arg0) {
        Log.e("微信回调",arg0.errStr+"");
        switch (arg0.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (arg0.getType() == 1) {
                    String code = ((SendAuth.Resp) arg0).code;
                    final String reqUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + wxAppid
                            + "&secret=" + wxSecret + "&code=" + code + "&grant_type=authorization_code";

                    httpClient = new VolleyHttpClient(this);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reqUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try {
                                openid = jsonObject.getString("openid");
                                String access_token = jsonObject.getString("access_token");

                                getUserInfo(access_token, openid);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("", error.getMessage());
                        }
                    });
                    httpClient.jsonReq(request);

                } else {
                    ToastUtils.show(this, "分享成功!");
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtils.show(this, "取消操作!");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastUtils.show(this, "微信授权失败!");
                break;
            default:
                ToastUtils.show(this, "网络不给力, 操作失败!");
                break;
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iw.handleIntent(intent, this);
    }
}
