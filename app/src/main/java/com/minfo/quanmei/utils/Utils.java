package com.minfo.quanmei.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.minfo.quanmei.R;
import com.minfo.quanmei.jni.JniClient;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liujing on 15/8/20.
 */
public class Utils {
    private Context context;
    private SharedPreferences sp;

    public  Utils(Context context){
        this.context = context;
        this.sp = context.getSharedPreferences("SpQm", Activity.MODE_PRIVATE);
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
        if (cardInfo.getPhoneNumber().length() == 0 || cardInfo.getPhoneNumber() == null) {
            phoneNum = cardInfo.getPhoneImsi();
        } else {
            phoneNum = cardInfo.getPhoneNumber();
        }

        if(phoneNum == null || phoneNum.equals("")){
            phoneNum = "000000000000000";
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
        ConnectivityManager manager = (ConnectivityManager) mAct.getSystemService(Context.CONNECTIVITY_SERVICE);
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



    public int getUserid(){
        return sp.getInt("userid", 0);
    }
    public void setUserid(int userid){
        sp.edit().putInt("userid",userid).commit();
    }

    public void setReceiveNum(String receiveType,int replyNum){
        sp.edit().putInt(receiveType,replyNum).commit();
    }
    public int getReceiveNum(String receiveType){
        return sp.getInt(receiveType, 0);
    }

    public void setLastMessageTime(String lastMessageTime){
        sp.edit().putString("lastMessageTime", lastMessageTime).commit();
    }
    public String getLastMessageTime(){
       return  sp.getString("lastMessageTime","");
    }


    public String getCUserid(){
        return sp.getString("cuserid", "");
    }
    public void setCUserid(String cuserid){
        sp.edit().putString("cuserid", cuserid).commit();
    }

    public void jumpAty(Context mContext,Class clazz,Bundle bundle){
        Intent intent = new Intent(mContext,clazz);
        if(bundle!=null) {
            intent.putExtra("info", bundle);
        }
        context.startActivity(intent);
    }

    public String convertNickname(String nickname){
        String temp = "";
        try {
            byte[] strTemp = nickname.getBytes("utf-8");
            for (int i = 0; i < strTemp.length; i++) {
                temp += strTemp[i] + "#";
            }
            return temp;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String convertChinese(String str){
        String temp = "";
        try {
            byte[] strTemp = str.getBytes("utf-8");
            for (int i = 0; i < strTemp.length; i++) {
                temp += strTemp[i] + "#";
            }
            return temp;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回应用版本名
     * @return
     */
    public String getVersionName(){
        PackageManager packageManager = context.getPackageManager();
        String version = "";
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    /**
     * 返回应用版本号
     * @return
     */
    public int getVersionCode(){
        PackageManager packageManager = context.getPackageManager();
        int  versionCode = 1;
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public Map<String,String> getParams(String mPostStr){
        int str_counter = (mPostStr.length() / 60);
        if((mPostStr.length() % 60) > 0){
            str_counter = str_counter + 1;
        }

        Map<String,String> params = new HashMap<>();
        params.put("p0", String.valueOf(str_counter + 3));
        params.put("p1", context.getResources().getString(R.string.appid));
        params.put("p2", "2");

        for(int i = 3; i <= (str_counter + 2); i++){
            if(mPostStr.length() < 60){
                params.put("p" + i, JniClient.GetEncodeStr(mPostStr.substring(0, mPostStr.length())));
            }
            else{
                params.put("p" + i, JniClient.GetEncodeStr(mPostStr.substring(0, 60)));
                mPostStr = mPostStr.substring(60);
            }
        }
        return params;
    }

    public void jumpPage(Class<?> mClass, Bundle bundle, Activity act){
        Intent it = new Intent(Settings.ACTION_ADD_ACCOUNT);
        it.setClass(context, mClass);
        if(bundle != null){
            it.putExtras(bundle);
        }
        context.startActivity(it);
        act.finish();
    }

    /**
     * 判断app是否启动
     * @param packageName
     * @return
     */
    public  boolean isAppAlive(String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                return true;
            }
        }
        return false;
    }

    public  int dip2px( float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽
     * @return
     */
    public int getScreenWidth(){
        Activity activity = (Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高
     * @return
     */
    public int getScreenHeight(){
        Activity activity = (Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    //保存字符串到文件中
    public void saveAsFileWriter(String content,String fielName) {

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(fielName);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
