package com.lam.gallery.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.MediaManager;
import com.lam.gallery.ui.PreViewViewPager;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity implements PreViewViewPager.OnClickItemViewListener{
    private static final String TAG = "PreviewActivity";
    @BindView(R.id.vp_preview)
    PreViewViewPager mVpPreview;
    @BindView(R.id.iv_header_back)
    ImageView mIvHeaderBack;
    @BindView(R.id.view_header_line)
    View mViewHeaderLine;
    @BindView(R.id.tv_preview_title)
    TextView mTvPreviewTitle;
    @BindView(R.id.bt_header_send)
    Button mBtHeaderSend;
    @BindView(R.id.rl_preview_header)
    RelativeLayout mRlPreviewHeader;
    @BindView(R.id.rv_preview_thumbnail)
    RecyclerView mRvPreviewThumbnail;
    @BindView(R.id.tv_footer_edit)
    TextView mTvFooterEdit;
    @BindView(R.id.iv_footer_origin)
    ImageView mIvFooterOrigin;
    @BindView(R.id.tv_footer_origin)
    TextView mTvFooterOrigin;
    @BindView(R.id.iv_footer_select)
    ImageView mIvFooterSelect;
    @BindView(R.id.tv_footer_select)
    TextView mTvFooterSelect;
    @BindView(R.id.rl_preview_footer)
    RelativeLayout mRlPreviewFooter;

    public static final String SELECTED_SET = "selected set";
    public static final String FILE_NAME = "file name";
    public static final String IS_ORIGIN_MEDIA = "is origin media";
    public static final String CURRENT_POSOTION = "current position";

    private HashSet<String> mSelectSet;
    private SparseArrayCompat<String> mMediaPathArray;
    private boolean isOriginMedia;
    private MediaManager mMediaManager;
    public int MAX_HEIGHT;
    private int mCurrentPosition;

    public static void start(Context context, HashSet<String> selectSet, String fileName, boolean isOriginMedia, int currentPosition) {
        Intent starter = new Intent(context, PreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_SET, selectSet);
        starter.putExtra(SELECTED_SET, bundle);
        starter.putExtra(FILE_NAME, fileName);
        starter.putExtra(IS_ORIGIN_MEDIA, isOriginMedia);
        starter.putExtra(CURRENT_POSOTION, currentPosition);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        mVpPreview.setOnClickItemViewListener(this);
        initialization();
    }


    @Override
    public void onClickItem(View v) {
        headerAndFooterAnimator();
    }

    /**
     * 初始化类工作
     */
    private void initialization() {
        MAX_HEIGHT = 0;
        mMediaPathArray = new SparseArrayCompat<>();
        mMediaManager = new MediaManager();
        Intent intent = getIntent();
        mSelectSet = (HashSet) intent.getBundleExtra(SELECTED_SET).get(SELECTED_SET);
        isOriginMedia = intent.getBooleanExtra(IS_ORIGIN_MEDIA, false);
        mCurrentPosition = intent.getIntExtra(CURRENT_POSOTION, 0);
        String fileName = intent.getStringExtra(FILE_NAME);
        if(fileName == null) {
            int position = 0;
            if(mSelectSet.size() != 0) {
                for(String path: mSelectSet) {
                    mMediaPathArray.put(position, path);
                    ++position;
                }
            }
        } else if(! fileName.equals("所有图片")) {
            mMediaPathArray = mMediaManager.findMediaByFileName(fileName);
        } else {
            List<Media> mediaList = mMediaManager.findAllMedia();
            int position = 0;
            for (Media media : mediaList) {
                mMediaPathArray.put(position, media.getUrl());
                ++position;
            }
        }
        if(mSelectSet.size() == 0) {
            mRvPreviewThumbnail.setVisibility(View.GONE);
        } else {
            mRvPreviewThumbnail.setVisibility(View.VISIBLE);
            mBtHeaderSend.setBackgroundColor(0xFF19C917);
            mBtHeaderSend.setText("发送(" + mSelectSet.size() + "/9)");
            mBtHeaderSend.setTextColor(Color.WHITE);
            if(mCurrentPosition != -1) {
                if(mSelectSet.contains(mMediaPathArray.get(mCurrentPosition))) {
                    mIvFooterSelect.setImageResource(R.drawable.select_green_16);
                }
            }
        }
        if(mCurrentPosition == -1) {//预览模式
            mTvPreviewTitle.setText("1/" + mSelectSet.size());
            mIvFooterSelect.setImageResource(R.drawable.select_green_16);
        } else {
            mTvPreviewTitle.setText((mCurrentPosition + 1) + "/" + mMediaPathArray.size());
        }
        if(isOriginMedia) {
            mIvFooterOrigin.setImageResource(R.drawable.footer_circle_green_16);
        }
    }

    /**
     * 点击图片header 和 footer 的动画
     */
    private void headerAndFooterAnimator() {
        final ValueAnimator headerAnimator;
        Animation alphaAnimation;
        if(MAX_HEIGHT == 0) {
            MAX_HEIGHT = mRlPreviewHeader.getHeight();
        }
        if(mSelectSet.size() == 0) {
            mRvPreviewThumbnail.setVisibility(View.GONE);
        } else {
            mRvPreviewThumbnail.setVisibility(View.VISIBLE);
        }
        if(mRlPreviewHeader.getHeight() == 0) {
            headerAnimator = ValueAnimator.ofInt(0, MAX_HEIGHT);
            alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_alpha_to_translucent);
            mRlPreviewFooter.startAnimation(alphaAnimation);
            mRvPreviewThumbnail.startAnimation(alphaAnimation);
        } else {
            headerAnimator = ValueAnimator.ofInt(MAX_HEIGHT, 0);
            alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_translucent_to_alpha);
            mRlPreviewFooter.startAnimation(alphaAnimation);
            mRvPreviewThumbnail.startAnimation(alphaAnimation);
        }
        final ValueAnimator footerAnimator = headerAnimator;
        headerAnimator.setDuration(300);
        headerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRlPreviewHeader.getLayoutParams().height = (int) animation.getAnimatedValue();
                mRlPreviewHeader.setLayoutParams(mRlPreviewHeader.getLayoutParams());
            }
        });
        footerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRlPreviewFooter.getLayoutParams().height = (int) animation.getAnimatedValue();
                mRlPreviewFooter.setLayoutParams(mRlPreviewFooter.getLayoutParams());
            }
        });
        headerAnimator.start();
        footerAnimator.setStartDelay(300);
    }
}
