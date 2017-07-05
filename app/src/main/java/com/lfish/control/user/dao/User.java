package com.lfish.control.user.dao;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class User {
    private String userId;
    private String hxId;
    private String userName;
    private String passWord;
    private String nickName;
    private EnterType enterType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHxId() {
        return hxId;
    }

    public void setHxId(String hxId) {
        this.hxId = hxId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public EnterType getEnterType() {
        return enterType;
    }

    public void setEnterType(EnterType enterType) {
        this.enterType = enterType;
    }

    public enum EnterType{
        Control,ControlChild;
    }
}
