package com.lfish.control.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lfish.control.BaseManager;
import com.lfish.control.ControlApplication;
import com.lfish.control.action.BaseBeanCmd;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.event.ActionListRefresh;
import com.lfish.control.event.ContactAsk;
import com.lfish.control.event.NeedReLogin;
import com.lfish.control.http.damian.ActionResult;
import com.lfish.control.utils.PhoneUtils;
import com.lfish.control.utils.Sputils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by SuZhiwei on 2017/5/7.
 */
public class HttpManager  extends BaseManager{

    //------------spdata
    public static final String HTTP_TOKEN = "http_token";
    public static final String HTTP_UUID = "http_uuid";
    public static final String HTTP_USER = "http_user";
    public static final String HTTP_PASSWORD = "http_password";
    //----------------------------------------end spdata

    private static final String HOST="http://47.92.77.123";
    private static final String USER_REGISTER ="/supervisor/app/register";
    private static final String USER_LOGIN ="/supervisor/app/login";
    private static final String USER_ACTIONLIST ="/supervisor/app/actionlist";

    public static final String BBSURL ="http://gjgj.bbs.am/";

    public void sendPostRequest(RequestParams requestParams,HttpBaseCallBack commonCallback ){
        requestParams.setConnectTimeout(10000);
        requestParams.addBodyParameter("token", Sputils.getInstance(ControlApplication.context).getObject(this, HTTP_TOKEN, ""));
        requestParams.addBodyParameter("uuid", Sputils.getInstance(ControlApplication.context).getObject(this, HTTP_UUID, ""));

        x.http().post(requestParams, commonCallback);
    }

    private static HttpManager httpManager;
    public static HttpManager getInstance(){
        if(httpManager==null){
            httpManager = new HttpManager();
        }
        return  httpManager;
    }

    private HttpManager() {
    }

    public void register(String userName,String passWord,HttpBaseCallBack httpBaseCallBack){
        RequestParams requestParams = new RequestParams();
        requestParams.setUri(HOST+USER_REGISTER);
        requestParams.addBodyParameter("username", userName);
        requestParams.addBodyParameter("password",passWord);
        requestParams.addBodyParameter("usertype","1"); //主账号
        requestParams.addBodyParameter("imei", PhoneUtils.getInstance().getImei(ControlApplication.context));

        sendPostRequest(requestParams,httpBaseCallBack);
    }

    public void login(String userName,String passWord,HttpBaseCallBack httpBaseCallBack){
        RequestParams requestParams = new RequestParams();
        requestParams.setUri(HOST+USER_LOGIN);
        requestParams.addBodyParameter("username", userName);
        requestParams.addBodyParameter("password",passWord);
        requestParams.addBodyParameter("imei", PhoneUtils.getInstance().getImei(ControlApplication.context));

        sendPostRequest(requestParams, httpBaseCallBack);
    }

    public void getAndRefreshActionList(HttpBaseCallBack httpBaseCallBack){
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("username", Sputils.getInstance(ControlApplication.context).getObject(this, HTTP_USER, ""));

        requestParams.setUri(HOST + USER_ACTIONLIST);
        sendPostRequest(requestParams, httpBaseCallBack);
    }

    /**
     * 已经尝试重新登录过了
     */
    private  boolean isAutoLogon =false;
    /**
     * 获取菜单
     */
    public void getAndRefreshActionList() {
        HttpManager.getInstance().getAndRefreshActionList(new HttpBaseCallBack<ActionResult>() {
            @Override
            public void onSuccess(ActionResult result) {
                if (result.getCode() == HttpResultCode.SUCCESS) {
                    for (final ActionResult.ActionResultBean actionResultBean : result.getData()) {
                        BaseBeanCmd cmd = new BaseBeanCmd() {
                            @Override
                            public String getShortDes() {
                                return null;
                            }

                            @Override
                            public String getLongDes() {
                                return null;
                            }

                            @Override
                            public int getCmdNumber() {
                                return actionResultBean.getId();
                            }

                            @Override
                            public int getDrawable() {
                                return 0;
                            }
                        };

                        Log.i("HttpManager",actionResultBean.getMenuName());
                        cmd.setLock(actionResultBean.getUseFlag() == 0 ? true : false);
                        cmd.setPrice(actionResultBean.getNormalPrice());
                        cmd.setMenuName(actionResultBean.getMenuName());
                        cmd.setMenuDes(actionResultBean.getMenuShortName());
                        cmd.setMenuIco(actionResultBean.getImgUrl());
                        CmdFactory.getInstance().addLockBean(cmd);
                    }

                    isAutoLogon = false;
                    Toast.makeText(ControlApplication.context, "获取功能列表成功", Toast.LENGTH_LONG).show();

                    EventBus.getDefault().post(new ActionListRefresh());

                } else {
                    EventBus.getDefault().post(new NeedReLogin(isAutoLogon));
                    isAutoLogon = true;
                }
            }
        });
    }

    @Override
    public void init(Context context) {

    }
}
