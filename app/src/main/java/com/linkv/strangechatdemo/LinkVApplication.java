package com.linkv.strangechatdemo;

import android.app.Application;

import com.linkv.strangechatdemo.common.GlobalParams;
import com.linkv.strangechatdemo.incomingcall.EngineManager;

/**
 * Created by Xiaohong on 2020/11/6.
 * desc:自定义Application类
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

    /**
     * 获取application 上下文。
     * @return
     */
    public static Application getApplication() {
        return mAppContext;
    }

}
