package com.lam.gallery.internal.imageGather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.internal.manager.LruCacheManager;
import com.lam.gallery.internal.utils.PhotoMetadataUtils;

import static android.graphics.BitmapFactory.decodeFile;


class ProcessBitmapBuilder extends ImageBuilder{
    @Override
    public Bitmap loadBitmap(Object params) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        decodeFile((String)params, newOpts);
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
        Bitmap bitmap = BitmapFactory.decodeFile((String)params, newOpts);
        LruCacheManager.addBitmapToCache(params + "", bitmap);
        return bitmap;
    }

    @Override
    public boolean canHandleBuilder(Object params) {
        return (params instanceof String && PhotoMetadataUtils.getBitmapSize((String)params) > ConfigSpec.getInstance().mMaxBitmapSize);
    }
}
