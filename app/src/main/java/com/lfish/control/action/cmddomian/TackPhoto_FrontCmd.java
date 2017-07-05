package com.lfish.control.action.cmddomian;


import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class TackPhoto_FrontCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "前拍照";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "前置摄像头拍一张照片";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.tack_photo_front;
	}

}
