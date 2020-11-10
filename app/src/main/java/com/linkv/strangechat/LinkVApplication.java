package com.linkv.strangechat;

import android.app.Application;

/**
 * Created by Xiaohong on 2020/11/6.
 * desc:
 */
public class LinkVApplication extends Application {

    private static Application mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化陌生人聊天引擎
        EngineManager.getInstance().initEngine(this);
        GlobalParams.initAudioFile(getApplicationContext());
        mAppContext = this;
    }

    public static Application getApplication() {
        return mAppContext;
    }

}
