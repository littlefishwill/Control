package com.lfish.control.action.cmdparams;

/**
 * Created by SuZhiwei on 2016/8/30.
 */
public class LockScreenParams {
    private boolean isOpen;
    private String passWord;

    public LockScreenParams(boolean isOpen, String passWord) {
        this.isOpen = isOpen;
        this.passWord = passWord;
    }

    public LockScreenParams(boolean isOpen) {
        this.isOpen = isOpen;
        passWord ="";
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
