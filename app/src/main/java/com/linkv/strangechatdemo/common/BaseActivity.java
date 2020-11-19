package com.linkv.strangechatdemo.common;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.linkv.strangechatdemo.incomingcall.EngineManager;
import com.linkv.strangechatdemo.user.CurrentUser;
import com.linkv.strangerchat.StrangerChat;

/**
 * Created by Xiaohong on 2020/11/19.
 * desc: Activity基类
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    // 当前登录用户单例
    protected CurrentUser mCurUserInstance = CurrentUser.instance();
    // 视频聊天管理类单例
    protected EngineManager mEngineManager;
    // 视频聊天引擎
    protected StrangerChat mStrangerChatEngine;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEngineManager = EngineManager.getInstance();
        mStrangerChatEngine = mEngineManager.getEngine();
    }
}
