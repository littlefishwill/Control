package com.lfish.control.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lfish.control.BaseManager;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class Sputils {
    private Context context;
    private static Sputils sputils;
    public static Sputils getInstance(Context context){
        if(sputils==null){
            sputils =new Sputils(context);
        }

        return sputils;
    }

    private Sputils(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences(String file){
        SharedPreferences preferences = context.getSharedPreferences(file,
                Context.MODE_PRIVATE);
        return preferences;
    }

    public  void putObject(BaseManager baseManager,String key,Object objec){
        SharedPreferences sharedPreferences = getSharedPreferences(baseManager.getSpConfigName());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Class<?> aClass = objec.getClass();
        if(aClass.equals(String.class)){
            edit.putString(key,(String)objec);
        }else if(aClass.equals(Integer.class)){
            edit.putInt(key, (int) objec);
        }else if(aClass.equals(Long.class)){
            edit.putLong(key, (Long) objec);
        }else if(aClass.equals(Boolean.class)){
            edit.putBoolean(key, (Boolean) objec);
        }else if(aClass.equals(Float.class)){
            edit.putFloat(key, (float) objec);
        }

        edit.commit();
    }

    public <T extends Object> T getObject(BaseManager baseManager,String key,T  objec){

        SharedPreferences sharedPreferences = getSharedPreferences(baseManager.getSpConfigName());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Class<?> aClass = objec.getClass();
        if(aClass.equals(String.class)){
           return (T)sharedPreferences.getString(key, (String) objec);
        }else if(aClass.equals(Integer.class)){
            return (T)(Integer)sharedPreferences.getInt(key, (Integer) objec);
        }else if(aClass.equals(Long.class)){
            return (T)(Long)sharedPreferences.getLong(key, (Long) objec);
        }else if(aClass.equals(Boolean.class)){
            return (T)(Boolean)sharedPreferences.getBoolean(key, (Boolean) objec);
        }else if(aClass.equals(Float.class)){
            return (T)(Float)sharedPreferences.getFloat(key, (Float) objec);
        }

        return objec;
    }

}
