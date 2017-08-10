package com.lam.galleryhome.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lam.gallery.Gallery;
import com.lam.gallery.task.BitmapTaskDispatcher;
import com.lam.galleryhome.R;
import com.lam.galleryhome.adapter.PathShowAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends PermissionActivity implements View.OnClickListener/*, GetSelectedMedia.GetSelectMediaListener*/ {
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
    private PathShowAdapter mPathShowAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<String> mMediaPath;

    private static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mMediaPath = new ArrayList<>();
        mIsOrigin = false;
        mBtSelectPicture.setOnClickListener(this);
        mBtClearSelect.setOnClickListener(this);

        mLinearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRvShowList.setLayoutManager(mLinearLayoutManager);
        mPathShowAdapter = new PathShowAdapter(mIsOrigin, mMediaPath);
        mRvShowList.setAdapter(mPathShowAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_select_picture)
            requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x963);
        else if(v.getId() == R.id.bt_clear_select) {
            Log.d(TAG, "onClick: ");
            mIsOrigin = false;
            mMediaPath.clear();
            mPathShowAdapter.setMediaPath(mMediaPath);

            mPathShowAdapter.setOrigin(mIsOrigin);
            mPathShowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        Gallery.from(HomeActivity.this)
                .choose()
//                .imageEngine(new PicassoEngine())
//                .imageEngine(new ImageLoaderEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    /**
     * 当下一个界面处理完返回该Activity时回调该方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode = " + requestCode + "resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mMediaPath.clear();
            mMediaPath.addAll(Gallery.obtainMediaPathResult(data));
            mIsOrigin = Gallery.obtainMediaIsOriginResult(data);
            mPathShowAdapter.setMediaPath(mMediaPath);
            Log.d(TAG, "onActivityResult: " + mIsOrigin);
            mPathShowAdapter.setOrigin(mIsOrigin);
            mPathShowAdapter.notifyDataSetChanged();
        }
    }
}
