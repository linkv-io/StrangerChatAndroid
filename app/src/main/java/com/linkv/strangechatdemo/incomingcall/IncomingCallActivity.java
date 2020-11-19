package com.linkv.strangechatdemo.incomingcall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linkv.strangechatdemo.common.BaseActivity;
import com.linkv.strangechatdemo.common.GlobalParams;
import com.linkv.strangechatdemo.R;
import com.linkv.strangechatdemo.common.RingTonPlayer;
import com.linkv.strangechatdemo.liveroom.LiveRoomActivity;
import com.linkv.strangechatdemo.user.User;

/**
 * Created by Xiaohong on 2020/11/5.
 * desc: 被叫页面
 */
public class IncomingCallActivity extends BaseActivity implements View.OnClickListener {

    private static String KEY_TID = "KEY_TID";
    private ImageButton mBtnHangUp;
    private ImageButton mBtnAccept;
    private TextView mTvName;
    private ImageView mIvIcon;
    private String mTUid;
    RingTonPlayer mRingTonInComingPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mRingTonInComingPlayer = new RingTonPlayer(this, GlobalParams.RINGTON_INCOMING_CALL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        initView();
        initData();
    }

    // 初始化来电用户信息。
    private void initData() {
        mTUid = getIntent().getStringExtra(KEY_TID);
        if (!TextUtils.isEmpty(mTUid)) {
            User user = new User(Integer.parseInt(mTUid));
            mTvName.setText(user.getName());
            mIvIcon.setImageResource(user.getAvatarId());
        }
    }

    private void initView() {
        mBtnAccept = findViewById(R.id.btn_accept);
        mBtnHangUp = findViewById(R.id.btn_hangup);
        mIvIcon = findViewById(R.id.iv_icon);
        mTvName = findViewById(R.id.tv_name);
        mBtnAccept.setOnClickListener(this);
        mBtnHangUp.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开启界面即开启来电铃声。
        mRingTonInComingPlayer.playRingtone();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 退出界面即停止播放来电铃声。
        mRingTonInComingPlayer.stopRingtone();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                // 接受，进入房间。
                if (mStrangerChatEngine != null) {
                    mStrangerChatEngine.answerCall(mTUid, true, false, "", null);
                }
                finish();
                // 房间ID为呼叫方ID
                LiveRoomActivity.actionStart(this, mTUid, mTUid);
                break;
            case R.id.btn_hangup:
                // 挂断
                if (mStrangerChatEngine != null) {
                    mStrangerChatEngine.answerCall(mTUid, false, false, "", null);
                }
                finish();
                break;
        }
    }

    /**
     * 开启本Activity
     * @param context 上下文
     * @param tid 来电用户ID
     */
    public static void actionStart(Context context, String tid) {
        Intent intent = new Intent(context, IncomingCallActivity.class);
        intent.putExtra(KEY_TID, tid);
        context.startActivity(intent);
    }

}
