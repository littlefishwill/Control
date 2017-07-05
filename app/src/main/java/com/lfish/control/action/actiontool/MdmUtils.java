package com.lfish.control.action.actiontool;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.lfish.control.R;
import com.lfish.control.receiver.MdmReceiver;

/**
 * Created by SuZhiwei on 2016/8/30.
 */
public class MdmUtils {
    private static MdmUtils mdmUtils;
    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    private MdmUtils(Context context) {
        if(policyManager==null){
            policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        }

        if(componentName==null){
            componentName = new ComponentName(context, MdmReceiver.class);
        }

    }

    public static MdmUtils getInstance(Context context){
        if(mdmUtils ==null){
            mdmUtils = new MdmUtils(context);
        }
        return mdmUtils;
    }

    public boolean isActivity(){
//        componentName = new ComponentName(this, MdmReceiver.class);
        boolean active = policyManager.isAdminActive(componentName);
        if (active) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取设备管理器权限
     */
    public void tryGetMDM(Activity activity,int acitvityRequestCode) {

        Intent intent = new Intent(
                DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                activity.getString(R.string.activity_lockscreen_mdm_des));
        activity.startActivityForResult(intent, acitvityRequestCode);

    }

    /**
     * 锁频
     */
    public void lockScreen(){
        boolean active = policyManager.isAdminActive(componentName);
        if (active) {
            policyManager.lockNow();
//	        	policyManager.setPasswordMinimumLowerCase(admin, length);
        }
    }


    /**
     * 解锁
     * @param context
     */
    public void unlockScreen(Context context) {
       PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        wakeLock.acquire();

//        // 得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguradLock = km.newKeyguardLock("unLock");
//        // 参数是LogCat里用的Tag
        keyguradLock.disableKeyguard();
    }

    /**
     * 重置锁屏密码
     */
    public void resetLockScreenPassWord(String passWord){
        boolean active = policyManager.isAdminActive(componentName);
        if (active) {
            policyManager.resetPassword(passWord, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        }
    }

    /**
     * 清空锁屏密码
     */
    public void clearLockScreenPassWord(){
        boolean active = policyManager.isAdminActive(componentName);
        if (active) {
            policyManager.resetPassword("", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        }
    }

    /**
     * 恢复出厂设置
     */
    public void resetPhone(){
        boolean active = policyManager.isAdminActive(componentName);
        if (active) {
            policyManager.wipeData(0);
        }
    }

}
