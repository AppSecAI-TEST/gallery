package com.lam.gallery.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lam.gallery.R;
import com.lam.gallery.Task.MediaTask;
import com.lam.gallery.manager.BitmapManager;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.ui.SquareImageView;

import java.util.HashSet;

/**
 * Created by lenovo on 2017/7/29.
 */

public class PreviewThumbnailAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private HashSet<String> mSelectSet;
    private SparseArray<String> mMediaPathArray;
    private int mSelectPos;
    private static OnSelectThumbnail sOnSelectThumbnail;

    public static void setOnSelectThumbnail(OnSelectThumbnail onSelectThumbnail) {
        sOnSelectThumbnail = onSelectThumbnail;
    }

    public PreviewThumbnailAdapter(HashSet<String> selectSet, SparseArray<String> mediaPathArray, int selectPos) {
        mSelectSet = selectSet;
        mMediaPathArray = mediaPathArray;
        mSelectPos = selectPos;
    }

    public void setMediaPathArray(SparseArray<String> mediaPathArray) {
        mMediaPathArray = mediaPathArray;
    }

    public void setSelectPos(int selectPos) {
        mSelectPos = selectPos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_thumbnail, null);
        ThumbnailViewHolder holder = new ThumbnailViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ThumbnailViewHolder thumbnailViewHolder = (ThumbnailViewHolder) holder;
        final SquareImageView thumbnailImage = thumbnailViewHolder.getThumbnailImage();
        //默认初始化防止复用的问题
        thumbnailViewHolder.getSelectedView().setVisibility(View.INVISIBLE);
        thumbnailImage.setTag(position);
        holder.itemView.setTag(position);

        //开始渲染绑定
        if(mSelectPos == (int) thumbnailImage.getTag()) {
            thumbnailViewHolder.getSelectedView().setVisibility(View.VISIBLE);
        }
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(mMediaPathArray.get(position));
        if(bitmap == null) {
            MediaTask.addTask(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapManager.processBitmap(mMediaPathArray.get(position), 60);
                    LruCacheManager.addBitmapToCache(mMediaPathArray.get(position), bitmap);
                    thumbnailImage.post(new Runnable() {
                        @Override
                        public void run() {
                            if((int)thumbnailImage.getTag() == position) {
                                thumbnailImage.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        } else {
            thumbnailImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return mSelectSet == null? 0 : mSelectSet.size();
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mSelectedView ;
        private SquareImageView mThumbnailImage;
        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            mSelectedView = (RelativeLayout) itemView.findViewById(R.id.rl_thumbnail_selected);
            mThumbnailImage = (SquareImageView) itemView.findViewById(R.id.siv_thumbnail);
        }
        public RelativeLayout getSelectedView() {
            return mSelectedView;
        }
        public SquareImageView getThumbnailImage() {
            return mThumbnailImage;
        }
    }

    @Override
    public void onClick(View v) {
        if(sOnSelectThumbnail != null) {
            sOnSelectThumbnail.onSelect((int) v.getTag());
        }
    }

    public interface OnSelectThumbnail {
        void onSelect(int position);
    }
}
