package com.lam.gallery.activity;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.Task.MediaTask;
import com.lam.gallery.adapter.RecyclerViewGridAdapter;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.MediaManager;

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


    private SparseArrayCompat<String> mFileNameArray;
    private SparseArrayCompat<String> mMediaNameArray;
    private SparseArrayCompat<String> mMediaPathArray;
    private SparseArrayCompat<String> mMediaStoreDateArray;
    private GridLayoutManager mGridLayoutManager;
    private boolean isOriginMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialization();
        initData();

        //RecyclerView相关
        RecyclerViewGridAdapter recyclerViewGridAdapter = new RecyclerViewGridAdapter(mMediaPathArray, mBtTitleSend, mTvFooterPreview);
        mRvGalleryGrid.setLayoutManager(mGridLayoutManager);
        mRvGalleryGrid.setAdapter(recyclerViewGridAdapter);

        mIvFooterOrigin.setOnClickListener(this);
        mTvFooterOrigin.setOnClickListener(this);
        mIvTitleLeftPoint.setOnClickListener(this);
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
    }

    /**
     * 从数据库获取Media数据
     */
    private void initData() {
        MediaManager mediaManager = new MediaManager();
        List<Media> mediaList = mediaManager.findAllMedia();
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
        }
    }
}
