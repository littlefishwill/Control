package com.lfish.control.control.fragment;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lfish.control.BaseFragment;
import com.lfish.control.R;
import com.lfish.control.child.ChildControlDeviceActivity;
import com.lfish.control.control.adapter.DevicesAdapter;
import com.lfish.control.control.nick.NickManager;
import com.lfish.control.db.dao.Device;
import com.lfish.control.user.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuZhiwei on 2016/7/2.
 */
public class DevicesFragment extends BaseFragment implements DevicesAdapter.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rcDevices;
    private List<Device> devices;
    private DevicesAdapter devicesAdapter;
    private View firstShow;
    private Button createControlNumber;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialDialog reNameDialog;
    @Override
    public int getLayout() {
        return R.layout.fm_devices;
    }

    @Override
    public void initFragment() {
        firstShow = findViewById(R.id.ll_nodeviceshow);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.device_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorchoose, R.color.btn_main_color_shadow,
                R.color.holo_orange_light, R.color.holo_red_light);
        createControlNumber = (Button) findViewById(R.id.btn_regist_child_number);
        rcDevices = (RecyclerView) findViewById(R.id.rv_devices);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcDevices.setLayoutManager(layoutManager);

        createControlNumber.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        logic();
    }

    private void logic() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String userName = UserManager.getInstance().getLoginUser(getActivity()).getUserName();
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    NickManager nickManager = NickManager.getInstance();
                    devices = new ArrayList<Device>();
                    for(String id:usernames){
                        Device device = new Device();
                        device.setHxid(id);
                        if(id.toLowerCase().equals(UserManager.testNumber.toLowerCase())){
                            device.setNick(getString(R.string.device_testnumber_nick));
                            device.setDes(getString(R.string.device_testnumber_des));
                        }else if(id.toLowerCase().equals(userName.toLowerCase()+"1")){
                            device.setNick(id);
                            device.setDes(String.format(getString(R.string.device_sys_autocreate_childnumber_des), userName.toLowerCase()+"1",userName.toLowerCase()+"1"));
                        }else{
                            Log.i("??????",nickManager.getNickName(id));
                            device.setNick(nickManager.getNickName(id));
                        }

                        devices.add(device);
                    }



                    devicesAdapter = new DevicesAdapter(devices,getActivity());
                    devicesAdapter.setOnItemClickListener(DevicesFragment.this);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 没有可控设备
                            if(devices.size()<1){
                                firstShow.setVisibility(View.VISIBLE);
                                return;
                            }
                            firstShow.setVisibility(View.GONE);
                            rcDevices.setAdapter(devicesAdapter);
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private OnDeviceChoosed onDeviceChoosed;

    public OnDeviceChoosed getOnDeviceChoosed() {
        return onDeviceChoosed;
    }

    public void setOnDeviceChoosed(OnDeviceChoosed onDeviceChoosed) {
        this.onDeviceChoosed = onDeviceChoosed;
    }

    @Override
    public void onClick(int pos, Device device) {
        Log.e("Tag",device.getNick()+"");
        if(onDeviceChoosed!=null){
            onDeviceChoosed.onChoose(device);
        }
    }

    @Override
    public void onLongClick(int pos, final Device device) {
        reNameDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.device_rename_des)
                .customView(R.layout.dialog_enter_normal, true)
                .positiveText(R.string.dialog_makesure)
                .negativeText(R.string.dialog_cancle)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View view = dialog.getView();
                        AutoCompleteTextView normal = (AutoCompleteTextView) view.findViewById(R.id.atv_dialog_normal_text);
//                        if (normal.getText().toString().length() < 1) {
//                            normal.setError(getResources().getString(R.string.error_field_required));
//                        }else{
                            NickManager.getInstance().saveNickName(device.getHxid(),normal.getText().toString());
                            logic();
                            reNameDialog.dismiss();
//                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        reNameDialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regist_child_number:
                if(onDeviceChoosed!=null){
                    onDeviceChoosed.onNeedCreateChildNumber();
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        logic();
        Snackbar.make(swipeRefreshLayout, getResources().getString(R.string.refresh_success), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    public interface OnDeviceChoosed{
        void onChoose(Device device);
        void onNeedCreateChildNumber();
    }
}
