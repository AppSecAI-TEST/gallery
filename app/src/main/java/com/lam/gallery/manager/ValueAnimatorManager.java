package com.lam.gallery.manager;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by lenovo on 2017/7/31.
 */

public class ValueAnimatorManager {

    public static ValueAnimator viewAnimator(int begin, int end, int duration, final View view) {
        ValueAnimator headerAnimator = ValueAnimator.ofInt(begin, end);
        headerAnimator.setDuration(duration);
        headerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.setLayoutParams(view.getLayoutParams());
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.setLayoutParams(view.getLayoutParams());
            }
        });
        return headerAnimator;
    }
}
