package com.peng.customwidget.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.peng.customwidget.R;

/**
 * create by Mr.Q on 2019/3/7.
 * 类介绍：
 *      1、可以随意设置大小的，用于显示 Gif 图的 View
 *      2、有暂停，播放功能
 */
public class GifViewAdaptSize extends View {
    private static final int DEFAULT_MOVIEW_DURATION = 1000;

    private int mMovieResourceId;
    private Movie mMovie;

    private long mMovieStart;
    private int mCurrentAnimationTime = 0;

    /**
     * Gif 帧绘制的起始位置，即左上角
     */
    private float mLeft;
    private float mTop;

    /**
     * 水平缩放因子
     */
    private float mWidthScale;


    /**
     * 垂直缩放因子
     */
    private float mHeightScale;


    /**
     * GIF 帧的宽高
     */
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;

    private volatile boolean mPaused = false;
    private boolean mVisible = true;
    private int measureModeWidth;
    private int measureWidth;
    private int measureModeHeight;
    private int measureHeight;
    private int movieWidth;
    private int movieHeight;

    public GifViewAdaptSize(Context context) {
        this(context, null);
    }

    public GifViewAdaptSize(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CustomTheme_gifMovieViewStyle);
    }

    public GifViewAdaptSize(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setViewAttributes(context, attrs, defStyle);
    }

    @SuppressLint("NewApi")
    private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {

        /**
         * 从 Android 3.0 开始，要绘制 Movie 到画布上，必须关闭硬件加速
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifMovieView, defStyle, R.style.Widget_GifMovieView);

        mMovieResourceId = array.getResourceId(R.styleable.GifMovieView_gif, -1);
        mPaused = array.getBoolean(R.styleable.GifMovieView_paused, false);

        array.recycle();

        if (mMovieResourceId != -1) {
            mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        }
    }

    public void setMovieResource(int movieResId) {
        this.mMovieResourceId = movieResId;
        mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        requestLayout();
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        requestLayout();
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;
        /**
         * 计算新movie 的起始时间，使得可以从同一帧开始
         */
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;
        }

        invalidate();
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mMovie != null) {
            movieWidth = mMovie.width();
            movieHeight = mMovie.height();
            /**
             * 计算水平缩放
             */
            measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                measureWidth = MeasureSpec.getSize(widthMeasureSpec);
                if (movieWidth > measureWidth) {
                    mWidthScale = (float) movieWidth / (float) measureWidth;
                } else {
                    mWidthScale = (float) measureWidth / (float) movieWidth;
                }
            }

            /**
             * 计算垂直缩放
             */
            measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                measureHeight = MeasureSpec.getSize(heightMeasureSpec);
                if (movieHeight > measureHeight) {
                    mHeightScale = (float) movieHeight / (float) measureHeight;
                } else {
                    mHeightScale = (float) measureHeight / (float) movieHeight;
                }
            }

            if (movieWidth > measureWidth) {
                mMeasuredMovieWidth = (int) (movieWidth / mWidthScale);
            } else {
                mMeasuredMovieWidth = (int) (movieWidth * mWidthScale);
            }
            if (movieHeight > measureHeight) {
                mMeasuredMovieHeight = (int) (movieHeight / mHeightScale);
            } else {
                mMeasuredMovieHeight = (int) (movieHeight * mHeightScale);
            }

            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);

        } else {
            /**
             * 没有 Movie 设置，只设置最小可用 size
             */
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        /**
         * 计算左上角，使得图像绘制在 View 中间
         */
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

        mVisible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    /**
     * 仅在View可见的时候才重绘
     */
    @SuppressLint("NewApi")
    private void invalidateView() {
        if (mVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }

    /**
     * 计算当前的动画时间
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int dur = mMovie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIEW_DURATION;
        }

        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * 绘制当前动画的 GIF 帧
     *
     * @param canvas
     */
    private void drawMovieFrame(Canvas canvas) {
        float widthScale = 0;
        float heightScale = 0;
        mMovie.setTime(mCurrentAnimationTime);
        canvas.save();

        if (movieWidth > measureWidth) {
            widthScale = 1 / mWidthScale;
        } else {
            widthScale = mWidthScale;
        }

        if (movieHeight > measureHeight) {
            heightScale = 1 / mHeightScale;
        } else {
            heightScale = mHeightScale;
        }

        canvas.scale(widthScale, heightScale);
        mMovie.draw(canvas, mLeft / widthScale, mTop / heightScale);
        canvas.restore();
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }
}


