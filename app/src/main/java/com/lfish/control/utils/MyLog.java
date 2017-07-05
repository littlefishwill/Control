package com.lfish.control.utils;

import android.util.Log;

public class MyLog {

	public static final Boolean DEBUG = true;

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}
	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}
	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}
	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}
}
