package com.linkv.strangechat.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linkv.strangechat.Gift;
import com.linkv.strangechat.R;
import com.linkv.strangechat.User;

/**
 * Created by Xiaohong on 2020/11/9.
 * desc: 静态礼物展示布局
 */
public class StaticGiftLayout extends LinearLayout {
    private ImageView mIvGiftSenderAvatar;
    private ImageView mIvGiftPic;
    private TextView mTvGiftName;
    private TextView mTvGiftNum;
    private TextView mTvGiftSenderName;


    public StaticGiftLayout(Context context) {
        super(context);
    }

    public StaticGiftLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticGiftLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIvGiftSenderAvatar = findViewById(R.id.iv_gift_sender_avatar);
        mTvGiftName = findViewById(R.id.tv_gift_name);
        mTvGiftSenderName = findViewById(R.id.tv_gift_sender_name);
        mIvGiftPic = findViewById(R.id.iv_gift_pic);
        mTvGiftNum = findViewById(R.id.tv_gift_num);
    }

    private int mLastStaticGiftId = -1;
    private int mGiftCount = 1;

    public void showStaticGift(int senderUid, Gift gift) {
        removeCallbacks(hideStaticGiftTask);
        if (mLastStaticGiftId == gift.getId()) {
            mGiftCount++;
        } else {
            mGiftCount = 1;
        }
        mLastStaticGiftId = gift.getId();
        if (!isShown()) {
            setVisibility(View.VISIBLE);
            startShowAnim();
        }
        User user = new User(senderUid);
        mTvGiftSenderName.setText(user.getName());
        mIvGiftSenderAvatar.setImageResource(user.getAvatarId());
        mTvGiftName.setText(gift.getName());
        mIvGiftPic.setImageResource(gift.getPreviewId());
        System.out.println("mGiftCount ======== " + mGiftCount);
        mTvGiftNum.setText(String.format(getContext().getString(R.string.gift_num), mGiftCount));
        startTextNumAnim();
        // 5秒后自动消失
        postDelayed(hideStaticGiftTask, 5000);

    }

    Runnable hideStaticGiftTask = new Runnable() {
        @Override
        public void run() {
            startHideAnim();
        }
    };

    // 展示动画
    public void startShowAnim() {
        ObjectAnimator animation1 = ObjectAnimator.ofFloat(this, "translationX", -getMeasuredWidth(), 0);
        animation1.setDuration(600);
        animation1.start();
        animation1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    // 消失动画
    public void startHideAnim() {
        ObjectAnimator animationHide = ObjectAnimator.ofFloat(this, "translationX", 0, -getMeasuredWidth());
        animationHide.setDuration(600);
        animationHide.start();
        animationHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(View.GONE);
            }
        });
    }


    public void startTextNumAnim() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animation1 = ObjectAnimator.ofFloat(mTvGiftNum, "scaleX", 1.0f, 2.0f, 1.0f, 1.2f);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(mTvGiftNum, "scaleY", 1.0f, 2.0f, 1.0f, 1.2f);
        animatorSet.playTogether(animation1, animation2);
        animatorSet.setDuration(1000);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

}
