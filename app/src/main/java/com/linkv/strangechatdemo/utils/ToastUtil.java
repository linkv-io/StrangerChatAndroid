package com.linkv.strangechatdemo.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.linkv.strangechatdemo.LinkVApplication;


/**
 * Created by Xiaohong on 2020/11/6.
 * desc:
 */
public class ToastUtil {
    private static Handler mHandler = new Handler(Looper.getMainLooper());


    public static void shawToast(String msg) {
        shawToast(msg, Toast.LENGTH_SHORT);

    }

    public static void shawToast(String msg, int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LinkVApplication.getApplication(), msg, duration).show();
            }
        });
    }

}
