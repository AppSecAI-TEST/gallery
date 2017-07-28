package com.lam.gallery;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2017/7/27.
 */

public class GalleryApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}