package com.lam.gallery.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.Task.ThreadTask;
import com.lam.gallery.adapter.PreviewThumbnailAdapter;
import com.lam.gallery.adapter.PreviewViewpagerAdapter;
import com.lam.gallery.db.Media;
import com.lam.gallery.db.SelectedMedia;
import com.lam.gallery.manager.MediaManager;
import com.lam.gallery.manager.ValueAnimatorManager;
import com.lam.gallery.ui.ToastUtil;
import com.lam.gallery.ui.UiManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener, PreviewThumbnailAdapter.OnThumbnailItemClickListener, PreviewViewpagerAdapter.OnClickHeaderAndFooterChange, ViewPager.OnPageChangeListener, SelectedMedia.UpdateUi {
    private static final String TAG = "PreViewActivity";
    @BindView(R.id.vp_preview)
    ViewPager mVpPreview;
    @BindView(R.id.iv_title_left_point)
    ImageView mIvTitleLeftPoint;
    @BindView(R.id.view_title_line)
    View mViewTitleLine;
    @BindView(R.id.bt_title_send)
    Button mBtTitleSend;
    @BindView(R.id.gallery_title)
    RelativeLayout mGalleryTitle;
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
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;

    private int mViewPagerCurrentPos;
    private List<Media> mPreviewMediaList;
    private int ENTER_MODE = 0;
    private int MAX_HEIGHT = 0;
    private int THUMBNAIL_HEIGHT = 0;
    private Handler mHandler;
    private PreviewThumbnailAdapter mPreviewThumbnailAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private PreviewViewpagerAdapter mPreviewViewpagerAdapter;
    public static final int ENTER_BY_SELECT = 1;
    public static final int ENTER_BY_UNSELECT = 2;
    public static final String CLICK_POS = "click to enter position";
    public static final String PREVIEW_MEDIA_FILE_NAME = "need to preview media's file name";

    public static void start(Context context, int clickEnterPos, String previewMediaFileName) {
        Intent starter = new Intent(context, PreviewActivity.class);
        starter.putExtra(CLICK_POS, clickEnterPos);
        starter.putExtra(PREVIEW_MEDIA_FILE_NAME, previewMediaFileName);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        ButterKnife.bind(this);

        init();
        getData();
        setListener();
    }

    //初始化数据
    private void init() {
        mHandler = new Handler();
        mPreviewMediaList = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPreviewThumbnail.setLayoutManager(mLinearLayoutManager);

        UiManager.updateSendButton(mBtTitleSend);
        UiManager.updataOiginView(mIvFooterOrigin);
        UiManager.updateThumbnailVisibility(mRvPreviewThumbnail);
    }

    //获取上一个activity传输的数据
    private void getData() {
        Intent intent = getIntent();
        mViewPagerCurrentPos = intent.getIntExtra(CLICK_POS, -1);
        final String fileName = intent.getStringExtra(PREVIEW_MEDIA_FILE_NAME);
        ThreadTask.clear();
        ThreadTask.addTask(new Runnable() {
            @Override
            public void run() {
                MediaManager mediaManager = new MediaManager();
                if (fileName == null)    //通过点击预览进入
                    mPreviewMediaList = SelectedMedia.cloneSelectMediaList();
                else if (fileName.equals("所有图片"))
                    mPreviewMediaList = mediaManager.findAllMedia();
                else
                    mPreviewMediaList = mediaManager.findMediaListByFileName(fileName);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initThumbnailUi();
                        initPreviewUi();
                    }
                });
            }
        });
    }

    //初始化ThumbnailUi
    private void initThumbnailUi() {
        if (mViewPagerCurrentPos == -1) {  //点击预览进入
            mPreviewThumbnailAdapter = new PreviewThumbnailAdapter(0);
            mTvHeaderTitle.setText("1/" + SelectedMedia.selectedMediaCount());
            mViewPagerCurrentPos = 0;
        }
        else {
            int clickToEnterMediaInSelectPos = SelectedMedia.getSelectedPosition(mPreviewMediaList.get(mViewPagerCurrentPos).getPath());
            if (clickToEnterMediaInSelectPos != -1) {
                ENTER_MODE = ENTER_BY_SELECT;
                mPreviewThumbnailAdapter = new PreviewThumbnailAdapter(clickToEnterMediaInSelectPos);
                mLinearLayoutManager.smoothScrollToPosition(mRvPreviewThumbnail, null, clickToEnterMediaInSelectPos);
            } else {
                ENTER_MODE = ENTER_BY_UNSELECT;
                mPreviewThumbnailAdapter = new PreviewThumbnailAdapter(-1);
            }
            mTvHeaderTitle.setText((mViewPagerCurrentPos + 1) + "/" + mPreviewMediaList.size());
        }
        mRvPreviewThumbnail.setAdapter(mPreviewThumbnailAdapter);
        mPreviewThumbnailAdapter.setOnThumbnailItemClickListener(PreviewActivity.this);

    }

    //初始化viewPagerUi
    private void initPreviewUi() {
        mPreviewViewpagerAdapter = new PreviewViewpagerAdapter(mPreviewMediaList);
        mVpPreview.setAdapter(mPreviewViewpagerAdapter);
        mVpPreview.setCurrentItem(mViewPagerCurrentPos);
        mPreviewViewpagerAdapter.setOnClickHeaderAndFooterChange(this);
        mVpPreview.addOnPageChangeListener(this);
        UiManager.updateSelect(mPreviewMediaList.get(mViewPagerCurrentPos).getPath(), mIvFooterSelect);
    }

    /**
     * 点击图片header 和 footer 的动画
     */
    private void headerAndFooterAnimator() {
        ValueAnimator headerAnimator;
        Animation alphaAnimation;
        if(MAX_HEIGHT == 0) {
            MAX_HEIGHT = mGalleryTitle.getHeight();
            THUMBNAIL_HEIGHT = mRvPreviewThumbnail.getHeight();
        }
        UiManager.updateThumbnailVisibility(mRvPreviewThumbnail);
        if(mGalleryTitle.getHeight() == 0) {
            mRlPreviewFooter.setVisibility(View.VISIBLE);
            headerAnimator = ValueAnimatorManager.viewAnimator(0, MAX_HEIGHT, 300, mGalleryTitle);
            alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_alpha_to_translucent);
            mRlPreviewFooter.startAnimation(alphaAnimation);
            if(SelectedMedia.selectedMediaCount() != 0)
                mRvPreviewThumbnail.startAnimation(alphaAnimation);
        } else {
            headerAnimator = ValueAnimatorManager.viewAnimator(MAX_HEIGHT, 0, 300, mGalleryTitle);
            alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.from_translucent_to_alpha);
            mRlPreviewFooter.startAnimation(alphaAnimation);
            if(SelectedMedia.selectedMediaCount() != 0) {
                mRvPreviewThumbnail.startAnimation(alphaAnimation);
            }
            mRvPreviewThumbnail.setVisibility(View.GONE);
            mRlPreviewFooter.setVisibility(View.GONE);
        }
        headerAnimator.start();
    }

    //监听事件设定
    private void setListener() {
        mIvFooterOrigin.setOnClickListener(this);
        mTvFooterOrigin.setOnClickListener(this);
        mTvFooterSelect.setOnClickListener(this);
        mIvFooterSelect.setOnClickListener(this);
        mBtTitleSend.setOnClickListener(this);
        mIvTitleLeftPoint.setOnClickListener(this);
        mIvFooterSelect.setOnClickListener(this);
        mTvFooterSelect.setOnClickListener(this);
        SelectedMedia.setUpdateUi(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_footer_origin || v.getId() == R.id.tv_footer_origin)
            UiManager.listenerUpdateOrigin(mIvFooterOrigin);
        if (v.getId() == R.id.iv_title_left_point)   //反馈数据给上一个activity
            finish();
        if (v.getId() == R.id.bt_title_send && SelectedMedia.selectedMediaCount() != 0)  //反馈数据给上一个activity
            finish();
        if(v.getId() == R.id.iv_footer_select || v.getId() == R.id.tv_footer_select ) {
            String clickPath = mPreviewMediaList.get(mViewPagerCurrentPos).getPath();
            UiManager.updateSelectListener(clickPath, mPreviewMediaList, mIvFooterSelect);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThumbnailItemClick(View view, int position) { //position获取点击的缩略图在已选列表的position
        String selectPath = SelectedMedia.getSelectedMediaList().get(position).getPath();
        int pos = MediaManager.findPosByPath(mPreviewMediaList, selectPath);
        if(pos != -1) {
            mViewPagerCurrentPos = pos;
            mVpPreview.setCurrentItem(mViewPagerCurrentPos);
            mTvHeaderTitle.setText((mViewPagerCurrentPos + 1) + "/" + mPreviewMediaList.size());
            mPreviewThumbnailAdapter.setCurrentPos(position);
            mPreviewThumbnailAdapter.notifyDataSetChanged();
        } else
            ToastUtil.showToast("暂时不支持查看该文件大图");
    }

    @Override
    public void headerAndFooterChange() {
        headerAndFooterAnimator();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mViewPagerCurrentPos = position;
        mTvHeaderTitle.setText((position + 1) + "/" + mPreviewMediaList.size());
        String viewpagerCurrentPath = mPreviewMediaList.get(position).getPath();
        UiManager.updateSelect(viewpagerCurrentPath, mIvFooterSelect);
        int posInThumbnail = SelectedMedia.getSelectedPosition(viewpagerCurrentPath);
        mPreviewThumbnailAdapter.setCurrentPos(posInThumbnail);
        if(posInThumbnail != -1)
            mLinearLayoutManager.smoothScrollToPosition(mRvPreviewThumbnail, null, posInThumbnail);
        mPreviewThumbnailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //被选中的列表发生变更时更新ui
    @Override
    public void updateAddSelectMediaUi() {
        UiManager.updateSendButton(mBtTitleSend);
        mIvFooterSelect.setImageResource(R.drawable.select_green_16);
        mPreviewThumbnailAdapter.setCurrentPos(SelectedMedia.selectedMediaCount() - 1);
        mPreviewThumbnailAdapter.notifyDataSetChanged();
        mLinearLayoutManager.smoothScrollToPosition(mRvPreviewThumbnail, null, SelectedMedia.selectedMediaCount() - 1);
        UiManager.updateThumbnailVisibility(mRvPreviewThumbnail);
    }

    @Override
    public void updateRemoveSelectMediaUi() {
        UiManager.updateSendButton(mBtTitleSend);
        mIvFooterSelect.setImageResource(R.drawable.select_alpha_16);
        mPreviewThumbnailAdapter.setCurrentPos(-1);
        mPreviewThumbnailAdapter.notifyDataSetChanged();
        UiManager.updateThumbnailVisibility(mRvPreviewThumbnail);
    }
}