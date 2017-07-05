package com.lfish.control.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by shenmegui on 2017/7/5.
 */
@DatabaseTable(tableName = "askinfos")
public class AskInfo {
    public static final int STATUE_UNREAD = 0;
    public static final int STATUE_ASK = 1;
    public static final int STATUE_AGREE = 2;
    public static final int STATUE_DISAGREE = 3;


    public AskInfo(String name, String reson) {
        this.name = name;
        this.reson = reson;
        this.time = System.currentTimeMillis();
        this.statue = STATUE_UNREAD;
    }


    @DatabaseField(id = true)
    private String name;

    @DatabaseField
    private String reson;

    @DatabaseField
    private long time;

    @DatabaseField
    private int statue;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }
}
