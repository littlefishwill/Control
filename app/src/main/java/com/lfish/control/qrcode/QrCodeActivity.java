package com.lfish.control.qrcode;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.hyphenate.chat.EMClient;
import com.lfish.control.BaseActivity;
import com.lfish.control.Config;
import com.lfish.control.R;
import com.lfish.control.control.dao.UserAskDao;
import com.lfish.control.utils.DesUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.camera.BitmapLuminanceSource;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;

import java.util.Hashtable;
import java.util.Vector;

/**
 * A login screen that offers login via email/password.
 */
public class QrCodeActivity extends BaseActivity implements View.OnClickListener {
    // UI references.
    private ImageView openPhotos,openLigth;
    private static final int QR_REQUEST_PHOTOCODE = 2;
    private boolean openLight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        openPhotos = (ImageView) findViewById(R.id.iv_qrcode_photos);
        openLigth = (ImageView) findViewById(R.id.iv_qrcode_light);

        openPhotos.setOnClickListener(this);
        openLigth.setOnClickListener(this);

        addScanFragment();


    }

    private void addScanFragment() {
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.view_qr_code_carme);
        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                addFormQrCode(result);
            }

            @Override
            public void onAnalyzeFailed() {
                Toast.makeText(QrCodeActivity.this, "扫描失败", Toast.LENGTH_LONG).show();
            }
        });

        /**
         * 替换我们的扫描控件
         */ getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
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

    public void addFormQrCode(String passCode){
        Log.i("QRCODE_PARSER",passCode);
        try {
            String decrypt = new DesUtils().decrypt(passCode);
            Log.i("QRCODE_PARSER",decrypt);
            UserAskDao userAskDao = UserAskDao.parserFormString(decrypt);
            if(userAskDao!=null){
                if(System.currentTimeMillis()-userAskDao.getTime()> Config.CHILD_QRCODE_FINSHTIME){
                    Toast.makeText(this, "二维码过期了", Toast.LENGTH_LONG).show();
                }else{
                    EMClient.getInstance().contactManager().addContact(userAskDao.getName(),Config.QRCODE_RESON_START_TAG+passCode);
                    new MaterialDialog.Builder(this)
                            .title(R.string.add_ask_request_caozuo_alert_title)
                            .content("已自动添加 "+userAskDao.getName()+" 为可控设备.")
                            .positiveText(R.string.agree).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                            .show();
                }
            }else{
                Toast.makeText(this, "二维码格式错误", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "二维码格式错误", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 解析二维码图片工具类 --- 工具类中的方法有误
     * @param analyzeCallback
     */
    public static void analyzeBitmap(Bitmap mBitmap, CodeUtils.AnalyzeCallback analyzeCallback) {

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(mBitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawResult != null) {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeSuccess(mBitmap, rawResult.getText());
            }
        } else {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeFailed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_qrcode_photos:
                Intent intentPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                intentPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                intentPhoto.setType("image/*");
                startActivityForResult(intentPhoto, QR_REQUEST_PHOTOCODE);
                break;
            case R.id.iv_qrcode_light:
                CodeUtils.isLightEnable(openLight);
                if(openLight){
                    openLigth.setImageResource(R.drawable.sys_ligth_off);
                }else{
                    openLigth.setImageResource(R.drawable.sys_ligth_on);
                }
                openLight = !openLight;
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_REQUEST_PHOTOCODE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片
                    analyzeBitmap (mBitmap,new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            addFormQrCode(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(QrCodeActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();

                        }});

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}
    }
}

