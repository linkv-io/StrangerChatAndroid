package com.linkv.strangechatdemo.liveroom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.im.imlogic.IMMsg;
import com.linkv.rtc.LVConstants;
import com.linkv.rtc.entity.LVAudioVolume;
import com.linkv.rtcsdk.bean.VideoQuality;
import com.linkv.strangechatdemo.common.BaseActivity;
import com.linkv.strangechatdemo.R;
import com.linkv.strangechatdemo.user.User;
import com.linkv.strangechatdemo.utils.LogUtils;
import com.linkv.strangechatdemo.utils.TimeUtils;
import com.linkv.strangechatdemo.utils.ToastUtil;
import com.linkv.strangechatdemo.widget.CircleImageView;
import com.linkv.strangechatdemo.widget.FrameImageView;
import com.linkv.strangechatdemo.widget.StaticGiftLayout;
import com.linkv.strangerchat.StrangerChat;
import com.zhouwei.blurlibrary.EasyBlur;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiaohong on 2020/11/5.
 * desc:
 */
public class LiveRoomActivity extends BaseActivity implements StrangerChat.StrangerRoomHandler, View.OnClickListener {
    private static final String TAG = "LiveRoomActivity";
    private static String KEY_TID = "KEY_TID";
    private static String KEY_ROOM_ID = "KEY_ROOM_ID";
    private TextView mTvTime;
    private ImageView mBtnClose;
    private View mBtnSwitchCamera;
    private View mBtnGift;
    private View mBtnMic;
    private ImageView mIvMic;
    private CircleImageView mIvOtherAvatar;
    private TextView mTvOtherName;
    private View mBtnEnableCamera;
    private ImageView mIvEnableCamera;
    private View mViewGroupTop;
    private View mViewGroupBottom;
    private ViewGroup mContainerVideoLarge;
    private ViewGroup mContainerVideoSmall;
    private boolean mIsMicOpen = true;
    private boolean mIsCameraOpen = true;
    private boolean mIsFront = true;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private FrameImageView mAnimGift;
    private View mLayoutGift;
    private boolean mIsBtnsShow = true;
    private String mRoomId;
    private StaticGiftLayout mContainerGift;
    private ImageView mIvBlurLarge;
    private ImageView mIvBlurSmall;

