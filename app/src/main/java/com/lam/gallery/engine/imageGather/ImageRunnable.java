package com.lam.gallery.engine.imageGather;

import android.graphics.Bitmap;

import com.lam.gallery.task.BitmapTaskDispatcher;

/**
 * Created by lenovo on 2017/8/9.
 */

public class ImageRunnable extends BitmapTaskDispatcher.TaskRunnable {
    private Bitmap mBitmap;
    private ImageBuilder mImageBuilder;
    private Object[] params;

    public ImageRunnable(ImageBuilder imageBuilder, Object[] params) {
        mImageBuilder = imageBuilder;
        this.params = params;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public Object doTask() {
        mBitmap = load();
        return mBitmap;
    }

    Bitmap load() {
        return mImageBuilder.loadBitmap(params);
    }
}
