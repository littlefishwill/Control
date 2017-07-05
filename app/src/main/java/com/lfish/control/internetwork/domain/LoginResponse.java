package com.lfish.control.internetwork.domain;

import com.lfish.control.user.dao.User;

/**
 * Created by SuZhiwei on 2016/9/10.
 */
public class LoginResponse extends BaseResponse {
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
