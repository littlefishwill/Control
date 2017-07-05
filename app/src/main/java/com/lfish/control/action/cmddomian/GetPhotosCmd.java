package com.lfish.control.action.cmddomian;


import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetPhotosCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "获取相册";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "所有照片发送给我";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_photos;
	}

}