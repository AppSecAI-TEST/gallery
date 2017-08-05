package com.lam.gallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lam.gallery.R;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.SelectedMedia;
import com.lam.gallery.factory.GalleryBitmapFactory;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/2.
 */

public class PreviewThumbnailAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final String TAG = "PreviewThumbnailAdapter";
    private OnThumbnailItemClickListener mOnThumbnailItemClickListener;
    private WeakReference<RecyclerView> mRecyclerView;
    private int mCurrentPos;
    private List<Media> mMediaList;

    public PreviewThumbnailAdapter(int currentPos) {
        mCurrentPos = currentPos;
        mMediaList = SelectedMedia.getSelectedMediaList();
    }

    public void setCurrentPos(int currentPos) {
        mCurrentPos = currentPos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<>((RecyclerView) parent);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_thumbnail, null);
        ThumbnailViewHolder holder = new ThumbnailViewHolder(view);
        //监听设置
        if (mOnThumbnailItemClickListener != null)
            holder.itemView.setOnClickListener(this);
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
        GalleryBitmapFactory.loadThumbnailWithTag(mMediaList.get(position).getPath(), mMediaList.get(position).getMediaId(), thumbnailImage, position);
    }

    private class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mSelectedView ;
        private ImageView mThumbnailImage;
        private ThumbnailViewHolder(View itemView) {
            super(itemView);
            mSelectedView = (RelativeLayout) itemView.findViewById(R.id.rl_thumbnail_selected);
            mThumbnailImage = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        }
        private RelativeLayout getSelectedView() {
            return mSelectedView;
        }
        private ImageView getThumbnailImage() {
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
