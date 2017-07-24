package com.lfish.control.control.nick;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lfish.control.BaseManager;
import com.lfish.control.ControlApplication;
import com.lfish.control.control.dao.NickNameDao;
import com.lfish.control.utils.Sputils;

/**
 * Created by shenmegui on 2017/7/24.
 */
public class NickManager extends BaseManager {
    public static final String spCacheName = "device_nick";
    private static NickManager nickUtils;
    private NickNameDao nickNameDao;

    public static NickManager getInstance(){
        if(nickUtils == null){
            nickUtils = new NickManager();
        }
        return nickUtils;
    }

    private NickManager() {

    }

    public String getNickName(String id){
        if(nickNameDao==null){
            String cacheString = Sputils.getInstance(ControlApplication.context).getObject(this,spCacheName,"");
            if(cacheString.length()<1){
                return id;
            }else{
                nickNameDao = new Gson().fromJson(cacheString, NickNameDao.class);
            }
        }else{}

        String s = nickNameDao.getCacheNick().get(id);
        if(s==null || s.length()<1){
            return id;
        }
        return s;

    }

    public void saveNickName(String id,String nick){
        if(nickNameDao==null){
            String cacheString = Sputils.getInstance(ControlApplication.context).getObject(this,spCacheName,"");
            if(cacheString.length()<1){
                nickNameDao = new NickNameDao();
            }else{
                nickNameDao = new Gson().fromJson(cacheString, NickNameDao.class);
            }
        }else{}


        Log.i("?????????",id+"?"+nick);
        nickNameDao.getCacheNick().put(id,nick);
        Sputils.getInstance(ControlApplication.context).putObject(this,spCacheName,new Gson().toJson(nickNameDao));
    }

    @Override
    public void init(Context context) {

    }
}
