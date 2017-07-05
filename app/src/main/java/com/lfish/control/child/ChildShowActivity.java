package com.lfish.control.child;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.lfish.control.BaseActivity;
import com.lfish.control.Config;
import com.lfish.control.R;
import com.lfish.control.control.dao.UserAskDao;
import com.lfish.control.db.AskInfoDao;
import com.lfish.control.db.dao.AskInfo;
import com.lfish.control.event.ContactAsk;
import com.lfish.control.http.HttpManager;
import com.lfish.control.user.UserManager;
import com.lfish.control.user.dao.User;
import com.lfish.control.user.login.LoginActivity;
import com.lfish.control.utils.DesUtils;
import com.lfish.control.utils.PhoneUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by SuZhiwei on 2016/9/10.
 */
public class ChildShowActivity extends BaseActivity {
    private Button exit;
    private ImageView qrCodeImage;
    private TextView tvloginName,tvUnReadAsk;
    private LinearLayout askList,controlList,activityBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childshow);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        qrCodeImage = (ImageView) findViewById(R.id.iv_csa_zcode);
        tvloginName = (TextView) findViewById(R.id.tv_login_name);
        tvUnReadAsk = (TextView) findViewById(R.id.tv_unread_ask);
        exit = (Button) findViewById(R.id.btn_childshow_exit);

        activityBtn = (LinearLayout) findViewById(R.id.ll_childshow_action);

        qrCodeLogic();
        askListLogic();
        exitLogic();

        //活动逻辑
        activityLogic();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ContactAsk event) {
        try {
            if(tvUnReadAsk==null){
                return;
            }
            List<AskInfo> askInfos = new AskInfoDao(this).querForStatue(AskInfo.STATUE_UNREAD);

            if(askInfos.size()<1){
                tvUnReadAsk.setVisibility(View.GONE);
            }else{
                tvUnReadAsk.setText(askInfos.size()+"");
                tvUnReadAsk.setVisibility(View.VISIBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void askListLogic() {
        onMessageEvent(null);
    }

    private void activityLogic() {
        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(HttpManager.BBSURL);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }

    private void qrCodeLogic() {
        new AsyncTask<Void, Void, Void>() {
            private Bitmap mBitmap;
            private String loginName;
            @Override
            protected Void doInBackground(Void... params) {

                //加密
                User loginUser = UserManager.getInstance().getLoginUser(ChildShowActivity.this);
                loginName = loginUser.getUserName();
                String passWord = loginUser.getPassWord();
                String imei= PhoneUtils.getInstance().getImei(ChildShowActivity.this);
                UserAskDao userAskDao = new UserAskDao(loginName, imei, System.currentTimeMillis());
                try {
                    DesUtils desUtils = new DesUtils(passWord);
                    String encrypt = desUtils.encrypt(userAskDao.toString());
                    mBitmap = CodeUtils.createImage(encrypt, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                qrCodeImage.setImageBitmap(mBitmap);
                tvloginName.setText(loginName);
                //定时更新二维码
                tvloginName.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        qrCodeLogic();
                    }
                }, Config.CHILD_QRCODE_FINSHTIME);
            }
        }.execute();

    }

    private void exitLogic() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        UserManager.getInstance().loginOut(ChildShowActivity.this);
                        open(LoginActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                moveTaskToBack(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
