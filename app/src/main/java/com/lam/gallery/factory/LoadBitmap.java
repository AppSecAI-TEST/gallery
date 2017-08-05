package com.lam.gallery.factory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.manager.MediaManager;

/**
 * Created by lenovo on 2017/8/4.
 */

public class LoadBitmap {
    private static final String TAG = "LoadBitmap";

    static class LruCacheBitmap implements GalleryBitmap {

        @Override
        public Bitmap loadBitmap(Object... param) {
            return LruCacheManager.getBitmapFromCache( (String)param[0]);
        }
    }


    static class OriginBitmap implements GalleryBitmap {

        @Override
        public Bitmap loadBitmap(Object... param) {
            return android.graphics.BitmapFactory.decodeFile( (String)param[0]);
        }
    }

    static class ThumbnailBitmap implements GalleryBitmap {

        @Override
        public Bitmap loadBitmap(Object... param) {
            return MediaManager.getThumbnail( (int) param[0]);
        }
    }

    static class ProcessBitmap implements GalleryBitmap {

        @Override
        public Bitmap loadBitmap(Object... param) {
            return compressImageFromFile( (String)param[0], (int)param[1], (int)param[2]);
        }
    }

    public static Bitmap processBitmap(String path, int reqWidth, int reqHeight) {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return android.graphics.BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            int heightRatio = Math.round((float) height / (float) reqWidth);
            int widthRatio = Math.round((float) width / (float) reqHeight);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        if(inSampleSize != 0 && (height * width / inSampleSize * inSampleSize) < 1000000)
            inSampleSize /= 2;
        Log.d(TAG, "calculateInSampleSize: " + inSampleSize);
        Log.d(TAG, "calculateInSampleSize: " + (height * width / inSampleSize * inSampleSize));
        return inSampleSize <= 0 ? 1 : inSampleSize;
    }


    //-----------


    public static Bitmap compressImageFromFile(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        int inSampleSize = 1;
        if (width > height && width > reqWidth) {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        } else if (width < height && height > (float)reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        if (inSampleSize <= 0)
            inSampleSize = 1;
        newOpts.inSampleSize = inSampleSize;//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(path, newOpts);
    }

}
