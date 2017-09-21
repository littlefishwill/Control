package com.lfish.control.action.cmddomian;

import com.lfish.control.R;
import com.lfish.control.action.BaseBeanCmd;

/**
 * Created by SuZhiwei on 2016/8/21.
 */
public class GetUserActivityCmd extends BaseBeanCmd {

    public static final int CMDNUMBER = 14;

    @Override
    public String getShortDes() {
        return "获取用户行为";
    }

    @Override
    public String getLongDes() {
        return "可以监控用户的行为";
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
