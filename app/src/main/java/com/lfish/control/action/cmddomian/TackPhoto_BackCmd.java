package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class TackPhoto_BackCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "后拍照";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "用后置摄像头拍一张照片";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.tack_photo_back;
	}

}
