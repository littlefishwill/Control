package com.lfish.control;

import android.content.Context;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public abstract class BaseManager {
    public abstract void init(Context context);
    public String getSpConfigName(){
        return getClass().getName();
    }
}
