package com.lam.gallery.manager;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class GalleryApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(mContext)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .writeDebugLogs()
                .threadPoolSize(4)
                .memoryCache(new LruMemoryCache((int) Runtime.getRuntime().maxMemory() / 8))
                .memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 8)
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static Context getContext() {
        return mContext;
    }
}
