package com.lam.galleryhome.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lam.galleryhome.R;

import java.util.List;

/**
 * Created by lenovo on 2017/8/3.
 */

public class PathShowAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PathShowAdapter";
    private boolean mIsOrigin;

    private List<String> mMediaPath;

    public PathShowAdapter(boolean isOrigin, List<String> mediaPath) {
        mIsOrigin = isOrigin;
        mMediaPath = mediaPath;
    }

    public void setMediaPath(List<String> mediaPath) {
        mMediaPath = mediaPath;
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
        selectedDisplayListViewHolder.getPathTextView().setText("图片路径： " + mMediaPath.get(position));
    }

    public class SelectedDisplayListViewHolder extends RecyclerView.ViewHolder{
        private TextView mPathTextView;

        public SelectedDisplayListViewHolder(View itemView) {
            super(itemView);
            mPathTextView = (TextView) itemView.findViewById(R.id.tv_path);
        }

        public TextView getPathTextView() {
            return mPathTextView;
        }
    }

    @Override
    public int getItemCount() {
        return mMediaPath == null ? 0 : mMediaPath.size();
    }
}
