package com.lam.galleryhome.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lam.gallery.db.Media;
import com.lam.gallery.manager.GalleryBitmapManager;
import com.lam.galleryhome.R;

import java.util.List;

/**
 * Created by lenovo on 2017/8/3.
 */

public class PathShowAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PathShowAdapter";
    private List<Media> mMediaList;
    private boolean mIsOrigin;

    public PathShowAdapter(List<Media> mediaList, boolean isOrigin) {
        mMediaList = mediaList;
        mIsOrigin = isOrigin;
    }

    public void setMediaList(List<Media> mediaList) {
        mMediaList = mediaList;
    }

    public void setOrigin(boolean origin) {
        mIsOrigin = origin;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
        SelectedDisplayListViewHolder holder = new SelectedDisplayListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        SelectedDisplayListViewHolder selectedDisplayListViewHolder = (SelectedDisplayListViewHolder) holder;
        final ImageView imageView = selectedDisplayListViewHolder.getImageView();
        selectedDisplayListViewHolder.getPathTextView().setText("图片路径： " + mMediaList.get(position).getPath());
        //设置标记
        imageView.setTag(position);
        //开始绑定渲染
        final String path = mMediaList.get(position).getPath();
        if(mIsOrigin) {
            GalleryBitmapManager.loadOriginBitmapWithTag(mMediaList.get(position).getPath(), imageView, position);
//            BitmapTaskDispatcher.getLIFOTaskDispatcher().loadProcessBitmapWithTag(mMediaList.get(position).getPath(), imageView, position, 200);
        } else {
            GalleryBitmapManager.loadThumbnailWithTag(path, mMediaList.get(position).getMediaId(), imageView, position);
//            BitmapTaskDispatcher.getLIFOTaskDispatcher().loadThumbnailWithTag(path, mMediaList.get(position).getMediaId(), imageView, position);
        }
    }

    public class SelectedDisplayListViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mPathTextView;

        public SelectedDisplayListViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_image);
            mPathTextView = (TextView) itemView.findViewById(R.id.tv_path);
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getPathTextView() {
            return mPathTextView;
        }
    }

    @Override
    public int getItemCount() {
        return mMediaList == null ? 0 : mMediaList.size();
    }
}
