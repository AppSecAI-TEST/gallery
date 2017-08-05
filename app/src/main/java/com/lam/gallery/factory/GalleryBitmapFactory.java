package com.lam.gallery.factory;

import android.support.annotation.Nullable;
import android.view.View;

public class GalleryBitmapFactory {
    private static final String TAG = "GalleryBitmapFactory";

    public static final int LOAD_LRU_CACHE = 0;
    public static final int LOAD_ORIGIN = 1;
    public static final int LOAD_THUMBNAIL = 2;
    public static final int LOAD_PROCESS = 3;

    private static LoadLruCacheBitmapChain sLoadLruCacheBitmapLink = new LoadLruCacheBitmapChain();
    private static LoadOriginBitmapChain sLoadOriginBitmapChain = new LoadOriginBitmapChain();
    private static LoadProcessBitmapChain sLoadProcessBitmapChain = new LoadProcessBitmapChain();
    private static LoadThumbnailBitmapChain sLoadThumbnailBitmapLink = new LoadThumbnailBitmapChain();

    /**
     * 加载缩略图
     * @param path  图片路径
     * @param thumbnailId  获取缩略图的id
     * @param view  需要添加图片的view
     * @param tag  防止乱序的标记位
     */
    public static void loadThumbnailWithTag(String path, int thumbnailId, View view, @Nullable Object tag) {
        sLoadLruCacheBitmapLink.setNextLink(sLoadThumbnailBitmapLink);
        sLoadLruCacheBitmapLink.loadBitmapToView(LOAD_THUMBNAIL, view, path, thumbnailId, tag);
    }

    /**
     * 加载原图
     * @param path  图片路径
     * @param thumbnailId  获取缩略图的id
     * @param view  需要添加图片的view
     * @param tag  防止乱序的标记位
     */
    public static void loadOriginBitmapWithTag(String path, int thumbnailId, View view, @Nullable Object tag) {
        sLoadLruCacheBitmapLink.setNextLink(sLoadOriginBitmapChain);
        sLoadLruCacheBitmapLink.loadBitmapToView(LOAD_ORIGIN, view, path, thumbnailId, tag);
    }

    /**
     * 加载压缩后的图片
     * @param path  图片路径
     * @param thumbnailId  获取缩略图的id
     * @param view  需要添加图片的view
     * @param tag  防止乱序的标记位
     */
    public static void loadProcessBitmapWithTag(String path, int thumbnailId, View view, @Nullable Object tag) {
        sLoadLruCacheBitmapLink.setNextLink(sLoadProcessBitmapChain);
        sLoadLruCacheBitmapLink.loadBitmapToView(LOAD_PROCESS, view, path, thumbnailId, tag);
    }
}
