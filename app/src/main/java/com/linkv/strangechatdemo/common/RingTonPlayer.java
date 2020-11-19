package com.linkv.strangechatdemo.common;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

import com.linkv.strangechatdemo.utils.LogUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Xiaohong on 2020/11/8.
 * desc: 铃声播放类
 */
public class RingTonPlayer {

    private static final String TAG = "RingTonPlayer";
    private MediaPlayer mPlayer;

    private Context context;
    private File ringTonFile;

    /**
     * 铃声播放构造方法
     * @param context 上下文
     * @param ringTonFile 要播放的铃声文件。
     */
    public RingTonPlayer(Context context, File ringTonFile) {
        this.context = context;
        this.ringTonFile = ringTonFile;
    }

    /**
     * 播放铃声
     */
    public void playRingtone() {
        if (mPlayer == null) {
            mPlayer = getRingTone();
        }
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    /**
     * 停止播放铃声
     */
    public void stopRingtone() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            LogUtils.d(TAG, "stopRingtone success");
        }
    }

    /**
     * 准备铃声的播放器
     * @return 返回铃声播放器
     */
    private MediaPlayer getRingTone() {
        MediaPlayer player = null;
        if (ringTonFile.exists()) {
            player = new MediaPlayer();
            try {
                player.setDataSource(ringTonFile.getPath());
                player.setLooping(true);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                player = null;
            }
        }
        if (player == null) {
            player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        }
        return player;
    }


}
