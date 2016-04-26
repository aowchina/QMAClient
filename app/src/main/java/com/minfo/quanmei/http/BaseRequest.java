
package com.minfo.quanmei.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.minfo.quanmei.BuildConfig;
import com.minfo.quanmei.jni.JniClient;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;



public class BaseRequest extends Request<BaseResponse> {

    /*static {
        System.loadLibrary("QUANMEI");
    }*/

    public static final String TAG="BaseRequest";

    private  Response.Listener<BaseResponse> mListener;
    private Map<String,String> mParams;

    public BaseRequest(int method, String url, Map<String,String> params, Response.Listener listener, Response.ErrorListener Errorlistener) {
        super(method, url, Errorlistener);

        mListener = listener;
        this.mParams =params;
//       setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



    @Override
    protected Response<BaseResponse> parseNetworkResponse(NetworkResponse response) {

        try {

            String jsonString  = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Log.d(TAG,"respone:"+jsonString);
            if (BuildConfig.DEBUG)
                Log.d(TAG,"respone:"+jsonString);
            BaseResponse baseResponse = parseJson(jsonString);
            return Response.success(baseResponse,HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    protected void deliverResponse(BaseResponse response) {

        mListener.onResponse(response);
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
    private BaseResponse parseJson(String json)
    {
        BaseResponse response = new BaseResponse();
        int errorcode =0;
        String msg=null;
        String data=null;
        int active = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            errorcode =  jsonObject.getInt("errorcode");
            data =jsonObject.getString("data");
            active = jsonObject.getInt("active");

            if(active==1) {
                if (data.length() > 0) {
                    data = decodeStr(data);
                    if(data.length()==0){
                        response.setEc(102);
                    }
                }
            }

        } catch (Exception e){
            e.printStackTrace();
            response.setEc(101);
        }

        response.setErrorcode(errorcode);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    private String decodeStr(String data){
        try {
            JSONObject jsonObj = new JSONObject(data);
            String DataJson = "";
            int count = jsonObj.getInt("cnt");
            for (int i = 0; i < count; i++) {
                String strTemp = jsonObj.getString(i + "");
                if (strTemp.length() != 0) {
                    DataJson += JniClient.GetDecodeStr(strTemp);
                }
            }
            data = DataJson;
        }catch (Exception e){
            return "";
        }
        return data;
    }


}
