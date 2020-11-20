package com.linkv.strangechatdemo;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.im.imcore.IMBridger;
import com.im.imlogic.IMMsg;
import com.im.imlogic.LVIMSDK;
import com.linkv.strangechatdemo.common.BaseActivity;
import com.linkv.strangechatdemo.common.GlobalParams;
import com.linkv.strangechatdemo.common.RingTonPlayer;
import com.linkv.strangechatdemo.incomingcall.EngineManager;
import com.linkv.strangechatdemo.incomingcall.IncomingCallActivity;
import com.linkv.strangechatdemo.liveroom.LiveRoomActivity;
import com.linkv.strangechatdemo.user.User;
import com.linkv.strangechatdemo.utils.LogUtils;
import com.linkv.strangechatdemo.utils.ToastUtil;
import com.linkv.strangechatdemo.widget.CircleImageView;

import java.util.Locale;

public class MainActivity extends BaseActivity implements View.OnClickListener, EngineManager.StrangerChatListener {
    final static String TAG = "MainActivity";

    private View mBtnCall;
    private EditText mEtTid;
    private TextView mTvName;
    private CircleImageView mImgAvatar;
    private TextView mTvUid;
    RingTonPlayer mRingTonCallPlayer;

    private String[] permissionNeeded = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO"};
    private boolean mIsStarted = false;
    // 上次点击呼叫的时间
    private long mLastClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRingTonCallPlayer = new RingTonPlayer(this, GlobalParams.RINGTON_VOIP_CALL);
        if (mEngineManager != null) {
            mEngineManager.setStrangerChatListener(this);
        }
        requestPermission();
        setContentView(R.layout.activity_main);
        initView();
        doLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsStarted = true;
    }

    /**
     * 登录IM
     */
    private void doLogin() {
        if (!LVIMSDK.sharedInstance().isAppUserLoginSucceed()) {
            int login = LVIMSDK.sharedInstance().login(getUid());
            if (login == 0) {
                // 0 代表登录成功，刷新用户信息。
                updateUserInfo();
            }
        }
    }


    /**
     * 刷新用户信息的界面展示。
     */
    private void updateUserInfo() {
        if (mCurUserInstance.getUser().getUid() != 0) {
            mTvName.setText(mCurUserInstance.getUser().getName());
            mTvUid.setText(String.format(Locale.getDefault(), getString(R.string.id), mCurUserInstance.getUser().getUid()));
            int avatarId = mCurUserInstance.getUser().getAvatarId();
            LogUtils.d(TAG, "avatarId = " + avatarId);
            Drawable drawable = getResources().getDrawable(avatarId, null);
            mImgAvatar.setImageDrawable(drawable);
        } else {
            mTvName.setText("");
            mTvUid.setText("");
        }
    }


    /**
     * 生成一个4位数的随机userId
     *
     * @return
     */
    private String getUid() {
        if (mCurUserInstance.getUser().getUid() == 0) {
            int uid = (int) (10000 * Math.random()) + 1;
            mCurUserInstance.setUser(new User(uid));
        }
        return mCurUserInstance.getUser().getUid() + "";
    }

    private void initView() {
        mBtnCall = findViewById(R.id.btn_call);
        mEtTid = findViewById(R.id.et_tid);
        mTvName = findViewById(R.id.tv_name);
        mImgAvatar = findViewById(R.id.tv_avatar);
        mTvUid = findViewById(R.id.tv_uid);
        mBtnCall.setOnClickListener(this);
        mBtnCall.setEnabled(false);
        mEtTid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 根据有无输入判断呼叫按状态
                int str_length = s.length();
                if (str_length > 0) {
                    mBtnCall.setEnabled(true);
                } else {
                    mBtnCall.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                // 点击呼叫，检查tid。
                String tid = mEtTid.getText().toString();
                long currentTimeMillis = System.currentTimeMillis();
                // 防止频繁点击呼叫
                if (currentTimeMillis - mLastClickTime > 1000 && !TextUtils.isEmpty(tid)) {
                    mLastClickTime = currentTimeMillis;
                    callUser(tid);
                }
                break;
        }
    }

    /**
     * 主动呼叫
     *
     * @param tid 要呼叫的用户ID
     */
    private void callUser(String tid) {
        mStrangerChatEngine.call(tid, false, "", new IMBridger.IMSendMessageListener() {
            @Override
            public void onIMSendMessageCallback(int i, String s, IMMsg imMsg, Object o) {
                LogUtils.d(TAG, "callUser code  = " + i);
                mRingTonCallPlayer.playRingtone();
            }
        });
    }


    @Override
    public int onCallReceived(String uid, boolean isAudio, long timestamp, String extra) {
        // 收到被叫时判断是否在住页面，是就开启被叫页面
        mRingTonCallPlayer.stopRingtone();
        if (mIsStarted) {
            IncomingCallActivity.actionStart(this, uid);
        }
        return 0;
    }


    @Override
    protected void onStop() {
        mIsStarted = false;
        super.onStop();
        // 退出界面停止播放呼叫铃声。
        mRingTonCallPlayer.stopRingtone();
    }

    @Override
    public int onAnswerCallReceived(String uid, boolean accept, boolean isAudio, long timestamp, String extra) {
        // 对方接听进入聊天室，否则提示对方拒接
        if (accept) {
            // 房间ID为被叫方ID
            LiveRoomActivity.actionStart(this, uid, uid);
        } else {
            ToastUtil.shawToast("对方已挂断");
            mRingTonCallPlayer.stopRingtone();
        }
        return 0;
    }

    @Override
    public int onHangupCallReceived(String uid, String extra) {
        // 提示对方挂断
        ToastUtil.shawToast("对方已挂断");
        return 0;
    }

    @Override
    public int onIMReceiveMessageHandler(String owner, IMMsg msg, int waitings, int packetSize, int waitLength, int bufferSize) {
        return 0;
    }


    /**
     * 申请摄像头和麦克风权限。
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionNeeded, 101);
            }
        }
    }


}
