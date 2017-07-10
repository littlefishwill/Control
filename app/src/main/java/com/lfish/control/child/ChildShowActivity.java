package com.lfish.control.child;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.lfish.control.BaseActivity;
import com.lfish.control.Config;
import com.lfish.control.R;
import com.lfish.control.action.actiontool.MdmUtils;
import com.lfish.control.control.dao.UserAskDao;
import com.lfish.control.db.AskInfoDao;
import com.lfish.control.db.dao.AskInfo;
import com.lfish.control.event.ContactAsk;
import com.lfish.control.http.HttpManager;
import com.lfish.control.user.UserManager;
import com.lfish.control.user.dao.User;
import com.lfish.control.user.login.LoginActivity;
import com.lfish.control.utils.DesUtils;
import com.lfish.control.utils.Md5;
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
    private View askList,controlList,activityBtn,yinSi;
    private MaterialDialog enterPassWordDialog,exitDialog;
    private int REQUEST_PROVISION_MANAGED_PROFILE = 110;
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

        activityBtn =  findViewById(R.id.ll_childshow_action);
        askList = findViewById(R.id.ll_childshow_asklist);
        controlList = findViewById(R.id.ll_childshow_control_list);
        yinSi = findViewById(R.id.ll_childshow_yinsi);

        qrCodeLogic();

        controlListLogic();
        exitLogic();

        //活动逻辑
        activityLogic();

        yinSiLogic();

        mdmLogic();
    }

    private void mdmLogic() {
        if(!MdmUtils.getInstance(this).isActivity()){
            MdmUtils.getInstance(this).tryGetMDM(this,REQUEST_PROVISION_MANAGED_PROFILE);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PROVISION_MANAGED_PROFILE) {
            if(resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.activity_lockscreen_mdm_activity_success), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), getString(R.string.activity_lockscreen_mdm_activity_failed), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void yinSiLogic() {
        yinSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppDetailSettingIntent(ChildShowActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        askListLogic();
    }

    private void controlListLogic() {
        controlList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 enterPassWordDialog = new MaterialDialog.Builder(ChildShowActivity.this)
                        .title(R.string.ask_need_password_tilte)
                        .customView(R.layout.dialog_enter_password, true)
                        .positiveText(R.string.dialog_makesure)
                        .negativeText(R.string.dialog_cancle)
                        .autoDismiss(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getView();
                                AutoCompleteTextView pass = (AutoCompleteTextView) view.findViewById(R.id.atv_dialog_pass);
                                String passWord = UserManager.getInstance().getLoginUser(ChildShowActivity.this).getPassWord();
                                if (pass.getText().toString().length() < 1) {
                                    pass.setError(getResources().getString(R.string.error_field_required));
                                } else if (passWord.equals(Md5.GET_32(pass.getText().toString().toLowerCase()))) {
                                    open(ChildControlDeviceActivity.class);
                                    enterPassWordDialog.dismiss();
                                } else {
                                    pass.setError(getResources().getString(R.string.error_field_password));
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                enterPassWordDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
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
        askList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(ChildAskRequestActivity.class);
            }
        });
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
                String imei= PhoneUtils.getInstance().getImei(ChildShowActivity.this);
                UserAskDao userAskDao = new UserAskDao(loginName, imei, System.currentTimeMillis());
                try {
                    DesUtils desUtils = new DesUtils();
                    String encrypt = desUtils.encrypt(userAskDao.toString());
                    Log.i("PassCode",encrypt);
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
                exitDialog=  new MaterialDialog.Builder(ChildShowActivity.this)
                        .title(R.string.ask_need_password_tilte)
                        .customView(R.layout.dialog_enter_password, true)
                        .positiveText(R.string.dialog_makesure)
                        .negativeText(R.string.dialog_cancle)
                        .autoDismiss(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getView();
                                AutoCompleteTextView pass = (AutoCompleteTextView) view.findViewById(R.id.atv_dialog_pass);
                                String passWord = UserManager.getInstance().getLoginUser(ChildShowActivity.this).getPassWord();
                                if (pass.getText().toString().length() < 1) {
                                    pass.setError(getResources().getString(R.string.error_field_required));
                                } else if (passWord.equals(Md5.GET_32(pass.getText().toString().toLowerCase()))) {
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
                                    exitDialog.dismiss();
                                } else {
                                    pass.setError(getResources().getString(R.string.error_field_password));
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                exitDialog.dismiss();
                            }
                        })
                        .show();
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

    /**
     * 跳转到权限设置界面
     */
    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

}
