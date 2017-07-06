package com.lfish.control.child;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.BaseActivity;
import com.lfish.control.R;
import com.lfish.control.child.adapter.AskRequestAdapter;
import com.lfish.control.child.adapter.ChildControlDeviceAdapter;
import com.lfish.control.db.AskInfoDao;
import com.lfish.control.db.dao.AskInfo;
import com.lfish.control.event.ContactAsk;
import com.lfish.control.user.UserManager;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by shenmegui on 2017/7/6.
 */
public class ChildControlDeviceActivity extends BaseActivity {

    private RecyclerView askRequestView;
    private ChildControlDeviceAdapter childControlDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controldevice);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        askRequestView = (RecyclerView) findViewById(R.id.rv_ask_request);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        askRequestView.setLayoutManager(layoutManager);

        askListLogic();

    }

    public void askListLogic(){
        if(childControlDeviceAdapter==null) {
                new AsyncTask<Void, Void, Void>() {
                    List<String> usernames;
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        childControlDeviceAdapter = new ChildControlDeviceAdapter(usernames, ChildControlDeviceActivity.this);
                        childControlDeviceAdapter.setOnItemClickListener(new ChildControlDeviceAdapter.OnItemClickListener() {

                            @Override
                            public void onClick(int pos, String name) {

                            }

                            @Override
                            public void onAgreeClick(int pos, String name) {
                                try {
                                    EMClient.getInstance().contactManager().declineInvitation(name);
                                    MaterialDialog dialog = new MaterialDialog.Builder(ChildControlDeviceActivity.this)
                                            .title(R.string.add_ask_request_caozuo_alert_title)
                                            .content("已删除"+name+".")
                                            .positiveText(R.string.agree)
                                            .show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        askRequestView.setAdapter(childControlDeviceAdapter);
                    }
                }.execute();
        }else{
                try {
                    childControlDeviceAdapter.setAskInfos(EMClient.getInstance().contactManager().getAllContactsFromServer());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                childControlDeviceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

}
