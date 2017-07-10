package com.lfish.control.event;

/**
 * Created by SuZhiwei on 2017/7/10.
 */
public class NeedReLogin {

    public NeedReLogin() {
    }

    public NeedReLogin(boolean needExit) {
        this.needExit = needExit;
    }

    private boolean needExit;

    public boolean isNeedExit() {
        return needExit;
    }

    public void setNeedExit(boolean needExit) {
        this.needExit = needExit;
    }
}
