package com.lam.gallery.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lam.gallery.R;
import com.lam.gallery.db.ConfigSpec;
import com.lam.gallery.db.Media;
import com.lam.gallery.ui.PhotoMetadataUtils;
import com.lam.gallery.ui.PreViewImageView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/2.
 */

public class PreviewViewpagerAdapter extends PagerAdapter implements PreViewImageView.OnClickItemViewListener {
    private static final String TAG = "PreviewViewpagerAdapter";
    private List<Media> mMediaList;
    private static OnClickHeaderAndFooterChange sOnClickHeaderAndFooterChange;
    private WeakReference<Activity> mActivityWeakReference;

    public void setOnClickHeaderAndFooterChange(OnClickHeaderAndFooterChange onClickHeaderAndFooterChange) {
        sOnClickHeaderAndFooterChange = onClickHeaderAndFooterChange;
    }

    public PreviewViewpagerAdapter(List<Media> mediaList, WeakReference<Activity> activityWeakReference) {
        mMediaList = mediaList;
        mActivityWeakReference = activityWeakReference;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.d(TAG, "instantiateItem: " + position);
        View view = View.inflate(container.getContext(), R.layout.item_preview_viewpager, null);
        final PreViewImageView mediaImage = (PreViewImageView) view.findViewById(R.id.pr_preview_media);
        //设置标记
        mediaImage.setTag(position);
        //渲染加载ui
        Point size = PhotoMetadataUtils.getBitmapSize(Uri.parse("file://" + mMediaList.get(position).getPath()), mActivityWeakReference.get());
        ConfigSpec.getInstance().mImageEngine.loadProcessImage(new WeakReference<ImageView>(mediaImage), size.x, size.y, position, mMediaList.get(position).getPath(), mMediaList.get(position).getMediaId());
        mediaImage.setOnClickItemViewListener(this);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mMediaList == null ? 0 : mMediaList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClickItem(PreViewImageView v) {
        if(sOnClickHeaderAndFooterChange != null) {
            sOnClickHeaderAndFooterChange.headerAndFooterChange();
        }
    }

    public interface OnClickHeaderAndFooterChange {
        void headerAndFooterChange();
    }

}
