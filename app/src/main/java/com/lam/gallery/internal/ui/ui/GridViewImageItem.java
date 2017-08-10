package com.lam.gallery.internal.ui.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class GridViewImageItem extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "GridViewImageItem";

    private static OnIntentToPreviewListener sOnIntentToPreviewListener;
    private GestureDetector mGestureDetector;

    public void setOnIntentToPreviewListener(OnIntentToPreviewListener onIntentToPreviewListener) {
        sOnIntentToPreviewListener = onIntentToPreviewListener;
    }

    public GridViewImageItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(getContext(), new SimpleListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    public interface OnIntentToPreviewListener {
        void onClickToIntent(View v);
    }

    private class SimpleListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(sOnIntentToPreviewListener != null) {
                sOnIntentToPreviewListener.onClickToIntent(GridViewImageItem.this);
            }
            return true;
        }
    }

}
