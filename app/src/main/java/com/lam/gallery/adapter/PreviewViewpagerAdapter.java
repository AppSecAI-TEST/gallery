package com.lam.gallery.adapter;

import android.graphics.Bitmap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.lam.gallery.R;
import com.lam.gallery.Task.MediaTask;
import com.lam.gallery.manager.BitmapManager;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.ui.PreViewImageView;

/**
 * Created by lenovo on 2017/7/29.
 */

public class PreviewViewpagerAdapter extends PagerAdapter {
    private static final String TAG = "PreviewViewpagerAdapter";

    private SparseArrayCompat<String> mMediaPathArray;

    public PreviewViewpagerAdapter(SparseArrayCompat<String> mediaPathArray) {
        mMediaPathArray = mediaPathArray;
    }

    public void setMediaPathArray(SparseArrayCompat<String> mediaPathArray) {
        mMediaPathArray = mediaPathArray;
    }

    @Override
    public int getCount() {
        return mMediaPathArray == null ? 0 : mMediaPathArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(container.getContext(), R.layout.item_preview_viewpager, null);
        final PreViewImageView mediaImage = (PreViewImageView) view.findViewById(R.id.pr_preview_media);
        //渲染ui
        mediaImage.setImageResource(R.drawable.loading);
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(mMediaPathArray.get(position));
        if(bitmap == null) {
            MediaTask.addTask(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapManager.processBitmap(mMediaPathArray.get(position), 1000);
                    LruCacheManager.addBitmapToCache(mMediaPathArray.get(position), bitmap);
                    mediaImage.post(new Runnable() {
                        @Override
                        public void run() {
                            mediaImage.setImageBitmap(bitmap);
                        }
                    });
                }
            });
        } else {
            mediaImage.setImageBitmap(bitmap);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
