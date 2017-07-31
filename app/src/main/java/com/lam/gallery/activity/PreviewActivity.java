package com.lam.gallery.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lam.gallery.Animation.ValueAnimatorManager;
import com.lam.gallery.R;
import com.lam.gallery.adapter.PreviewThumbnailAdapter;
import com.lam.gallery.adapter.PreviewViewpagerAdapter;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.MediaManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity implements PreviewThumbnailAdapter.OnSelectThumbnail, View.OnClickListener, PreviewViewpagerAdapter.OnClickHeaderAndFooterChange, PreviewViewpagerAdapter.OnClickSelect {
    private static final String TAG = "PreviewActivity";
    @BindView(R.id.vp_preview)
    ViewPager mVpPreview;
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
    public static final String CURRENT_POSITION = "current position";
    public static final int INTENT_BY_PREVIEW = 1;
    public static final int INTENT_BY_SELECTED = 2;
    public static final int INTENT_BY_UNSELECTED = 3;

    private HashSet<String> mSelectSet;
    private SparseArray<String> mMediaPathArray;
    private SparseArray<String> mSelectedMediaArray;
    private boolean isOriginMedia;
    private MediaManager mMediaManager;
    public int MAX_HEIGHT;
    public int THUMBNAIL_HEIGHT;
    private int mCurrentPosition;
    private LinearLayoutManager mLinearLayoutManager;
    private PreviewThumbnailAdapter mPreviewThumbnailAdapter;
    private PreviewViewpagerAdapter mPreviewViewpagerAdapter;
    private static int PREVIEW_MODE;

    public static void start(Context context, HashSet<String> selectSet, String fileName, boolean isOriginMedia, int currentPosition) {
        Intent starter = new Intent(context, PreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_SET, selectSet);
        starter.putExtra(SELECTED_SET, bundle);
        starter.putExtra(FILE_NAME, fileName);
        starter.putExtra(IS_ORIGIN_MEDIA, isOriginMedia);
        starter.putExtra(CURRENT_POSITION, currentPosition);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        mMediaManager = new MediaManager();
        mSelectedMediaArray = new SparseArray<>();
        mMediaPathArray = new SparseArray<>();

        initIntent();
        initUi();
        clickProcess();

        mPreviewViewpagerAdapter = new PreviewViewpagerAdapter(null, mTvPreviewTitle, mSelectSet, mTvFooterSelect, mIvFooterSelect);
        mVpPreview.setAdapter(mPreviewViewpagerAdapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPreviewThumbnail.setLayoutManager(mLinearLayoutManager);
        mPreviewThumbnailAdapter = new PreviewThumbnailAdapter(mSelectSet, mSelectedMediaArray, -1);
        mRvPreviewThumbnail.setAdapter(mPreviewThumbnailAdapter);
        mPreviewViewpagerAdapter.setOnClickHeaderAndFooterChange(this);
        mPreviewViewpagerAdapter.setOnClickSelect(this);

        initViewPager();
        initThumbnail();
    }

    /**
     * 获取从MainActivity传递的数据 及 一些必要的成员变量初始化
     */
    private void initIntent() {
        Intent intent = getIntent();
        mSelectSet = (HashSet) intent.getBundleExtra(SELECTED_SET).get(SELECTED_SET);
        String fileName = intent.getStringExtra(FILE_NAME);
        isOriginMedia = intent.getBooleanExtra(IS_ORIGIN_MEDIA, false);
        mCurrentPosition = intent.getIntExtra(CURRENT_POSITION, -1);
        //一些必要的数据转化
        //获得已选图片的稀疏数组
        int position = 0;
        if(mSelectSet.size() != 0) {
            Iterator<String> iterator = mSelectSet.iterator();
            while(iterator.hasNext()) {
                mSelectedMediaArray.put(position, iterator.next());
                ++position;
            }
        }
        //获得viewPager需要展示的mediaArray
        if(fileName == null) {  //当filename传了空值即表示是点击预览进入的
            mMediaPathArray = mSelectedMediaArray.clone();
        } else if(fileName.equals("所有图片")) {
            List<Media> mediaList = mMediaManager.findAllMedia();
            position = 0;
            for (Media media : mediaList) {
                mMediaPathArray.put(position, media.getUrl());
                ++position;
            }
        } else {
            mMediaPathArray = mMediaManager.findMediaByFileName(fileName);
        }
        //进入模式的初识化
        if(fileName == null) {
            PREVIEW_MODE = INTENT_BY_PREVIEW;
        } else {
            String enterPath = mMediaPathArray.get(mCurrentPosition);
            if(enterPath == null) {
                PREVIEW_MODE = INTENT_BY_UNSELECTED;
            } else {
                PREVIEW_MODE = INTENT_BY_SELECTED;
            }
        }
    }

    /**
     * UI的初始化
     */
    private void initUi() {
        //标题的初始化
        if(PREVIEW_MODE == INTENT_BY_PREVIEW) {
            mTvPreviewTitle.setText("1/" + mSelectSet.size());
        } else {
            mTvPreviewTitle.setText(mCurrentPosition + "/" + mMediaPathArray.size());
        }
        //发送的初始化
        if(mSelectSet.size() != 0) {
            mBtHeaderSend.setBackgroundColor(0xFF19C917);
            mBtHeaderSend.setText("发送(" + mSelectSet.size() + "/9)");
            mBtHeaderSend.setTextColor(Color.WHITE);
        }
        //原图的初始化
        if(isOriginMedia) {
            mIvFooterOrigin.setImageResource(R.drawable.footer_circle_green_16);
        }
        //选择的初始化
        if(mSelectSet.size() != 0 && (MODE_PRIVATE == INTENT_BY_SELECTED || PREVIEW_MODE == INTENT_BY_PREVIEW)) {
            mIvFooterSelect.setImageResource(R.drawable.select_green_16);
        }
    }

    /**
     * header 和 footer 点击事件的监听
     */
    private void clickProcess() {
        mIvHeaderBack.setOnClickListener(this);
        mBtHeaderSend.setOnClickListener(this);
        mIvFooterOrigin.setOnClickListener(this);
        mTvFooterOrigin.setOnClickListener(this);
        //选择图片事件的点击不再这里处理
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_back:
                finish();
                break;
            case R.id.bt_header_send:
                if(mSelectSet.size() != 0)
                    finish();
                break;
            case R.id.iv_footer_origin:
                clickUpdateOriginUi();
                break;
            case R.id.tv_footer_origin:
                clickUpdateOriginUi();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        MainActivity.start(PreviewActivity.this, isOriginMedia, mSelectSet);
        super.onDestroy();
    }

    /**
     * 点击原图修改ui事件
     */
    private void clickUpdateOriginUi() {
        if(isOriginMedia) {
            mIvFooterOrigin.setImageResource(R.drawable.footer_circle_16);
        } else {
            mIvFooterOrigin.setImageResource(R.drawable.footer_circle_green_16);
        }
        isOriginMedia = ! isOriginMedia;
    }

    /**
     * viewpager 的初始化渲染
     */
    private void initViewPager() {
        mPreviewViewpagerAdapter.setMediaPathArray(mMediaPathArray);
        mPreviewViewpagerAdapter.notifyDataSetChanged();
        mVpPreview.setCurrentItem(mCurrentPosition);
    }

    /**
     * 缩略图 的初始化渲染
     */
    private void initThumbnail() {
        if(mSelectSet.size() == 0) {
            mRvPreviewThumbnail.setVisibility(View.GONE);
            mRvPreviewThumbnail.setAlpha(0.0f);
        } else {
            mRvPreviewThumbnail.setVisibility(View.VISIBLE);
            mRvPreviewThumbnail.setAlpha(0.85f);
            if(PREVIEW_MODE == INTENT_BY_PREVIEW) {
                mPreviewThumbnailAdapter.setSelectPos(0);
            } else if(PREVIEW_MODE == INTENT_BY_SELECTED){
                String enterPath = mMediaPathArray.get(mCurrentPosition);
                for (int i = 0; i < mSelectedMediaArray.size(); ++i) {
                    String path = mSelectedMediaArray.get(i);
                    if(path.equals(enterPath)) {
                        mPreviewThumbnailAdapter.setSelectPos(i);
                        break;
                    }
                }
            }
            mPreviewThumbnailAdapter.notifyDataSetChanged();
            mPreviewThumbnailAdapter.setOnSelectThumbnail(this);
        }
    }

    /**
     * 点击图片header 和 footer 的动画
     */
    private void headerAndFooterAnimator() {
        ValueAnimator headerAnimator;
        Animation alphaAnimation;
        if(MAX_HEIGHT == 0) {
            MAX_HEIGHT = mRlPreviewHeader.getHeight();
            THUMBNAIL_HEIGHT = mRvPreviewThumbnail.getHeight();
        }
        if(mRlPreviewHeader.getHeight() == 0) {
            mRvPreviewThumbnail.setVisibility(View.VISIBLE);
            mRlPreviewFooter.setVisibility(View.VISIBLE);
            headerAnimator = ValueAnimatorManager.viewAnimator(0, MAX_HEIGHT, 300, mRlPreviewHeader);
            alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_alpha_to_translucent);
            mRlPreviewFooter.startAnimation(alphaAnimation);
            mRvPreviewThumbnail.startAnimation(alphaAnimation);
        } else {
            headerAnimator = ValueAnimatorManager.viewAnimator(MAX_HEIGHT, 0, 300, mRlPreviewHeader);
            alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_translucent_to_alpha);
            mRlPreviewFooter.startAnimation(alphaAnimation);
            mRvPreviewThumbnail.startAnimation(alphaAnimation);
            mRvPreviewThumbnail.setVisibility(View.GONE);
            mRlPreviewFooter.setVisibility(View.GONE);
        }
        headerAnimator.start();
    }

    @Override
    public void onSelect(int position) {
        mPreviewThumbnailAdapter.setSelectPos(position);
        mPreviewThumbnailAdapter.notifyDataSetChanged();
        //viewPager也要改变
        String clickPath = mSelectedMediaArray.get(position);
        if(PREVIEW_MODE == INTENT_BY_PREVIEW) {
            mVpPreview.setCurrentItem(mMediaPathArray.indexOfValue(clickPath));
        } else {
            for(int i = 0; i < mMediaPathArray.size(); ++i) {
                if(clickPath.equals(mMediaPathArray.get(i))) {
                    mVpPreview.setCurrentItem(i);
                    return;
                }
            }
            Toast.makeText(this, "暂时不支持查看该文件大图", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void headerAndFooterChange() {
        headerAndFooterAnimator();
    }

    @Override
    public void clickSelect(int position) {
//        String clickedPath = mMediaPathArray.get(position);
//        if(mSelectSet.remove(clickedPath)) {  //点击选择的图片本就在已选集合中, 移除
//            for(int i = 0; i < mSelectedMediaArray.size(); ++i) {  //从稀疏数组中删除
//                if(mSelectedMediaArray.get(i) != null && mSelectedMediaArray.get(i).equals(clickedPath)) {
//                    mSelectedMediaArray.remove(i);
//                    break;
//                }
//            }
//            mIvFooterSelect.setImageResource(R.drawable.select_green_16);  //选择图标的修改
//        } else {
//            mSelectSet.add(clickedPath);
//            mSelectedMediaArray.put(mSelectedMediaArray.keyAt(mSelectedMediaArray.size()) + 1, clickedPath);
//            mIvFooterSelect.setImageResource(R.drawable.select_alpha_16);  //选择图标的修改
//        }
//        mPreviewThumbnailAdapter.setMediaPathArray(mSelectedMediaArray);
//        mPreviewThumbnailAdapter.notifyDataSetChanged();
//        if(mSelectSet.size() != 0) {  //发送按钮的修改
//            mBtHeaderSend.setBackgroundColor(0xFF19C917);
//            mBtHeaderSend.setText("发送(" + mSelectSet.size() + "/9)");
//            mBtHeaderSend.setTextColor(Color.WHITE);
//        } else {
//            mBtHeaderSend.setBackgroundColor(0xFF094909);
//            mBtHeaderSend.setText("发送");
//            mBtHeaderSend.setTextColor(0xFFA1A1A1);
//        }
//        if(MODE_PRIVATE == INTENT_BY_PREVIEW) { //当是点击预览进入时，viewPager中的数据也需要更新
//            mMediaPathArray = mSelectedMediaArray.clone();
//            mPreviewViewpagerAdapter.setMediaPathArray(mMediaPathArray);
//            mPreviewViewpagerAdapter.notifyDataSetChanged();
//            mTvPreviewTitle.setText(mCurrentPosition + "/" + mMediaPathArray.size());
//        }
    }
}
