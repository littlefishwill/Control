package com.lfish.control.control.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.lfish.control.utils.DesUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.uuzuche.lib_zxing.camera.BitmapLuminanceSource;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by SuZhiwei on 2016/7/31.
 */
public class ContacUsFragment extends BaseFragment {

    @Override
    public int getLayout() {
        return R.layout.fragment_contactus;
    }

    @Override
    public void initFragment() {

    }


}
