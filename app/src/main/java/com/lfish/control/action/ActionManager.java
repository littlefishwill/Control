package com.lfish.control.action;

import android.content.Context;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.lfish.control.BaseManager;
import com.lfish.control.action.actiontool.AliLocationTool;

import java.util.List;

/**
 * Created by SuZhiwei on 2016/7/3.
 */
public class ActionManager extends BaseManager {
    private static ActionManager actionManager;
    public static  ActionManager getInstance(){
        if(actionManager ==null){
            actionManager = new ActionManager();
        }
        return actionManager;
    }
    @Override
    public void init(Context context) {
        CmdFactory.getInstance().init();
        ActionFactory.getInstance().init();
        AliLocationTool.getInstace().init(context);
        registerEventListener(context);
    }

    protected void registerEventListener(Context context) {
        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                for (EMMessage m : messages) {
                    CmdFactory.getInstance().runReciveCmd(m);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }
}
