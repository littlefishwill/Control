package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class VideoOnlineCmd extends BaseBeanCmd {

	public static final int CMDNUMBER  = 12;

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "视频通话";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "跟我视频通话吧";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return CMDNUMBER;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_videoonline;
	}

}
