package com.lam.gallery.engine.impl;

import android.util.Log;
import android.widget.ImageView;

import com.lam.gallery.engine.ImageEngine;
import com.lam.gallery.internal.imageGather.ImageGather;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2017/8/9.
 */

public class ImageGatherEngine implements ImageEngine {
    private static final String TAG = "ImageGatherEngine";

    @Override
    public void loadThumbnail(WeakReference<ImageView> imageViewWeakReference, Object tag, Object params) {
        Log.d(TAG, "loadThumbnail: ");
        ImageGather.with()
                .into(imageViewWeakReference, params, tag);
    }

    @Override
    public void loadProcessImage(WeakReference<ImageView> imageViewWeakReference, int resizeX, int resizeY, Object tag, Object path, Object id) {
        ImageGather.with()
                .into(imageViewWeakReference, path, tag);
    }

    @Override
    public void loadImage(WeakReference<ImageView> imageViewWeakReference, Object tag, Object params) {

    }
}

