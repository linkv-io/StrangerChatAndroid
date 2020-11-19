package com.linkv.strangechatdemo.user;

import com.linkv.strangechatdemo.R;

/**
 * Created by Xiaohong on 2020/11/6.
 * desc: 用户信息
 */
public class User {
    private int uid;
    // 用户名
    private String name;
    // 用户头像资源ID
    private int avatarId;

    private static String[] nameArray = {"Mike", "Snow Wing", "Snow", "Json", "Steven", "江城子", "故人初", "泪倾城", "清风叹", "月下客"};

    private static int[] avatarArray = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9, R.drawable.avatar10,};


    public User(int uid) {
        this.uid = uid;
    }

    public User() {
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        // 根据uid生成name
        name = nameArray[uid % 10];
        return name;
    }


    public int getAvatarId() {
        // 根据uid生成avatarId
        avatarId = avatarArray[uid % 10];
        return avatarId;
    }


}
