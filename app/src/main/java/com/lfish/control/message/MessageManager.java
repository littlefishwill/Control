package com.lfish.control.message;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.lfish.control.receiver.CallReceiver;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class MessageManager {
    public static  void init(Context context){
        EMOptions options = new EMOptions();
        // 不需要验证
        options.setAcceptInvitationAlways(true);
        EaseUI.getInstance().init(context, options);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        context.registerReceiver(new CallReceiver(), callFilter);
    }


}
