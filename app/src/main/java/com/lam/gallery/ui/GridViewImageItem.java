package com.lam.gallery.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lam.gallery.R;


public class GridViewImageItem extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "GridViewImageItem";

    private int mSelectLength;
    private int mViewLength;
    private boolean isMove;
    private static OnIntentToPreviewListener sOnIntentToPreviewListener;

    public static void setOnIntentToPreviewListener(OnIntentToPreviewListener onIntentToPreviewListener) {
        sOnIntentToPreviewListener = onIntentToPreviewListener;
    }

    public GridViewImageItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mSelectLength =  BitmapFactory.decodeResource(getResources(), R.drawable.select_green_16).getHeight() + 6;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        mViewLength = getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ");
                isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:
                if(x < (mViewLength - mSelectLength) && y > mSelectLength && ! isMove) {
                    if(sOnIntentToPreviewListener != null) {
                        sOnIntentToPreviewListener.onClickToIntent(this);
                    }
                }
                isMove = false;
        }
        return true;
    }

    public interface OnIntentToPreviewListener {
        void onClickToIntent(View v);
    }

}
