package com.lam.galleryhome;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lam.gallery.db.Media;
import com.lam.gallery.manager.GalleryBitmapFactory;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.manager.MediaManager;
import com.lam.galleryhome.Task.ThreadTask;

import java.util.List;

/**
 * Created by lenovo on 2017/8/3.
 */

public class PathShowAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PathShowAdapter";
    public List<Media> mMediaList;
    private boolean mIsOrigin;
    private Handler mHandler;

    public PathShowAdapter(List<Media> mediaList, boolean isOrigin) {
        Log.d(TAG, "PathShowAdapter: ");
        mMediaList = mediaList;
        mIsOrigin = isOrigin;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void setMediaList(List<Media> mediaList) {
        mMediaList = mediaList;
    }

    public void setOrigin(boolean origin) {
        mIsOrigin = origin;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
        SelectedDisplayListViewHolder holder = new SelectedDisplayListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        SelectedDisplayListViewHolder selectedDisplayListViewHolder = (SelectedDisplayListViewHolder) holder;
        final ImageView imageView = selectedDisplayListViewHolder.getImageView();
        final TextView sizeTextView = selectedDisplayListViewHolder.getSizeTextView();
        selectedDisplayListViewHolder.getPathTextView().setText("图片路径： " + mMediaList.get(position).getPath());
        //设置标记
        imageView.setTag(position);
        //开始绑定渲染
        final String path = mMediaList.get(position).getPath();
        if(mIsOrigin) {
            Bitmap bitmap = LruCacheManager.getBitmapFromCache(mMediaList.get(position).getPath() + "200");
            if(bitmap == null) {
                imageView.setImageResource(R.drawable.loading);
                ThreadTask.addTask(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = GalleryBitmapFactory.processBitmap(mMediaList.get(position).getPath(), 200);
                        LruCacheManager.addBitmapToCache(mMediaList.get(position).getPath() + "200", bitmap);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(bitmap != null && (int)imageView.getTag() == position) {
                                    imageView.setImageBitmap(bitmap);
                                    sizeTextView.setText("图片大小： " + GalleryBitmapFactory.getBitmapSize(bitmap));
                                }
                            }
                        });
                    }
                });
            } else {
                imageView.setImageBitmap(bitmap);
                sizeTextView.setText("图片大小： " + GalleryBitmapFactory.getBitmapSize(bitmap));
            }
        } else {
            Bitmap bitmap = LruCacheManager.getBitmapFromCache(path);
            if(bitmap == null) {
                imageView.setImageResource(R.drawable.loading);
                ThreadTask.addTask(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = MediaManager.getThumbnail(mMediaList.get(position).getMediaId());
                        LruCacheManager.addBitmapToCache(path, bitmap);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if((int)imageView.getTag() == position) {
                                    imageView.setImageBitmap(bitmap);
                                    sizeTextView.setText("图片大小： " + GalleryBitmapFactory.getBitmapSize(bitmap));
                                }
                            }
                        });
                    }
                });
            } else {
                imageView.setImageBitmap(bitmap);
                sizeTextView.setText("图片大小： " + GalleryBitmapFactory.getBitmapSize(bitmap));
            }
        }
    }

    public class SelectedDisplayListViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mPathTextView;
        private TextView mSizeTextView;

        public SelectedDisplayListViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_image);
            mPathTextView = (TextView) itemView.findViewById(R.id.tv_path);
            mSizeTextView = (TextView) itemView.findViewById(R.id.tv_size);
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getPathTextView() {
            return mPathTextView;
        }

        public TextView getSizeTextView() {
            return mSizeTextView;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: getSelectedMedia" + mMediaList.size());
        return mMediaList.size();
    }
}
