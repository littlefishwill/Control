package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

public class GetWeChatSdCmd extends BaseBeanCmd {

	@Override
	public String getShortDes() {
		// TODO Auto-generated method stub
		return "微信存储";
	}

	@Override
	public String getLongDes() {
		// TODO Auto-generated method stub
		return "获取微信存儲空間,包含语音ARM,视频MP4,图片JPG";
	}

	@Override
	public int getCmdNumber() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int getDrawable() {
		// TODO Auto-generated method stub
		return R.drawable.cmd_wechat_sd;
	}

}