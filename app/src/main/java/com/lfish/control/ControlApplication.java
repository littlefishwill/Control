package com.lfish.control;

import android.app.Application;

import com.lfish.control.action.ActionFactory;
import com.lfish.control.action.ActionManager;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.message.MessageManager;

import org.xutils.x;

import c.b.BP;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class ControlApplication extends Application {

    public static ControlApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initFrame();

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
        BP.init(context, "你的Application ID");

    }
}
