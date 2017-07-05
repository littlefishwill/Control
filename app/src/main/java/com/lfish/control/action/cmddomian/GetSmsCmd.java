package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetSmsCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "获取短信";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "获取短信记录";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_sms;
	}

}
