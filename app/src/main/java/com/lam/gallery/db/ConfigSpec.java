package com.lam.gallery.db;

import com.lam.gallery.engine.ImageEngine;
import com.lam.gallery.engine.impl.ImageGatherEngine;

/**
 * Created by lenovo on 2017/8/9.
 */

public final class ConfigSpec {
    private static final String TAG = "ConfigSpec";
    public int mMaxSelected;
    public int mThemeId;
    public float mProcessReqWidth;
    public float mProcessReqHeight;
    public int mSemaphoreSubmitSize;
    public ImageEngine mImageEngine;
    public boolean mCache;
    public int mResizeX;
    public int mResizeY;

    private ConfigSpec() {
    }

    public static ConfigSpec getInstance() {
        return InstanceHolder.CONFIG_SPEC;
    }

    public static ConfigSpec getDefaultInstance() {
        ConfigSpec configSpec = getInstance();
        configSpec.reset();
        return configSpec;
    }

    private void reset() {
        mMaxSelected = 9;
        mThemeId = 0;
        mProcessReqWidth = 1080f;
        mProcessReqHeight = 1920f;
        mSemaphoreSubmitSize = Runtime.getRuntime().availableProcessors();
        mImageEngine = new ImageGatherEngine();
        mCache = true;
        mResizeX = 320;
        mResizeY = 320;
    }

    private static final class InstanceHolder {
        public static final ConfigSpec CONFIG_SPEC = new ConfigSpec();
    }
}
