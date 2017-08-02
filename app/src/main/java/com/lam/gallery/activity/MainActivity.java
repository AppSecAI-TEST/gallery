package com.lam.gallery.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
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
import com.lam.gallery.Task.ThreadTask;
import com.lam.gallery.adapter.FileListAdapter;
import com.lam.gallery.adapter.MediaGridAdapter;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.MediaFile;
import com.lam.gallery.db.SelectedMedia;
import com.lam.gallery.manager.MediaManager;
import com.lam.gallery.manager.ValueAnimatorManager;
import com.lam.gallery.ui.UiManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lam.gallery.R.id.tv_footer_preview;

public class MainActivity extends AppCompatActivity implements MediaManager.InitDataListener, View.OnClickListener, MediaGridAdapter.onClickToIntent, SelectedMedia.UpdateUi, FileListAdapter.OnFileItemClickListener {

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
    @BindView(R.id.iv_footer_file_name)
    ImageView mIvFooterFileName;
    @BindView(R.id.iv_footer_origin)
    ImageView mIvFooterOrigin;
    @BindView(R.id.tv_footer_origin)
    TextView mTvFooterOrigin;
    @BindView(tv_footer_preview)
    TextView mTvFooterPreview;
    @BindView(R.id.rl_gallery_footer)
    RelativeLayout mRlGalleryFooter;
    @BindView(R.id.view_file_list_background)
    View mViewFileListBackground;
    @BindView(R.id.rv_file_list)
    RecyclerView mRvFileList;
    @BindView(R.id.rl_file_item)
    RelativeLayout mRlFileItem;

    private List<Media> mMediaList;
    private List<MediaFile> mMediaFileList;
    private List<Media> mSelectMediaFileList;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private MediaGridAdapter mMediaGridAdapter;
    private FileListAdapter mFilesListAdapter;
    private Handler mHandler;
    private int mSelectedFilePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelectedMedia.setUpdateUi(this);
    }

    //数据的初始化
    private void init() {
        mSelectedFilePos = 0;
        mMediaList = new ArrayList<>();
        mMediaFileList = new ArrayList<>();
        mSelectMediaFileList = new ArrayList<>();
        mGridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mHandler = new Handler();
        ThreadTask.addTask(new Runnable() {
            @Override
            public void run() {     //开启子线程加载数据
                MediaManager mediaManager = new MediaManager();
                mediaManager.findAllMedia(MainActivity.this);
            }
        });
        //先加载空白的RecyclerView
        mMediaGridAdapter = new MediaGridAdapter(mMediaList);
        mMediaGridAdapter.setOnClickToIntent(this);
        mRvGalleryGrid.setLayoutManager(mGridLayoutManager);
        mRvGalleryGrid.setAdapter(mMediaGridAdapter);
        mFilesListAdapter = new FileListAdapter(mMediaFileList, mSelectedFilePos);
        mFilesListAdapter.setOnFileItemClickListener(this);
        mRvFileList.setLayoutManager(mLinearLayoutManager);
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

    @Override
    public void getData(List mediaList, final List mediaFileList) {
        mMediaList = mediaList;
        mMediaFileList = mediaFileList;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMediaGridAdapter.setMediaList(mMediaList);
                mMediaGridAdapter.notifyDataSetChanged();
                mFilesListAdapter.setMediaFileList(mediaFileList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_footer_origin || v.getId() == R.id.tv_footer_origin)
            UiManager.listenerUpdateOrigin(mIvFooterOrigin);
        if(v.getId() == R.id.iv_title_left_point)   //反馈数据给客户端
            finish();
        if(v.getId() == R.id.bt_title_send && SelectedMedia.getSelectedMediaList().size() != 0) //反馈数据给客户端
            finish();
        if(v.getId() == R.id.iv_footer_file_name || v.getId() == R.id.tv_footer_file_name || v.getId() == R.id.view_file_list_background)
            fileListAnimator();
        if(v.getId() == R.id.tv_footer_preview && SelectedMedia.selectedMediaCount() != 0)
            PreViewActivity.start(this, -1, null);
    }

    /**
     * 文件选择列表出现动画
     */
    private void fileListAnimator() {
        mFilesListAdapter.notifyDataSetChanged();
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

    @Override
    public void clickToIntent(int position) {
        Log.d(TAG, "clickToIntent: ");
        PreViewActivity.start(this, position, mMediaFileList.get(mSelectedFilePos).getFileName());
    }

    @Override
    public void updateAddSelectMediaUi() {
        Log.d(TAG, "updateAddSelectMediaUi: ");
        UiManager.updatePreViewText(mTvFooterPreview);
        UiManager.updateSendButton(mBtTitleSend);
    }

    @Override
    public void updateRemoveSelectMediaUi() {
        Log.d(TAG, "updateRemoveSelectMediaUi: ");
        UiManager.updatePreViewText(mTvFooterPreview);
        UiManager.updateSendButton(mBtTitleSend);
    }

    @Override
    public void onFileItemClick(View view, final int position) {
        Log.d(TAG, "onFileItemClick: " + position);
        mSelectedFilePos = position;
        mTvFooterFileName.setText(mMediaFileList.get(position).getFileName());
        mMediaGridAdapter.setMediaList(null);
        mMediaGridAdapter.notifyDataSetChanged();
        ThreadTask.clear();
        if(position == 0) {
            mMediaGridAdapter.setMediaList(mMediaList);
            mMediaGridAdapter.notifyDataSetChanged();
            mSelectMediaFileList = mMediaList;
        } else {
            ThreadTask.addTask(new Runnable() {
                @Override
                public void run() {
                    MediaManager mediaManager = new MediaManager();
                    mSelectMediaFileList = mediaManager.findMediaListByFileName(mMediaFileList.get(position).getFileName());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mMediaGridAdapter.setMediaList(mSelectMediaFileList);
                            mMediaGridAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
        mFilesListAdapter.setSelectedFilePos(position);
        fileListAnimator();
        UiManager.updateSendButton(mBtTitleSend);
        UiManager.updatePreViewText(mTvFooterPreview);
    }
//    //获取previewActivity变更ui的通知
//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//
//    }

    @Override
    protected void onDestroy() {
        ThreadTask.clear();
        ThreadTask.shutDown();
        SelectedMedia.clearData();
        UiManager.setIsOriginMedia(false);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UiManager.updataOiginView(mIvFooterOrigin);
        UiManager.updateSendButton(mBtTitleSend);
        UiManager.updatePreViewText(mTvFooterPreview);
        mMediaGridAdapter.notifyDataSetChanged();
    }
}
