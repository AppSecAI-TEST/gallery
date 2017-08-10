package com.lam.gallery.internal.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.internal.entity.MediaFile;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lenovo on 2017/8/1.
 */

public class FileListAdapter extends BaseRecyclerViewAdapter<FileListAdapter.FileListViewHolder> implements View.OnClickListener {

    private List<MediaFile> mMediaFileList;
    private int mSelectedFilePos;

    public FileListAdapter(List<MediaFile> mediaFileList, int selectedFilePos) {
        mMediaFileList = mediaFileList;
        mSelectedFilePos = selectedFilePos;
    }

    public void setMediaFileList(List<MediaFile> mediaFileList) {
        mMediaFileList = mediaFileList;
    }

    public void setSelectedFilePos(int selectedFilePos) {
        mSelectedFilePos = selectedFilePos;
    }

    @Override
    public FileListViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_list, null);
        FileListViewHolder fileListViewHolder = new FileListViewHolder(view);
        if (mOnItemClickListener != null)
            fileListViewHolder.itemView.setOnClickListener(this);
        return fileListViewHolder;
    }

    @Override
    public void onBindVH(FileListViewHolder holder, int position) {
        ImageView fileNameSelectedView = holder.getFileSelect();
        final ImageView fileCoverView = holder.getFileCover();
        //初始化类
        fileNameSelectedView.setVisibility(View.GONE);
        // 标记类
        fileCoverView.setTag(position);
        //渲染加载ui
        holder.getFileName().setText(mMediaFileList.get(position).getFileName());
        holder.getFileCount().setText(mMediaFileList.get(position).getCount() + "张");
        if(mSelectedFilePos == position) {
            holder.getFileSelect().setImageResource(R.drawable.footer_circle_green_16);
            holder.getFileSelect().setVisibility(View.VISIBLE);
        }
        ConfigSpec.getInstance().mImageEngine.loadThumbnail(new WeakReference<>(fileCoverView),
                position, mMediaFileList.get(position).getCoverPathId());
    }

    public class FileListViewHolder extends RecyclerView.ViewHolder{
        private ImageView fileCover;
        private TextView fileName;
        private TextView fileCount;
        private ImageView fileSelect;
        private FileListViewHolder(View itemView) {
            super(itemView);
            fileCover = (ImageView) itemView.findViewById(R.id.iv_file_cover);
            fileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            fileCount = (TextView) itemView.findViewById(R.id.tv_file_count);
            fileSelect = (ImageView) itemView.findViewById(R.id.iv_file_select);
        }
        private ImageView getFileCover() {
            return fileCover;
        }
        private TextView getFileName() {
            return fileName;
        }
        private TextView getFileCount() {
            return fileCount;
        }
        private ImageView getFileSelect() {
            return fileSelect;
        }
    }

    @Override
    public int getItemCount() {
        return mMediaFileList == null? 0 : mMediaFileList.size();
    }

}
