package com.lam.gallery.manager;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.lam.gallery.GalleryApplication;
import com.lam.gallery.R;
import com.lam.gallery.Task.ThreadTask;
import com.lam.gallery.db.Media;

/**
 * Created by lenovo on 2017/7/28.
 */

public class GalleryBitmapFactory {

    private static Handler mHandler;

    private static Handler getHandler(){
        if(mHandler == null) {
            synchronized (Handler.class){
                mHandler = new Handler(GalleryApplication.getContext().getMainLooper());
            }
        }
        return mHandler;
    }

    public static void loadThumbnail(final Media media, final ImageView imageView, final Object tag) {
        final String path = media.getPath();
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(path);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.loading);
            ThreadTask.addTask(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = MediaManager.getThumbnail(media.getMediaId());
                    LruCacheManager.addBitmapToCache(path, bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if((int)imageView.getTag() == (int) tag) {
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

    public static Bitmap processBitmap(String path, int SIZE) {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(path, options);
        int inSampleSize = calculateInSampleSize(options, SIZE, SIZE);
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return android.graphics.BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static long getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
