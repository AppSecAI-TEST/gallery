package com.lam.gallery.factory;

import android.graphics.Bitmap;

import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.manager.MediaManager;

import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_LRU_CACHE;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_ORIGIN;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_PROCESS;
import static com.lam.gallery.factory.GalleryBitmapFactory.LOAD_THUMBNAIL;
import static com.lam.gallery.manager.MediaManager.getCompressMedia;

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
        return getCompressMedia((String)param[0]);
    }
}
