package com.lam.galleryhome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lam.gallery.GetSelectedMedia;
import com.lam.gallery.activity.MainActivity;
import com.lam.gallery.db.Media;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import android.view.View;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, GetSelectedMedia.GetSelectMediaListener {
    private static final String TAG = "HomeActivity";
    @BindView(R.id.bt_select_picture)
    Button mBtSelectPicture;
    @BindView(R.id.rv_show_list)
    RecyclerView mRvShowList;

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
        if (v.getId() == R.id.bt_select_picture) {
            MainActivity.start(this, false);
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
        Log.d(TAG, "getSelectedMedia: " + mPathShowAdapter.mMediaList.size());
    }
}
