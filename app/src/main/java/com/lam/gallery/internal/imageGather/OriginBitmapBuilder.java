package com.lam.gallery.internal.imageGather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.internal.utils.PhotoMetadataUtils;

/**
 * Created by lenovo on 2017/8/10.
 */

public class OriginBitmapBuilder extends ImageBuilder {
    @Override
    public Bitmap loadBitmap(Object params) {
        return BitmapFactory.decodeFile((String)params);
    }

    @Override
    public boolean canHandleBuilder(Object params) {
        return (params instanceof String && PhotoMetadataUtils.getBitmapSize((String)params) <= ConfigSpec.getInstance().mMaxBitmapSize);
    }
}
