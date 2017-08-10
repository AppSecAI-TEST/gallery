package com.lam.gallery.internal.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by lenovo on 2017/7/31.
 */

public class PreViewImageView extends android.support.v7.widget.AppCompatImageView {
    private static OnClickItemViewListener sOnClickItemViewListener;

    public void setOnClickItemViewListener(OnClickItemViewListener onClickItemViewListener) {
        sOnClickItemViewListener = onClickItemViewListener;
    }

    public interface OnClickItemViewListener {
        void onClickItem(PreViewImageView v);
    }

    private int mScreenHeight;
    private int mScreenWidth;
    private PointF mLeftAndTopPoint;
    private PointF mRightAndBottomPoint;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private Matrix mMatrix;
    private boolean isEnlarged;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private boolean isDefault;
    private float mDefaultScaleSize;
    private float mMaxScaleSize;

    public PreViewImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLeftAndTopPoint = new PointF();
        mRightAndBottomPoint = new PointF();
        mMatrix = new Matrix();
        mGestureDetector = new GestureDetector(getContext(), new SimpleListener());
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    private static final String TAG = "PreViewImageView";

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if(drawable != null) {
            mBitmapWidth = drawable.getIntrinsicWidth();
            mBitmapHeight = drawable.getIntrinsicHeight();
            mScreenHeight = getHeight();
            mScreenWidth = getWidth();
            putCenter(1.0f * mScreenWidth / mBitmapWidth);
            isDefault = true;
            mDefaultScaleSize = getScaleSize();
            mMaxScaleSize = 2.0f * mScreenHeight / mBitmapHeight;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(getDrawable() != null) {
            mBitmapWidth = getDrawable().getIntrinsicWidth();
            mBitmapHeight = getDrawable().getIntrinsicHeight();
            mScreenHeight = getHeight();
            mScreenWidth = getWidth();
            putCenter(1.0f * mScreenWidth / mBitmapWidth);
            isDefault = true;
            mDefaultScaleSize = getScaleSize();
            mMaxScaleSize = 2.0f * mScreenHeight / mBitmapHeight;
        }
    }

    //初始化图片数据
    private void initData(float scaleSize) {
        mLeftAndTopPoint.set(0.0f, 0.0f);
        mRightAndBottomPoint.set(mBitmapWidth, mBitmapHeight);
        mMatrix.set(new Matrix());
        scaleImage(scaleSize, 0.0f, 0.0f);
    }

    //缩放图片
    private void scaleImage(float scaleSize, float fx, float fy) {
        mLeftAndTopPoint.set(fx - (fx - mLeftAndTopPoint.x) * scaleSize, fy - (fy - mLeftAndTopPoint.y) * scaleSize);
        mRightAndBottomPoint.set(fx + (mRightAndBottomPoint.x - fx) * scaleSize, fy + (mRightAndBottomPoint.y - fy) * scaleSize);
        mMatrix.postScale(scaleSize, scaleSize, fx, fy);
        setImageMatrix(mMatrix);
    }

    //将图片置于屏幕中心
    //主要用于双击事件、图片放缩过度时
    private void putCenter(float scaleSize) {
        Log.d(TAG, "putCenter: ");
        initData(scaleSize);
        float dx = mScreenWidth / 2 - mRightAndBottomPoint.x / 2;
        float dy = mScreenHeight / 2 - mRightAndBottomPoint.y / 2;
        translationImage(dx, dy);
    }

    //平移图片  dx  dy  为中心点的移动位移
    private void translationImage(float dx, float dy) {
        mLeftAndTopPoint.x += dx;
        mRightAndBottomPoint.x += dx;
        mLeftAndTopPoint.y += dy;
        mRightAndBottomPoint.y += dy;
        mMatrix.postTranslate(dx,dy);
        setImageMatrix(mMatrix);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        Log.d(TAG, "onTouchEvent: ");
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        adjustBound();
        if(mLeftAndTopPoint.x == 0 || mRightAndBottomPoint.x == mScreenWidth)
            getParent().requestDisallowInterceptTouchEvent(false);
        return true;
    }

    //获得当前缩放比例
    private float getScaleSize() {
        float[] scaleSize = new float[9];
        mMatrix.getValues(scaleSize);
        return scaleSize[Matrix.MSCALE_X];

    }

    //调整边缘问题
    private void adjustBound() {
        //当底部小于屏高的19/20并且顶部小于0，回弹底部
        if(mLeftAndTopPoint.y < 0 && mRightAndBottomPoint.y < (19.0f / 20.0f * mScreenHeight))
            translationImage(0, (float) mScreenHeight - mRightAndBottomPoint.y);
        //当顶部大于1/20屏高并且底部大于0，回弹顶部
        if(mLeftAndTopPoint.y > (1.0f / 20.0f * mScreenHeight) && mRightAndBottomPoint.y > mScreenHeight)
            translationImage(0, -mLeftAndTopPoint.y);
        //当左大于1/20屏宽并且右大于屏宽，回弹左边
        if(mLeftAndTopPoint.x > (1.0f / 20.0f * mScreenWidth) && mRightAndBottomPoint.x > mScreenWidth)
            translationImage(-mLeftAndTopPoint.x, 0);
        //当右小于19/20屏宽并且左小于0，回弹右边
        if(mLeftAndTopPoint.x < 0 && mRightAndBottomPoint.x < (19.0f / 20.0f * mScreenWidth))
            translationImage((float)mScreenWidth - mRightAndBottomPoint.x, 0);
        
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float scaleSize = getScaleSize();
            if (getDrawable() == null)
                return true;

            if ((scaleSize < mMaxScaleSize && scaleFactor > 1.0f)
                    || (scaleSize > mDefaultScaleSize && scaleFactor < 1.0f)) {
                if (scaleFactor * scaleSize < mDefaultScaleSize)
                    scaleFactor = mDefaultScaleSize / scaleSize;
                if (scaleFactor * scaleSize > mMaxScaleSize)
                    scaleFactor = mMaxScaleSize / scaleSize;
                scaleImage(scaleFactor, mScreenWidth / 2, mScreenHeight / 2);
                if(getScaleSize() > mDefaultScaleSize)
                    isDefault = false;
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }

    private class SimpleListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap: ");
            if(! isEnlarged){ //双击放大
                if((mRightAndBottomPoint.y - mLeftAndTopPoint.y) >= mScreenHeight) {  //如果本来图片宽度与屏幕相同
                    putCenter(1.5f * mScreenWidth / mBitmapWidth);
                } else {
                    putCenter(1.0f * mScreenHeight / mBitmapHeight);
                }
                isDefault = false;
                isEnlarged = !isEnlarged;
            } else {
                putCenter(1.0f * mScreenWidth / mBitmapWidth);
                isEnlarged = !isEnlarged;
                isDefault = true;
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(TAG, "onSingleTapConfirmed: ");
            if(sOnClickItemViewListener != null) {
                    sOnClickItemViewListener.onClickItem(PreViewImageView.this);
                }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(!isDefault) {
                if((mRightAndBottomPoint.y - mLeftAndTopPoint.y) < mScreenHeight)
                    translationImage(-distanceX, 0);
                else translationImage(-distanceX, -distanceY);
            }
            return true;
        }

    }
}
