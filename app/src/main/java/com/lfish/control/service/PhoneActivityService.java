package com.lfish.control.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Created by shenmegui on 2017/9/15.
 */
public class PhoneActivityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Toast.makeText(this,event.getPackageName(),Toast.LENGTH_LONG).show();
        Log.i("PhoneActivityService",event.getPackageName().toString());
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.notificationTimeout=100;
        setServiceInfo(serviceInfo);
        super.onServiceConnected();

        Log.i("PhoneActivityService","start");
    }
}
