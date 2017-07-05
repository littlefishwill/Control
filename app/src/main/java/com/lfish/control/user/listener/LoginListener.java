package com.lfish.control.user.listener;

import com.lfish.control.user.dao.User;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public interface LoginListener {
    void onSuccess(User user);
    void onFail(String message,int code);
}
