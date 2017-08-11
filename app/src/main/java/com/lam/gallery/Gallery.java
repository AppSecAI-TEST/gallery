package com.lam.gallery;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.lam.gallery.ui.GalleryActivity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/7.
 */

public final class Gallery {

    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;

    private Gallery(Activity activity) {
        this(activity, null);
    }

    private Gallery(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private Gallery(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    public static Gallery from(Activity activity) {
        return new Gallery(activity);
    }

    public static Gallery from(Fragment fragment) {
        return new Gallery(fragment);
    }

    public static int[] obtainMediaIdsResult(Intent data) {
        return data.getIntArrayExtra(GalleryActivity.EXTRA_RESULT_SELECTION_ID);
    }

    public static List<String> obtainMediaPathResult(Intent data) {
        return data.getStringArrayListExtra(GalleryActivity.EXTRA_RESULT_SELECTION_PATH);
    }

    public static boolean obtainMediaIsOriginResult(Intent data) {
        return data.getBooleanExtra(GalleryActivity.EXTRA_RESULT_SELECTION_ORIGIN, false);
    }

    public ConfigCreator choose() {
        return new ConfigCreator(this);
    }

    @Nullable
    public Activity getActivity() {
        return mContext.get();
    }

    @Nullable
    public Fragment getFragment() {
        return mFragment == null ? null : mFragment.get();
    }
}
