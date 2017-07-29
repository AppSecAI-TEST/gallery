package com.lam.gallery.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.Task.MediaTask;
import com.lam.gallery.adapter.FilesListAdapter;
import com.lam.gallery.adapter.MediaGridAdapter;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.MediaManager;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.iv_title_left_point)
    ImageView mIvTitleLeftPoint;
    @BindView(R.id.view_title_line)
    View mViewTitleLine;
    @BindView(R.id.bt_title_send)
    Button mBtTitleSend;
    @BindView(R.id.gallery_title)
    RelativeLayout mGalleryTitle;
    @BindView(R.id.rv_gallery_grid)
    RecyclerView mRvGalleryGrid;
    @BindView(R.id.tv_footer_file_name)
    TextView mTvFooterFileName;
    @BindView(R.id.iv_footer_origin)
    ImageView mIvFooterOrigin;
    @BindView(R.id.tv_footer_preview)
    TextView mTvFooterPreview;
    @BindView(R.id.rl_gallery_footer)
    RelativeLayout mRlGalleryFooter;
    @BindView(R.id.tv_footer_origin)
    TextView mTvFooterOrigin;
    @BindView(R.id.rv_file_list)
    RecyclerView mRvFileList;
    @BindView(R.id.iv_footer_file_name)
    ImageView mIvFooterFileName;
    @BindView(R.id.view_file_list_background)
    View mViewFileListBackground;
    @BindView(R.id.rl_file_item)
    RelativeLayout mRlFileItem;


    private SparseArrayCompat<String> mFileNameArray;
    private SparseArrayCompat<String> mMediaNameArray;
    private SparseArrayCompat<String> mMediaPathArray;
    private SparseArrayCompat<String> mMediaStoreDateArray;
    private SparseArrayCompat<String> mFilesNameArray;
    private SparseArrayCompat<String> mFilesCountArray;
    private SparseArrayCompat<String> mFileCoverArray;
    private SparseArrayCompat<String> mSelectFilePictureArray;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private MediaManager mMediaManager;
    private MediaGridAdapter mMediaGridAdapter;
    private FilesListAdapter mFilesListAdapter;
    private boolean isOriginMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialization();
        initData();

        //RecyclerView相关
        mMediaGridAdapter = new MediaGridAdapter(mMediaPathArray, mBtTitleSend, mTvFooterPreview);
        mRvGalleryGrid.setLayoutManager(mGridLayoutManager);
        mRvGalleryGrid.setAdapter(mMediaGridAdapter);

        mFilesListAdapter = new FilesListAdapter(mFilesNameArray, mFilesCountArray, mFileCoverArray, mMediaPathArray.size());
        mRvFileList.setLayoutManager(mLinearLayoutManager);
        mRvFileList.setAdapter(mFilesListAdapter);
        mFilesListAdapter.setOnFileListItemClickListener(new FilesListAdapter.OnFileListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 0) {
                    mMediaGridAdapter.setMediaPathArray(mMediaPathArray);
                    //mMediaGridAdapter = new MediaGridAdapter(mMediaPathArray, mBtTitleSend, mTvFooterPreview);
                } else {
                    mSelectFilePictureArray = mMediaManager.findMediaByFileName(mFilesNameArray.get(position));
                    Log.d(TAG, "onItemClick: " + mSelectFilePictureArray.size());
//                    mMediaGridAdapter = new MediaGridAdapter(mSelectFilePictureArray, mBtTitleSend, mTvFooterPreview);
                    mMediaGridAdapter.setMediaPathArray(mSelectFilePictureArray);
                }
                mRvGalleryGrid.setLayoutManager(mGridLayoutManager);
                mMediaGridAdapter.notifyDataSetChanged();
                mTvFooterFileName.setText(mFilesNameArray.get(position));
                fileListAnimator();
            }
        });

        mIvFooterOrigin.setOnClickListener(this);
        mTvFooterOrigin.setOnClickListener(this);
        mIvTitleLeftPoint.setOnClickListener(this);
        mTvFooterFileName.setOnClickListener(this);
        mIvFooterFileName.setOnClickListener(this);
        mViewFileListBackground.setOnClickListener(this);
    }

    /**
     * 成员变量等初始化工作
     */
    private void initialization() {
        this.mFileNameArray = new SparseArrayCompat<>();
        this.mMediaNameArray = new SparseArrayCompat<>();
        this.mMediaPathArray = new SparseArrayCompat<>();
        this.mMediaStoreDateArray = new SparseArrayCompat<>();
        this.mGridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        this.mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        this.mFilesNameArray = new SparseArrayCompat<>();
        this.mFilesCountArray = new SparseArrayCompat<>();
        this.mFileCoverArray = new SparseArrayCompat<>();
        this.mSelectFilePictureArray = new SparseArrayCompat<>();
        this.mMediaManager = new MediaManager();
    }

    /**
     * 从数据库获取Media数据
     */
    private void initData() {
        List<Media> mediaList = mMediaManager.findAllMedia();
        int position = 0;
        for (Media media : mediaList) {
            mFileNameArray.put(position, media.getFileName());
            mMediaNameArray.put(position, media.getImageName());
            mMediaPathArray.put(position, media.getUrl());
            mMediaStoreDateArray.put(position, media.getStoreDate());
            ++position;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaTask.shutDown();
    }

    /**
     * 监听事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_footer_origin:
                if (isOriginMedia) {
                    mIvFooterOrigin.setImageResource(R.drawable.footer_circle_green_16);
                } else {
                    mIvFooterOrigin.setImageResource(R.drawable.footer_circle_16);
                }
                isOriginMedia = !isOriginMedia;
                break;
            case R.id.tv_footer_origin:
                if (isOriginMedia) {
                    mIvFooterOrigin.setImageResource(R.drawable.footer_circle_green_16);
                } else {
                    mIvFooterOrigin.setImageResource(R.drawable.footer_circle_16);
                }
                isOriginMedia = !isOriginMedia;
                break;
            case R.id.iv_title_left_point:
                finish();
                break;
            case R.id.tv_footer_file_name:
                initFileListData();
                fileListAnimator();
                break;
            case R.id.iv_footer_file_name:
                initFileListData();
                fileListAnimator();
                break;
            case R.id.view_file_list_background:
                if (mRlFileItem.getVisibility() != View.GONE) {
                    fileListAnimator();
                }
                break;
        }
    }

    /**
     * 文件选择列表出现动画
     */
    private void fileListAnimator() {
        mRvFileList.setVisibility(View.VISIBLE);
        mViewFileListBackground.setVisibility(View.VISIBLE);
        int maxHeight = mRvGalleryGrid.getHeight() - mRlGalleryFooter.getHeight();
        final ValueAnimator fileListAnimator;
        final ValueAnimator fileBackAnimator;
        if (mRvFileList.getHeight() == 0) {
            fileListAnimator = ValueAnimator.ofInt(0, maxHeight);
            fileBackAnimator = ValueAnimator.ofInt(0, maxHeight);
            Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_alpha_to_translucent);
            mViewFileListBackground.startAnimation(alphaAnimation);
        } else {
            fileListAnimator = ValueAnimator.ofInt(mRvFileList.getLayoutParams().height, 0);
            fileBackAnimator = ValueAnimator.ofInt(mViewFileListBackground.getLayoutParams().height, 0);
            Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_translucent_to_alpha);
            mViewFileListBackground.startAnimation(alphaAnimation);
        }
        fileListAnimator.setDuration(300);
        fileListAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRvFileList.getLayoutParams().height = (int) animation.getAnimatedValue();
                mRvFileList.setLayoutParams(mRvFileList.getLayoutParams());
            }
        });
        fileBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mViewFileListBackground.getLayoutParams().height = (int) animation.getAnimatedValue();
                mViewFileListBackground.setLayoutParams(mViewFileListBackground.getLayoutParams());
            }
        });
        fileListAnimator.start();
        fileBackAnimator.start();
    }

    /**
     *
     */
    private void initFileListData() {
        mFilesNameArray.clear();
        mFileCoverArray.clear();
        mFilesCountArray.clear();
        //首先获得文件名相关数组
        HashSet tempSet = new HashSet();
        int position = 0;
        mFilesNameArray.put(position, "所有图片");
        mFilesCountArray.put(position, mMediaPathArray.size() + "");
        mFileCoverArray.put(position, mMediaPathArray.get(0));
        ++position;
        for (int i = 0; i < mFileNameArray.size(); ++i) {
            String fileName = mFileNameArray.get(i);
            if (!tempSet.contains(fileName)) {
                tempSet.add(fileName);
                mFilesNameArray.put(position, fileName);
                Log.d(TAG, "initFileListData: " + fileName);
                ++position;
            }
        }
        position = 1;
        for (int i = 1; i < mFilesNameArray.size(); ++i) {
            mSelectFilePictureArray = mMediaManager.findMediaByFileName(mFilesNameArray.get(i));
            mFilesCountArray.put(position, mSelectFilePictureArray.size() + "");
            mFileCoverArray.put(position, mSelectFilePictureArray.get(0));
            ++position;
        }
        //置空tempSet
        tempSet.clear();
        tempSet = null;
        mFilesListAdapter.setFileCoverArray(mFileCoverArray);
        mFilesListAdapter.setFilesNameArray(mFilesNameArray);
        mFilesListAdapter.setFilesCountArray(mFilesCountArray);
        mFilesListAdapter.setAllMediaCount(mMediaPathArray.size());
        mFilesListAdapter.notifyDataSetChanged();
    }
}
