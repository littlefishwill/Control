package com.lfish.control.action.actiondomian;

import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.AliLocationTool;

public class GetLocationAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.i(GetLocationAction.this.getClass().getName(),"getlocation");

		AliLocationTool.getInstace().getLocation(new AMapLocationListener() {
			@Override
			public void onLocationChanged(AMapLocation aMapLocation) {
				sendLocationMessage(aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getAddress());
			}
		});
	}
}
