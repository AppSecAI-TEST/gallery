package com.lam.gallery.engine.impl;

import android.widget.ImageView;

import com.lam.gallery.R;
import com.lam.gallery.db.ConfigSpec;
import com.lam.gallery.engine.ImageEngine;
import com.lam.gallery.manager.GalleryApplication;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2017/8/9.
 */

public class PicassoEngine implements ImageEngine {
    private static final String TAG = "PicassoEngine";

    @Override
    public void loadThumbnail(WeakReference<ImageView> imageViewWeakReference, Object tag, Object id) {
        Picasso.with(GalleryApplication.getContext()).load(CONTENT_URI_HEAD + id).resize(ConfigSpec.getInstance().mResizeX, ConfigSpec.getInstance().mResizeY).centerCrop().placeholder(R.drawable.loading).into(imageViewWeakReference.get());
    }

    @Override
    public void loadProcessImage(WeakReference<ImageView> imageViewWeakReference, Object tag, Object path, Object id) {
        Picasso.with(GalleryApplication.getContext()).load(ImageDownloader.Scheme.FILE.wrap((String)path)).priority(Picasso.Priority.HIGH).placeholder(R.drawable.loading).into(imageViewWeakReference.get());
    }

    @Override
    public void loadImage(WeakReference<ImageView> imageViewWeakReference, Object tag, Object params) {

    }
}
