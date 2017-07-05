package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class AudioOnlineCmd extends BaseBeanCmd {

	public static final int CMDNUMBER  = 13;

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "语音监控";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "打开麦克风，我想听你在说些什么";
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
