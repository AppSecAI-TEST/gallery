package com.lam.gallery.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lam.gallery.R;
import com.lam.gallery.db.ConfigSpec;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.SelectedMedia;
import com.lam.gallery.ui.GridViewImageItem;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/10.
 */


@SuppressWarnings("WeakerAccess")
public class MediaGridAdapter1 extends BaseRecyclerViewAdapter<MediaGridAdapter1.GridViewHolder> {
    private List<Media> mMediaList;
    private static onClickToIntent sOnClickToIntent;

    public MediaGridAdapter1(List<Media> mediaList) {
        mMediaList = mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        mMediaList = mediaList;
    }

    public void setOnClickToIntent(onClickToIntent onClickToIntent) {
        sOnClickToIntent = onClickToIntent;
    }

    @Override
    public GridViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, null);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindVH(GridViewHolder holder, int position) {
        final ImageView selectImage = (holder).getImageView();
        final GridViewImageItem gridViewImageItem = ( holder).getGridViewImageItem();
        //初始化
        selectImage.setImageResource(R.drawable.select_alpha_16);
        gridViewImageItem.clearColorFilter();
        //设置标记setTag类
        gridViewImageItem.setTag(position);
        selectImage.setTag(position);
        holder.itemView.setTag(position);
        //加载渲染ui
        String path = mMediaList.get(position).getPath();
        if(SelectedMedia.getSelectedPosition(path) != -1 && (int)gridViewImageItem.getTag() == position) { //该图片在已选集合中
            selectImage.setImageResource(R.drawable.select_green_16);
            gridViewImageItem.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
        gridViewImageItem.setImageResource(R.drawable.loading);
        ConfigSpec.getInstance().mImageEngine.loadThumbnail(new WeakReference<>((ImageView)gridViewImageItem), position, mMediaList.get(position).getMediaId());
        //设置监听
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

     class GridViewHolder extends RecyclerView.ViewHolder {
        private GridViewImageItem mGridViewImageItem;
        private ImageView mImageView;
        public GridViewHolder(View itemView) {
            super(itemView);
            mGridViewImageItem = (GridViewImageItem) itemView.findViewById(R.id.gvi_media_image);
            mImageView = (ImageView)itemView.findViewById(R.id.iv_media_select);
        }
        private GridViewImageItem getGridViewImageItem() {
            return mGridViewImageItem;
        }

        private ImageView getImageView() {
            return mImageView;
        }
    }

    @Override
    public void onClick(View v) {
        if(sOnClickToIntent != null) {
            sOnClickToIntent.clickToIntent((int)v.getTag());
        }
    }

    public interface onClickToIntent {
        void clickToIntent(int position);
    }
}
