package com.lam.gallery.internal.imageGather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lam.gallery.R;
import com.lam.gallery.internal.GalleryApplication;


public abstract class ImageBuilder {
    private ImageBuilder mNextImageBuilder;

    public final Bitmap builder(Object... params) {
        Bitmap bitmap = loadBitmap(params);
        if(bitmap != null) {
            return bitmap;
        } else {
            if(mNextImageBuilder != null) {
                return this.mNextImageBuilder.builder(params);
            } else {
                return BitmapFactory.decodeResource(GalleryApplication.getContext().getResources(), R.drawable.loading);
            }
        }
    }

    public void setNextBuilder(ImageBuilder nextImageBuilder) {
        this.mNextImageBuilder = nextImageBuilder;
    }

    public abstract Bitmap loadBitmap(Object... params);

    public abstract boolean canHandleBuilder(Object... params);
}
