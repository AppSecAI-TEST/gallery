package com.lam.gallery.internal.imageGather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.internal.manager.LruCacheManager;

import static android.graphics.BitmapFactory.decodeFile;


public class ProcessBitmapBuilder extends ImageBuilder{
    @Override
    public Bitmap loadBitmap(Object... params) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        decodeFile((String)params[0], newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        float reqWidth = ConfigSpec.getInstance().mProcessReqWidth;
        float reqHeight = ConfigSpec.getInstance().mProcessReqHeight;
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
        Bitmap bitmap = BitmapFactory.decodeFile((String)params[0], newOpts);
        LruCacheManager.addBitmapToCache(params[0] + "", bitmap);
        return bitmap;
    }

    @Override
    public boolean canHandleBuilder(Object... params) {
        return (params[0] instanceof String);
    }
}
