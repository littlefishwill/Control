package com.lfish.control.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lfish.control.service.StartService;

/**
 * Created by shenmegui on 2017/9/12.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    //重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        //后边的XXX.class就是要启动的服务
        Intent service = new Intent(context,StartService.class);
        context.startService(service);
    }

}