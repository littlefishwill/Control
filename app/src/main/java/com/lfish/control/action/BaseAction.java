package com.lfish.control.action;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class BaseAction {
    private String actionName;
    private int actionId;
    private int actionIco;

    public BaseAction(String actionName, int actionId, int actionIco) {
        this.actionName = actionName;
        this.actionId = actionId;
        this.actionIco = actionIco;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getActionIco() {
        return actionIco;
    }

    public void setActionIco(int actionIco) {
        this.actionIco = actionIco;
    }
}
