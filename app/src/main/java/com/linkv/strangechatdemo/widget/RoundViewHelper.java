package com.linkv.strangechatdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.linkv.strangechatdemo.R;


public class RoundViewHelper {
    private final int NO_ROUND_CONFIG = 0; //没有配置圆角
    private final int CIRCLE_CONFIG = -1; //配置成圆形
    private Path mCirclePath = new Path();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private Paint mLayerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mRoundCorner = NO_ROUND_CONFIG;

    public RoundViewHelper(Context context) {
        this(context, null);
    }

    public RoundViewHelper(Context context, AttributeSet attrs) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mLayerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        initAttrs(context, attrs);
    }

    public boolean hasRoundConfig() {
        return mRoundCorner != NO_ROUND_CONFIG;
    }

    public void setCircle() {
        mRoundCorner = CIRCLE_CONFIG;
    }

    public void onMeasure(int width, int height) {
        float radius = Math.min(width, height) / 2f;
        mCirclePath.reset();
        if (mRoundCorner == CIRCLE_CONFIG) {
            mCirclePath.addCircle(width / 2f, height / 2f, radius, Path.Direction.CCW);
        } else {
            mCirclePath.addRoundRect(new RectF(0, 0, width, height), mRoundCorner, mRoundCorner, Path.Direction.CCW);
        }
    }

    /**
     * draw之前调用
     */
    public void drawBefore(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mCirclePath, mPaint);
        canvas.saveLayer(0, 0, width, height, mLayerPaint, Canvas.ALL_SAVE_FLAG);
    }

    /**
     * draw之后调用
     */
    public void drawAfter(Canvas canvas) {
        canvas.restore();
        canvas.restore();
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LVRelativeLayout);
            mRoundCorner = typedArray.getDimensionPixelSize(R.styleable.LVRelativeLayout_lvCorner, NO_ROUND_CONFIG);
            boolean isCircleConfig = typedArray.getBoolean(R.styleable.LVRelativeLayout_lvCircle, false);
            if (isCircleConfig) {
                mRoundCorner = CIRCLE_CONFIG;
            }
            typedArray.recycle();
        }
    }
}
