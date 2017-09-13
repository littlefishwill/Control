package com.lfish.control;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.lfish.control.control.fragment.ActionProductFragment;
import com.lfish.control.control.fragment.AddControlNumberFragment;
import com.lfish.control.control.fragment.ContacUsFragment;
import com.lfish.control.control.fragment.CustomMadeFragment;
import com.lfish.control.control.fragment.DevicesFragment;
import com.lfish.control.control.fragment.EaseChatFragment;
import com.lfish.control.db.dao.Device;
import com.lfish.control.event.NeedReLogin;
import com.lfish.control.http.HttpBaseCallBack;
import com.lfish.control.http.HttpManager;
import com.lfish.control.http.HttpResultCode;
import com.lfish.control.http.damian.LoginResult;
import com.lfish.control.qrcode.QrCodeActivity;
import com.lfish.control.user.UserManager;
import com.lfish.control.user.dao.User;
import com.lfish.control.user.login.LoginActivity;
import com.lfish.control.utils.Sputils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment homeFragment,addControlNmuberFragment;
    private DevicesFragment devicesFragment;
    private ActionProductFragment actionProductFragment;
    private ContacUsFragment contacUsFragment;
    private CustomMadeFragment customMadeFragment;
    private int cacheId;
    private View headContain;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView name = (TextView) headerView.findViewById(R.id.tv_main_nav_head_user);
        headContain = headerView.findViewById(R.id.nav_view);
        name.setText("欢迎您到到来, " + UserManager.getInstance().getLoginUser(this).getUserName());
        TextView version = (TextView) headerView.findViewById(R.id.tv_main_nav_head_version);
        version.setText(getResources().getString(R.string.app_slogen) + " " + getAppVersionName(this));
        devicesLogic();

        //刷新功能列表
        HttpManager.getInstance().getAndRefreshActionList();

        setHeadBackGround();


    }

    private void setHeadBackGround() {
        // 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(MainActivity.this);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        // 设置 背景

        navigationView.getHeaderView(0).setBackgroundDrawable(wallpaperDrawable);

    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    private void devicesLogic() {
        devicesFragment = new DevicesFragment();
        devicesFragment.setOnDeviceChoosed(new DevicesFragment.OnDeviceChoosed() {
            @Override
            public void onChoose(Device device) {
                toControlDevice(device);
                cacheId = -1;
            }

            @Override
            public void onNeedCreateChildNumber() {
                createChildNumber();
                cacheId = -1;
            }
        });
        addFragment(devicesFragment);
    }

    private void createChildNumber() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (addControlNmuberFragment == null) {
                    addControlNmuberFragment = new AddControlNumberFragment();
                }
                setTitle("创建可控账号");
                addFragment(addControlNmuberFragment);
            }
        });
    }

    private void toControlDevice(final Device device){
        if(homeFragment==null){
            homeFragment = new EaseChatFragment();
        }
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, device.getHxid());

        homeFragment.setArguments(args);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTitle("监控 '" + device.getNick() + "'");
                addFragment(homeFragment);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        setIconsVisible(menu,true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_qrcode) {
            open(QrCodeActivity.class);
            return true;
        }else if(id == R.id.action_refresh){
            //刷新功能列表
            HttpManager.getInstance().getAndRefreshActionList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setIconsVisible(Menu menu, boolean flag) {
        //判断menu是否为空
        if(menu != null) {
            try {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        cacheId = id;
       if (id == R.id.nav_devices) {
            setTitle("可控设备");
            addFragment(devicesFragment);
        } else if (id == R.id.nav_addchildnumber) {
           createChildNumber();

        } else if (id == R.id.nav_manage) {
           setTitle("解锁功能");
            if(actionProductFragment==null){
                actionProductFragment = new ActionProductFragment();
            }
           addFragment(actionProductFragment);
        } else if (id == R.id.nav_talk) {
           setTitle("联系我们");
           if(contacUsFragment==null){
               contacUsFragment = new ContacUsFragment();
           }
           addFragment(contacUsFragment);
        } else if (id == R.id.nav_privateservice) {
           setTitle("私人定制");
           if(customMadeFragment==null){
               customMadeFragment = new CustomMadeFragment();
               customMadeFragment.setOnContactClikListener(new CustomMadeFragment.onContactClikListener() {
                   @Override
                   public void onClick() {
                       setTitle("联系我们");
                       if(contacUsFragment==null){
                           contacUsFragment = new ContacUsFragment();
                       }
                       addFragment(contacUsFragment);
                   }
               });
           }
           addFragment(customMadeFragment);
       } else if (id == R.id.nav_exit) {
           EMClient.getInstance().logout(true, new EMCallBack() {
               @Override
               public void onSuccess() {
                   UserManager.getInstance().loginOut(MainActivity.this);
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

            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Fragment payStubFragmentBase){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.rl_main_contain, payStubFragmentBase);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void removeFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.remove(fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(cacheId == R.id.nav_devices) {
                moveTaskToBack(true);
            }else {
                addFragment(devicesFragment);
                cacheId = R.id.nav_devices;
            }
            return true;
        }
        return false;
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
    public void onMessageEvent(NeedReLogin event) {

        if(event.isNeedExit()){
            EMClient.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    UserManager.getInstance().loginOut(MainActivity.this);
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
        }else{
            User loginUser = UserManager.getInstance().getLoginUser(this);
            HttpManager.getInstance().login(loginUser.getUserName(), loginUser.getPassWord(), new HttpBaseCallBack<LoginResult>() {

                @Override
                public void onSuccess(LoginResult result) {
                    LoginResult.LoginResultData data = result.getData();
                    if (result.getCode() == HttpResultCode.SUCCESS) {
                        //刷新功能列表
                        Sputils.getInstance(ControlApplication.context).putObject(HttpManager.getInstance(),HttpManager.HTTP_TOKEN,data.getToken());
                        Sputils.getInstance(ControlApplication.context).putObject(HttpManager.getInstance(),HttpManager.HTTP_UUID,data.getUuid());
//                        Sputils.getInstance(ControlApplication.context).putObject(HttpManager.getInstance(), HttpManager.HTTP_USER, mEmailView.getText().toString());

                        HttpManager.getInstance().getAndRefreshActionList();
                    }else{
                        Toast.makeText(MainActivity.this,"自动登录失败，请重新登录",Toast.LENGTH_LONG).show();
                        finish();
                        open(LoginActivity.class);
                    }
                }
            });
        }

    }

}
