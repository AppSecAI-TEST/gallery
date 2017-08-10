package com.lam.gallery.engine.imageGather;

import android.graphics.Bitmap;
import android.util.Log;

import com.lam.gallery.manager.LruCacheManager;


public class LruCacheBitmapBuilder extends ImageBuilder {

    @Override
    public Bitmap loadBitmap(Object... params) {
        Log.d("ImageGather", "loadBitmap: LruCacheBitmapBuilder " + LruCacheManager.getBitmapFromCache(params[0] + ""));
        return LruCacheManager.getBitmapFromCache(params[0] + "");
    }

    @Override
    public boolean canHandleBuilder(Object... params) {
        Log.d("ImageGather", "loadBitmap: LruCacheBitmapBuilder " + LruCacheManager.getBitmapFromCache(params[0] + ""));
        return LruCacheManager.getBitmapFromCache(params[0] + "") != null;
    }
}
