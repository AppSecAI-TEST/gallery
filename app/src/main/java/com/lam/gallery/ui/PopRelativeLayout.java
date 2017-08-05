package com.lam.gallery.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.lam.gallery.R;

/**
 * Created by lenovo on 2017/8/3.
 */

public class PopRelativeLayout extends RelativeLayout implements Animator.AnimatorListener {

    private ValueAnimator mFileListShowAnimator;
    private ValueAnimator mFileBackShowAnimator;
    private ValueAnimator mFileListHideAnimator;
    private ValueAnimator mFileBackHideAnimator;
    private Animation mShowAlphanimation;
    private Animation mHideAlphaanimation;
    private int mDuration;
    private int LIST_MAX_HEIGHT;
    private int BACKGROUND_MAX_HEIGHT;
    private static boolean mIsVisible;

    public PopRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LIST_MAX_HEIGHT = getHeight() - 3 * 112;
        BACKGROUND_MAX_HEIGHT = getHeight() - LIST_MAX_HEIGHT;
        mDuration = 300;
        mIsVisible = false;
    }

    private void initAnimator() {
        mFileListShowAnimator = ValueAnimatorManager.viewAnimator(0, LIST_MAX_HEIGHT, mDuration, getChildAt(1));
        mFileBackShowAnimator = ValueAnimatorManager.viewAnimator(0, BACKGROUND_MAX_HEIGHT, mDuration, getChildAt(0));
        mShowAlphanimation = AnimationUtils.loadAnimation(getContext(), R.anim.from_alpha_to_translucent);
        mFileListShowAnimator.addListener(this);
        mFileBackShowAnimator.addListener(this);

        mFileListHideAnimator = ValueAnimatorManager.viewAnimator(getChildAt(1).getLayoutParams().height, 0, mDuration, getChildAt(1));
        mFileBackHideAnimator = ValueAnimatorManager.viewAnimator(getChildAt(0).getLayoutParams().height, 0, mDuration, getChildAt(0));
        mHideAlphaanimation = AnimationUtils.loadAnimation(getContext(), R.anim.from_translucent_to_alpha);
        mFileListHideAnimator.addListener(this);
        mFileBackHideAnimator.addListener(this);
    }

    public void runAnimator() {
        initAnimator();
        if(! mIsVisible) {   //变为可见
            getChildAt(0).startAnimation(mShowAlphanimation);
            mFileListShowAnimator.start();
            mFileBackShowAnimator.start();
        } else {
            getChildAt(0).startAnimation(mHideAlphaanimation);
            mFileListHideAnimator.start();
            mFileBackHideAnimator.start();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if(! mIsVisible) {
            for(int i = 0; i < getChildCount(); ++i) {
                getChildAt(i).setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mIsVisible = ! mIsVisible;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
