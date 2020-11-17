package com.linkv.strangechatdemo;

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

    public RingTonPlayer(Context context, File ringTonFile) {
        this.context = context;
        this.ringTonFile = ringTonFile;
    }

    public void playRingtone() {
        if (mPlayer == null) {
            mPlayer = getRingTone();
        }
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    public void stopRingtone() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            LogUtils.d(TAG, "stopRingtone success");
        }
    }

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
