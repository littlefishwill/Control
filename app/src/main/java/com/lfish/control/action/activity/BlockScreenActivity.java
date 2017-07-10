package com.lfish.control.action.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lfish.control.BaseActivity;
import com.lfish.control.R;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.action.cmddomian.LockScreenCmd;
import com.lfish.control.action.cmdparams.LockScreenParams;
import com.lfish.control.user.login.LoginActivity;

/**
 * Created by SuZhiwei on 2016/8/21.
 */
public class BlockScreenActivity extends BaseActivity implements View.OnClickListener {
    private EditText lockpass;
    private Button btSubmit,btCancle;
    private String toChatUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        toChatUsername = getIntent().getStringExtra("user");
        setContentView(R.layout.activity_lockscreen);
        lockpass = (EditText) findViewById(R.id.lockpass);
        btSubmit = (Button) findViewById(R.id.bt_lockscreen_submit);
        btCancle = (Button) findViewById(R.id.bt_lockscreen_cancle);
        lockpass.setError(getString(R.string.activity_lockscreen_tick));

        btCancle.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_lockscreen_submit:
                CmdFactory.getInstance().SendCmd(LockScreenCmd.CMDNUMBER, toChatUsername,new LockScreenParams(true,lockpass.getText().toString()));
                break;
            case R.id.bt_lockscreen_cancle:
                CmdFactory.getInstance().SendCmd(LockScreenCmd.CMDNUMBER, toChatUsername,new LockScreenParams(false));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                open(LoginActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
