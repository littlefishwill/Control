package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetQQSdCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "QQ存儲";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "获取QQ sd 卡目录下的文件";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 11;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_qq_sd;
	}

}
