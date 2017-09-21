package com.lfish.control.action.cmdparams;

/**
 * Created by SuZhiwei on 2016/8/30.
 */
public class USerActivityParams {
    private boolean isOpen;

    public USerActivityParams(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
