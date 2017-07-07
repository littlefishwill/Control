package com.lfish.control.control.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.BaseFragment;
import com.lfish.control.Config;
import com.lfish.control.R;
import com.lfish.control.control.dao.UserAskDao;
import com.lfish.control.db.AskInfoDao;
import com.lfish.control.db.dao.AskInfo;
import com.lfish.control.utils.DesUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.uuzuche.lib_zxing.camera.BitmapLuminanceSource;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by SuZhiwei on 2016/7/31.
 */
public class AddControlNumberFragment extends BaseFragment implements View.OnClickListener {
    private AutoCompleteTextView autoCompleteTextView,autoCompleteReason;
    private Button btnSendAsk,btnOpenQRcode,btnOpenPhotoForQrCode;
    private static final int QR_REQUEST_CODE = 1;
    private static final int QR_REQUEST_PHOTOCODE = 2;

    @Override
    public int getLayout() {
        return R.layout.fm_addcontrolnumber;
    }

    @Override
    public void initFragment() {
        ZXingLibrary.initDisplayOpinion(getActivity());
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.atv_enter_number);
        autoCompleteReason =  (AutoCompleteTextView) findViewById(R.id.atv_enter_resaon);

        btnSendAsk = (Button) findViewById(R.id.btn_number_search);
        btnOpenQRcode = (Button) findViewById(R.id.btn_qrcode_search);
        btnOpenPhotoForQrCode = (Button) findViewById(R.id.btn_qrcode_search_usephoto);

        btnSendAsk.setOnClickListener(this);
        btnOpenQRcode.setOnClickListener(this);
        btnOpenPhotoForQrCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_number_search:
                if (TextUtils.isEmpty(autoCompleteTextView.getText().toString())) {
                    autoCompleteTextView.setError(getString(R.string.error_field_required));
                    return;
                }
                try {
                    EMClient.getInstance().contactManager().addContact(autoCompleteTextView.getText().toString().trim(), autoCompleteReason.getText().toString());
                    MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                            .title(R.string.add_qrcode_search_alert_title)
                            .content("请求控制"+autoCompleteTextView.getText().toString().trim()+"成功！   等待对方同意后即可控制。")
                            .positiveText(R.string.agree)
                            .show();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_qrcode_search:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, QR_REQUEST_CODE);
                break;
            case R.id.btn_qrcode_search_usephoto:
                Intent intentPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                intentPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                intentPhoto.setType("image/*");
                startActivityForResult(intentPhoto, QR_REQUEST_PHOTOCODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    addFormQrCode(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (requestCode == QR_REQUEST_PHOTOCODE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getActivity().getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片
                    analyzeBitmap (mBitmap,new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            addFormQrCode(result);
//                            Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();

                        }});

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}
    }

    public void addFormQrCode(String passCode){
        Log.i("QRCODE_PARSER",passCode);
        try {
            String decrypt = new DesUtils().decrypt(passCode);
            Log.i("QRCODE_PARSER",decrypt);
            UserAskDao userAskDao = UserAskDao.parserFormString(decrypt);
            if(userAskDao!=null){
                if(System.currentTimeMillis()-userAskDao.getTime()> Config.CHILD_QRCODE_FINSHTIME){
                    Toast.makeText(getActivity(), "二维码过期了", Toast.LENGTH_LONG).show();
                }else{
                    EMClient.getInstance().contactManager().addContact(userAskDao.getName(),Config.QRCODE_RESON_START_TAG+passCode);
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.add_ask_request_caozuo_alert_title)
                            .content("已自动添加 "+userAskDao.getName()+" 为可控设备.")
                            .positiveText(R.string.agree)
                            .show();
                }
            }else{
                Toast.makeText(getActivity(), "二维码格式错误", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "二维码格式错误", Toast.LENGTH_LONG).show();
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
}
