package com.lam.gallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lam.gallery.R;
import com.lam.gallery.Task.MediaTask;
import com.lam.gallery.manager.BitmapManager;
import com.lam.gallery.manager.LruCacheManager;
import com.lam.gallery.ui.GridViewImageItem;

import java.util.HashSet;

/**
 * Created by lenovo on 2017/7/28.
 */

public class RecyclerViewGridAdapter extends RecyclerView.Adapter{
    private static final String TAG = "RecyclerViewGridAdapter";
    private Context mContext;
    private HashSet<String> mSelectSet;
    private SparseArrayCompat<String> mMediaPathArray;
    private Button mBtSend;
    private TextView mTvPreview;

    public RecyclerViewGridAdapter(SparseArrayCompat<String> mediaPathArray, Button btSend, TextView tvPreview) {
        mMediaPathArray = mediaPathArray;
        mSelectSet = new HashSet<>();
        mBtSend = btSend;
        mTvPreview = tvPreview;
    }

    public HashSet<String> getSelectSet() {
        return mSelectSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, null);
        final GridViewHolder gridViewHolder = new GridViewHolder(view);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ImageView selectImage = ((GridViewHolder) holder).getImageView();
        final GridViewImageItem gridViewImageItem = ((GridViewHolder) holder).getGridViewImageItem();
        //初始化类
        gridViewImageItem.setImageResource(R.drawable.loading);
        if(! mSelectSet.contains(mMediaPathArray.get(position))) {
            selectImage.setImageResource(R.drawable.select_alpha_16);
            gridViewImageItem.clearColorFilter();
        } else {
            selectImage.setImageResource(R.drawable.select_green_16);
            gridViewImageItem.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
        gridViewImageItem.setTag(position);
        
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(mMediaPathArray.get(position));
        if(bitmap == null) {
            MediaTask.addTask(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapManager.processBitmap(mMediaPathArray.get(position));
                    LruCacheManager.addBitmapToCache(mMediaPathArray.get(position), bitmap);
                    gridViewImageItem.post(new Runnable() {
                        @Override
                        public void run() {
                            if((int)gridViewImageItem.getTag() == position) {
                                gridViewImageItem.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        } else {
            gridViewImageItem.setImageBitmap(bitmap);
        }


        //设置内部监听
        ((GridViewHolder) holder).getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! mSelectSet.contains(mMediaPathArray.get(position))) {
                    if(mSelectSet.size() == 9) {
                        Toast.makeText(mContext, "你最多只能选择9张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        selectImage.setImageResource(R.drawable.select_green_16);
                        gridViewImageItem.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        mSelectSet.add(mMediaPathArray.get(position));
                    }
                } else {
                    selectImage.setImageResource(R.drawable.select_alpha_16);
                    gridViewImageItem.clearColorFilter();
                    mSelectSet.remove(mMediaPathArray.get(position));
                }
                int selectCount = mSelectSet.size();
                if(selectCount != 0) {
                    mBtSend.setBackgroundColor(0xFF19C917);
                    mBtSend.setText("发送(" + selectCount + "/9)");
                    mBtSend.setTextColor(Color.WHITE);
                    mTvPreview.setText("预览(" + selectCount + ")");
                    mTvPreview.setTextColor(Color.WHITE);
                } else {
                    mBtSend.setBackgroundColor(0xFF094909);
                    mBtSend.setText("发送");
                    mBtSend.setTextColor(0xFFA1A1A1);
                    mTvPreview.setText("预览");
                    mTvPreview.setTextColor(0xFF5B5B5B);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMediaPathArray == null ? 0 : mMediaPathArray.size();
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
}
