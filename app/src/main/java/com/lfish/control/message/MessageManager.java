package com.lfish.control.message;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.Config;
import com.lfish.control.ControlApplication;
import com.lfish.control.control.dao.UserAskDao;
import com.lfish.control.db.AskInfoDao;
import com.lfish.control.db.DbManager;
import com.lfish.control.db.dao.AskInfo;
import com.lfish.control.event.ContactAsk;
import com.lfish.control.receiver.CallReceiver;
import com.lfish.control.user.UserManager;
import com.lfish.control.user.dao.User;
import com.lfish.control.utils.DesUtils;
import com.lfish.control.utils.PhoneUtils;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class MessageManager {
    public static  void init(Context context){
        EMOptions options = new EMOptions();
        // 不需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(context, options);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        context.registerReceiver(new CallReceiver(), callFilter);

        contactLogic();
    }

    public static void contactLogic(){
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
            }

            @Override
            public void onContactInvited(String username, String reason) {
                Log.i("MessageManager",username+":"+reason);
                //收到好友邀请
                // 1.测试机 自动同意申请
                if(UserManager.testNumber.toLowerCase().equals(username.toLowerCase())){
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(username);
                        return;
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }

                //2. 扫码申请的直接通过请求
                if(reason.startsWith(Config.QRCODE_RESON_START_TAG) && reason.length()>2){
                    String substring = reason.substring(2, reason.length());
                    User loginUser = UserManager.getInstance().getLoginUser(ControlApplication.context);
                    try {
                        DesUtils desUtils = new DesUtils(loginUser.getPassWord());
                        String decrypt = desUtils.decrypt(substring);
                        UserAskDao userAskDao = UserAskDao.parserFormString(decrypt);
                        if(userAskDao!=null){
                            // imei 号相同
                            if(userAskDao.getImei().equals(PhoneUtils.getInstance().getImei(ControlApplication.context))){
                                // 时间在有效期内
                                if(System.currentTimeMillis()-userAskDao.getTime()<Config.CHILD_QRCODE_FINSHTIME){
                                    EMClient.getInstance().contactManager().acceptInvitation(username);
                                    return;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    AskInfoDao askInfoDao = new AskInfoDao(ControlApplication.context);
                    AskInfo askInfo = new AskInfo();
                    askInfo.setAskreson(reason);
                    askInfo.setName(username);
                    askInfo.setTime(System.currentTimeMillis());
                    askInfo.setStatue(AskInfo.STATUE_UNREAD);
                    askInfoDao.createIfNotExists_AskInfo(askInfo);
                    EventBus.getDefault().post(new ContactAsk());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
            }

            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
            }
        });
    }


}
