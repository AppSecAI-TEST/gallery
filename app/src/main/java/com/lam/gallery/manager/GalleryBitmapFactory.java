package com.lam.gallery.manager;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.lam.gallery.GalleryApplication;
import com.lam.gallery.R;
import com.lam.gallery.task.BitmapTaskDispatcher;

/**
 * Created by lenovo on 2017/7/28.
 */

public class GalleryBitmapFactory {

    private static Handler mHandler;
    private static Bitmap mBitmap;

    private static Handler getHandler(){
        if(mHandler == null) {
            synchronized (Handler.class){
                mHandler = new Handler(GalleryApplication.getContext().getMainLooper());
            }
        }
        return mHandler;
    }

//    public static Bitmap loadBitmapFromLruCache(String path) {
//        return LruCacheManager.getBitmapFromCache(path);
//    }

//    public static Bitmap loadBitmapThumbnailFromFile(String path, int thumbnailId) {
//        Bitmap bitmap = MediaManager.getThumbnail(thumbnailId);
//        LruCacheManager.addBitmapToCache(path, bitmap);
//        return bitmap;
//    }

//    public static Bitmap loadBitmapFromFile(String path) {
//        Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(path);
//        LruCacheManager.addBitmapToCache(path, bitmap);
//        return bitmap;
//    }

//    public static void loadThumbnailWithTag(final String path, final int thumbnailId, final ImageView imageView, final Object tag) {
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(path);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = MediaManager.getThumbnail(thumbnailId);
//                    LruCacheManager.addBitmapToCache(path, bitmap);
//                    getHandler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if((int)imageView.getTag() == (int) tag) {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

//    public static void loadThumbnail(final String path, final int thumbnailId, final ImageView imageView) {
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(path);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = MediaManager.getThumbnail(thumbnailId);
//                    LruCacheManager.addBitmapToCache(path, bitmap);
//                    getHandler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

//    public static void loadProcessBitmap(final String path, final ImageView imageView, final int reqHeight, final int reqWidth) {
//        final String lruCacheKey = reqHeight + reqWidth + path;
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = GalleryBitmapFactory.processBitmap(path, reqHeight, reqWidth);
//                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

//    public static void loadProcessBitmapWithTag(final String path, final ImageView imageView, final Object tag, final int reqHeight, final int reqWidth) {
//        final String lruCacheKey = reqHeight + reqWidth + path;
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = GalleryBitmapFactory.processBitmap(path, reqHeight, reqWidth);
//                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(bitmap != null && (int)imageView.getTag() == (int)tag) {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

//    public static void loadProcessBitmap(final String path, final ImageView imageView, final int reqSize) {
//        final String lruCacheKey = reqSize + reqSize + path;
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = GalleryBitmapFactory.processBitmap(path, reqSize, reqSize);
//                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

//    public static void loadProcessBitmapWithTag(final String path, final ImageView imageView, final Object tag, final int reqSize) {
//        final String lruCacheKey = reqSize + reqSize + path;
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = GalleryBitmapFactory.processBitmap(path, reqSize, reqSize);
//                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(bitmap != null && (int)imageView.getTag() == (int)tag) {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

//    public static void loadOriginBitmapWithTag(final String path, final ImageView imageView, final Object tag) {
//        final String lruCacheKey = "origin" + path;
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(path);
//                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(bitmap != null && (int)imageView.getTag() == (int)tag) {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

//    public static void loadOriginBitmap(final String path, final ImageView imageView) {
//        final String lruCacheKey = "origin" + path;
//        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
//        if(bitmap == null) {
//            imageView.setImageResource(R.drawable.loading);
//            ThreadManager.addTask(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(path);
//                    LruCacheManager.addBitmapToCache(lruCacheKey, bitmap);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    });
//                }
//            });
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }

    public static void loadThumbnailWithTag(final String path, final int thumbnailId, final ImageView imageView, final Object tag) {
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(path);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.loading);
            BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
                @Override
                public void doTask() {
                    final Bitmap bitmap = MediaManager.getThumbnail(thumbnailId);
                    LruCacheManager.addBitmapToCache(path, bitmap);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if ((int) imageView.getTag() == (int) tag) {
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
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.loading);
            BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
                @Override
                public void doTask() {
                    final Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(path);
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
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(lruCacheKey);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.loading);
            BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
                @Override
                public void doTask() {
                    final Bitmap bitmap = GalleryBitmapFactory.processBitmap(path, reqSize, reqSize);
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

    public static Bitmap processBitmap(String path, int reqWidth, int reqHeight) {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(path, options);
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
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
}
