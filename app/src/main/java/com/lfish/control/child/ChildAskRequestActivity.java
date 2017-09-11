package com.lfish.control.child;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.BaseActivity;
import com.lfish.control.R;
import com.lfish.control.child.adapter.AskRequestAdapter;
import com.lfish.control.db.AskInfoDao;
import com.lfish.control.db.dao.AskInfo;
import com.lfish.control.event.ContactAsk;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;

/**
 * Created by shenmegui on 2017/7/6.
 */
public class ChildAskRequestActivity extends BaseActivity {

    private RecyclerView askRequestView;
    private AskInfoDao askInfoDao;
    private AskRequestAdapter askRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_request);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        askRequestView = (RecyclerView) findViewById(R.id.rv_ask_request);

        //1.将所有未读信息 更新为请求状态
        askInfoDao = new AskInfoDao(this);
        try {
            askInfoDao.updateAllStatue(AskInfo.STATUE_UNREAD,AskInfo.STATUE_ASK);
            EventBus.getDefault().post(new ContactAsk());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        askRequestView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        askListLogic();
    }

    public void askListLogic(){
        if(askRequestAdapter==null) {
            try {
                askRequestAdapter = new AskRequestAdapter(askInfoDao.querForAll(), this);
                askRequestAdapter.setOnItemClickListener(new AskRequestAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int pos, AskInfo askInfo) {

                    }

                    @Override
                    public void onAgreeClick(int pos, AskInfo askInfo) {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(askInfo.getName());
                            askInfo.setStatue(AskInfo.STATUE_AGREE);
                            askInfoDao.createOrUpdate_AskInfo(askInfo);
                            MaterialDialog dialog = new MaterialDialog.Builder(ChildAskRequestActivity.this)
                                    .title(R.string.add_ask_request_caozuo_alert_title)
                                    .content("已同意"+askInfo.getName()+"的请求.")
                                    .positiveText(R.string.agree)
                                    .show();
                            askRequestAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                askRequestView.setAdapter(askRequestAdapter);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                askRequestAdapter.setAskInfos(askInfoDao.querForAll());
                askRequestAdapter.notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
