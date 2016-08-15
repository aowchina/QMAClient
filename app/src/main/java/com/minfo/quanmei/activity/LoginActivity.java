package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.receiver.LoginSuccessReceiver;
import com.minfo.quanmei.service.LoginSuccessService;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LoadingDialog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivLeft;
    private TextView tvRight;
    private TextView tvTitle;

    private TextView tvPolicy;
    private TextView tvSecurity;


    private TextView tvForgetPwd;
    private EditText etUserName;
    private EditText etPwd;

    private String phoneNumber;
    private String pwd;

    private LoadingDialog loadingDialog;

    /*--QQ相关--*/
    private Tencent mTencent;
    public static QQAuth mQQAuth;
    private IUiListener listener;
    private UserInfo userInfo;
    String url;
    private String openid;


    //微博相关
    public static final String APP_KEY = "3430399716";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private String uid;
    private String token;
    private String weiboInfoUrl = "https://api.weibo.com/2/users/show.json";

    //微信相关
    private String AppID = "wx719c1e4eebfb3fb7";
    private String AppSecret = "b931d772cbf4ad7d1330779dc054108d";
    private IWXAPI mWeixinAPI;

    public static  boolean isJumpLogin = false;//表示是否是在应用某个界面跳转过来的而不是init页面过来的

    public static Handler liujing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        url = getResources().getString(R.string.api_baseurl) + "public/Login.php";

        liujing = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0) {
                    if(!isJumpLogin) {
                        utils.jumpPage(MainActivity.class, null, LoginActivity.this);
                    }else{
                        finish();
                    }
                }
            }
        };
    }

    @Override
    protected void findViews() {

        loadingDialog = new LoadingDialog(this);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        etUserName = (EditText) findViewById(R.id.et_user_name);
        etPwd = (EditText) findViewById(R.id.et_password);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("登录");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("注册");
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_password);
        tvForgetPwd.setOnClickListener(this);

        tvPolicy = (TextView) findViewById(R.id.tv_policy);
        tvSecurity = (TextView) findViewById(R.id.tv_security);

        tvPolicy.setText(Html.fromHtml("<u>"+"全美用户许可协议"+"</u>"));
        tvSecurity.setText(Html.fromHtml("<u>"+"隐私条款"+"</u>"));

        tvPolicy.setOnClickListener(this);
        tvSecurity.setOnClickListener(this);


    }

    @Override
    protected void initViews() {


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                Intent intent = new Intent(this, Register1Activity.class);
                startActivity(intent);
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
            case R.id.btn_qq_login:
                qqLogin();
                break;
            case R.id.btn_weibo_login:
                weiboLogin();
                break;
            case R.id.btn_login_normal:
                if (checkInput()) {
                    loginNormal();
                }
                break;
            case R.id.btn_wechat_login:
                wechatLogin();
                break;
            case R.id.tv_policy:
                PolicyActivity.CATEGORY_TAG = 0;
                utils.jumpAty(this,PolicyActivity.class,null);
                break;
            case R.id.tv_security:
                PolicyActivity.CATEGORY_TAG = 1;
                utils.jumpAty(this,PolicyActivity.class,null);
                break;
        }
    }

    /**
     * 微信登录
     */
    private void wechatLogin() {

        mWeixinAPI = WXAPIFactory.createWXAPI(this, AppID, true);
        mWeixinAPI.registerApp(AppID);

        if (mWeixinAPI.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            mWeixinAPI.sendReq(req);
        } else {
            ToastUtils.show(this, "您需要先安装微信");
        }
    }

    /**
     * 正常登录
     */
    private void loginNormal() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + etUserName.getText().toString() + "*" + etPwd.getText().toString());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                User user = response.getObj(User.class);
                utils.setUserid(user.getUserid());
                utils.setLogin(true);
                utils.setUserimg(user.getUserimg());

                Constant.user = user;
                if(!isJumpLogin) {
                    utils.jumpAty(LoginActivity.this, MainActivity.class, null);
                }else{
                    sendBroadcast(new Intent("com.minfo.quanmei.load.head.image"));
                    setResult(1);
                }

                LoginActivity.this.finish();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                int errorcode = response.getErrorcode();
                if(errorcode==10){
                    ToastUtils.show(LoginActivity.this,"手机号不符合要求");
                }else if(errorcode==11) {
                    ToastUtils.show(LoginActivity.this,"密码不符合要求");
                }if(errorcode==13){
                    ToastUtils.show(LoginActivity.this,"手机号不存在");
                }else if(errorcode==14){
                    ToastUtils.show(LoginActivity.this,"密码错误");
                }else {
                    ToastUtils.show(LoginActivity.this,"服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                Log.e(TAG,code+"");
                ToastUtils.show(LoginActivity.this,msg);
            }
        });
    }

    /**
     * 检查登录输入信息
     */
    private boolean checkInput() {
        phoneNumber = etUserName.getText().toString();
        pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.show(this, "手机号不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "密码不能为空！");
            return false;
        }
        if (!MyCheck.isTel(phoneNumber)) {
            ToastUtils.show(this, "手机号格式不对！");
            return false;
        }
        if(!MyCheck.isPsw(pwd)){
            ToastUtils.show(this,"密码必须是6-15位大小写字母或数字");
            return false;
        }
        return true;


    }

    /**
     * 微博登录
     */
    private void weiboLogin() {
        mAuthInfo = new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * qq登录
     */
    private void qqLogin() {
        mQQAuth = QQAuth.createInstance(getString(R.string.qq_appid), getApplicationContext());
        mTencent = Tencent.createInstance(getString(R.string.qq_appid), this.getApplicationContext());
        listener = new IUiListener() {
            @Override
            public void onError(UiError arg0) {

            }

            @Override
            public void onComplete(Object response) {
                try {
                    JSONObject jo = (JSONObject) response;
                    int ret = jo.getInt("ret");
                    if (ret == 0) {
                        openid = jo.getString("openid");
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openid);
                        mTencent.setAccessToken(accessToken, expires);
                        //获取用户信息
                        userInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
                        userInfo.getUserInfo(new QQUserInfoListener());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancel() {
            }
        };
        mTencent.loginWithOEM(this, "all", listener, "10000144", "10000144", "xxxx");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void reqThirdLoginServer(int type, String thirdId, String nickname, String imgUrl) {
        String url = getResources().getString(R.string.api_baseurl) + "public/Login_dsf.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + type + "*" + thirdId + "*" + nickname + "*" + imgUrl);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog.show();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e(TAG,response.toString());
                loadingDialog.dismiss();
                User user = response.getObj(User.class);
                utils.setUserid(user.getUserid());
                Constant.user = user;
                if(!isJumpLogin) {
                    utils.jumpAty(LoginActivity.this, MainActivity.class, null);
                }
                LoginActivity.this.finish();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loadingDialog.dismiss();
                ToastUtils.show(LoginActivity.this,"服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(LoginActivity.this,msg);
            }
        });


    }

    /**
     * qq登录获取用户信息回调接口
     */
    private class QQUserInfoListener implements IUiListener {

        @Override
        public void onComplete(Object value) {
            if (value == null) {
                return;
            }
            try {
                JSONObject jo = (JSONObject) value;
                String nickName = utils.convertNickname(jo.getString("nickname"));
                String gender = jo.getString("gender");
                String figureUrl = jo.getString("figureurl_qq_1");
                String level = jo.getString("level");

                reqThirdLoginServer(3, openid, nickName, figureUrl);


            } catch (Exception e) {
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    /**
     * 新浪微博登录授权
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {//AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken); //保存Token
                uid = mAccessToken.getUid();
                token = mAccessToken.getToken();

                weiboInfoUrl = "https://api.weibo.com/2/users/show.json?source=" + APP_KEY + "&access_token=" + token + "&uid=" + uid;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weiboInfoUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String weibonickname = utils.convertNickname(jsonObject.getString("screen_name"));
                            String imgUrl = jsonObject.getString("avatar_hd");
                            reqThirdLoginServer(2, uid, weibonickname, imgUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

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
                httpClient.jsonReq(jsonObjectRequest);
            } else {
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }

        @Override
        public void onCancel() {

        }
    }

}
