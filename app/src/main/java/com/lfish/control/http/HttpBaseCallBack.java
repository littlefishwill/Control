package com.lfish.control.http;

import org.xutils.common.Callback;

/**
 * Created by SuZhiwei on 2017/5/7.
 */
public abstract class HttpBaseCallBack<T extends BaseResultData> implements Callback.CommonCallback<T> {

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
