package com.lam.gallery.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lam.gallery.Animation.ValueAnimatorManager;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaGridAdapter.onClickToIntent {
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


    private SparseArray<String> mFileNameArray;
    private SparseArray<String> mMediaNameArray;
    private SparseArray<String> mMediaPathArray;
    private SparseArray<String> mMediaStoreDateArray;
    private SparseArray<String> mFilesNameArray;
    private SparseArray<String> mFilesCountArray;
    private SparseArray<String> mFileCoverArray;
    private SparseArray<String> mSelectFilePictureArray;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private MediaManager mMediaManager;
    private MediaGridAdapter mMediaGridAdapter;
    private FilesListAdapter mFilesListAdapter;
    private boolean isOriginMedia;
    private int mSelectedFilePos;

    public static final String IS_ORIGIN_MEDIA = "is origin media";
    public static final String SELECTED_SET = "selected set";

    public static void start(Context context, boolean isOriginMedia, HashSet<String> selectSet) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(IS_ORIGIN_MEDIA, isOriginMedia);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_SET, selectSet);
        starter.putExtra(SELECTED_SET, bundle);
        context.startActivity(starter);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialization();
        initData();

        //RecyclerView相关
        mMediaGridAdapter = new MediaGridAdapter(mMediaPathArray, mBtTitleSend, mTvFooterPreview);
        mMediaGridAdapter.setOnClickToIntent(this);
        mRvGalleryGrid.setLayoutManager(mGridLayoutManager);
        mRvGalleryGrid.setAdapter(mMediaGridAdapter);

        mFilesListAdapter = new FilesListAdapter(mFilesNameArray, mFilesCountArray, mFileCoverArray, mMediaPathArray.size());
        mRvFileList.setLayoutManager(mLinearLayoutManager);
        mRvFileList.setAdapter(mFilesListAdapter);
        mFilesListAdapter.setOnFileListItemClickListener(new FilesListAdapter.OnFileListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSelectedFilePos = position;
                if(position == 0) {
                    mMediaGridAdapter.setMediaPathArray(mMediaPathArray);
                } else {
                    mSelectFilePictureArray = mMediaManager.findMediaByFileName(mFilesNameArray.get(position));
                    Log.d(TAG, "onItemClick: " + mSelectFilePictureArray.size());
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
        mTvFooterPreview.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: ");
        setIntent(intent);
        if(intent.getBundleExtra(SELECTED_SET) != null) {
            HashSet<String> selectSet = (HashSet) intent.getBundleExtra(SELECTED_SET).get(SELECTED_SET);
            isOriginMedia = intent.getBooleanExtra(IS_ORIGIN_MEDIA, false);
            //更新UI
            mMediaGridAdapter.setSelectSet(selectSet);
            mMediaGridAdapter.notifyDataSetChanged();
            if(isOriginMedia) {
                mIvFooterOrigin.setImageResource(R.drawable.footer_circle_green_16);
            } else {
                mIvFooterOrigin.setImageResource(R.drawable.footer_circle_16);
            }
            if(selectSet.size() == 0 ) {
                mBtTitleSend.setBackgroundColor(0xFF094909);
                mBtTitleSend.setText("发送");
                mBtTitleSend.setTextColor(0xFFA1A1A1);
                mTvFooterPreview.setText("预览");
                mTvFooterPreview.setTextColor(0xFF5B5B5B);
            } else {
                mBtTitleSend.setBackgroundColor(0xFF19C917);
                mBtTitleSend.setText("发送(" + selectSet.size() + "/9)");
                mBtTitleSend.setTextColor(Color.WHITE);
                mTvFooterPreview.setText("预览(" + selectSet.size() + ")");
                mTvFooterPreview.setTextColor(Color.WHITE);
            }
        }
    }

    /**
     * 成员变量等初始化工作
     */
    private void initialization() {
        this.mFileNameArray = new SparseArray<>();
        this.mMediaNameArray = new SparseArray<>();
        this.mMediaPathArray = new SparseArray<>();
        this.mMediaStoreDateArray = new SparseArray<>();
        this.mGridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        this.mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        this.mFilesNameArray = new SparseArray<>();
        this.mFilesCountArray = new SparseArray<>();
        this.mFileCoverArray = new SparseArray<>();
        this.mSelectFilePictureArray = new SparseArray<>();
        this.mMediaManager = new MediaManager();
        this.isOriginMedia = false;
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
                if (! isOriginMedia) {
                    mIvFooterOrigin.setImageResource(R.drawable.footer_circle_green_16);
                } else {
                    mIvFooterOrigin.setImageResource(R.drawable.footer_circle_16);
                }
                isOriginMedia = !isOriginMedia;
                break;
            case R.id.tv_footer_origin:
                if (! isOriginMedia) {
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
            case R.id.tv_footer_preview:
                if(mMediaGridAdapter.getSelectSet().size() != 0) {
                    PreviewActivity.start(this, mMediaGridAdapter.getSelectSet(), null, isOriginMedia, -1);
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

        ValueAnimator fileListAnimator;
        ValueAnimator fileBackAnimator;
        if (mRvFileList.getHeight() == 0) {
            fileListAnimator = ValueAnimatorManager.viewAnimator(0, maxHeight, 300, mRvFileList);
            fileBackAnimator = ValueAnimatorManager.viewAnimator(0, maxHeight, 300, mViewFileListBackground);
            Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_alpha_to_translucent);
            mViewFileListBackground.startAnimation(alphaAnimation);
        } else {
            fileListAnimator = ValueAnimatorManager.viewAnimator(mRvFileList.getLayoutParams().height, 0, 300, mRvFileList);
            fileBackAnimator = ValueAnimatorManager.viewAnimator(mViewFileListBackground.getLayoutParams().height, 0, 300, mViewFileListBackground);
            Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_translucent_to_alpha);
            mViewFileListBackground.startAnimation(alphaAnimation);
        }
        fileListAnimator.start();
        fileBackAnimator.start();
    }

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

    @Override
    public void clickToIntent(int position) {
        String fileName = mFilesNameArray.get(mSelectedFilePos);
        if(fileName == null) {
            fileName = "所有图片";
        }
        PreviewActivity.start(this, mMediaGridAdapter.getSelectSet(), fileName, isOriginMedia, position);
    }
}
