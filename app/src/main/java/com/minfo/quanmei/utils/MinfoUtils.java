package com.minfo.quanmei.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.minfo.quanmei.R;

/**
 * 公共方法类
 * @author wangrui@min-fo.com
 */
public class MinfoUtils {
	private Context context;
	private SharedPreferences sp;
	private static List<AlertDialog> dlgList = new ArrayList<AlertDialog>();
	
	public  MinfoUtils(Context context){
		this.context = context;
		this.sp = context.getSharedPreferences("SpIpet", Activity.MODE_PRIVATE);
	}
	
	//获取本地保存的intime记录
	public int getIntime(int tag){
		int return_intime = 0;
		switch(tag){
		case 1:
			return_intime = sp.getInt("act", 0);
			break;
		case 2:
			return_intime = sp.getInt("cs", 0);
			break;
		case 3:
			return_intime = sp.getInt("ly", 0);
			break;
		case 4:
			return_intime = sp.getInt("jy", 0);
			break;
		case 5:
			return_intime = sp.getInt("jz", 0);
			break;
		}
		
		return return_intime;
	}
	
	public int getUserid(){
		return sp.getInt("userid", 0);
	}
	
    /** 
    * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
    */  
    public int dip2px(Context context, float dpValue) {  
      final float scale = context.getResources().getDisplayMetrics().density;  
      return (int) (dpValue * scale + 0.5f);  
    }  
      
    /** 
    * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
    */  
    public int px2dip(Context context, float pxValue) {  
      final float scale = context.getResources().getDisplayMetrics().density;  
      return (int) (pxValue / scale + 0.5f);  
    }   
	
	public List <NameValuePair> getParam(String mPostStr){
        List <NameValuePair> params = new ArrayList <NameValuePair>(); 
        params.add(new BasicNameValuePair("minfo", mPostStr));
        return params;
    }
	
	public String getServerData(String mURL, String mPostStr) throws JSONException{
        HttpPost httpRequest = new HttpPost(mURL); 
        try{ 
        	/*HTTP request*/
        	httpRequest.setEntity(new UrlEncodedFormEntity(getParam(mPostStr), HTTP.UTF_8)); 
        	/*HTTP response*/
        	HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
        	System.out.println("httpResponse.getStatusLine().getStatusCode()"+httpResponse.getStatusLine().getStatusCode());
        	/*状态码200 ok*/
        	if(httpResponse.getStatusLine().getStatusCode() == 200){ 
        		return EntityUtils.toString(httpResponse.getEntity()); 
        	}
        } 
        catch (ClientProtocolException e){  
        	e.printStackTrace(); 
        } 
        catch (IOException e){  
        	e.printStackTrace(); 
        } 
        catch (Exception e){  
        	e.printStackTrace();  
        }
    	return null;
    }
	
	
	
	public void cleanWait(){
		for (int i = 0; i < dlgList.size(); i++) {
			dlgList.get(i).dismiss();
		}
		dlgList.clear();
	}
	
	/**
     * 获取 基本信息 字符串
     * @return appid+deviceid+version+ostype+厂家+机型+SDK+phonenum(或者imsi)
     * author wangrui@min-fo.com
     * date 2014-05-21
     */
    public String getBasePostStr(){
    	SIMCardInfo cardInfo = new SIMCardInfo(context);
		String phoneNum = null;
		if (cardInfo.getPhoneNumber().length() == 0
				|| cardInfo.getPhoneNumber() == null) {
			phoneNum = cardInfo.getPhoneImsi();
		} else {
			phoneNum = cardInfo.getPhoneNumber();
		}
		String postString = context.getResources().getString(R.string.appid)
				+ "*" + cardInfo.getDeviceId() + "*"
				+ context.getResources().getString(R.string.version)
				+ "*android" + DeviceInfo.getVersionReleaseInfo() + "*"
				+ DeviceInfo.getManufacturerInfo() + "*"
				+ DeviceInfo.getModleInfo() + "*"
				+ DeviceInfo.getVersionSDKInfo() + "*" + phoneNum;
		return postString;
    }
	
	/**
	 * 判断是否连网,3G or Wifi
	 * @param mAct:调用此方法的Activity
	 * @author wangrui
	 * @date 2014-05-21
	 */
	public Boolean isOnLine(Activity mAct) {
		ConnectivityManager manager = (ConnectivityManager) mAct
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 向指定的Handler send massage
	 * @param handler: 监听线程的handler对象
	 * @param msgtag: 发送的message
	 * author wangrui
	 * date 2015-03-13
	 */
	public void sendMsg(Handler handler, int msgtag){
		Message m = new Message();
		m.what = msgtag;
		handler.sendMessage(m);
	}
	/**
	 * 页面跳转
	 * author wangrui
	 */
	public void intentPage(Class<?> mClass, Bundle bundle, Activity act){
		Intent it = new Intent();
		it.setClass(context, mClass);
		if(bundle != null){
			it.putExtras(bundle);
		}
		context.startActivity(it);
		act.finish();
	}
	
	/**
	 * 转换字符串，当传递有汉字的内容时需转化
	 * author wangrui
	 */
	public String changeStr(String mStr) {
		String str = "";
		byte[] strByte = mStr.getBytes();
		for (int i = 0; i < strByte.length; i++) {
			str += strByte[i] + "#";
		}
		return str.trim();
	}
	
}