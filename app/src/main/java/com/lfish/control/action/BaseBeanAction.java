package com.lfish.control.action;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

import com.google.gson.Gson;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.lfish.control.BaseActivity;

public abstract class BaseBeanAction implements Serializable,Runnable {
	/**
	 * 命令唯一id
	 */
	private String actionId;
	/**
	 * 命令动作识别码
	 */
	private int cmd;
	/**
	 * 命令发送时间
	 */
	private long time;
	/**
	 * 命令来自
	 */
	private String from;
	/**
	 * 命令发去
	 */
	private String to;
	/**
	 * 命令参数
	 */
	private String properties;

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	/**
	 * 将自身转换为gosn字符串
	 * @return
	 */
	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	};


	public void SendFile(String path){
		EMMessage message = EMMessage.createFileSendMessage(path, getFrom());
		EMClient.getInstance().chatManager().sendMessage(message);

	}

	public void sendTxt(String txt){
		EMMessage message = EMMessage.createTxtSendMessage(txt, getFrom());
//发送消息
		EMClient.getInstance().chatManager().sendMessage(message);
	}

	public void sendLocationMessage(double latitude, double longitude, String locationAddress) {
		EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, getFrom());
		message.setChatType(EMMessage.ChatType.Chat);
		EMClient.getInstance().chatManager().sendMessage(message);
	}

	public void sendImageMessage(String path){
		EMMessage imageSendMessage = EMMessage.createImageSendMessage(path, false, getFrom());
		EMClient.getInstance().chatManager().sendMessage(imageSendMessage);
	}
}
