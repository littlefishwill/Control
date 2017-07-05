package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetDeviceMsgCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "获取设备信息";
	}
	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.phonemsg;
	}
	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "获取设备的信息：包括（品牌，android内核版本，网络ip等）";
	}
}
