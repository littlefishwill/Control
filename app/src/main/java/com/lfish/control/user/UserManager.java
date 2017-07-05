package com.lfish.control.user;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lfish.control.BaseManager;
import com.lfish.control.config.HttpConfig;
import com.lfish.control.internetwork.domain.LoginResponse;
import com.lfish.control.user.dao.User;
import com.lfish.control.user.listener.LoginListener;
import com.lfish.control.utils.Sputils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class UserManager extends BaseManager {
    private static UserManager userManager;
    private User cacheUser;
    public static final String testNumber  = "GJ-TEST";
    public static UserManager getInstance(){
        if(userManager==null){
            userManager = new UserManager();
        }
        return userManager;
    }

    private UserManager() {

    }

    @Override
    public void init(Context context) {

    }

    public static String SP_KEY_ISLOGIN ="islogin";


    /**
     * 是否登录
     * @param context
     * @return
     */
    public boolean isLogin(Context context){
        return Sputils.getInstance(context).getObject(this,SP_KEY_ISLOGIN,false);
    }

    public static String SP_KEY_FIRST_ENTER ="firstenter";

    /**
     * 是否是第一次进入
     * @param context
     * @return
     */
    public boolean isFisrtEnter(Context context){
        return Sputils.getInstance(context).getObject(this,SP_KEY_FIRST_ENTER,true);
    }

    /**
     * 毁掉第一次进入
     * @param context
     */
    public void destoryFisrtEnter(Context context){
         Sputils.getInstance(context).putObject(UserManager.this,SP_KEY_FIRST_ENTER,true);
    }

    public void loginOut(Context context){
        Sputils.getInstance(context).putObject(UserManager.this,SP_KEY_ISLOGIN,false);

    }

    public User getLoginUser(Context context){
        if(cacheUser!=null){
            return cacheUser;
        }
        String userJson = Sputils.getInstance(context).getObject(this, SP_KEY_USER, "");
        User user = new Gson().fromJson(userJson, User.class);
        return user;
    }

    public static String SP_KEY_USER= "user";
    private LoginListener loginListener;
    private Context context;
    private User user;
    public void login(final Context context,String userName,String passWord,LoginListener loginListener){
        this.loginListener = loginListener;
        this.context = context;
        user = new User();
        user.setUserName(userName);
        user.setPassWord(passWord);
        user.setHxId(userName);
        EMClient.getInstance().login(userName,passWord,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！"+user);
                UserManager.getInstance().saveLoginUser(user);
                Sputils.getInstance(context).putObject(UserManager.this,SP_KEY_ISLOGIN,true);
                if(UserManager.this.loginListener!=null){
                    UserManager.this.loginListener.onSuccess(user);
                }
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
                Sputils.getInstance(context).putObject(UserManager.this, SP_KEY_ISLOGIN, false);
                if(UserManager.this.loginListener!=null){
                    UserManager.this.loginListener.onFail(message,code);
                }
            }
        });
    }

    /**
     * 存储loginuser
     * @param user
     */
    public void saveLoginUser(User user){
        cacheUser = user;
        Sputils.getInstance(context).putObject(UserManager.this, SP_KEY_USER, new Gson().toJson(user));
    }


}
