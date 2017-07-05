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

    @DatabaseField(id = true)
    private String name;

    @DatabaseField
    private String askreson;

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

    public String getAskreson() {
        return askreson;
    }

    public void setAskreson(String askreson) {
        this.askreson = askreson;
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
