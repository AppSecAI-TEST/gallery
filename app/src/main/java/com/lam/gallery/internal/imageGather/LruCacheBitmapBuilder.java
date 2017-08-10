package com.lam.gallery.internal.imageGather;

import android.graphics.Bitmap;

import com.lam.gallery.internal.manager.LruCacheManager;


class LruCacheBitmapBuilder extends ImageBuilder {

    @Override
    public Bitmap loadBitmap(Object params) {
        return LruCacheManager.getBitmapFromCache(params + "");
    }

    @Override
    public boolean canHandleBuilder(Object params) {
        return LruCacheManager.getBitmapFromCache(params + "") != null;
    }
}
