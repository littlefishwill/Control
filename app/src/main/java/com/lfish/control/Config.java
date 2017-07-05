package com.lfish.control;

public class Config {

	/**
	 * 二维码过期时间
	 */
	public static long CHILD_QRCODE_FINSHTIME = 10*60*1000;

	/**
	 * 二维码 申请控制 开始标记
	 */
	public static String QRCODE_RESON_START_TAG ="p:";
	
	public static Object lock = new Object();
	
	private static String REQUEST_HTTPHOST = "http://love.rexiaopu.com";

	public static String UPYUNIMAGEHOST = "http://img.xinzhimei.com";
	
	/**
	 * 登录
	 */
	public static String REQUEST_LOGIN = REQUEST_HTTPHOST + "/user/login";

	/**
	 * 获取手机验证码
	 */
	public static String REQUEST_GETCODE = REQUEST_HTTPHOST + "/user/regCode";

	/**
	 * 用户注册
	 */
	public static String REQUEST_REGISTER = REQUEST_HTTPHOST + "/user/register";
	
	/**
	 * 用户信息
	 */
	public static String REQUEST_USERINFO = REQUEST_HTTPHOST + "/user/info";
	
	
}
