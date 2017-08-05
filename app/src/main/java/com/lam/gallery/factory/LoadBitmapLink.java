package com.lam.gallery.factory;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lam.gallery.GalleryApplication;
import com.lam.gallery.R;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.task.BitmapTaskDispatcher;

import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_LRU_CACHE;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_ORIGIN;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_PROCESS;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_THUMBNAIL;

/**
 * Created by lenovo on 2017/8/5.
 */

abstract class BaseLink {
    private static final String TAG = "BaseLink";

    private BaseLink nextLink;

    private static Handler mHandler;

    static Handler getHandler(){
        if(mHandler == null) {
            synchronized (Handler.class){
                mHandler = new Handler(GalleryApplication.getContext().getMainLooper());
            }
        }
        return mHandler;
    }

    public BaseLink getNextLink() {
        return nextLink;
    }

    public void setNextLink(BaseLink nextLink) {
        this.nextLink = nextLink;
    }

    public abstract void loadBitmapToView(int type, String path, int thumbnailId, View view, Object tag);
}

class LoadLruCacheBitmapLink extends BaseLink {
    private static final String TAG = "LoadLruCacheBitmapLink";
    @Override
    public void loadBitmapToView(int type, String path, int thumbnailId, View view, Object tag) {
        String lruCacheKey = path;
        if(type == LOAD_ORIGIN) {
            lruCacheKey = "origin" + path;
        } else if(type == LOAD_PROCESS) {
            lruCacheKey = "Process" + path;
        }
        Bitmap bitmap = GalleryBitmap.getBitmap(LOAD_LRU_CACHE).loadBitmap(lruCacheKey);
        if(bitmap != null) {
            ((ImageView)view).setImageBitmap(bitmap);
        } else {
            if(getNextLink() != null) {
                getNextLink().loadBitmapToView(type, path, thumbnailId, view, tag);
            }
        }
    }
}

class LoadThumbnailBitmapLink extends BaseLink {
    @Override
    public void loadBitmapToView(int type, final String path, final int thumbnailId, final View view, final Object tag) {
        ((ImageView)view).setImageResource(R.drawable.loading);
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public void doTask() {
                final Bitmap bitmap = GalleryBitmap.getBitmap(LOAD_THUMBNAIL).loadBitmap(thumbnailId);
                LruCacheManager.addBitmapToCache(path, bitmap);
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(bitmap != null && (int)view.getTag() == (int)tag) {
                            ((ImageView)view).setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }
}

class LoadProcessBitmap extends BaseLink {
    private static final String TAG = "LoadProcessBitmap";
    @Override
    public void loadBitmapToView(int type, final String path, int thumbnailId, final View view, final Object tag) {
        Log.d(TAG, "loadBitmapToView: ");
        final String lruCacheKey = "Process" + path;
        ((ImageView)view).setImageResource(R.drawable.loading);
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public void doTask() {
                final Bitmap bitmap = GalleryBitmap.getBitmap(LOAD_PROCESS).loadBitmap(path);
                Log.d(TAG, "doTask: " + bitmap);
                LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(bitmap != null && (int)view.getTag() == (int)tag) {
                            ((ImageView)view).setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }
}

class LoadOriginBitmap extends BaseLink {

    @Override
    public void loadBitmapToView(int type, final String path, int thumbnailId, final View view, final Object tag) {
        final String lruCacheKey = "origin" + path;
        ((ImageView)view).setImageResource(R.drawable.loading);
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public void doTask() {
                final Bitmap bitmap = GalleryBitmap.getBitmap(LOAD_ORIGIN).loadBitmap(path);
                LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(bitmap != null && (int)view.getTag() == (int)tag) {
                            ((ImageView)view).setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }
}


