package com.lfish.control.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by shenmegui on 2017/9/12.
 */
public class BatteryUtils {
    public BatteryUtils() {

    }

    private static BatteryUtils batteryUtils;
    private  String currentBattery;
    private BatteryReceiver receiver;

    public  static BatteryUtils getInstance(){
        if(batteryUtils==null) {
            batteryUtils =   new BatteryUtils();
        }
        return batteryUtils;
    }
    public void init(Context context){
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatteryReceiver();
        context.registerReceiver(receiver, filter);
    }

    public class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getExtras().getInt("level");// 获得当前电量
            int total = intent.getExtras().getInt("scale");// 获得总电量
            int percent = current * 100 / total;
            currentBattery = percent + "%";
        }
    }

    public void unRegister(Context context){
        context.unregisterReceiver(receiver);
    }

    public String getCurrentBattery() {
        return currentBattery;
    }
}
