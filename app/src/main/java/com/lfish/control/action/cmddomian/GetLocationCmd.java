package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetLocationCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "获取位置";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "告诉我你现在的位置";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_location;
	}

}