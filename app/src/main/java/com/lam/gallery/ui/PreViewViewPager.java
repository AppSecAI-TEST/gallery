package com.lam.gallery.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;

/**
 * Created by lenovo on 2017/7/29.
 */

public class PreViewViewPager extends ViewPager {

    private boolean isMove;
    private static OnClickItemViewListener sOnClickItemViewListener;
    private GestureDetector mGestureDetector;

    public PreViewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        mGestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener());
    }

    public static void setOnClickItemViewListener(OnClickItemViewListener onClickItemViewListener) {
        sOnClickItemViewListener = onClickItemViewListener;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isMove = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                isMove = true;
//                break;
//            case MotionEvent.ACTION_UP:
//                if(sOnClickItemViewListener != null && ! isMove) {
//                    sOnClickItemViewListener.onClickItem(this);
//                }
//                isMove = false;
//        }
//        return false;
//    }

    public interface OnClickItemViewListener {
        void onClickItem(View v);
    }

//    private class SimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            if(sOnClickItemViewListener != null && ! isMove) {
//                sOnClickItemViewListener.onClickItem(PreViewViewPager.this);
//            }
//            return true;
//        }
//    }
}
