package com.minfo.quanmei.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.minfo.quanmei.utils.Utils;

/**
 * Created by liujing on 15/12/13.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.cancel(2);
        Utils utils = new Utils(context);
        if(utils.isAppAlive("com.minfo.quanmei")){
            Intent intent1 = new Intent(context,MessageActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }else{
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.minfo.quanmei");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        }
    }
}
