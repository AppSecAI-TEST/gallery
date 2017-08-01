package com.lam.gallery.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.lam.gallery.adapter.PreviewViewpagerAdapter;

/**
 * Created by lenovo on 2017/7/31.
 */

public class PreViewImageView extends android.support.v7.widget.AppCompatImageView {
//
//    public static final int CHANGED = 0;
//    public static final int ORIGIN = 1;
//    int mMode = ORIGIN;
//
//    float mOriginBitmapWidth;
//    float mOriginBitmapHeight;
//    float mScreenWidth;
//    float mScreenHeight;
//    boolean isScaling;
//    boolean isBackToSmaller;
//    boolean isBackToBigger;
//    Matrix mCurrentMatrix;
//    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
//    GestureDetector gestureDetector = new GestureDetector(getContext(), new SimpleListener());
//
//    private PointF mLeftAndTopPoint;
//    private PointF mRightAndBottomPoint;
//    private PointF mCurrentPoint;
//
    private static OnClickItemViewListener sOnClickItemViewListener;

    public static void setOnClickItemViewListener(PreviewViewpagerAdapter onClickItemViewListener) {
        sOnClickItemViewListener = onClickItemViewListener;
    }
//
//    public PreViewImageView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        setOnTouchListener(new TouchListener());
//        mLeftAndTopPoint = new PointF();
//        mRightAndBottomPoint = new PointF();
//        mCurrentPoint = new PointF();
//
//        mCurrentMatrix = getImageMatrix();
//        setScaleType(ScaleType.MATRIX);
//    }
//
//    @Override
//    public void setImageBitmap(Bitmap bm) {
//
//        super.setImageBitmap(bm);
//
//        mOriginBitmapWidth = bm.getWidth();
//        mOriginBitmapHeight = bm.getHeight();
//
//    }
//
//    /**
//     * 进行图片平移，并实时更新图片所处位置的左上角和右下角的坐标定位
//     * @param dx  相对x轴平移的距离
//     * @param dy  相对Y轴平移的距离
//     */
//    public void translationImage(float dx, float dy){
//        mLeftAndTopPoint.x += dx;
//        mRightAndBottomPoint.x += dx;
//        mLeftAndTopPoint.y += dy;
//        mRightAndBottomPoint.y += dy;
//
//        mCurrentMatrix.postTranslate(dx,dy);
//
//    }
//
//    /**
//     * 进行图片的放缩，，并实时更新图片所处位置的左上角和右下角的坐标定位
//     * @param scale  缩放的倍数
//     * @param fx  缩放中心x
//     * @param fy  缩放中心y
//     */
//    public void scaleImage(float scale, float fx, float fy){
//        mLeftAndTopPoint.set(fx - (fx - mLeftAndTopPoint.x) * scale, fy - (fy - mLeftAndTopPoint.y) * scale);
//        mRightAndBottomPoint.set(fx + (mRightAndBottomPoint.x - fx) * scale, fy + (mRightAndBottomPoint.y - fy) * scale);
//        mCurrentMatrix.postScale(scale, scale, fx, fy);
//    }
//
//    public void setEnlargedMatrix(float fx){
//        centerImageBitmap();
//        float scaleSize = mScreenHeight / mOriginBitmapHeight * mOriginBitmapWidth / mScreenWidth;
//        scaleImage(scaleSize, fx, 0);
//        translationImage(0, -mLeftAndTopPoint.y);
//
//    }
//
//    public void centerImageBitmap(){
//        initCurrentMatrix();
//        float dx = mScreenWidth /2-mRightAndBottomPoint.x/2;
//        float dy = mScreenHeight /2-mRightAndBottomPoint.y/2;
//        translationImage(dx,dy);
//        setImageMatrix(mCurrentMatrix);
//
//    }
//
//    public void centerY(){
//        float dy = mScreenHeight /2+(mLeftAndTopPoint.y- mRightAndBottomPoint.y)/2-mLeftAndTopPoint.y;
//        translationImage(0,dy);
//
//    }
//
//    public void centerX(){
//        float dx = mScreenWidth /2+(mLeftAndTopPoint.x- mRightAndBottomPoint.x)/2-mLeftAndTopPoint.x;
//        translationImage(dx,0);
//    }
//
//    public void initCurrentMatrix(){
//        mScreenHeight = getHeight();
//        mScreenWidth = getWidth();
//        mLeftAndTopPoint.set(0.0f, 0.0f);
//        mRightAndBottomPoint.set(mOriginBitmapWidth, mOriginBitmapHeight);
//        mCurrentMatrix.set(new Matrix());
//        scaleImage(mScreenWidth / mOriginBitmapWidth, 0, 0);
//        setImageMatrix(mCurrentMatrix);
//
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        centerImageBitmap();
//    }
//
//    public boolean shouldMoveX(float dx){
//        return (!isScaling
//                &&!isBackToBigger
//                &&!isBackToSmaller
//                && (mLeftAndTopPoint.x<=0
//                &&mRightAndBottomPoint.x>= mScreenWidth
//                ||mLeftAndTopPoint.x>0&&dx<0
//                ||mRightAndBottomPoint.x< mScreenWidth &&dx>0));
//    }
//
//    public boolean shouldMoveY(float dy){
//        return (!isScaling
//                &&!isBackToBigger
//                &&!isBackToSmaller
//                &&(mLeftAndTopPoint.y<=-60
//                && mRightAndBottomPoint.y >= mScreenHeight +60
//                ||mLeftAndTopPoint.y<-60&&dy>0
//                || mRightAndBottomPoint.y > mScreenHeight +60&&dy<0));
//    }
//    public boolean shouldScale(float scaleFactor){
//
//        float scaleSize = getScaleSize();
//
//        if(mOriginBitmapWidth > mScreenWidth &&(
//                scaleSize >= 3 && scaleFactor > 1
//                        || scaleSize*scaleFactor< mScreenWidth / mOriginBitmapWidth / 2 && scaleFactor < 1)){
//            return false;
//        }
//
//        //当图片宽度小于控件
//        else if(mScreenWidth > mOriginBitmapWidth && (
//                scaleSize>=3&&scaleFactor>1
//                        ||scaleSize*scaleFactor< mOriginBitmapWidth / mScreenWidth /2&&scaleFactor<1)){
//            return false;
//        }
//        return true;
//    }
//
//    public float getScaleSize(){
//        float[] value = new float[9];
//        mCurrentMatrix.getValues(value);
//        return value[Matrix.MSCALE_X];
//    }
//
//    private static final String TAG = "ScalableImageView";
//
//    private class SimpleListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            if(mMode != ORIGIN){
//                centerImageBitmap();
//                mMode = ORIGIN;
//            }else {
//                setEnlargedMatrix(e.getX());
//                setImageMatrix(mCurrentMatrix);
//                mMode = CHANGED;
//            }
//            return true;
//        }
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            if(sOnClickItemViewListener != null) {
//                    sOnClickItemViewListener.onClickItem(PreViewImageView.this);
//                }
//            return true;
//        }
//    }
//
//    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
//
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//
//            if(isBackToBigger||isBackToSmaller)
//                return true;
//            if(shouldScale(detector.getScaleFactor())){
//                scaleImage(detector.getScaleFactor(),detector.getFocusX(),(mRightAndBottomPoint.y-mLeftAndTopPoint.y)/2+mLeftAndTopPoint.y);
//                centerY();
//                setImageMatrix(mCurrentMatrix);
//            }
//            return true;
//        }
//
//        @Override
//        public boolean onScaleBegin(ScaleGestureDetector detector) {
//            isScaling = true;
//            return true;
//        }
//
//        @Override
//        public void onScaleEnd(final ScaleGestureDetector detector) {
//            if(!isBackToSmaller && getScaleSize() >= 2) {
//                final ValueAnimator animator = ValueAnimator.ofInt(0, 1);
//                animator.setDuration(300);
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        if (getScaleSize() > 2) {
//                            scaleImage(0.95f, detector.getFocusX(), (mRightAndBottomPoint.y - mLeftAndTopPoint.y) / 2 + mLeftAndTopPoint.y);
//                            centerY();
//                            setImageMatrix(mCurrentMatrix);
//                        }
//                        else isBackToSmaller = false;
//                    }
//                });
//                animator.start();
//                isBackToSmaller = true;
//            }
//
//            //判断此时图片是否过度缩小，是则开始回弹动画
//            if(!isBackToBigger && getScaleSize() <= mScreenWidth / mOriginBitmapWidth) {
//                final ValueAnimator animator = ValueAnimator.ofInt(0, 1);
//                animator.setDuration(300);
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//
//                        if (getScaleSize() < mScreenWidth / mOriginBitmapWidth) {
//                            scaleImage(1.05f, detector.getFocusX(), (mRightAndBottomPoint.y - mLeftAndTopPoint.y) / 2 + mLeftAndTopPoint.y);
//                            centerY();
//                            centerX();
//                            setImageMatrix(mCurrentMatrix);
//                            Log.d(TAG, "onAnimationUpdate: ");
//                        }
//                        else isBackToBigger = false;
//                    }
//                });
//                animator.start();
//                isBackToBigger = true;
//            }
//            isScaling = false;
//        }
//    }
//
//    private class TouchListener implements OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//
//            scaleGestureDetector.onTouchEvent(event);
//            gestureDetector.onTouchEvent(event);
//
//            switch (event.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                    mCurrentPoint.y = event.getY();
//                    mCurrentPoint.x = event.getX();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    if (mRightAndBottomPoint.x-mLeftAndTopPoint.x > mScreenWidth ||mRightAndBottomPoint.y-mLeftAndTopPoint.y > mScreenWidth)
//                    {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                    float x = event.getX();
//                    float y = event.getY();
//
//                    float dx = x - mCurrentPoint.x;
//                    float dy = y - mCurrentPoint.y;
//
//                    if(shouldMoveX(dx)&&Math.abs(dx)<20) {
//                        translationImage(dx, 0);
//                        setImageMatrix(mCurrentMatrix);
//
//                    }else {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                    if(shouldMoveY(dy)&&Math.abs(dy)<20){
//                        translationImage(0, dy);
//                        setImageMatrix(mCurrentMatrix);
//                    }
//                    mCurrentPoint.x = x;
//                    mCurrentPoint.y = y;
//
//                    break;
//                default:
//                    break;
//            }
//            return true;
//        }
//    }
//
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
    public void setImageBitmap(Bitmap bm) {
        mBitmapWidth = bm.getWidth();
        mBitmapHeight = bm.getHeight();
        mScreenHeight = getHeight();
        mScreenWidth = getWidth();
        putCenter(1.0f * mScreenWidth / mBitmapWidth);
        maxScale = (6.0f / 5.0f * mScreenHeight / mBitmapHeight);
        super.setImageBitmap(bm);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
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
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                MODE = TRANSLATION;
                mCurrentPoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if(MODE == TRANSLATION && isEnlarged) {
                    float dx = event.getX() - mCurrentPoint.x;
                    float dy = event.getY() - mCurrentPoint.y;
                    translationImage(dx / getScaleSize(), dy / getScaleSize());
                } else if(MODE == SCALE) {
                    mEndDis = distance(event);
                    if(mEndDis > 50f) {
                        float scale = mEndDis / mStartDis;
                        scaleImage(scale / getScaleSize(), mMiddlePoint.x, mMiddlePoint.y);
                        isScale = true;
                        isEnlarged = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                MODE = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                MODE = SCALE;
                mStartDis = distance(event);
                Log.d(TAG, "onTouchEvent:mStartDis =  " + mStartDis);
                if(mStartDis > 50f) {
                    mMiddlePoint = middle(event);
                }
                break;
        }
        adjustBounds();
        if(mLeftAndTopPoint.x >= 0 || mRightAndBottomPoint.x <= mScreenWidth) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
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
            if(! isEnlarged){ //双击放大
                putCenter(1.0f * mScreenHeight / mBitmapHeight);
                isEnlarged = !isEnlarged;
            } else {
                putCenter(1.0f * mScreenWidth / mBitmapWidth);
                isEnlarged = !isEnlarged;
            }
            getParent().requestDisallowInterceptTouchEvent(false);
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
