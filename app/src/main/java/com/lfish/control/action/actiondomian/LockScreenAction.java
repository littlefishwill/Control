package com.lfish.control.action.actiondomian;

import com.google.gson.Gson;
import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanAction;
import com.lfish.control.action.actiontool.MdmUtils;
import com.lfish.control.action.cmdparams.LockScreenParams;

/**
 * Created by SuZhiwei on 2016/8/21.
 */
public class LockScreenAction extends BaseBeanAction {
    @Override
    public void run() {
        LockScreenParams lockScreenParams = new Gson().fromJson(getProperties(),LockScreenParams.class);
        if(lockScreenParams.isOpen()){
            String des ="";
            if(lockScreenParams.getPassWord().length()>0){
                MdmUtils.getInstance(ControlApplication.context).resetLockScreenPassWord(lockScreenParams.getPassWord());
                des = "已经锁屏了，嘻嘻~，您设置的密码是:"+ lockScreenParams.getPassWord()+" 可别忘记喽~";
            }else{
                MdmUtils.getInstance(ControlApplication.context).clearLockScreenPassWord();
                des = "已经锁屏了，嘻嘻~,没有设置密码~";
            }
            sendTxt(des);
            MdmUtils.getInstance(ControlApplication.context).lockScreen();

        }else{
            sendTxt("已经解锁屏幕了~，嘻嘻~");
            MdmUtils.getInstance(ControlApplication.context).clearLockScreenPassWord();
            MdmUtils.getInstance(ControlApplication.context).lockScreen();
            MdmUtils.getInstance(ControlApplication.context).unlockScreen(ControlApplication.context);
        }
    }
}
