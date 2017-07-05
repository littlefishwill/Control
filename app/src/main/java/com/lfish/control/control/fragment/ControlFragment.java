package com.lfish.control.control.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hyphenate.easeui.controller.EaseUI;
import com.lfish.control.BaseFragment;
import com.lfish.control.R;
import com.lfish.control.action.BaseAction;
import com.lfish.control.control.adapter.ActionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenmegui on 2016/7/1.
 */
public class ControlFragment extends BaseFragment {

    private RecyclerView rvActions;
    private ActionAdapter actionAdapter;


    @Override
    public int getLayout() {
        return R.layout.fm_control;
    }

    @Override
    public void initFragment() {
        rvActions = (RecyclerView) findViewById(R.id.rv_action);
        rvActions.setLayoutManager(new GridLayoutManager(getActivity(), 4));
//        rvActions.setLayoutManager(linearLayoutManager);
        initData();
    }

    private void initData() {
        List<BaseAction> actions = new ArrayList<BaseAction>();
        actions.add(new BaseAction("手机信息",1,R.drawable.phone_msg));
        actions.add(new BaseAction("位置信息",2,R.drawable.phone_msg));
        actions.add(new BaseAction("通讯录",3,R.drawable.phone_msg));
        actions.add(new BaseAction("短信",4,R.drawable.phone_msg));
        actions.add(new BaseAction("浏览器记录",5,R.drawable.phone_msg));

        rvActions.setAdapter(new ActionAdapter(actions,getActivity()));

    }


}
