package com.lfish.control.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.lfish.control.db.dao.AskInfo;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by shenmegui on 2017/7/5.
 */
public class AskInfoDao {
    private Dao askInfoDaos;

    public AskInfoDao(Context context) {
        try {
            DbManager helper = DbManager.getHelper(context);
            askInfoDaos = helper.getDao(AskInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("SQL",e.toString());
        }
    }

    public void createIfNotExists_AskInfo(AskInfo askInfo) throws SQLException {
        askInfoDaos.createIfNotExists(askInfo);
    }

    public void createOrUpdate_AskInfo(AskInfo askInfo) throws SQLException {
        askInfoDaos.createOrUpdate(askInfo);
    }

    public List<AskInfo> querForAll() throws SQLException {
            return askInfoDaos.queryForAll();
    }

    public List<AskInfo> querForStatue(int statue) throws SQLException {

        return askInfoDaos.queryForEq("statue",statue);
    }

    public void updateAllStatue(int statueFrom,int statueTo) throws SQLException {
            askInfoDaos.updateBuilder().updateColumnValue("statue",statueTo).where().eq("statue",statueFrom);
    }

}
