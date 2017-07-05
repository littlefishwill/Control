package com.lfish.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.action.actiontool.MdmUtils;
import com.lfish.control.action.cmddomian.GetDeviceMsgCmd;
import com.lfish.control.action.cmddomian.GetInstallAppsCmd;
import com.lfish.control.child.ChildShowActivity;
import com.lfish.control.user.UserManager;
import com.lfish.control.user.dao.User;
import com.lfish.control.user.login.LoginActivity;
import com.lfish.control.utils.Sputils;
import com.lfish.control.view.dialog.EnterDialog;

public class Splash extends BaseActivity {
    private static final int SKIP_MAIN = 0;
    private static final int SKIP_LOGIN = 1;
    private static final int SKIP_CHILD = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SKIP_LOGIN:
                    open(LoginActivity.class);
                    finish();
                    break;
                case SKIP_MAIN:
                    open(MainActivity.class);
                    finish();
                    break;
                case SKIP_CHILD:
                    open(ChildShowActivity.class);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        tryActionLock();
        logic();

    }

    private void logic() {
        // 1 .激活设备管理器
        if(!MdmUtils.getInstance(this).isActivity()){
            MdmUtils.getInstance(this).tryGetMDM(this,REQUEST_PROVISION_MANAGED_PROFILE);
            return;
        }else {
            skipLogic();
        }
    }

    private int REQUEST_PROVISION_MANAGED_PROFILE = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PROVISION_MANAGED_PROFILE) {
            if(resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.activity_lockscreen_mdm_activity_success), Toast.LENGTH_SHORT).show();
                skipLogic();
            }else {
                skipLogic();
                Toast.makeText(getApplicationContext(), getString(R.string.activity_lockscreen_mdm_activity_failed), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void skipLogic(){
        UserManager userManager = UserManager.getInstance();
        if(userManager.isLogin(this)){
            User loginUser = userManager.getLoginUser(this);
            switch (loginUser.getEnterType()){
                case Control:
                    handler.sendEmptyMessageDelayed(SKIP_MAIN,3000);
                    break;
                case ControlChild:
                    handler.sendEmptyMessageDelayed(SKIP_CHILD,3000);
                    break;
            }
        }else{
            handler.sendEmptyMessageDelayed(SKIP_LOGIN,3000);
        }
    }

    private void tryActionLock() {
        GetDeviceMsgCmd getDeviceMsgCmd = new GetDeviceMsgCmd();
        getDeviceMsgCmd.setLock(true);
        getDeviceMsgCmd.setPrice(12.5D);

        GetInstallAppsCmd getInstallAppsCmd = new GetInstallAppsCmd();
        getInstallAppsCmd.setLock(true);
        getInstallAppsCmd.setPrice(99.9d);
        CmdFactory.getInstance().addLockBean(getInstallAppsCmd);
        CmdFactory.getInstance().addLockBean(getDeviceMsgCmd);
    }
}
