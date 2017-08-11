package com.lam.gallery.engine.impl;

import android.widget.ImageView;

import com.lam.gallery.R;
import com.lam.gallery.engine.ImageEngine;
import com.lam.gallery.internal.entity.ConfigSpec;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2017/8/10.
 */

public class ImageLoaderEngine implements ImageEngine {
    private static final String TAG = "ImageLoaderEngine";
    private DisplayImageOptions mOptions;

    public ImageLoaderEngine() {
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .cacheInMemory(ConfigSpec.getInstance().mCache)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnLoading(R.drawable.loading)
                .build();
    }

    @Override
    public void loadThumbnail(WeakReference<ImageView> imageViewWeakReference, Object tag, Object params) {
        ImageLoader.getInstance().displayImage(CONTENT_URI_HEAD + params, imageViewWeakReference.get(), mOptions);
    }

    @Override
    public void loadProcessImage(WeakReference<ImageView> imageViewWeakReference, int resizeX, int resizeY, Object tag, Object path, Object id) {
        ImageLoader.getInstance().displayImage(FILE_URI_HEAD + path, imageViewWeakReference.get(), mOptions);
    }

    @Override
    public void loadImage(WeakReference<ImageView> imageViewWeakReference, Object tag, Object params) {

    }
}
