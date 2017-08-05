package com.lam.gallery.manager;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by lenovo on 2017/7/28.
 */

public class LruCacheManager {
    private static final String TAG = "LruCacheManager";
    private volatile static LruCache<String, Bitmap> mLruCache;

    private static LruCache<String, Bitmap> getLruCache() {
        if(mLruCache == null) {
            synchronized (LruCache.class) {
                if(mLruCache == null) {
                    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                    int cacheSize = maxMemory / 4;
                    mLruCache = new LruCache<String, Bitmap>(cacheSize) {
                        @Override
                        protected int sizeOf(String key, Bitmap value) {
                            return value.getByteCount() / 1024;
                        }
                    };
                }
            }
        }
        Log.d(TAG, "getLruCache: " + mLruCache);
        return mLruCache;
    }

    /**
     * 添加bitmap到缓存中
     * @param key 以bitmap的路径作为键值
     * @param bitmap
     */
    public static void addBitmapToCache(String key, Bitmap bitmap) {
        if(key == null || bitmap == null)
            return;
        getLruCache().put(key, bitmap);
    }

    /**
     * 从缓存中获得Bitmap
     * @param key 以bitmap的路径作为键值
     * @return 当缓存中没有该bitm时，返回 null
     */
    public static Bitmap getBitmapFromCache(String key) {
        return getLruCache().get(key);
    }

}
