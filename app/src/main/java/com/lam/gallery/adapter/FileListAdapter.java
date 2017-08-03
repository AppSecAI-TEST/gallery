package com.lam.gallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.db.MediaFile;
import com.lam.gallery.manager.GalleryBitmapFactory;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/1.
 */

public class FileListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<MediaFile> mMediaFileList;
    private int mSelectedFilePos;
    protected OnFileItemClickListener mOnFileItemClickListener;
    private WeakReference<RecyclerView> mRecyclerView;

    public FileListAdapter(List<MediaFile> mediaFileList, int selectedFilePos) {
        mMediaFileList = mediaFileList;
        mSelectedFilePos = selectedFilePos;
    }

    public void setOnFileItemClickListener(OnFileItemClickListener listener) {
        mOnFileItemClickListener = listener;
    }

    public void setMediaFileList(List<MediaFile> mediaFileList) {
        mMediaFileList = mediaFileList;
    }

    public void setSelectedFilePos(int selectedFilePos) {
        mSelectedFilePos = selectedFilePos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<>((RecyclerView) parent);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_list, null);
        FileListViewHolder fileListViewHolder = new FileListViewHolder(view);
        return fileListViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FileListViewHolder fileListViewHolder = (FileListViewHolder) holder;
        ImageView fileNameSelectedView = fileListViewHolder.getFileSelect();
        final ImageView fileCoverView = fileListViewHolder.getFileCover();
        //初始化类
        fileNameSelectedView.setVisibility(View.GONE);
        // 标记类
        fileCoverView.setTag(position);
        //渲染加载ui
        fileListViewHolder.getFileName().setText(mMediaFileList.get(position).getFileName());
        fileListViewHolder.getFileCount().setText(mMediaFileList.get(position).getCount() + "张");
        if(mSelectedFilePos == position) {
            fileListViewHolder.getFileSelect().setImageResource(R.drawable.footer_circle_green_16);
            fileListViewHolder.getFileSelect().setVisibility(View.VISIBLE);
        }
        GalleryBitmapFactory.loadThumbnailWithTag(mMediaFileList.get(position).getFileCoverPath(), mMediaFileList.get(position).getCoverPathId(), fileCoverView, position);
        if (holder != null) {
            if (mOnFileItemClickListener != null)
                holder.itemView.setOnClickListener(this);
        }
    }

    public class FileListViewHolder extends RecyclerView.ViewHolder{
        private ImageView fileCover;
        private TextView fileName;
        private TextView fileCount;
        private ImageView fileSelect;
        public FileListViewHolder(View itemView) {
            super(itemView);
            fileCover = (ImageView) itemView.findViewById(R.id.iv_file_cover);
            fileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            fileCount = (TextView) itemView.findViewById(R.id.tv_file_count);
            fileSelect = (ImageView) itemView.findViewById(R.id.iv_file_select);
        }
        public ImageView getFileCover() {
            return fileCover;
        }
        public TextView getFileName() {
            return fileName;
        }
        public TextView getFileCount() {
            return fileCount;
        }
        public ImageView getFileSelect() {
            return fileSelect;
        }
    }

    @Override
    public int getItemCount() {
        return mMediaFileList == null? 0 : mMediaFileList.size();
    }

    @Override
    public void onClick(View v) {
        RecyclerView recyclerView = mRecyclerView.get();
        if (recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            mOnFileItemClickListener.onFileItemClick(v, position);
        }
    }

    public interface OnFileItemClickListener {
        void onFileItemClick(View view, int position);
    }

}
