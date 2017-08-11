package com.lam.gallery.internal.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2017/8/10.
 */

public abstract class BaseRecyclerViewAdapter <T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements View.OnClickListener {
    OnItemClickListener mOnItemClickListener;
    private WeakReference<RecyclerView> mRecyclerView;

    public WeakReference<RecyclerView> getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<>((RecyclerView) parent);
        return onCreateVH(parent, viewType);
    }

    public abstract T onCreateVH(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (holder != null) {
            if (mOnItemClickListener != null)
                holder.itemView.setOnClickListener(this);
            onBindVH(holder, position);
        }
    }

    abstract public void onBindVH(T holder, int position);

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        RecyclerView recyclerView = mRecyclerView.get();
        if (recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            mOnItemClickListener.onItemClick(v, position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
