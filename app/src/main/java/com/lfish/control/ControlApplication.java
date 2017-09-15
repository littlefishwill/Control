package com.lfish.control;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.lfish.control.action.ActionManager;
import com.lfish.control.message.MessageManager;
import com.lfish.control.utils.BatteryUtils;

import org.xutils.x;

import c.b.BP;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class ControlApplication extends MultiDexApplication {

    public static ControlApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initFrame();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initFrame() {
        //1.初始化 环信消息通道
        MessageManager.init(this);
        // 初始化action
        ActionManager.getInstance().init(this);

        // 初始化xutis3
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        // 初始化 支付
        BP.init(context, "littlefish");

        //
        BatteryUtils.getInstance().init(this);


    }
}
