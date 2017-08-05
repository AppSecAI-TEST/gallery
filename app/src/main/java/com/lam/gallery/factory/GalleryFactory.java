package com.lam.gallery.factory;

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

}
