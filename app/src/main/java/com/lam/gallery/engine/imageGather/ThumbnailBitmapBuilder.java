package com.lam.gallery.engine.imageGather;

import android.graphics.Bitmap;
import android.util.Log;

import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.manager.MediaManager;


public class ThumbnailBitmapBuilder extends ImageBuilder {
    @Override
    public Bitmap loadBitmap(Object... params) {
        Bitmap bitmap = MediaManager.getThumbnail((int) params[0]);
        LruCacheManager.addBitmapToCache(params[0] + "", bitmap);
        Log.d("ImageGather", "loadBitmap: ThumbnailBitmapBuilder" + bitmap);
        return bitmap;
    }

    @Override
    public boolean canHandleBuilder(Object... params) {
        return (params[0] instanceof Integer);
    }
}
