package com.lam.gallery.factory;

import android.view.View;


/**
 * Created by lenovo on 2017/7/28.
 */

public class GalleryBitmapFactory {
    private static final String TAG = "GalleryBitmapFactory";

    public static final int LOAD_LRU_CACHE = 0;
    public static final int LOAD_ORIGIN = 1;
    public static final int LOAD_THUMBNAIL = 2;
    public static final int LOAD_PROCESS = 3;

    private static LoadLruCacheBitmapLink sLoadLruCacheBitmapLink = new LoadLruCacheBitmapLink();
    private static LoadOriginBitmap sLoadOriginBitmap = new LoadOriginBitmap();
    private static LoadProcessBitmap sLoadProcessBitmap = new LoadProcessBitmap();
    private static LoadThumbnailBitmapLink sLoadThumbnailBitmapLink = new LoadThumbnailBitmapLink();

    public static void loadThumbnailWithTag(String path, int thumbnailId, View imageView, Object tag) {
        sLoadLruCacheBitmapLink.setNextLink(sLoadThumbnailBitmapLink);
        sLoadLruCacheBitmapLink.loadBitmapToView(LOAD_THUMBNAIL, path, thumbnailId, imageView, tag);
    }

    public static void loadOriginBitmapWithTag(String path, int thumbnailId, View imageView, Object tag) {
        sLoadLruCacheBitmapLink.setNextLink(sLoadOriginBitmap);
        sLoadLruCacheBitmapLink.loadBitmapToView(LOAD_ORIGIN, path, thumbnailId, imageView, tag);
    }

    public static void loadProcessBitmapWithTag(String path, int thumbnailId, View imageView, Object tag) {
        sLoadLruCacheBitmapLink.setNextLink(sLoadProcessBitmap);
        sLoadLruCacheBitmapLink.loadBitmapToView(LOAD_PROCESS, path, thumbnailId, imageView, tag);
    }
}
