package com.linkv.strangechatdemo.user;

/**
 * Created by Xiaohong on 2020/11/6.
 * desc: 当前登录用户单例类
 */
public class CurrentUser {
    private static CurrentUser mInstance;
    private User user =  new User();

    private CurrentUser() {
    }

    public static CurrentUser instance() {
        if (mInstance == null) {
            synchronized (CurrentUser.class) {
                if (mInstance == null) {
                    mInstance = new CurrentUser();
                }
            }
        }
        return mInstance;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
