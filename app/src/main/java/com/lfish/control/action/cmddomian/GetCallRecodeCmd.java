package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetCallRecodeCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "通话记录";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "获取通话记录";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 9;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_callrecode;
	}

}