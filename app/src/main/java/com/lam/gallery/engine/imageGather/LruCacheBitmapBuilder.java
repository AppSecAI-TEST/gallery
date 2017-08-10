package com.lam.gallery.engine.imageGather;

import android.graphics.Bitmap;

import com.lam.gallery.manager.LruCacheManager;


public class LruCacheBitmapBuilder extends ImageBuilder {

    @Override
    public Bitmap loadBitmap(Object... params) {
        return LruCacheManager.getBitmapFromCache(params[0] + "");
    }

    @Override
    public boolean canHandleBuilder(Object... params) {
        return LruCacheManager.getBitmapFromCache(params[0] + "") != null;
    }
}
