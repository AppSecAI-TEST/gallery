package com.lam.gallery.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

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

    public static final int TRANSLATION = 1;
    public static final int SCALE = 2;

    private int mScreenHeight;
    private int mScreenWidth;
    private PointF mLeftAndTopPoint;
    private PointF mRightAndBottomPoint;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private Matrix mMatrix;
    private PointF mCurrentPoint;
    private PointF mMiddlePoint;
    private boolean isEnlarged;
    private boolean isScale;
    private float maxScale;
    private int MODE = 0;
    private float mStartDis;
    private float mEndDis;
    private GestureDetector mGestureDetector;


    public PreViewImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLeftAndTopPoint = new PointF();
        mRightAndBottomPoint = new PointF();
        mCurrentPoint = new PointF();
        mMiddlePoint = new PointF();
        mMatrix = new Matrix();
        mGestureDetector = new GestureDetector(getContext(), new SimpleListener());
    }

    private static final String TAG = "PreViewImageView";

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mBitmapWidth = getDrawable().getIntrinsicWidth();
        mBitmapHeight = getDrawable().getIntrinsicHeight();
        mScreenHeight = getHeight();
        mScreenWidth = getWidth();
        putCenter(1.0f * mScreenWidth / mBitmapWidth);
        super.onLayout(changed, left, top, right, bottom);
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

    //边缘问题
    private void adjustBounds() {
        if(mLeftAndTopPoint.x > 0) { //当左边缘碰边
            Log.d(TAG, "adjustBounds: 1  " + mLeftAndTopPoint.x);
            if((mRightAndBottomPoint.y - mLeftAndTopPoint.y) <= (3.0f / 2.0f) * mScreenHeight) {
                float scale = getScaleSize();
                if(scale < 1.0f * mScreenWidth / mBitmapWidth)
                    scale = 1.0f * mScreenWidth / mBitmapWidth;
                initData(scale);
                float dy = mScreenHeight / 2 - mRightAndBottomPoint.y / 2;
                translationImage(0, dy);
            }
        } else if(mRightAndBottomPoint.x < mScreenWidth) {
            Log.d(TAG, "adjustBounds: 2");
            float scale = getScaleSize();
            if(scale < 1.0f * mScreenWidth / mBitmapWidth)
                scale = 1.0f * mScreenWidth / mBitmapWidth;
            initData(scale);
            float dx = mScreenWidth - mRightAndBottomPoint.x;
            float dy = mScreenHeight / 2 - mRightAndBottomPoint.y / 2;
            translationImage(dx, dy);
        } else if (mRightAndBottomPoint.y > (6.0f / 5.0f) * mScreenHeight && !isScale) {
            Log.d(TAG, "adjustBounds: 3");
            putCenter(1.0f * mScreenHeight / mBitmapHeight);
            isEnlarged = false;
            isScale = false;
        } else if (mLeftAndTopPoint.y < -(1.0f / 5.0f) * mScreenHeight && !isScale) {
            Log.d(TAG, "adjustBounds: 4");
            putCenter(1.0f * mScreenHeight / mBitmapHeight);
            isEnlarged = false;
            isScale = false;
        }
        if(mBitmapWidth != 0 && getScaleSize() < mScreenWidth / mBitmapWidth) {
            Log.d(TAG, "adjustBounds: 5");
            putCenter(1.0f * mScreenWidth / mBitmapWidth);
            isEnlarged = false;
            isScale = false;
        } else if(getScaleSize() > (3.0f / 2.0f) * mScreenHeight / mBitmapHeight) {
            Log.d(TAG, "adjustBounds: 6");
            putCenter((3.0f / 2.0f) * mScreenHeight / mBitmapHeight);
        }
        if(mLeftAndTopPoint.y > 0 && isScale) {
            Log.d(TAG, "adjustBounds: 7" + getScaleSize());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        mGestureDetector.onTouchEvent(event);
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                getParent().requestDisallowInterceptTouchEvent(true);
//                MODE = TRANSLATION;
//                mCurrentPoint.set(event.getX(), event.getY());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(MODE == TRANSLATION && isEnlarged) {
//                    float dx = event.getX() - mCurrentPoint.x;
//                    float dy = event.getY() - mCurrentPoint.y;
//                    translationImage(dx / getScaleSize(), dy / getScaleSize());
//                } else if(MODE == SCALE) {
//                    mEndDis = distance(event);
//                    if(mEndDis > 50f) {
//                        float scale = mEndDis / mStartDis;
//                        scaleImage(scale / getScaleSize(), mMiddlePoint.x, mMiddlePoint.y);
//                        isScale = true;
//                        isEnlarged = true;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                MODE = 0;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                MODE = SCALE;
//                mStartDis = distance(event);
//                Log.d(TAG, "onTouchEvent:mStartDis =  " + mStartDis);
//                if(mStartDis > 50f) {
//                    mMiddlePoint = middle(event);
//                }
//                break;
//        }
//        adjustBounds();
//        if(mLeftAndTopPoint.x >= 0 || mRightAndBottomPoint.x <= mScreenWidth) {
//            getParent().requestDisallowInterceptTouchEvent(false);
//        }
        return true;
    }

    //获得当前缩放比例
    private float getScaleSize() {
        float[] scaleSize = new float[9];
        mMatrix.getValues(scaleSize);
        return scaleSize[Matrix.MSCALE_X];

    }

    //用于计算两个触点之间的距离
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        //使用勾股定理返回两点间距离
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    //用于计算两触点的中间点
    private PointF middle(MotionEvent event) {
        float middleX = (event.getX(1) + event.getX(0)) / 2;
        float middleY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(middleX, middleY);
    }

    private class SimpleListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap: ");
            if(! isEnlarged){ //双击放大
                putCenter(1.0f * mScreenHeight / mBitmapHeight);
                isEnlarged = !isEnlarged;
            } else {
                putCenter(1.0f * mScreenWidth / mBitmapWidth);
                isEnlarged = !isEnlarged;
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(sOnClickItemViewListener != null) {
                    sOnClickItemViewListener.onClickItem(PreViewImageView.this);
                }
            return true;
        }
    }
}
