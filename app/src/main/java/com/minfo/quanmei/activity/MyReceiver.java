package com.minfo.quanmei.activity;

/**
 * Created by liujing on 15/10/31.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.minfo.quanmei.R;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Utils;
import com.minfo.quanmei.widget.ForceExitDialog;

import java.util.Calendar;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Utils utils = new Utils(context);

        this.context = context;
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG,regId);
            MyApplication.getInstance().registrationId = regId;
            reqSetPushID(regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.e(TAG,message);
            if(message!=null&&message.equals("设备已登录")){
                ForceExitDialog forceExitDialog = new ForceExitDialog(context);
                forceExitDialog.show();
            }else if(message!=null&&(message.equals("您收到一个赞~")||message.equals("您收到一条回复~"))){

                String  receiveType = message.equals("您收到一条回复~")?"receiveReply":"receiveReprove";
                int num = utils.getReceiveNum(receiveType);
                num++;
                utils.setReceiveNum(receiveType, num);
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DATE);
                utils.setLastMessageTime(month+"月"+day+"日");

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                /*Intent intent1 = new Intent(context,GroupNotifyActivity.class);
                intent1.putExtra("message",message);
                PendingIntent pi = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new Notification(R.mipmap.ic_launcher,message,System.currentTimeMillis());
                notification.setLatestEventInfo(context,"全美",message, pi);
                manager.notify(1,notification);*/
                //设置点击通知栏的动作为启动另外一个广播

                Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.
                        getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentTitle(message)
                        .setTicker(message)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher);

                manager.notify(2, builder.build());
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");


            if(utils.isAppAlive("com.minfo.quanmei")){

                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage("com.minfo.quanmei");
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(launchIntent);
            }else{

            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 记录pushId
     * @param reqId
     */
    private void reqSetPushID(String reqId) {
        VolleyHttpClient httpClient = new VolleyHttpClient(context);
        String url = context.getResources().getString(R.string.api_baseurl)+"public/SetJpush.php";
        Utils utils = new Utils(context);
        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+reqId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
            }

            @Override
            public void onRequestError(int code, String msg) {
            }

        });
    }
}