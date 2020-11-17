package com.linkv.strangechatdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CircleImageView extends AppCompatImageView {
    private RoundViewHelper roundViewHelper;


    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        roundViewHelper = new RoundViewHelper(context, null);
        roundViewHelper.setCircle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        roundViewHelper.onMeasure(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        roundViewHelper.drawBefore(canvas);
        super.draw(canvas);
        roundViewHelper.drawAfter(canvas);
    }
}
