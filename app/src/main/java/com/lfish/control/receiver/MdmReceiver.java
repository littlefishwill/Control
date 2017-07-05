package com.lfish.control.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.widget.Toast;

public class MdmReceiver extends DeviceAdminReceiver {

	@Override
	public void onEnabled(android.content.Context context, android.content.Intent intent) {
		// TODO Auto-generated method stub
		super.onEnabled(context, intent);
//		Toast.makeText(context, "1", 1).show();
	}
	
	@Override
	public void onDisabled(android.content.Context context, android.content.Intent intent) {
		// TODO Auto-generated method stub
		super.onDisabled(context, intent);
		Toast.makeText(context, "1", 1).show();
	}
	
}
