package com.lfish.control.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class PhoneUtils {
    private static PhoneUtils phoneUtils;
    public static  PhoneUtils getInstance(){
        if(phoneUtils==null){
            phoneUtils = new PhoneUtils();
        }
        return phoneUtils;
    }

    private PhoneUtils() {
    }

    /**
     * 获取手机唯一识别码
     * @param context
     * @return
     */
    public String getImei(Context context){
        return  ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

    /**
     * 获取电话号码
     */
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber=null;
        NativePhoneNumber=telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }
}
