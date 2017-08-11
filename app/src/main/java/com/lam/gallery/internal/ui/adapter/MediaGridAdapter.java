package com.lam.gallery.internal.ui.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lam.gallery.R;
import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.internal.entity.Media;
import com.lam.gallery.internal.entity.SelectedMedia;
import com.lam.gallery.internal.task.BitmapTaskDispatcher;
import com.lam.gallery.internal.ui.view.GridViewImageItem;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;


public class MediaGridAdapter extends BaseRecyclerViewAdapter<MediaGridAdapter.GridViewHolder> implements GridViewImageItem.OnIntentToPreviewListener {
    private static final String TAG = "MediaGridAdapter";
    private List<Media> mMediaList;
    private static onClickToIntent sOnClickToIntent;
    private boolean isLoad;

    public MediaGridAdapter(List<Media> mediaList) {
        mMediaList = mediaList;
        isLoad = true;
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
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        gridViewHolder.getGridViewImageItem().setOnIntentToPreviewListener(this);
        getRecyclerView().get().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int firstVisibleItem = ((GridLayoutManager)getRecyclerView().get().getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItem = ((GridLayoutManager)getRecyclerView().get().getLayoutManager()).findLastVisibleItemPosition();
                //静止加载
                if(newState == SCROLL_STATE_IDLE) {
                    isLoad = false;
                    Log.d(TAG, "onScrollStateChanged: " + getRecyclerView().get().getChildAt(lastVisibleItem - firstVisibleItem));
                    for(int i = firstVisibleItem; i <= lastVisibleItem; ++i) {
                        ConfigSpec.getInstance().mImageEngine.loadThumbnail(
                                new WeakReference<>((ImageView)getRecyclerView().get().getChildAt(i - firstVisibleItem).findViewById(R.id.gvi_media_image))
                                , i, mMediaList.get(i).getMediaId());
                    }
                } else if(newState == SCROLL_STATE_TOUCH_SCROLL) {
                    isLoad = true;
                } else {
                    isLoad = false;
                    BitmapTaskDispatcher.clear();
                }
            }
        });
        return gridViewHolder;
    }

    @Override
    public void onBindVH(MediaGridAdapter.GridViewHolder holder, int position) {
        Log.d(TAG, "onBindVH: " + position);
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
        //防止滑动不加载第一次进入界面时不调用加载
        if(isLoad)
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

    @Override
    public void onClickToIntent(View v) {
        if(sOnClickToIntent != null) {
            sOnClickToIntent.clickToIntent((int)v.getTag());
        }
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        private GridViewImageItem mGridViewImageItem;
        private ImageView mImageView;
        GridViewHolder(View itemView) {
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