
package com.minfo.quanmei.http;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.minfo.quanmei.widget.LoadingDialog;

import java.util.Map;


public class VolleyHttpClient {


    private VolleyHttpClient mInstance;

    private VolleySingleton volleySingleton;

    private LoadingDialog mDialig;

    private Context mContext;

//    public static synchronized VolleyHttpClient getInstance(Context context){
//        if(mInstance == null){
//            mInstance = new VolleyHttpClient(context);
//        }
//        mInstance.initDialog(context);
//        return mInstance;
//    }


    public VolleyHttpClient(Context context) {

        mContext = context;
        initDialog(context);
        volleySingleton = VolleySingleton.getInstance(context);

    }

    private void initDialog(Context context) {

        if (context instanceof Activity)
            mDialig = new LoadingDialog(context);
    }

    public void jsonReq(JsonObjectRequest request){
        volleySingleton.addToRequestQueue(request);
    }


    public void post(String url, Map<String, String> params,int loadingMsg,final RequestListener listener) {

        request(Request.Method.POST, url, params, loadingMsg, listener);
    }


    public void get(String url,int loadingMsg,final RequestListener listener) {

        request(Request.Method.GET, url, null, loadingMsg, listener);
    }

    public void request(int method,String url,Map<String, String> params,int loadingMsg,final RequestListener listener) {


        if (listener != null)
            listener.onPreRequest();

        BaseRequest request = new BaseRequest(method,url,params,new Response.Listener<BaseResponse>() {

                    public void onResponse(BaseResponse response) {
                        if (listener != null) {
                            if (response.isSuccess()) {
                                if(response.getEc()==0) {
                                    listener.onRequestSuccess(response);
                                }else{
                                    int ec = response.getEc();
                                    switch (ec){
                                        case 101:
                                            listener.onRequestError(ec,"数据格式错误");
                                            break;
                                        case 102:
                                            listener.onRequestError(ec,"解密失败");
                                            break;
                                    }
                                }
                            }else{
                                listener.onRequestNoData(response);
                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String errMsg = null;
                            int errCode = -1;
                            if (error == null) {

                                errMsg = "请求服务器出错，错误代码未知";
                            } else {
                                errMsg = VolleyErrorHelper.getMessage(mContext, error);

                                errCode = error.networkResponse == null ? errCode : error.networkResponse.statusCode;
                            }
                            if (listener != null) {
                                listener.onRequestError(errCode, errMsg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );


        volleySingleton.addToRequestQueue(request);
    }


    private void showLoading(int msg) {

        if (msg > 0 && mDialig != null) {

            mDialig.setMessage(msg);
            mDialig.show();

        }

    }


    private void dismissLoading() {

        if (mDialig != null && mDialig.isShowing())
            mDialig.dismiss();
    }

}
