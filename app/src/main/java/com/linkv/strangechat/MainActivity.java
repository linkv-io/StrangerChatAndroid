package com.linkv.strangechat;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.im.imcore.IMBridger;
import com.im.imlogic.IMMsg;
import com.im.imlogic.LVIMSDK;
import com.linkv.strangechat.utils.LogUtils;
import com.linkv.strangechat.utils.ToastUtil;
import com.linkv.strangechat.widget.CircleImageView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EngineManager.StrangerChatListener {
    final static String TAG = "MainActivity";

    private View mBtnCall;
    private EditText mEtTid;
    private TextView mTvName;
    private CircleImageView mImgAvatar;
    private TextView mTvUid;
    CurrentUser mCurUserInstance = CurrentUser.instance();
    RingTonPlayer mRingTonCallPlayer;

    private String[] permissionNeeded = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EngineManager.getInstance().setStrangerChatListener(this);
        mRingTonCallPlayer = new RingTonPlayer(this,GlobalParams.RINGTON_VOIP_CALL);
        super.onCreate(savedInstanceState);
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

    private void doLogin() {
        if (!LVIMSDK.sharedInstance().isAppUserLoginSucceed()) {
            int login = LVIMSDK.sharedInstance().login(getUid());
            if (login == 0) {
                onLoginSucceed();
            }
        }
    }

    private void onLoginSucceed() {
        updateUserInfo();
    }

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


    // 生成一个4位数userId
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
                if (!TextUtils.isEmpty(tid)) {
                    // 呼叫
                    callUser(tid);
                }
                break;
        }
    }

    private void callUser(String tid) {
        EngineManager.getInstance().getEngine().call(tid, false, "", new IMBridger.IMSendMessageListener() {
            @Override
            public void onIMSendMessageCallback(int i, String s, IMMsg imMsg, Object o) {
                LogUtils.d(TAG, "callUser code  = " + i);
                mRingTonCallPlayer.playRingtone();
            }
        });
    }

    // 显示拨打电话界面。
    private void showCallView() {
    }


    @Override
    public int onCallReceived(String uid, boolean isAudio, long timestamp, String extra) {
        mRingTonCallPlayer.stopRingtone();
        IncomingCallActivity.actionStart(this, uid);
        return 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRingTonCallPlayer.stopRingtone();
    }

    @Override
    public int onAnswerCallReceived(String uid, boolean accept, boolean isAudio, long timestamp, String extra) {
        // 对方接听进入聊天室，否则提示对方拒接
        if (accept) {
            // 房间ID为呼叫方ID
            LiveRoomActivity.actionStart(this, uid, mCurUserInstance.getUser().getUid() + "");
        } else {
            ToastUtil.shawToast("对方已挂断");
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


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionNeeded, 101);
            }
        }
    }




}
