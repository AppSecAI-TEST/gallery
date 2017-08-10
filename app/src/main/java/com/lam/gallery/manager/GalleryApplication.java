package com.lam.gallery.manager;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class GalleryApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(mContext)
                .writeDebugLogs()
                .threadPoolSize(4)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(5 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static Context getContext() {
        return mContext;
    }
}
