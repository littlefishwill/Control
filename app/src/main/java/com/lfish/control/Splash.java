package com.lfish.control;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.action.cmddomian.GetDeviceMsgCmd;
import com.lfish.control.action.cmddomian.GetInstallAppsCmd;
import com.lfish.control.child.ChildShowActivity;
import com.lfish.control.user.UserManager;
import com.lfish.control.user.dao.User;
import com.lfish.control.user.login.LoginActivity;
import tyrantgit.explosionfield.ExplosionField;

public class Splash extends BaseActivity {
    private static final int SKIP_MAIN = 0;
    private static final int SKIP_LOGIN = 1;
    private static final int SKIP_CHILD = 2;
    private static final int SKIP_ANIM = 3;
    private ImageView splashLogo;
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
                case SKIP_ANIM:
                    ExplosionField explosionField = ExplosionField.attach2Window(Splash.this);
                    explosionField.explode(splashLogo);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashLogo = (ImageView) findViewById(R.id.iv_splash_logo);

//        tryActionLock();
        logic();

    }

    private void logic() {
        // 1 .激活设备管理器
        skipLogic();
    }


    private void skipLogic(){
        UserManager userManager = UserManager.getInstance();
        handler.sendEmptyMessageDelayed(SKIP_ANIM,1900);
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
