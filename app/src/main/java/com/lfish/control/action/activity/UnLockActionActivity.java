package com.lfish.control.action.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lfish.control.BaseActivity;
import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.http.HttpManager;
import com.lfish.control.view.PayView;

/**
 * Created by SuZhiwei on 2016/9/11.
 */
public class UnLockActionActivity extends BaseActivity {
    private BaseBeanCmd baseBeanCmd;
    private ImageView acitonIco;
    private TextView actionName,actionDes,actionPrice;
    private PayView payView;
    private boolean isClick =false;
//    private Button unLockPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlockaction);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        baseBeanCmd =  CmdFactory.getInstance().getCmd(getIntent().getIntExtra("actionId",-1));
        acitonIco = (ImageView) findViewById(R.id.iv_unlock_action_ico);
        actionName = (TextView) findViewById(R.id.tv_unlock_action_name);
        actionDes = (TextView) findViewById(R.id.tv_unlock_action_des);
        actionPrice = (TextView) findViewById(R.id.tv_unlock_action_price);
        payView = (PayView) findViewById(R.id.pv_unlock);
        payView.setOnPayClickListener(new PayView.OnPayClickListener() {
            @Override
            public void onClick() {
                isClick = true;
            }
        });
//        unLockPay = (Button) findViewById(R.id.bt_unlock_action_pay);

        acitonIco.setImageResource(baseBeanCmd.getDrawable());
        Glide.with(this).load(baseBeanCmd.getMenuIco()).placeholder(R.drawable.loddingaction_ico2).into(acitonIco);
        actionName.setText(baseBeanCmd.getMenuName());
        actionDes.setText("功能描述："+baseBeanCmd.getMenuDes());

        if(baseBeanCmd.isLock()){
            payView.setVisibility(View.VISIBLE);
            actionPrice.setText("功能价格： " + baseBeanCmd.getPrice()+"元");
            payView.setPrice(baseBeanCmd.getPrice() + "元");
            payView.setPayParams(baseBeanCmd);
        }else{
            payView.setVisibility(View.GONE);
            if(0d == baseBeanCmd.getPrice()){
                actionPrice.setText("该功能可免费使用  已解锁");
            }else {
                actionPrice.setText("该功能原价 " + baseBeanCmd.getPrice() + "元  已解锁");
            }
//            unLockPay.setVisibility(View.GONE);
        }

        if(baseBeanCmd.getPrice()<=0){
            payView.setVisibility(View.GONE);
        }


    }

    public static void openUnload(Context context,int actionId){
        Intent intent = new Intent(context,UnLockActionActivity.class);
        intent.putExtra("actionId",actionId);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isClick){
            paydialog();
        }
    }

    protected void paydialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UnLockActionActivity.this);
          builder.setMessage("您是否已经支付成功？");
          builder.setTitle("支付提示");
          builder.setPositiveButton("已经支付成功", new DialogInterface.OnClickListener() {
              @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
                    UnLockActionActivity.this.finish();
                  HttpManager.getInstance().getAndRefreshActionList();
                   }
              });
          builder.setNegativeButton("支付失败", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
                   }
              });
          builder.create().show();
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
