package com.lam.gallery.engine.imageGather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lam.gallery.R;
import com.lam.gallery.manager.GalleryApplication;



public class EmptyBuilder extends ImageBuilder {
    @Override
    public Bitmap loadBitmap(Object... params) {
        return BitmapFactory.decodeResource(GalleryApplication.getContext().getResources(), R.drawable.loading);
    }

    @Override
    public boolean canHandleBuilder(Object... params) {
        return true;
    }
}