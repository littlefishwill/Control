package com.lfish.control.control.dao;

import java.util.HashMap;

/**
 * Created by shenmegui on 2017/7/24.
 */
public class NickNameDao {
    private HashMap<String,String> cacheNick = new HashMap<String,String>();

    public HashMap<String, String> getCacheNick() {
        if(cacheNick==null){
            cacheNick = new HashMap<String,String>();
        }
        return cacheNick;
    }

    public void setCacheNick(HashMap<String, String> cacheNick) {
        this.cacheNick = cacheNick;
    }




}
