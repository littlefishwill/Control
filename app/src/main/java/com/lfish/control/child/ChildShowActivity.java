package com.lfish.control.child;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lfish.control.BaseActivity;
import com.lfish.control.R;
import com.lfish.control.user.UserManager;
import com.lfish.control.user.login.LoginActivity;

/**
 * Created by SuZhiwei on 2016/9/10.
 */
public class ChildShowActivity extends BaseActivity {
    Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childshow);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        exit = (Button) findViewById(R.id.btn_childshow_exit);
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
