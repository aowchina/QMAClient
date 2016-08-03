package com.minfo.quanmei.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by liujing on 16/6/7.
 */
public class JumpProductDetailReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("receiver", "收到广播了么么么么");
        Bundle bundle = intent.getBundleExtra("info");
        Intent intent1 = new Intent(context,ProductDetailActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra("info",bundle);
        context.startActivity(intent1);
    }
}
