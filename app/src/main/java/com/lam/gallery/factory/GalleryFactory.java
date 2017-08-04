package com.lam.gallery.factory;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lam.gallery.manager.LruCacheManager;

/**
 * Created by lenovo on 2017/8/4.
 */

public class GalleryFactory {
    public static final int LOAD_LRU_CACHE = 0;
    public static final int LOAD_ORIGIN = 1;
    public static final int LOAD_THUMBNAIL = 2;
    public static final int LOAD_PROCESS = 3;

    public static GalleryBitmap getBitmap(int loadType) {
        if(loadType == LOAD_LRU_CACHE) {
            return new LoadBitmap.LruCacheBitmap();
        } else if(loadType == LOAD_ORIGIN) {
            return new LoadBitmap.OriginBitmap();
        } else if(loadType == LOAD_THUMBNAIL) {
            return new LoadBitmap.ThumbnailBitmap();
        } else if(loadType == LOAD_PROCESS) {
            return new LoadBitmap.ProcessBitmap();
        }
        return null;
    }

    public static void cacheBitmap(String key, Bitmap bitmap) {
        LruCacheManager.addBitmapToCache(key, bitmap);
    }

    public static void setBitmapInView(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    public static void setBitmapWithTagInView(ImageView view, Bitmap bitmap, Object tag) {
        if(bitmap != null && (int)view.getTag() == (int)tag) {
            view.setImageBitmap(bitmap);
        }
    }

    public interface GalleryBitmap {
        Bitmap loadBitmap(Object... params);
    }
}
