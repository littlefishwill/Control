package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

/**
 * Created by SuZhiwei on 2016/8/21.
 */
public class LockScreenCmd extends BaseBeanCmd {

    public static final int CMDNUMBER = 11;

    @Override
    public String getShortDes() {
        return "一键锁屏";
    }

    @Override
    public String getLongDes() {
        return "你不听话，我给你锁屏";
    }

    @Override
    public int getCmdNumber() {
        return CMDNUMBER;
    }

    @Override
    public int getDrawable() {
        return R.drawable.cmd_blockscreen;
    }
}
