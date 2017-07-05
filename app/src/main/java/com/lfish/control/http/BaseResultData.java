package com.lfish.control.http;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpResponse;

/**
 * Created by SuZhiwei on 2017/5/7.
 */
@HttpResponse(parser = BaseJsonParser.class)
public class BaseResultData {
    private int code;
    private String msg;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
