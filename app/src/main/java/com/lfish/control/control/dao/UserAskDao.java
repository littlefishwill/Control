package com.lfish.control.control.dao;

/**
 * Created by shenmegui on 2017/7/5.
 */
public class UserAskDao {
    private String name;
    private String imei;
    private long time;

    public UserAskDao(String name, String imei, long time) {
        this.name = name;
        this.imei = imei;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return name+"?"+imei+"?"+time;
    }

    public static UserAskDao parserFormString(String code){
        String[] split = code.split("\\?");
        if(split.length==3){
            return new UserAskDao(split[0],split[1],Long.parseLong(split[2]));
        }
        return null;
    }
}
