package com.lam.gallery;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;

import com.lam.gallery.ui.GalleryActivity;
import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.engine.ImageEngine;

/**
 * Created by lenovo on 2017/8/7.
 */

public final class ConfigCreator {
    private static final String TAG = "ConfigCreator";
    private final Gallery mGallery;
    private final ConfigSpec mConfigSpec;

    ConfigCreator(Gallery gallery) {
        mGallery = gallery;
        mConfigSpec = ConfigSpec.getDefaultInstance();

    }

    public ConfigCreator maxSelected(int maxSelected) {
        mConfigSpec.mMaxSelected = maxSelected;
        return this;
    }

    public ConfigCreator themeId(int id) {
        mConfigSpec.mThemeId = id;
        return this;
    }

    public ConfigCreator processReqWidth(float processReqWidth) {
        mConfigSpec.mProcessReqWidth = processReqWidth;
        return this;
    }

    public ConfigCreator processReqHeight(float processReqHeight) {
        mConfigSpec.mProcessReqHeight = processReqHeight;
        return this;
    }

    public ConfigCreator semaphoreSubmitSize(int semaphoreSubmitSize) {
        mConfigSpec.mSemaphoreSubmitSize = semaphoreSubmitSize;
        return this;
    }

    public ConfigCreator imageEngine(ImageEngine imageEngine) {
        mConfigSpec.mImageEngine = imageEngine;
        return this;
    }

    public ConfigCreator isCache(boolean isCache) {
        mConfigSpec.mCache = isCache;
        return this;
    }

    public ConfigCreator reSizeX(int reSizeX) {
        mConfigSpec.mResizeX = reSizeX;
        return this;
    }

    public ConfigCreator reSizeY(int reSizeY) {
        mConfigSpec.mResizeY = reSizeY;
        return this;
    }

    public ConfigCreator maxBitmapSize(int size) {
        mConfigSpec.mMaxBitmapSize = size * mConfigSpec.K * mConfigSpec.K;
        return this;
    }

    public void forResult(int requestCode) {
        Activity activity = mGallery.getActivity();
        if(activity == null) {
            return;
        }
        Intent intent = new Intent(activity, GalleryActivity.class);

        Fragment fragment = mGallery.getFragment();
        if(fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            Log.d(TAG, "forResult: ");
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
