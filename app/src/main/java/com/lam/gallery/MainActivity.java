package com.lam.gallery;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lam.gallery.db.Media;
import com.lam.gallery.db.MediaManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.iv_title_left_point)
    ImageView mIvTitleLeftPoint;
    @BindView(R.id.view_title_line)
    View mViewTitleLine;
    @BindView(R.id.gallery_title)
    RelativeLayout mGalleryTitle;
    @BindView(R.id.rl_gallery_grid)
    RecyclerView mRlGalleryGrid;
    @BindView(R.id.tv_footer_file_name)
    TextView mTvFooterFileName;
    @BindView(R.id.rl_gallery_footer)
    RelativeLayout mRlGalleryFooter;

    private SparseArrayCompat<String> mFileNameArray;
    private SparseArrayCompat<String> mMediaNameArray;
    private SparseArrayCompat<String> mMediaPathArray;
    private SparseArrayCompat<String> mMediaStoreDateArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialization();
        initData();
    }

    /**
     * 成员变量等初始化工作
     */
    private void initialization() {
        this.mFileNameArray = new SparseArrayCompat<>();
        this.mMediaNameArray = new SparseArrayCompat<>();
        this.mMediaPathArray = new SparseArrayCompat<>();
        this.mMediaStoreDateArray = new SparseArrayCompat<>();
    }

    /**
     * 从数据库获取Media数据
     */
    private void initData() {
        MediaManager mediaManager = new MediaManager();
        List<Media> mediaList = mediaManager.findAllMedia();
        int position = 0;
        for(Media media: mediaList) {
            mFileNameArray.put(position, media.getFileName());
            mMediaNameArray.put(position, media.getImageName());
            mMediaPathArray.put(position, media.getUrl());
            mMediaStoreDateArray.put(position, media.getStoreDate());
            ++position;
        }
    }
}
