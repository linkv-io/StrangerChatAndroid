package com.linkv.strangechat;

import android.content.Context;

import com.linkv.strangechat.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Xiaohong on 2020/11/8.
 * desc:
 */
public class GlobalParams {

    public static File RINGTON_HANG_UP;
    public static File RINGTON_VOIP_CALL;
    public static File RINGTON_INCOMING_CALL;

    // 初始化铃声文件地址
    public static void initAudioFile(Context context) {
        RINGTON_HANG_UP = new File(context.getFilesDir(), "hungup.mp3");
        RINGTON_VOIP_CALL = new File(context.getFilesDir(), "voip_call.mp3");
        RINGTON_INCOMING_CALL = new File(context.getFilesDir(), "voip_calling_ring.mp3");
        copyFileFromRaw(context, R.raw.hungup, RINGTON_HANG_UP);
        copyFileFromRaw(context, R.raw.voip_call, RINGTON_VOIP_CALL);
        copyFileFromRaw(context, R.raw.voip_calling_ring, RINGTON_INCOMING_CALL);
    }

    // 将raw目录中的资源文件拷贝到本地
    private static void copyFileFromRaw(Context context, int rawFileId, File localFile) {
        if (!localFile.exists()) {
            InputStream inputStream = context.getResources().openRawResource(rawFileId);
            try {
                FileUtils.copyToFile(inputStream, localFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
