package com.lfish.control.action.actiontool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareUtils {
	public static String Config = "CONFIG";
	//net Status
	public static String  netStatus = "NET_STATUS";
	public static String  netStatus_default = "3G";
	public static final String NETWORK_TYPE_WIFI = "WIFI"; 
	public static final String NETWOKR_TYPE_2G = "2G"; 
	public static final String NETWOKR_TYPE_4G = "4G"; 
	
	//Server ip and port
	public static String  ServerAddress = "SERVER_AD";
	public static String  ServerPort = "SERVER_PORT";
	
	//GPS status
	public static String GpsFlag = "GPSFLAG";
	public static String GpsFlag_Open = "OPEN";
	public static String GpsFlag_Close = "CLOSE";
	public static String Gps_Longitude = "Longitude";
	public static String Gps_Latitude = "Latitude";
	//record staus
	public static final String IS_RECORD = "is_record"; 
	public static final String IS_VIDEO = "is_video"; 
	public static final String IS_OPenCallRECORD = "is_OpenCallrecord"; 
	public static final String IS_Camera = "is_camera"; 
	public static final String Screen_Width = "screenwidth"; 
	public static final String Screen_hight = "screenhight"; 
	public static final String AudioSource = "AudioSource";
	public static final String SystemVolume = "SystemVolume";
	
	public static void putString(Context context, String name, String key,
			String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static void putInt(Context context, String name, String key,
			int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static void putDouble(Context context, String name, String key,
			double value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, (float) value);
		editor.commit();
	}
	public static void putBoolean(Context context, String name, String key,
			Boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static String getString(Context context, String name, String key,String strDefault) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		String valueString = sharedPreferences.getString(key, strDefault);
		return valueString;
	}
	public static int getInt(Context context, String name, String key, int idefault) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		int valueString = sharedPreferences.getInt(key, idefault);
		return valueString;
	}
	public static float getfloat(Context context, String name, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		float valueString = sharedPreferences.getFloat(key, 0);
		return valueString;
	}
	public static Boolean getBoolean(Context context, String name, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		Boolean valueString = sharedPreferences.getBoolean(key, false);

		return valueString;
	}
}
