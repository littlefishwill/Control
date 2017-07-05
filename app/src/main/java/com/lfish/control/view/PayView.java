package com.lfish.control.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lfish.control.ControlApplication;
import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.action.activity.WebQrCodePayActivity;
import com.lfish.control.http.HttpManager;
import com.lfish.control.utils.Sputils;

/**
 * Created by SuZhiwei on 2016/9/16.
 */
public class PayView extends LinearLayout implements View.OnClickListener {
    private TextView weChatPrice,aliPrice;
    private RelativeLayout wechatContain,aliContain;
    private BaseBeanCmd baseBeanCmd;
    public PayView(Context context) {
        super(context);
    }

    public PayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_pay, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        weChatPrice = (TextView) findViewById(R.id.tv_unlock_wechatprice);
        aliPrice = (TextView) findViewById(R.id.tv_unlock_aliprice);
        wechatContain = (RelativeLayout) findViewById(R.id.rl_wechat);
        aliContain = (RelativeLayout) findViewById(R.id.rl_alipay);
        wechatContain.setOnClickListener(this);
        aliContain.setOnClickListener(this);
    }

    public void setPrice(String price){
        weChatPrice.setText(price);
        aliPrice.setText(price);
    }

    public void setPayParams(BaseBeanCmd baseBeanCmd){
        this.baseBeanCmd = baseBeanCmd;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_alipay:
                aliPay();
                break;
            case R.id.rl_wechat:
                weChatPay();
                break;
        }
    }

    public void aliPay() {
//        WebQrCodePayActivity.openPay(getContext(),baseBeanCmd.getCmdNumber(),"1");

        baseBeanCmd =  CmdFactory.getInstance().getCmd(baseBeanCmd.getCmdNumber());
        float price = (float) baseBeanCmd.getPrice();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");

        String user = Sputils.getInstance(ControlApplication.context).getObject(HttpManager.getInstance(),HttpManager.HTTP_USER,"");
        String payid = Sputils.getInstance(ControlApplication.context).getObject(HttpManager.getInstance(),HttpManager.HTTP_UUID,"")+"-"+baseBeanCmd.getCmdNumber();
        Uri content_url = Uri.parse("http://codepay.fateqq.com:52888/creat_order/?price="+price+"&pay_id="+user+"&type=1&token=fsKGJ6wD9XbHLnWhpU1fo6OKwFZVdY47&act=0&id=10506&debug=1&pay_type=1&param="+payid+"&notify_url=http://47.92.77.123/supervisor/app/notify");
        intent.setData(content_url);
        getContext().startActivity(intent);
        if(onPayClickListener!=null){
            onPayClickListener.onClick();
        }

    }

    public void weChatPay(){
//        WebQrCodePayActivity.openPay(getContext(),baseBeanCmd.getCmdNumber(),"3");
        baseBeanCmd =  CmdFactory.getInstance().getCmd(baseBeanCmd.getCmdNumber());
        float price = (float) baseBeanCmd.getPrice();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        String user = Sputils.getInstance(ControlApplication.context).getObject(HttpManager.getInstance(),HttpManager.HTTP_USER,"");
        String payid = Sputils.getInstance(ControlApplication.context).getObject(HttpManager.getInstance(),HttpManager.HTTP_UUID,"")+"-"+baseBeanCmd.getCmdNumber();
        Uri content_url = Uri.parse("http://codepay.fateqq.com:52888/creat_order/?price="+price+"&pay_id="+user+"&type=3&token=fsKGJ6wD9XbHLnWhpU1fo6OKwFZVdY47&act=0&id=10506&debug=1&pay_type=1&param="+payid+"&notify_url=http://47.92.77.123/supervisor/app/notify");
        intent.setData(content_url);
        getContext().startActivity(intent);
        if(onPayClickListener!=null){
            onPayClickListener.onClick();
        }
    }

    private OnPayClickListener onPayClickListener;

    public void setOnPayClickListener(OnPayClickListener onPayClickListener) {
        this.onPayClickListener = onPayClickListener;
    }

    public interface  OnPayClickListener{
        void onClick();
    }

}
