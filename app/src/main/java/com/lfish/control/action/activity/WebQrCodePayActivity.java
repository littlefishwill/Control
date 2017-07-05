package com.lfish.control.action.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import com.lfish.control.BaseActivity;
import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;
import com.lfish.control.action.CmdFactory;

/**
 * Created by SuZhiwei on 2017/4/21.
 */
public class WebQrCodePayActivity extends BaseActivity {
    private WebView webView;
    private BaseBeanCmd baseBeanCmd;
    private String payType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webqrcodepay);
        webView = (WebView) findViewById(R.id.wv_qrcode_pay);
        payType = getIntent().getStringExtra("payType");
        baseBeanCmd =  CmdFactory.getInstance().getCmd(getIntent().getIntExtra("actionId",-1));
        float price = (float) baseBeanCmd.getPrice();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://codepay.fateqq.com:52888/creat_order/?price="+price+"&pay_id=111111&type="+payType+"&token=fsKGJ6wD9XbHLnWhpU1fo6OKwFZVdY47&act=0&id=10506&debug=1&pay_type=1");
        intent.setData(content_url);
        startActivity(intent);
//        webView.loadUrl("http://codepay.fateqq.com:52888/creat_order/?price="+price+"&pay_id=111111&type="+payType+"&token=fsKGJ6wD9XbHLnWhpU1fo6OKwFZVdY47&act=0&id=10506&debug=1&pay_type=1");

    }

    public static void openPay(Context context,int actionId,String payType){
        Intent intent = new Intent(context,WebQrCodePayActivity.class);
        intent.putExtra("actionId",actionId);
        intent.putExtra("payType",payType);
        context.startActivity(intent);
    }
}
