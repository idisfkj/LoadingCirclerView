package com.idisfkj.loadingcirclerview.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.idisfkj.loadingcirclerview.R;

import com.idisfkj.loadingcirclerview.common.utils.DisplayUtils;

/**
 * Created by idisfkj on 17/3/12.
 * Email : idisfkj@gmail.com.
 */

public class LoadingCirclerView extends ImageView{

    private final int DEFAULT_SIZE = 80;                   //控件默认大小
    private final int CIRCLE_WIDTH = 3;                    //圆的默认宽度
    private final int DRAW_INTERVAL_TIME = 100;            //圆弧默认初始定时更新时间
    private final int CIRCLE_PERCENT_INCREMENTAL = 25;     //圆弧默认的百分比增量
    private final int DRAW_INTERVAL_TIME_DECREMENT = 25;   //圆弧默认的定时更新的减量
    private final int CIRCLE_FIXED_TIME = 1200;            //默认固定圆形的显示时间

    private int mCirclePercent;                            //圆弧的百分比
    private Paint mPaint;
    private RectF mRectF;
    private int mWidth;
    private int mHeight;
    private int mCircleWidth = CIRCLE_WIDTH;
    private int mIntervalTime = DRAW_INTERVAL_TIME;        //View的更新时间
    private int mCirclerPercentIncremental = CIRCLE_PERCENT_INCREMENTAL;
    private int mDrawIntervalTimeTDecrement = DRAW_INTERVAL_TIME_DECREMENT;

    private boolean isStart;
    private boolean isRestart;
    private PeriodFinishListener listener;

    public LoadingCirclerView(Context context) {
        super(context);
        init();
    }

    public LoadingCirclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingCirclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = width;
        } else {
            mWidth = DisplayUtils.dip2px(DEFAULT_SIZE);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        } else {
            mHeight = DisplayUtils.dip2px(DEFAULT_SIZE);
        }
        //百分比弧矩形
        mRectF = new RectF(mCircleWidth, mCircleWidth, mWidth - mCircleWidth, mHeight - mCircleWidth);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStart) {
            if (mCirclePercent <= 100) { //开始画圆弧
                isRestart = true;
                canvas.drawArc(mRectF, 270, -mCirclePercent / 100.f * 360, false, mPaint);
                mCirclePercent += mCirclerPercentIncremental;
            } else if (isRestart) { //是否重新开始画圆弧
                //周期性完成的回调
                if (listener != null) {
                    listener.onFinish();
                }
                isRestart = false;
                //重置数据
                setReset();

                //绘制完整的圆
                canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - mCircleWidth, mPaint);
                //显示完整圆的时间
                postInvalidateDelayed(CIRCLE_FIXED_TIME);
            }
            if (isStart && isRestart) {
                //定时更新ui
                postInvalidateDelayed(mIntervalTime);
                mIntervalTime -= mDrawIntervalTimeTDecrement;
            }
        } else {
            //绘制完整的圆
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - mCircleWidth, mPaint);
        }
        super.onDraw(canvas);
    }

    public void startAnimation() {
        isStart = true;
    }

    public void stopAnimation() {
        isStart = false;
    }

    public boolean hasStartAnimation() {
        return isStart;
    }

    public void setReset() {
        mCirclePercent = 0;
        mIntervalTime = DRAW_INTERVAL_TIME;
    }

    /**
     * 周期性回调监听
     */
    public interface PeriodFinishListener {
        void onFinish();
    }

    public void setPeriodFinishListener(PeriodFinishListener listener) {
        this.listener = listener;
    }

    /**
     * 圆弧宽度
     *
     * @param circleWidth
     */
    public void setCircleWidth(int circleWidth) {
        mCircleWidth = circleWidth;
    }

    /**
     * ui更新的初始时间
     *
     * @param intervalTime
     */
    public void setIntervalTime(int intervalTime) {
        mIntervalTime = intervalTime;
    }

    /**
     * 圆弧的百分比增量
     *
     * @param circlerPercentIncremental
     */
    public void setCirclerPercentIncremental(int circlerPercentIncremental) {
        mCirclerPercentIncremental = circlerPercentIncremental;
    }

    /**
     * ui更新时间的递减量
     *
     * @param drawIntervalTimeTDecrement
     */
    public void setDrawIntervalTimeTDecrement(int drawIntervalTimeTDecrement) {
        mDrawIntervalTimeTDecrement = drawIntervalTimeTDecrement;
    }

}
