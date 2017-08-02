package com.lam.gallery.db;

import android.util.Log;

import com.lam.gallery.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/8/1.
 */

public class SelectedMedia {
    private static final String TAG = "SelectedMedia";
    private static List<Media> selectedMediaList = new ArrayList<>();
    private static UpdateUi sUpdateUi;

    public static void setUpdateUi(UpdateUi updateUi) {
        sUpdateUi = updateUi;
    }

    public static List<Media> getSelectedMediaList() {
        return selectedMediaList;
    }

    public static boolean addSelected(Media selectMedia) {
        if(selectedMediaList.size() == 9) {
            ToastUtil.showToast("你最多只能选择9张图片");
            return false;
        } else {
            if(selectedMediaList == null)
                selectedMediaList = new ArrayList<>();
            selectedMediaList.add(selectMedia);
            if(sUpdateUi != null) {
                sUpdateUi.updateAddSelectMediaUi();
            }
            Log.d(TAG, "addSelected: " + selectedMediaList.size());
            return true;
        }
    }

    public static int selectedMediaCount() {
        return selectedMediaList.size();
    }

    public static void removeByPosition(int position) {
        selectedMediaList.remove(position);
        if(sUpdateUi != null) {
            sUpdateUi.updateRemoveSelectMediaUi();
        }
        Log.d(TAG, "removeByPosition: " + selectedMediaList.size());
    }

    public static boolean removeByPath(String path) {
        for(int i = 0; i < selectedMediaList.size(); ++i) {
            if(selectedMediaList.get(i).getPath().equals(path)) {
                removeByPosition(i);
                return true;
            }
        }
        Log.d(TAG, "removeByPath: " + selectedMediaList.size());
        return false;
    }

    //查找路径对应的position，存在则返回position，否则返回-1
    public static int getSelectedPosition(String path) {
        int position = -1;
        for(int i = 0; i < selectedMediaList.size(); ++i) {
            if(selectedMediaList.get(i).getPath().equals(path)) {
                position = i;
            }
        }
        return position;
    }

    public static List<Media> cloneSelectMediaList() {
        List<Media> mediaList = new ArrayList<>();
        for(int i = 0; i < selectedMediaList.size(); ++i) {
            mediaList.add(selectedMediaList.get(i));
        }
        return mediaList;
    }

    public static void clearData() {
        selectedMediaList.clear();
    }

    public interface UpdateUi {
        void updateAddSelectMediaUi();
        void updateRemoveSelectMediaUi();
    }
}
