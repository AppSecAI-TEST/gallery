package com.lam.galleryhome.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lam.gallery.GetSelectedMedia;
import com.lam.gallery.activity.MainActivity;
import com.lam.gallery.db.Media;
import com.lam.galleryhome.R;
import com.lam.galleryhome.adapter.PathShowAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import android.view.View;

public class HomeActivity extends PermissionActivity implements View.OnClickListener, GetSelectedMedia.GetSelectMediaListener {
    private static final String TAG = "HomeActivity";
    @BindView(R.id.bt_select_picture)
    Button mBtSelectPicture;
    @BindView(R.id.rv_show_list)
    RecyclerView mRvShowList;
    @BindView(R.id.bt_clear_select)
    Button mBtClearSelect;
    @BindView(R.id.rl_menu)
    RelativeLayout mRlMenu;

    private boolean mIsOrigin;
    private List<Media> mSelectedMediaList;
    private PathShowAdapter mPathShowAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mSelectedMediaList = new ArrayList<>();
        mIsOrigin = false;
        mBtSelectPicture.setOnClickListener(this);
        mBtClearSelect.setOnClickListener(this);

        mLinearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRvShowList.setLayoutManager(mLinearLayoutManager);
        mPathShowAdapter = new PathShowAdapter(mSelectedMediaList, mIsOrigin);
        mRvShowList.setAdapter(mPathShowAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetSelectedMedia.getSelectedMedia(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_select_picture)
            requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x963);
        else if(v.getId() == R.id.bt_clear_select) {
            mSelectedMediaList.clear();
            mIsOrigin = false;
            mPathShowAdapter.setMediaList(mSelectedMediaList);
            mPathShowAdapter.setOrigin(mIsOrigin);
            mPathShowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getSelectedMedia(boolean isOrigin, List<Media> mediaList) {
        mSelectedMediaList.clear();
        mSelectedMediaList.addAll(mediaList);
        mIsOrigin = isOrigin;
        mPathShowAdapter.setMediaList(mSelectedMediaList);
        mPathShowAdapter.setOrigin(mIsOrigin);
        mPathShowAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        MainActivity.start(this, false);
    }
}
