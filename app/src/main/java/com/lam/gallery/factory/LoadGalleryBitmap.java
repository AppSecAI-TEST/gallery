package com.lam.gallery.factory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.manager.MediaManager;

import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_LRU_CACHE;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_ORIGIN;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_PROCESS;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_THUMBNAIL;

/**
 * 加载图片的接口
 */
public interface LoadGalleryBitmap {

    Bitmap loadBitmap(Object... params);
}

class GalleryBitmap {

    public static LoadGalleryBitmap getBitmap(int loadType) {
        if(loadType == LOAD_LRU_CACHE) {
            return new LruCacheBitmapLoad();
        } else if(loadType == LOAD_ORIGIN) {
            return new OriginBitmapLoad();
        } else if(loadType == LOAD_THUMBNAIL) {
            return new ThumbnailBitmapLoad();
        } else if(loadType == LOAD_PROCESS) {
            return new ProcessBitmapLoad();
        }
        return null;
    }
}

class LruCacheBitmapLoad implements LoadGalleryBitmap {

    @Override
    public Bitmap loadBitmap(Object... param) {
        return LruCacheManager.getBitmapFromCache((String)param[0]);
    }
}

class OriginBitmapLoad implements LoadGalleryBitmap {

    @Override
    public Bitmap loadBitmap(Object... param) {
        return android.graphics.BitmapFactory.decodeFile((String)param[0]);
    }
}

class ThumbnailBitmapLoad implements LoadGalleryBitmap {

    @Override
    public Bitmap loadBitmap(Object... param) {
        return MediaManager.getThumbnail((int) param[0]);
    }
}

class ProcessBitmapLoad implements LoadGalleryBitmap {

    @Override
    public Bitmap loadBitmap(Object... param) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile((String)param[0], newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        float reqWidth = 768f;
        float reqHeight = 1280f;
        int inSampleSize = 1;
        if (width > height && width > reqWidth) {
            inSampleSize = Math.round(width /reqWidth);
        } else if (width < height && height >reqHeight) {
            inSampleSize = Math.round(height /reqHeight);
        }
        if (inSampleSize <= 0)
            inSampleSize = 1;
        newOpts.inSampleSize = inSampleSize;//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile((String)param[0], newOpts);
    }
}
