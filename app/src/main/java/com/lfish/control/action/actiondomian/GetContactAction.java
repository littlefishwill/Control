package com.lfish.control.action.actiondomian;

import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.LinkMan;

public class GetContactAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		LinkMan contactManager = new LinkMan();
		String contactMsg = contactManager.getLinks();
		if(contactMsg!=null)
		{
			sendTxt(contactMsg);
		}
	}
}
