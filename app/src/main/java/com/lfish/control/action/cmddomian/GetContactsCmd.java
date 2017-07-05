package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetContactsCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "获取通讯录";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "获取通讯录列表";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_contact;
	}

}
