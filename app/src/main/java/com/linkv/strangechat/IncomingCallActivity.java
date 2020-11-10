package com.linkv.strangechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Xiaohong on 2020/11/5.
 * desc:
 */
public class IncomingCallActivity extends AppCompatActivity implements View.OnClickListener {

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

    private void initData() {
        mTUid = getIntent().getStringExtra(KEY_TID);
        mTvName.setText(mTUid);
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
        mRingTonInComingPlayer.playRingtone();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRingTonInComingPlayer.stopRingtone();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                // 接受，进入房间。
                EngineManager.getInstance().getEngine().answerCall(mTUid, true, false, "", null);
                finish();
                // 房间ID为呼叫方ID
                LiveRoomActivity.actionStart(this, mTUid, mTUid);
                break;
            case R.id.btn_hangup:
                mRingTonInComingPlayer.playRingtone();
                // 挂断
                finish();
                break;
        }
    }


    public static void actionStart(Context context, String tid) {
        Intent intent = new Intent(context, IncomingCallActivity.class);
        intent.putExtra(KEY_TID, tid);
        context.startActivity(intent);
    }

}
