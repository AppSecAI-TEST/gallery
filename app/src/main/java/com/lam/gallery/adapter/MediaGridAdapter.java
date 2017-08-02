package com.lam.gallery.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lam.gallery.R;
import com.lam.gallery.Task.ThreadTask;
import com.lam.gallery.db.Media;
import com.lam.gallery.manager.MediaManager;
import com.lam.gallery.db.SelectedMedia;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.ui.GridViewImageItem;

import java.util.List;

/**
 * Created by lenovo on 2017/8/1.
 */

public class MediaGridAdapter extends RecyclerView.Adapter implements GridViewImageItem.OnIntentToPreviewListener{
    private static final String TAG = "MediaGridAdapter";

    private List<Media> mMediaList;
    private Handler mHandler;
    private static onClickToIntent sOnClickToIntent;

    public MediaGridAdapter(List<Media> mediaList) {
        mMediaList = mediaList;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void setMediaList(List<Media> mediaList) {
        mMediaList = mediaList;
    }

    public static void setOnClickToIntent(onClickToIntent onClickToIntent) {
        sOnClickToIntent = onClickToIntent;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, null);
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        gridViewHolder.getGridViewImageItem().setOnIntentToPreviewListener(this);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final ImageView selectImage = ((GridViewHolder) holder).getImageView();
        final GridViewImageItem gridViewImageItem = ((GridViewHolder) holder).getGridViewImageItem();
        //初始化
        gridViewImageItem.setImageResource(R.drawable.loading);
        selectImage.setImageResource(R.drawable.select_alpha_16);
        //设置标记setTag类
        gridViewImageItem.setTag(position);
        selectImage.setTag(position);
        //加载渲染ui
        ThreadTask.addTask(new Runnable() {
            @Override
            public void run() {
                String path = mMediaList.get(position).getPath();
                if(SelectedMedia.getSelectedPosition(path) != -1 && (int)gridViewImageItem.getTag() == position) { //该图片在已选集合中
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            selectImage.setImageResource(R.drawable.select_green_16);
                            gridViewImageItem.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        }
                    });
                }
                Bitmap bitmap = LruCacheManager.getBitmapFromCache(path);
                if(bitmap == null) {
                    bitmap = MediaManager.getThumbnail(mMediaList.get(position).getMediaId());
                    LruCacheManager.addBitmapToCache(path, bitmap);
                }
                final Bitmap correctBitmap = bitmap;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if((int)gridViewImageItem.getTag() == position) {
                            gridViewImageItem.setImageBitmap(correctBitmap);
                        }
                    }
                });
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mMediaList.get((int) v.getTag()).getPath();
                Media selectMedia = mMediaList.get((int) v.getTag());
                if(SelectedMedia.getSelectedPosition(path) != -1) { //该图片在已选集合中
                    SelectedMedia.removeByPath(path);
                    selectImage.setImageResource(R.drawable.select_alpha_16);
                    gridViewImageItem.clearColorFilter();
                } else {
                    if(SelectedMedia.addSelected(selectMedia)) {
                        selectImage.setImageResource(R.drawable.select_green_16);
                        gridViewImageItem.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMediaList == null ? 0 : mMediaList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        private GridViewImageItem mGridViewImageItem;
        private ImageView mImageView;
        public GridViewHolder(View itemView) {
            super(itemView);
            mGridViewImageItem = (GridViewImageItem) itemView.findViewById(R.id.gvi_media_image);
            mImageView = (ImageView)itemView.findViewById(R.id.iv_media_select);
        }
        public GridViewImageItem getGridViewImageItem() {
            return mGridViewImageItem;
        }

        public ImageView getImageView() {
            return mImageView;
        }
    }


    public interface onClickToIntent {
        void clickToIntent(int position);
    }

    @Override
    public void onClickToIntent(View v) {
        if(sOnClickToIntent != null) {
            sOnClickToIntent.clickToIntent((int)v.getTag());
        }
    }
}
