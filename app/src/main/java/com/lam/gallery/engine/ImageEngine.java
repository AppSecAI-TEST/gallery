package com.lam.gallery.engine;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2017/8/7.
 */

public interface ImageEngine {
    String CONTENT_URI_HEAD = "content://media/external/images/media/";
    String FILE_URI_HEAD = "file://";

    void loadThumbnail(WeakReference<ImageView> imageViewWeakReference, Object tag, Object id);

    void loadProcessImage(WeakReference<ImageView> imageViewWeakReference, int resizeX, int resizeY, Object tag, Object path, Object id);

    void loadImage(WeakReference<ImageView> imageViewWeakReference, Object tag, Object params);
}
