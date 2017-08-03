package com.lam.gallery.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lam.gallery.R;
import com.lam.gallery.Task.ThreadTask;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.SelectedMedia;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.manager.MediaManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/2.
 */

public class PreviewThumbnailAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final String TAG = "PreviewThumbnailAdapter";
    protected OnThumbnailItemClickListener mOnThumbnailItemClickListener;
    private WeakReference<RecyclerView> mRecyclerView;
    private int mCurrentPos;
    private Handler mHandler;
    private List<Media> mMediaList;

    public PreviewThumbnailAdapter(int currentPos) {
        mCurrentPos = currentPos;
        mHandler = new Handler(Looper.getMainLooper());
        mMediaList = SelectedMedia.getSelectedMediaList();
    }

    public void setCurrentPos(int currentPos) {
        mCurrentPos = currentPos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<RecyclerView>((RecyclerView) parent);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_thumbnail, null);
        ThumbnailViewHolder holder = new ThumbnailViewHolder(view);
        //监听设置
        if (holder != null) {
            if (mOnThumbnailItemClickListener != null)
                holder.itemView.setOnClickListener(this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ThumbnailViewHolder thumbnailViewHolder = (ThumbnailViewHolder) holder;
        final ImageView thumbnailImage = thumbnailViewHolder.getThumbnailImage();
        RelativeLayout selectedView = thumbnailViewHolder.getSelectedView();
        //初始化
        selectedView.setVisibility(View.INVISIBLE);
        //标记类
        thumbnailImage.setTag(position);
        //渲染加载ui
        if(mCurrentPos == position)
            selectedView.setVisibility(View.VISIBLE);
        final String path = mMediaList.get(position).getPath();
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(path);
        if(bitmap == null) {
            thumbnailImage.setImageResource(R.drawable.loading);
            ThreadTask.addTask(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = MediaManager.getThumbnail(mMediaList.get(position).getMediaId());
                    LruCacheManager.addBitmapToCache(path, bitmap);
                    mHandler.post(new Runnable() {
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

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mSelectedView ;
        private ImageView mThumbnailImage;
        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            mSelectedView = (RelativeLayout) itemView.findViewById(R.id.rl_thumbnail_selected);
            mThumbnailImage = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        }
        public RelativeLayout getSelectedView() {
            return mSelectedView;
        }
        public ImageView getThumbnailImage() {
            return mThumbnailImage;
        }
    }

    @Override
    public int getItemCount() {
        return mMediaList == null ? 0 : mMediaList.size();
    }

    public void setOnThumbnailItemClickListener(OnThumbnailItemClickListener listener) {
        mOnThumbnailItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        RecyclerView recyclerView = mRecyclerView.get();
        if (recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            mOnThumbnailItemClickListener.onThumbnailItemClick(v, position);

        }
    }

    public interface OnThumbnailItemClickListener {
        void onThumbnailItemClick(View view, int position);
    }
}
