package com.lfish.control.control.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.BaseFragment;
import com.lfish.control.R;
import com.lfish.control.user.UserManager;

/**
 * Created by SuZhiwei on 2016/7/31.
 */
public class AddControlNumberFragment extends BaseFragment implements View.OnClickListener {
    private AutoCompleteTextView autoCompleteTextView,autoCompleteReason;
    private Button btnSendAsk;
    @Override
    public int getLayout() {
        return R.layout.fm_addcontrolnumber;
    }

    @Override
    public void initFragment() {
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.atv_enter_number);
        autoCompleteReason =  (AutoCompleteTextView) findViewById(R.id.atv_enter_resaon);

        btnSendAsk = (Button) findViewById(R.id.btn_number_search);
        btnSendAsk.setOnClickListener(this);
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
        }
    }
}
