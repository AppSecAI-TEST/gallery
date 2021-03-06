package com.lam.gallery.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.R2;
import com.lam.gallery.internal.entity.Media;
import com.lam.gallery.internal.entity.MediaFile;
import com.lam.gallery.internal.entity.SelectedMedia;
import com.lam.gallery.internal.ui.PreviewActivity;
import com.lam.gallery.internal.ui.adapter.FileListAdapter;
import com.lam.gallery.internal.ui.adapter.MediaGridAdapter;
import com.lam.gallery.internal.ui.view.UiManager;
import com.lam.gallery.internal.ui.view.ValueAnimatorManager;
import com.lam.gallery.internal.manager.MediaManager;
import com.lam.gallery.internal.task.BitmapTaskDispatcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity implements MediaManager.InitDataListener, View.OnClickListener, MediaGridAdapter.onClickToIntent, SelectedMedia.UpdateUi, FileListAdapter.OnItemClickListener {

    private static final String TAG = "GalleryActivity";
    @BindView(R2.id.iv_title_left_point)
    ImageView mIvTitleLeftPoint;
    @BindView(R2.id.view_title_line)
    View mViewTitleLine;
    @BindView(R2.id.bt_title_send)
    Button mBtTitleSend;
    @BindView(R2.id.gallery_title)
    RelativeLayout mGalleryTitle;
    @BindView(R2.id.rv_gallery_grid)
    RecyclerView mRvGalleryGrid;
    @BindView(R2.id.tv_footer_file_name)
    TextView mTvFooterFileName;
    @BindView(R2.id.iv_footer_file_name)
    ImageView mIvFooterFileName;
    @BindView(R2.id.iv_footer_origin)
    ImageView mIvFooterOrigin;
    @BindView(R2.id.tv_footer_origin)
    TextView mTvFooterOrigin;
    @BindView(R2.id.tv_footer_preview)
    TextView mTvFooterPreview;
    @BindView(R2.id.rl_gallery_footer)
    RelativeLayout mRlGalleryFooter;
    @BindView(R2.id.view_file_list_background)
    View mViewFileListBackground;
    @BindView(R2.id.rv_file_list)
    RecyclerView mRvFileList;
    @BindView(R2.id.rl_file_item)
    RelativeLayout mRlFileItem;

    private List<Media> mMediaList;
    private List<MediaFile> mMediaFileList;
    private List<Media> mSelectMediaFileList;
    private MediaGridAdapter mMediaGridAdapter;
    private FileListAdapter mFilesListAdapter;
    private Handler mHandler;
    private int mSelectedFilePos;
    public static final String EXTRA_RESULT_SELECTION_ID = "extra_result_selection_ids";
    public static final String EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path";
    public static final String EXTRA_RESULT_SELECTION_ORIGIN = "extra_result_selection_origin";
    private static final int REQUEST_CODE_PREVIEW = 0x741;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        setListener();
    }

    //数据的初始化
    private void init() {
        mSelectedFilePos = 0;
        mMediaList = new ArrayList<>();
        mMediaFileList = new ArrayList<>();
        mSelectMediaFileList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(GalleryActivity.this, 3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GalleryActivity.this);
        mHandler = new Handler();
        BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
            @Override
            public Object doTask() {
                MediaManager mediaManager = new MediaManager();
                mediaManager.findAllMedia(GalleryActivity.this);
                return null;
            }
        });
        //先加载空白的RecyclerView
        mMediaGridAdapter = new MediaGridAdapter(mMediaList);
        mMediaGridAdapter.setOnClickToIntent(this);
        mRvGalleryGrid.setLayoutManager(gridLayoutManager);
        mRvGalleryGrid.setAdapter(mMediaGridAdapter);
        mFilesListAdapter = new FileListAdapter(mMediaFileList, mSelectedFilePos);
        mFilesListAdapter.setOnItemClickListener(this);
        mRvFileList.setLayoutManager(linearLayoutManager);
        mRvFileList.setAdapter(mFilesListAdapter);

    }

    //监听事件的设定
    private void setListener() {
        mIvFooterOrigin.setOnClickListener(this);
        mTvFooterOrigin.setOnClickListener(this);
        mIvFooterFileName.setOnClickListener(this);
        mTvFooterFileName.setOnClickListener(this);
        mViewFileListBackground.setOnClickListener(this);
        mIvTitleLeftPoint.setOnClickListener(this);
        mBtTitleSend.setOnClickListener(this);
        mTvFooterPreview.setOnClickListener(this);
    }

    //文件选择列表出现动画
    private void fileListAnimator() {
        mFilesListAdapter.notifyDataSetChanged();
        mRvFileList.setVisibility(View.VISIBLE);
        mViewFileListBackground.setVisibility(View.VISIBLE);
        int maxHeight = mRvGalleryGrid.getHeight() - mRlGalleryFooter.getHeight();
        Log.d(TAG, "fileListAnimator: " + mRlGalleryFooter.getHeight());
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

    @Override
    public void getData(List<Media> mediaList, final List<MediaFile> mediaFileList) {
        mMediaList = mediaList;
        mMediaFileList = mediaFileList;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMediaGridAdapter.setMediaList(mMediaList);
                mMediaGridAdapter.notifyDataSetChanged();
                mFilesListAdapter.setMediaFileList(mediaFileList);
                mMediaGridAdapter.setLoad(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_footer_origin || v.getId() == R.id.tv_footer_origin)
            UiManager.listenerUpdateOrigin(mIvFooterOrigin);
        if(v.getId() == R.id.iv_title_left_point)   //反馈数据给客户端
            backToMain();
        if(v.getId() == R.id.iv_footer_file_name || v.getId() == R.id.tv_footer_file_name || v.getId() == R.id.view_file_list_background)
            fileListAnimator();
        if(v.getId() == R.id.tv_footer_preview && SelectedMedia.selectedMediaCount() != 0)
            PreviewActivity.start(new WeakReference<Activity>(GalleryActivity.this), -1, null, REQUEST_CODE_PREVIEW);
        if(v.getId() == R.id.bt_title_send && SelectedMedia.getSelectedMediaList().size() != 0)        //反馈数据给客户端
            intentForResult();
    }

    @Override
    public void clickToIntent(int position) {
        PreviewActivity.start(new WeakReference<Activity>(GalleryActivity.this), position, mMediaFileList.get(mSelectedFilePos).getFileName(), REQUEST_CODE_PREVIEW);
    }

    @Override
    public void updateAddSelectMediaUi() {
        UiManager.updatePreViewText(mTvFooterPreview);
        UiManager.updateSendButton(mBtTitleSend);
    }

    @Override
    public void updateRemoveSelectMediaUi() {
        UiManager.updatePreViewText(mTvFooterPreview);
        UiManager.updateSendButton(mBtTitleSend);
    }

    //文件选择监听
    @Override
    public void onItemClick(View view, final int position) {
        Log.d(TAG, "onItemClick: " + position);
        mSelectedFilePos = position;
        mTvFooterFileName.setText(mMediaFileList.get(position).getFileName());
        mMediaGridAdapter.setMediaList(null);
        mMediaGridAdapter.notifyDataSetChanged();
        BitmapTaskDispatcher.clear();
        if(position == 0) {
            mMediaGridAdapter.setMediaList(mMediaList);
            mMediaGridAdapter.notifyDataSetChanged();
            mSelectMediaFileList = mMediaList;
        } else {
            BitmapTaskDispatcher.getLIFOTaskDispatcher().addTask(new BitmapTaskDispatcher.TaskRunnable() {
                @Override
                public Object doTask() {
                    MediaManager mediaManager = new MediaManager();
                    mSelectMediaFileList = mediaManager.findMediaListByFileName(mMediaFileList.get(position).getFileName());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mMediaGridAdapter.setMediaList(mSelectMediaFileList);
                            mMediaGridAdapter.notifyDataSetChanged();
                        }
                    });
                    return null;
                }
            });
        }
        mFilesListAdapter.setSelectedFilePos(position);
        fileListAnimator();
        UiManager.updateSendButton(mBtTitleSend);
        UiManager.updatePreViewText(mTvFooterPreview);
        mMediaGridAdapter.setLoad(true);
    }

    //取消选择返回主module
    private void backToMain() {
        BitmapTaskDispatcher.clear();
        SelectedMedia.clearData();
        UiManager.setIsOriginMedia(false);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelectedMedia.setUpdateUi(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mRvFileList.getHeight() != 0)
            fileListAnimator();
        else {
            backToMain();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        BitmapTaskDispatcher.clear();
        SelectedMedia.clearData();
        BitmapTaskDispatcher.shutDown();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PREVIEW && resultCode == RESULT_OK) {
            intentForResult();
        }
        UiManager.updateOriginView(mIvFooterOrigin);
        UiManager.updateSendButton(mBtTitleSend);
        UiManager.updatePreViewText(mTvFooterPreview);
        mMediaGridAdapter.notifyDataSetChanged();
        mMediaGridAdapter.setLoad(true);
    }

    private void intentForResult() {
        Intent result = new Intent();
        result.putExtra(EXTRA_RESULT_SELECTION_ID, SelectedMedia.getSelectedMediaIds());
        result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, SelectedMedia.getSelectedMediaPath());
        result.putExtra(EXTRA_RESULT_SELECTION_ORIGIN, UiManager.isOriginMedia);
        setResult(RESULT_OK, result);
        finish();
    }
}
