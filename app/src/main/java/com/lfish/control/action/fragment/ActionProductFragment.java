package com.lfish.control.action.fragment;

import android.view.View;
import android.widget.AdapterView;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.lfish.control.BaseFragment;
import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;
import com.lfish.control.action.CmdFactory;
import com.lfish.control.action.activity.UnLockActionActivity;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by SuZhiwei on 2016/9/11.
 */
public class ActionProductFragment extends BaseFragment implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {
    private EaseChatExtendMenu chatExtendMenu;
    private AdapterView.OnItemClickListener onItemClickListener;
    @Override
    public int getLayout() {
        return R.layout.fm_actionprduct;
    }

    @Override
    public void initFragment() {
        chatExtendMenu = (EaseChatExtendMenu) findViewById(com.hyphenate.easeui.R.id.extend_menu);
        Iterator<Map.Entry<Integer, Class>> iterator = CmdFactory.getInstance().getCmdSet().entrySet().iterator();
        while(iterator.hasNext()){
            BaseBeanCmd cmd = CmdFactory.getInstance().getCmd(iterator.next().getKey());
            if(cmd!=null)
                chatExtendMenu.registerMenuItem(cmd.getMenuName(), cmd.getMenuIco(), cmd.getCmdNumber(),cmd.isLock(),cmd.getPrice(), ActionProductFragment.this);
        }
        chatExtendMenu.init(true);
    }


    @Override
    public void onClick(int itemId, View view) {
        BaseBeanCmd cmd = CmdFactory.getInstance().getCmd(itemId);

        // 判断是否解锁
//        if(cmd.isLock()){
            UnLockActionActivity.openUnload(getActivity(), cmd.getCmdNumber());
//            return;
//        }
    }
}
