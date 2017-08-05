package com.lam.gallery.factory;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.lam.gallery.manager.GalleryApplication;
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

/**
 * 加载图片的抽象方法
 */
abstract class BaseChain {
    private static final String TAG = "BaseChain";

    private BaseChain nextLink;

    private static Handler mHandler;

    static Handler getHandler(){
        if(mHandler == null) {
            synchronized (Handler.class){
                mHandler = new Handler(GalleryApplication.getContext().getMainLooper());
            }
        }
        return mHandler;
    }

    BaseChain getNextLink() {
        return nextLink;
    }

    void setNextLink(BaseChain nextLink) {
        this.nextLink = nextLink;
    }

    public abstract void loadBitmapToView(int type, View view, Object... params);
}

/**
 * 加载缓存中的图片
 * 如果在缓存中拿到对应图片，则加载到view中
 * 否则，交给下一步来加载图片
 */
class LoadLruCacheBitmapChain extends BaseChain {
    @Override
    public void loadBitmapToView(int type, View view, Object... params) {
        String lruCacheKey = (String)params[0];
        if(type == LOAD_ORIGIN) {
            lruCacheKey = "origin" + params[0];
        } else if(type == LOAD_PROCESS) {
            lruCacheKey = "Process" + params[0];
        }
        LoadGalleryBitmap loadGalleryBitmap = GalleryBitmap.getBitmap(LOAD_LRU_CACHE);
        if(loadGalleryBitmap != null) {
            Bitmap bitmap = loadGalleryBitmap.loadBitmap(lruCacheKey);
            if(bitmap != null) {
                ((ImageView)view).setImageBitmap(bitmap);
            } else {
                if(getNextLink() != null) {
                    getNextLink().loadBitmapToView(type, view, params[0], params[1], params[2]);
                }
            }
        }
    }
}

/**
 * 加载并显示缩略图
 */
class LoadThumbnailBitmapChain extends BaseChain {
    @Override
    public void loadBitmapToView(int type, final View view, final Object... params) {
        ((ImageView)view).setImageResource(R.drawable.loading);
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public void doTask() {
                LoadGalleryBitmap loadGalleryBitmap = GalleryBitmap.getBitmap(LOAD_THUMBNAIL);
                if(loadGalleryBitmap != null) {
                    final Bitmap bitmap = loadGalleryBitmap.loadBitmap((int)params[1]);
                    LruCacheManager.addBitmapToCache((String)params[0], bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null && params[2] == null) {
                                ((ImageView)view).setImageBitmap(bitmap);
                            } else if(bitmap != null && (int)view.getTag() == (int)params[2]) {
                                ((ImageView)view).setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }
        });
    }
}

/**
 * 加载并展示压缩的图片
 */
class LoadProcessBitmapChain extends BaseChain {

    @Override
    public void loadBitmapToView(int type, final View view, final Object... params) {
        final String lruCacheKey = "Process" + params[0];
        ((ImageView)view).setImageResource(R.drawable.loading);
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public void doTask() {
                LoadGalleryBitmap loadGalleryBitmap = GalleryBitmap.getBitmap(LOAD_PROCESS);
                if(loadGalleryBitmap != null) {
                    final Bitmap bitmap = loadGalleryBitmap.loadBitmap((String)params[0]);
                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null && params[2] == null) {
                                ((ImageView)view).setImageBitmap(bitmap);
                            } else if(bitmap != null && (int)view.getTag() == (int)params[2]) {
                                ((ImageView)view).setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }
        });
    }
}

/**
 * 加载并展示原图
 */
class LoadOriginBitmapChain extends BaseChain {

    @Override
    public void loadBitmapToView(int type, final View view, final Object... params) {
        final String lruCacheKey = "origin" + params[0];
        ((ImageView)view).setImageResource(R.drawable.loading);
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public void doTask() {
                LoadGalleryBitmap loadGalleryBitmap = GalleryBitmap.getBitmap(LOAD_ORIGIN);
                if(loadGalleryBitmap != null) {
                    final Bitmap bitmap = loadGalleryBitmap.loadBitmap((String)params[0]);
                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null && params[2] == null) {
                                ((ImageView)view).setImageBitmap(bitmap);
                            } else if(bitmap != null && (int)view.getTag() == (int)params[2]) {
                                ((ImageView)view).setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }
        });
    }
}


