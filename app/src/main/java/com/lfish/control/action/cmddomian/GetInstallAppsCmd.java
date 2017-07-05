package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetInstallAppsCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "应用列表";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "获取已经安装的应用";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_applist;
	}

}
