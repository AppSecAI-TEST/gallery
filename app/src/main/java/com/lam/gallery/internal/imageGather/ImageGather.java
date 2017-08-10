package com.lam.gallery.internal.imageGather;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.lam.gallery.internal.GalleryApplication;
import com.lam.gallery.internal.task.BitmapTaskDispatcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class ImageGather {
    private static final String TAG = "ImageGather";

    private static volatile ImageGather sImageGather = null;
    private final List<ImageBuilder> mImageBuilders;

    private Handler mUiHandler;

    private Handler getUiHandler() {
        if(mUiHandler == null) {
            synchronized (Handler.class) {
                if(mUiHandler == null) {
                    mUiHandler = new Handler(GalleryApplication.getContext().getMainLooper());
                }
            }
        }
        return mUiHandler;
    }

    public static ImageGather with() {
        if(sImageGather == null) {
            synchronized (ImageGather.class) {
                if(sImageGather == null) {
                    sImageGather = new ImageGather();
                }
            }
        }
        return sImageGather;
    }

    private List<ImageBuilder> getImageBuilders() {
        return mImageBuilders;
    }

    private ImageGather() {
        List<ImageBuilder> imageBuilders = new ArrayList<>();
        imageBuilders.add(new LruCacheBitmapBuilder());
        imageBuilders.add(new ThumbnailBitmapBuilder());
        imageBuilders.add(new ProcessBitmapBuilder());
        mImageBuilders = new ArrayList<>(imageBuilders);
    }

    public void into(final WeakReference<ImageView> imageViewWeakReference, final Object params, @Nullable final Object tag) {
        if(! (Looper.getMainLooper().getThread() == Thread.currentThread())) {
            throw new IllegalStateException("ImageGather.into() should run in main thread.");
        }
//        Log.d(TAG, "into: " + new LruCacheBitmapBuilder().loadBitmap(params));
//        if(new LruCacheBitmapBuilder().loadBitmap(params) == null)
//            imageViewWeakReference.get().setImageResource(R.drawable.loading);
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public Object doTask() {
                final Bitmap bitmap = forRequest(params).builder(params);
                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(bitmap != null && tag == null) {
                            imageViewWeakReference.get().setImageBitmap(bitmap);
                        } else if(bitmap != null && imageViewWeakReference.get() != null && (int)imageViewWeakReference.get().getTag() == (int)tag) {
                            imageViewWeakReference.get().setImageBitmap(bitmap);
                        }
                    }
                });
                return null;
            }
        });
    }


    private ImageBuilder forRequest(Object params) {
        List<ImageBuilder> imageBuilders = sImageGather.getImageBuilders();
        for(int i = 0; i < imageBuilders.size(); ++i) {
            Log.d(TAG, "forRequest: " + i);
            ImageBuilder imageBuilder = imageBuilders.get(i);
            if(imageBuilder.canHandleBuilder(params)) {
                return imageBuilder;
            }
        }
        return new EmptyBuilder();
    }
}