    // 当前视频时间（秒）。
    int mCurrentSeconds = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_room);
        initView();
        initData();
    }

    private void initData() {
        mRoomId = getIntent().getStringExtra(KEY_ROOM_ID);
        String tid = getIntent().getStringExtra(KEY_TID);
        if (tid != null) {
            User other = new User(Integer.parseInt(tid));
            // 设置对方用户信息
            mIvOtherAvatar.setImageResource(other.getAvatarId());
            mTvOtherName.setText(other.getName());
        }
        // 登录聊天室之前请设置聊天室事件回调监听。
        mStrangerChatEngine.setStrangerRoomHandler(this);
        mStrangerChatEngine.loginRoom(mCurUserInstance.getUser().getUid() + "", mRoomId, true);
    }

    private void initView() {
        mTvTime = findViewById(R.id.tv_time_count);
        mBtnClose = findViewById(R.id.btn_close);
        mBtnGift = findViewById(R.id.btn_gift);
        mBtnMic = findViewById(R.id.btn_mic);
        mIvMic = findViewById(R.id.iv_mic);
        mIvOtherAvatar = findViewById(R.id.iv_other_avatar);
        mTvOtherName = findViewById(R.id.tv_other_name);
        mBtnEnableCamera = findViewById(R.id.btn_enable_camera);
        mIvEnableCamera = findViewById(R.id.iv_enable_camera);
        mBtnSwitchCamera = findViewById(R.id.btn_switch_camera);
        mViewGroupBottom = findViewById(R.id.view_group_bottom);
        mViewGroupTop = findViewById(R.id.view_group_top);
        mContainerVideoLarge = findViewById(R.id.container_video_large);
        mContainerVideoSmall = findViewById(R.id.container_video_small);
        mLayoutGift = findViewById(R.id.layout_gift);
        mAnimGift = findViewById(R.id.anim_gift);
        mContainerGift = findViewById(R.id.container_gift);
        mIvBlurLarge = findViewById(R.id.iv_blur_large);
        mIvBlurSmall = findViewById(R.id.iv_blur_small);
        mBtnClose.setOnClickListener(this);
        mBtnEnableCamera.setOnClickListener(this);
        mBtnGift.setOnClickListener(this);
        mBtnMic.setOnClickListener(this);
        mBtnSwitchCamera.setOnClickListener(this);
        mContainerVideoLarge.setOnClickListener(this);
        mContainerVideoSmall.setOnClickListener(this);
        initGiftView();
    }

    /**
     * 初始化礼物面板
     */
    private void initGiftView() {
        View plane = findViewById(R.id.item_gift_air_plane);
        plane.setOnClickListener(this);
        initGiftItem(plane, R.drawable.airplane, 200);
        View angel = findViewById(R.id.item_gift_angel);
        angel.setOnClickListener(this);
        initGiftItem(angel, R.drawable.angel, 2000);
        View cup = findViewById(R.id.item_gift_cup);
        cup.setOnClickListener(this);
        initGiftItem(cup, R.drawable.coffee3x, 20);
        View bear = findViewById(R.id.item_gift_bear);
        bear.setOnClickListener(this);
        initGiftItem(bear, R.drawable.beer3x, 100);
        View castle = findViewById(R.id.item_gift_castle);
        castle.setOnClickListener(this);
        initGiftItem(castle, R.drawable.castle, 1000);
        View boat = findViewById(R.id.item_gift_boat);
        boat.setOnClickListener(this);
        initGiftItem(boat, R.drawable.boat, 5000);
        View siyecao = findViewById(R.id.item_gift_siyecao);
        siyecao.setOnClickListener(this);
        initGiftItem(siyecao, R.drawable.siyecao3x, 50);
        View candy = findViewById(R.id.item_gift_candy);
        candy.setOnClickListener(this);
        initGiftItem(candy, R.drawable.tang3x, 100);
    }

    /**
     * 初始化礼物项
     * @param giftView 礼物信息显示控件
     * @param previewId  礼物预览图
     * @param price 礼物价格
     */
    private void initGiftItem(View giftView, int previewId, int price) {
        ImageView ivPreview = giftView.findViewById(R.id.iv_preview);
        TextView tvPrice = giftView.findViewById(R.id.tv_price);
        ivPreview.setImageResource(previewId);
        tvPrice.setText(String.valueOf(price));
    }


    /**
     * 开启本Activity
     * @param context 上下文
     * @param tid 对方用户ID
     * @param roomId 聊天室ID
     */
    public static void actionStart(Context context, String tid, String roomId) {
        Intent intent = new Intent(context, LiveRoomActivity.class);
        intent.putExtra(KEY_TID, tid);
        intent.putExtra(KEY_ROOM_ID, roomId);
        context.startActivity(intent);
    }


    @Override
    public void onCallTimeChanged(int duration, String roomId) {
    }

    @Override
    public int onGiftReceived(String giftId, int count, String sendUid, List<String> uids, String roomId, String extra) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gift gift = Gift.createGiftById(Integer.parseInt(giftId));
                if (gift.isStatic()) {
                    // 静态礼物
                    mContainerGift.showStaticGift(Integer.parseInt(sendUid), gift);
                } else {
                    // 播放动画礼物
                    playGiftAnim(gift.getAnimId());
                }
            }
        });
        return 0;
    }

    @Override
    public int onApplyForBeamReceived(String uid, String roomId, int position, String extra) {
        return 0;
    }

    @Override
    public int onAnswerApplyForBeam(String uid, boolean accept, String roomId, int position, String extra) {
        return 0;
    }

    @Override
    public int onKickBeamOffReceived(String uid, String roomId, int position, String extra) {
        return 0;
    }

    @Override
    public int onEndRoomReceived(String roomId, String extra) {
        return 0;
    }

    @Override
    public int onUserEntered(String uid, String roomId) {
        return 0;
    }

    @Override
    public int onUserLeft(String uid, String roomId) {
        return 0;
    }

    @Override
    public int onUserMicrophoneChanged(String uid, String roomId, boolean isOpen) {

        return 0;
    }

    @Override
    public int onUserVideoCameraChanged(String uid, String roomId, boolean isOpen) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 对方禁用摄像头，
                if (isOpen) {
                    // 去除模糊
                    mIvBlurLarge.setVisibility(View.GONE);
                } else {
                    // 加高斯模糊。
                    showBlurView(Integer.parseInt(uid), mIvBlurLarge);
                }
            }
        });
        return 0;
    }

    /**
     * 显示高斯模糊的头像
     * @param uid 对应头像的用户ID
     * @param blurView 待显示高斯模糊头像控件。
     */
    private void showBlurView(int uid, ImageView blurView) {
        User user = new User(uid);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), user.getAvatarId());
        Bitmap blur = boxBlur(bitmap);
        blurView.setImageBitmap(blur);
        blurView.setVisibility(View.VISIBLE);
    }


    /**
     * 将bitmap进行
     * @param srcBitmap
     * @return
     */
    private Bitmap boxBlur(Bitmap srcBitmap) {
        return EasyBlur.with(this)
                .bitmap(srcBitmap) //要模糊的图片
                .radius(10)//模糊半径
                .scale(4)//指定模糊前缩小的倍数
                .policy(EasyBlur.BlurPolicy.FAST_BLUR)//使用fastBlur
                .blur();
    }

    @Override
    public void onRemoteStreamEnd(String userId) {
        // 远端流退出
        ToastUtil.shawToast("对方已挂断");
        finish();
    }

    @Override
    public void onRemoteStreamAdd(String userId) {

        LogUtils.d(TAG, "onRemoteStreamAdd = " + userId);
        // 拉流
        mStrangerChatEngine.startPlayingStream(userId, mContainerVideoLarge, false);

    }

    @Override
    public void onRoomDisconnect(int i, String s) {
        ToastUtil.shawToast("聊天室连接已断开");
    }

    @Override
    public void onRoomConnected(String s) {
        LogUtils.d(TAG, "onRoomConnected");
        // 进入房间成功，开启预览。
        mStrangerChatEngine.startPreview(mCurUserInstance.getUser().getUid() + "", mContainerVideoSmall, true);
        mStrangerChatEngine.startPublishing();
        mHandler.post(mTimeCountTask);
    }

    Runnable mTimeCountTask = new Runnable() {
        @Override
        public void run() {
            mTvTime.setText(TimeUtils.transferTimeFormat(mCurrentSeconds));
            mCurrentSeconds++;
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onRemoteQualityUpdate(String s, VideoQuality videoQuality) {
        LogUtils.d(TAG, "onRemoteQualityUpdate s = " + videoQuality.frameHeight);
    }

    @Override
    public void onPublisherQualityUpdate(String s, VideoQuality videoQuality) {
        LogUtils.d(TAG, "onPublisherQualityUpdate  s = " + videoQuality.frameHeight);

    }

    @Override
    public void onVideoSizeChanged(String s, int i, int i1) {

    }

    @Override
    public void onRoomMessageReceive(IMMsg imMsg) {

    }

    @Override
    public void onPublishStateUpdate(int i) {
        LogUtils.d(TAG, "onPublishStateUpdate i = " + i);
    }

    @Override
    public void onPlayStateUpdate(int i, String s) {
        LogUtils.d(TAG, "onPlayStateUpdate i = " + i + "  s = " + s);
    }

    @Override
    public void onMixComplete(boolean success) {

    }

    @Override
    public void onAudioMixStream(ByteBuffer audioBuffer, int sampleRate, int channels, int samplesPerChannel, int bitsPerSample, LVConstants.AudioRecordType type) {

    }

    @Override
    public void onAudioVolumeUpdate(ArrayList<LVAudioVolume> volumes) {

    }

    @Override
    public String onMediaSideInfoInPublishVideoFrame() {
        return null;
    }

    @Override
    public void onExitRoomComplete() {
        LogUtils.d(TAG, " onExitRoomComplete");
    }

    @Override
    public long onDrawFrame(ByteBuffer i420Buffer, int width, int height, int strideY, String userId, String ext) {
        LogUtils.d(TAG, " onDrawFrame");
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                mStrangerChatEngine.logoutRoom();
                finish();
                break;
            case R.id.btn_gift:
                // 打开礼物界面。
                mLayoutGift.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_mic:
                // 打开或关闭麦克风
                mIsMicOpen = !mIsMicOpen;
                mStrangerChatEngine.setMuteOutput(!mIsMicOpen);
                mIvMic.setImageResource(mIsMicOpen ? R.drawable.audio_on2x : R.drawable.audio_off2x);
                break;
            case R.id.btn_enable_camera:
                // 开启或关闭摄像头
                mIsCameraOpen = !mIsCameraOpen;
                if (mIsCameraOpen) {
                    mStrangerChatEngine.startCapture(mCurUserInstance.getUser().getUid() + "", mContainerVideoSmall, true);
                    mIvBlurSmall.setVisibility(View.GONE);
                } else {
                    // 关闭摄像头是显示自己的高斯模糊头像
                    showBlurView(mCurUserInstance.getUser().getUid(), mIvBlurSmall);
                    mStrangerChatEngine.stopCapture();
                }
                mIvEnableCamera.setImageResource(mIsCameraOpen ? R.drawable.video_on2x : R.drawable.video_off2x);
                break;
            case R.id.btn_switch_camera:
                mIsFront = !mIsFront;
                mStrangerChatEngine.useFrontCamera(mIsFront);
                mBtnSwitchCamera.setSelected(mIsFront);
                break;
            case R.id.container_video_large:
                setBtnsVisible();
                break;
            case R.id.item_gift_air_plane:
                mStrangerChatEngine.sendGift("0", 1, null, mRoomId, "", null);
                playGiftAnim(R.drawable.anim_gift_plane);
                break;
            case R.id.item_gift_castle:
                mStrangerChatEngine.sendGift("1", 1, null, mRoomId, "", null);
                playGiftAnim(R.drawable.anim_gift_castle);
                break;
            case R.id.item_gift_angel:
                mStrangerChatEngine.sendGift("2", 1, null, mRoomId, "", null);
                playGiftAnim(R.drawable.anim_gift_angel);
                break;
            case R.id.item_gift_boat:
                mStrangerChatEngine.sendGift("3", 1, null, mRoomId, "", null);
                playGiftAnim(R.drawable.anim_gift_boat);
                break;
            case R.id.item_gift_cup:
                mStrangerChatEngine.sendGift("4", 1, null, mRoomId, "", null);
                mContainerGift.showStaticGift(mCurUserInstance.getUser().getUid(), Gift.createGiftById(4));
                break;
            case R.id.item_gift_siyecao:
                mStrangerChatEngine.sendGift("5", 1, null, mRoomId, "", null);
                mContainerGift.showStaticGift(mCurUserInstance.getUser().getUid(), Gift.createGiftById(5));
                break;
            case R.id.item_gift_bear:
                mStrangerChatEngine.sendGift("6", 1, null, mRoomId, "", null);
                mContainerGift.showStaticGift(mCurUserInstance.getUser().getUid(), Gift.createGiftById(6));
                break;
            case R.id.item_gift_candy:
                mStrangerChatEngine.sendGift("7", 1, null, mRoomId, "", null);
                mContainerGift.showStaticGift(mCurUserInstance.getUser().getUid(), Gift.createGiftById(7));
                break;
        }
    }

    /**
     * 隐藏或展示操作按钮
     */
    private void setBtnsVisible() {
        mIsBtnsShow = !mIsBtnsShow;
        mViewGroupTop.setVisibility(mIsBtnsShow ? View.VISIBLE : View.GONE);
        mViewGroupBottom.setVisibility(mIsBtnsShow ? View.VISIBLE : View.GONE);
        mLayoutGift.setVisibility(View.GONE);
    }

    private void playGiftAnim(int giftAnimId) {
        mLayoutGift.setVisibility(View.GONE);
        mAnimGift.setVisibility(View.VISIBLE);
        mAnimGift.loadAnimation(giftAnimId);
        mAnimGift.setmAnimationListener(() -> {
            mAnimGift.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mStrangerChatEngine.logoutRoom();
        mStrangerChatEngine.stopPublishingStream();
        mStrangerChatEngine.stopPreview();
        mCurrentSeconds = 0;
        mHandler.removeCallbacks(mTimeCountTask);
    }

    @Override
    public void onBackPressed() {
        // 按返回键，如果礼物面板打开则关闭礼物面板。否则退出Activity。
        if (mLayoutGift.isShown()) {
            mLayoutGift.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
