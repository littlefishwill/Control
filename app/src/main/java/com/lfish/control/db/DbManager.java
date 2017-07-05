package com.lfish.control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lfish.control.db.dao.Device;
import java.sql.SQLException;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class DbManager extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "control";
    public DbManager(Context context) {
        super(context, TABLE_NAME, null,0);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTable(connectionSource, Device.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try
        {
            TableUtils.dropTable(connectionSource, Device.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
