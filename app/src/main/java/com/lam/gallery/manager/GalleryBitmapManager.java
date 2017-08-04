package com.lam.gallery.manager;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.lam.gallery.GalleryApplication;
import com.lam.gallery.R;
import com.lam.gallery.factory.GalleryFactory;
import com.lam.gallery.task.BitmapTaskDispatcher;


/**
 * Created by lenovo on 2017/7/28.
 */

public class GalleryBitmapManager {
    private static final String TAG = "GalleryBitmapManager";

    private static Handler mHandler;

    private static Handler getHandler(){
        if(mHandler == null) {
            synchronized (Handler.class){
                mHandler = new Handler(GalleryApplication.getContext().getMainLooper());
            }
        }
        return mHandler;
    }

    public static void loadThumbnailWithTag(final String path, final int thumbnailId, final ImageView imageView, final Object tag) {
        Bitmap bitmap = GalleryFactory.getBitmap(GalleryFactory.LOAD_LRU_CACHE).loadBitmap(path);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.loading);
            BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
                @Override
                public void doTask() {
                    final Bitmap bitmap = GalleryFactory.getBitmap(GalleryFactory.LOAD_THUMBNAIL).loadBitmap(thumbnailId);
                    LruCacheManager.addBitmapToCache(path, bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null && (int)imageView.getTag() == (int)tag) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void loadOriginBitmapWithTag(final String path, final ImageView imageView, final Object tag) {
        final String lruCacheKey = "origin" + path;
        Bitmap bitmap = GalleryFactory.getBitmap(GalleryFactory.LOAD_LRU_CACHE).loadBitmap(lruCacheKey);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.loading);
            BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
                @Override
                public void doTask() {
                    final Bitmap bitmap = GalleryFactory.getBitmap(GalleryFactory.LOAD_ORIGIN).loadBitmap(path);
                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null && (int)imageView.getTag() == (int)tag) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void loadProcessBitmapWithTag(final String path, final ImageView imageView, final Object tag, final int reqSize) {
        final String lruCacheKey = reqSize + reqSize + path;
        Bitmap bitmap = GalleryFactory.getBitmap(GalleryFactory.LOAD_LRU_CACHE).loadBitmap(lruCacheKey);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.loading);
            BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
                @Override
                public void doTask() {
                    final Bitmap bitmap = GalleryFactory.getBitmap(GalleryFactory.LOAD_PROCESS).loadBitmap(path, reqSize, reqSize);
                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null && (int)imageView.getTag() == (int)tag) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

}
