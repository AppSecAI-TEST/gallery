package com.lam.gallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.Task.MediaTask;
import com.lam.gallery.manager.BitmapManager;
import com.lam.gallery.manager.LruCacheManager;

/**
 * Created by lenovo on 2017/7/29.
 */

public class FilesListAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private static final String TAG = "FilesListAdapter";
    private SparseArrayCompat<String> mFilesNameArray;
    private SparseArrayCompat<String> mFilesCountArray;
    private SparseArrayCompat<String> mFileCoverArray;
//    private SparseArrayCompat<String> mSelectFilePictureArray;
    private Context mContext;
    private int mAllMediaCount;
    private OnFileListItemClickListener mOnFileListItemClickListener = null;
    private int mFileSelectPosition = 0;

    public FilesListAdapter(SparseArrayCompat<String> filesNameArray, SparseArrayCompat<String> filesCountArray, SparseArrayCompat<String> fileCoverArray, int allMediaCount) {
        mFilesNameArray = filesNameArray;
        mFilesCountArray = filesCountArray;
        mFileCoverArray = fileCoverArray;
//        mSelectFilePictureArray = new SparseArrayCompat<>();
        mAllMediaCount = allMediaCount;
    }

    public void setFilesNameArray(SparseArrayCompat<String> filesNameArray) {
        mFilesNameArray = filesNameArray;
    }

    public void setFilesCountArray(SparseArrayCompat<String> filesCountArray) {
        mFilesCountArray = filesCountArray;
    }

    public void setFileCoverArray(SparseArrayCompat<String> fileCoverArray) {
        mFileCoverArray = fileCoverArray;
    }

    public void setAllMediaCount(int allMediaCount) {
        mAllMediaCount = allMediaCount;
    }

    public void setOnFileListItemClickListener(OnFileListItemClickListener onFileListItemClickListener) {
        mOnFileListItemClickListener = onFileListItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_list, null);
        FileListViewHolder fileListViewHolder = new FileListViewHolder(view);
        view.setOnClickListener(this);
        return fileListViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FileListViewHolder fileListViewHolder = (FileListViewHolder) holder;
        holder.itemView.setTag(position);
        fileListViewHolder.getFileSelect().setImageResource(R.drawable.loading);
        if(mFileSelectPosition == position) {
            fileListViewHolder.getFileSelect().setImageResource(R.drawable.footer_circle_green_16);
            fileListViewHolder.getFileSelect().setVisibility(View.VISIBLE);
        } else {
            fileListViewHolder.getFileSelect().setVisibility(View.GONE);
        }
        fileListViewHolder.getFileCover().setTag(position);

        fileListViewHolder.getFileName().setText(mFilesNameArray.get(position));
        fileListViewHolder.getFileCount().setText(mFilesCountArray.get(position) + "张");
        Bitmap bitmap = LruCacheManager.getBitmapFromCache(mFileCoverArray.get(position));
        if(bitmap == null) {
            MediaTask.addTask(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapManager.processBitmap(mFileCoverArray.get(position), 90);
                    LruCacheManager.addBitmapToCache(mFileCoverArray.get(position), bitmap);
                    fileListViewHolder.getFileCover().post(new Runnable() {
                        @Override
                        public void run() {
                            if((int)fileListViewHolder.getFileCover().getTag() == position) {
                                fileListViewHolder.getFileCover().setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        } else {
            fileListViewHolder.getFileCover().setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return mFilesNameArray == null ? 0 : mFilesNameArray.size();
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
    public void onClick(View v) {
        if (mOnFileListItemClickListener != null) {      //getTag获取的即是点击位置
            mFileSelectPosition = (int) v.getTag();
            Log.d(TAG, "onClick: " + mFileSelectPosition);
            mOnFileListItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public interface OnFileListItemClickListener {
        void onItemClick(View view, int position);
    }


}
