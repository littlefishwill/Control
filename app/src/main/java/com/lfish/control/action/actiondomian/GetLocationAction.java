package com.lfish.control.action.actiondomian;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.BaiduLocationTool;

public class GetLocationAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.i(GetLocationAction.this.getClass().getName(),"getlocation");
		BaiduLocationTool.getInstace().getLocation(new BaiduLocationTool.LocationCallBack() {

			@Override
			public void onSuccess(BDLocation arg0) {
				// TODO Auto-generated method stub
				sendLocationMessage(arg0.getLatitude(), arg0.getLongitude(), arg0.getAddress().address);
			}
		});
	}

}
