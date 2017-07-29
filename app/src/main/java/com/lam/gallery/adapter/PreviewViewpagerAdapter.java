package com.lam.gallery.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by lenovo on 2017/7/29.
 */

public class PreviewViewpagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
