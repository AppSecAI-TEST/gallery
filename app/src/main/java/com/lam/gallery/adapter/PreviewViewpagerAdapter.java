package com.lam.gallery.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.Task.MediaTask;
import com.lam.gallery.manager.BitmapManager;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.ui.PreViewImageView;

import java.util.HashSet;

/**
 * Created by lenovo on 2017/7/29.
 */

public class PreviewViewpagerAdapter extends PagerAdapter implements PreViewImageView.OnClickItemViewListener, View.OnClickListener {
    private static final String TAG = "PreviewViewpagerAdapter";

    private SparseArray<String> mMediaPathArray;
    private TextView mTvPreviewTitle;
    private HashSet<String> mSelectSet;
    private TextView mTvSelect;
    private ImageView mIvSelect;

    private static OnClickHeaderAndFooterChange sOnClickHeaderAndFooterChange;
    private static OnClickSelect sOnClickSelect;

    public static void setOnClickSelect(OnClickSelect onClickSelect) {
        sOnClickSelect = onClickSelect;
    }

    public static void setOnClickHeaderAndFooterChange(OnClickHeaderAndFooterChange onClickHeaderAndFooterChange) {
        sOnClickHeaderAndFooterChange = onClickHeaderAndFooterChange;
    }

    public PreviewViewpagerAdapter(SparseArray<String> mediaPathArray, TextView tvPreviewTitle, HashSet<String> selectSet, TextView tvSelect, ImageView ivSelect) {
        mMediaPathArray = mediaPathArray;
        mTvPreviewTitle = tvPreviewTitle;
        mSelectSet = selectSet;
        mTvSelect = tvSelect;
        mIvSelect = ivSelect;
    }


    public void setMediaPathArray(SparseArray<String> mediaPathArray) {
        mMediaPathArray = mediaPathArray;
    }

    public HashSet<String> getSelectSet() {
        return mSelectSet;
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
        mediaImage.setTag(position);
        mIvSelect.setTag(position);
        mTvSelect.setTag(position);
        mediaImage.setOnClickItemViewListener(this);
        //渲染ui
        mediaImage.setImageResource(R.drawable.loading);
        mTvPreviewTitle.setText((position + 1) + "/" + mMediaPathArray.size());
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(mMediaPathArray.get(position));
        if(bitmap == null) {
            MediaTask.addTask(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapManager.processBitmap(mMediaPathArray.get(position), 300);
                    LruCacheManager.addBitmapToCache(mMediaPathArray.get(position), bitmap);
                    mediaImage.post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null && (int)mediaImage.getTag() == position) {
                                mediaImage.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        } else {
            mediaImage.setImageBitmap(bitmap);
        }
        mIvSelect.setOnClickListener(this);
        mTvSelect.setOnClickListener(this);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public void onClickItem(PreViewImageView v) {
        if(sOnClickHeaderAndFooterChange != null) {
            sOnClickHeaderAndFooterChange.headerAndFooterChange();
        }
    }

    @Override
    public void onClick(View v) {
        if(sOnClickSelect != null) {
            sOnClickSelect.clickSelect((int) v.getTag());
        }
    }

    public interface OnClickHeaderAndFooterChange {
        void headerAndFooterChange();
    }

    public interface OnClickSelect {
        void clickSelect(int position);
    }
}
