package com.lfish.control.action.actiondomian;

import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.AppManager;

public class GetInstallAppsAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		AppManager manger = new AppManager(ControlApplication.context);
		String getInstallLists = manger.GetInstallLists();
		sendTxt(getInstallLists);
		
	}

}
