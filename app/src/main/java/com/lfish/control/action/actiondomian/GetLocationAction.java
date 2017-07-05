package com.lfish.control.action.actiondomian;

import com.baidu.location.BDLocation;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.BaiduLocationTool;

public class GetLocationAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BaiduLocationTool.getInstace().getLocation(new BaiduLocationTool.LocationCallBack() {

			@Override
			public void onSuccess(BDLocation arg0) {
				// TODO Auto-generated method stub
				sendLocationMessage(arg0.getLatitude(), arg0.getLongitude(), arg0.getAddress().address);
			}
		});
	}

}
