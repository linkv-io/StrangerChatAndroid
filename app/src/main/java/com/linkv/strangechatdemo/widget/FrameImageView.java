package com.linkv.strangechatdemo.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by Xiaohong on 2020/11/2.
 * desc:
 */
public class FrameImageView extends AppCompatImageView {

    private AnimationDrawable mDrawable;
    private OnAnimationEndListener mAnimationListener;
    private boolean mStop;

    public void setmAnimationListener(OnAnimationEndListener mAnimationListener) {
        this.mAnimationListener = mAnimationListener;
    }


    public FrameImageView(Context context) {
        this(context, null);
    }

    public FrameImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void loadAnimation(int res) {
        mStop = false;
        setImageResource(res);
        mDrawable = (AnimationDrawable) getDrawable();
        if (mDrawable != null) {
            mDrawable.start();
            int mDuration = 0;
            for (int i = 0; i < mDrawable.getNumberOfFrames(); i++) {
                mDuration += mDrawable.getDuration(i);
            }
            postDelayed(mAnimaTask, mDuration);
        }
    }

    public void loopLoadAnimation(int res) {
        mStop = false;
        setImageResource(res);
        mDrawable = (AnimationDrawable) getDrawable();
        if (mDrawable != null) {
            mDrawable.start();
        }

    }

    public void stopLoopAnimation() {
        if (mDrawable != null) {
            mDrawable.stop();
        }
    }

    public void stop() {
        mStop = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawable != null && mDrawable.isRunning()) {
            mStop = true;
            mDrawable.stop();
            mDrawable = null;
        }
        removeCallbacks(mAnimaTask);
    }

    private Runnable mAnimaTask = new Runnable() {
        @Override
        public void run() {
            if (mDrawable != null) {
                mDrawable.stop();
                mDrawable = null;
            }
            if (mAnimationListener != null && !mStop) {
                mAnimationListener.animationEnd();
            }
        }
    };

    public interface OnAnimationEndListener {
        void animationEnd();
    }
}
