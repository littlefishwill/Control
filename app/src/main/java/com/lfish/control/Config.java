package com.lfish.control;

public class Config {
	
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
