package com.lfish.control.http.damian;

import com.lfish.control.http.BaseJsonParser;
import com.lfish.control.http.BaseResultData;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by SuZhiwei on 2017/5/8.
 */
@HttpResponse(parser = BaseJsonParser.class)
public class LoginResult extends BaseResultData {
    private LoginResultData data;

    public LoginResultData getData() {
        return data;
    }

    public void setData(LoginResultData data) {
        this.data = data;
    }

    public class LoginResultData{
        private String uuid;
        private String token;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
