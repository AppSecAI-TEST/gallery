package com.lam.gallery.internal.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lam.gallery.R;
import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.internal.entity.Media;
import com.lam.gallery.internal.entity.SelectedMedia;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/2.
 */

public class PreviewThumbnailAdapter extends BaseRecyclerViewAdapter<PreviewThumbnailAdapter.ThumbnailViewHolder> implements View.OnClickListener {
    private static final String TAG = "PreviewThumbnailAdapter";
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
    public ThumbnailViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_thumbnail, null);
        ThumbnailViewHolder holder = new ThumbnailViewHolder(view);
        return holder;
    }

    @Override
    public void onBindVH(ThumbnailViewHolder holder, int position) {
        ThumbnailViewHolder thumbnailViewHolder = holder;
        final ImageView thumbnailImage = thumbnailViewHolder.getThumbnailImage();
        RelativeLayout selectedView = thumbnailViewHolder.getSelectedView();
        //初始化
        selectedView.setVisibility(View.INVISIBLE);
        //标记类
        thumbnailImage.setTag(position);
        //渲染加载ui
        if(mCurrentPos == position)
            selectedView.setVisibility(View.VISIBLE);
        ConfigSpec.getInstance().mImageEngine.loadThumbnail(new WeakReference<>(thumbnailImage), position, mMediaList.get(position).getMediaId());
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
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

}
