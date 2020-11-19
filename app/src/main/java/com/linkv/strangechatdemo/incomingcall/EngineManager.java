package com.linkv.strangechatdemo.incomingcall;

import android.app.Application;

import com.im.imlogic.IMMsg;
import com.im.imlogic.LVIMSDK;
import com.linkv.strangechatdemo.LocalConfig;
import com.linkv.strangechatdemo.user.CurrentUser;
import com.linkv.strangechatdemo.utils.LogUtils;
import com.linkv.strangechatdemo.utils.ToastUtil;
import com.linkv.strangerchat.StrangerChat;


/**
 * Created by Xiaohong on 2020/11/5.
 * desc: 视频聊天引擎管理单例
 */
public class EngineManager implements StrangerChat.IMEventHandler {
    private final static String TAG = "StrangerChatEngine";
    private StrangerChat mEngine;
    private static EngineManager instance;

    private StrangerChatListener mStrangerChatListener;

    public void setStrangerChatListener(StrangerChatListener listener) {
        mStrangerChatListener = listener;
    }

    public static EngineManager getInstance() {
        if (instance == null) {
            synchronized (EngineManager.class) {
                if (instance == null) {
                    instance = new EngineManager();
                }
            }
        }
        return instance;
    }

    private EngineManager() {
    }

    /**
     * 获取视频引擎
     * @return 请先调用initEngine方法初始化，否则返回null
     */
    public StrangerChat getEngine() {
        return mEngine;
    }

    /**
     * 初始化SDK
     * @param application 应用Application
     */
    public void initEngine(Application application) {
        // APP_ID和APP_SECRET请在LinkV官网获取。 商务合作与技术交流请加QQ群：1160896626
        mEngine = StrangerChat.createEngine(application, LocalConfig.APP_ID, LocalConfig.APP_SECRET, false,
                i -> {
                    LogUtils.d(TAG, "onInitResult code = " + i);
                }, this);
    }


    @Override
    public int onCallReceived(String uid, boolean isAudio, long timestamp, String extra) {
        // 收到呼叫
        if (mStrangerChatListener != null) {
            mStrangerChatListener.onCallReceived(uid, isAudio, timestamp, extra);
        }
        return 0;
    }

    /**
     * 呼叫对方的回应
     * @param uid 对方用户ID
     * @param accept 是否接听，true为接听，false为挂断
     * @param isAudio 是否仅音频
     * @param timestamp 回应时的时间戳
     * @param extra 负载数据
     * @return
     */
    @Override
    public int onAnswerCallReceived(String uid, boolean accept, boolean isAudio, long timestamp, String extra) {
        if (mStrangerChatListener != null) {
            mStrangerChatListener.onAnswerCallReceived(uid, accept, isAudio, timestamp, extra);
        }
        return 0;
    }

    /**
     * 收到某个用户挂断通话的回调
     * @param uid 对方用户ID
     * @param extra 负载数据
     * @return
     */
    @Override
    public int onHangupCallReceived(String uid, String extra) {
        if (mStrangerChatListener != null) {
            mStrangerChatListener.onHangupCallReceived(uid, extra);
        }
        return 0;
    }


    @Override
    public void onQueryIMToken() {
        // 实际中token是需要你们的服务端去向我们的服务端获取。
        String token = "i_am_token";
        LVIMSDK.sharedInstance().setIMToken(CurrentUser.instance().getUser().getUid()+"", token);
    }

    @Override
    public void onIMAuthFailed(String uid, String token, int ecode, int rcode, boolean isTokenExpired) {
        ToastUtil.shawToast("IM鉴权失败");
    }

    @Override
    public void onIMAuthSucceed(String uid, String token, long unReadMsgSize) {
        ToastUtil.shawToast("IM鉴权成功");

    }

    @Override
    public void onIMTokenExpired(String uid, String token) {
        ToastUtil.shawToast("IM Token过期");
    }

    @Override
    public int onIMReceiveMessageHandler(String owner, IMMsg msg, int waitings, int packetSize, int waitLength, int bufferSize) {
        if (mStrangerChatListener != null) {
            mStrangerChatListener.onIMReceiveMessageHandler(owner, msg, waitings, packetSize, waitLength, bufferSize);
        }
        return 0;
    }


    public interface StrangerChatListener {
        // 收到某个用户的呼叫
        int onCallReceived(String uid, boolean isAudio, long timestamp, String extra);

        // 收到某个用户同意或拒绝的指令
        int onAnswerCallReceived(String uid, boolean accept, boolean isAudio, long timestamp, String extra);

        // 收到某个用户挂断通话的回调
        int onHangupCallReceived(String uid, String extra);

        // 收到全局消息
        int onIMReceiveMessageHandler(String owner, IMMsg msg, int waitings, int packetSize, int waitLength, int bufferSize);

    }


}
