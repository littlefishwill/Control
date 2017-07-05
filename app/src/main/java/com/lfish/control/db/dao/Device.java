package com.lfish.control.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
@DatabaseTable(tableName = "devices")
public class Device {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String nick;

    @DatabaseField
    private String hxid;

    @DatabaseField
    private String des;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
