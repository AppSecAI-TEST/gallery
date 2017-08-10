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
    private static List<Media> sSelectedMediaList = new ArrayList<>();
    private static UpdateUi sUpdateUi;

    /**
     * 设置更新ui的监听器
     * @param updateUi
     */
    public static void setUpdateUi(UpdateUi updateUi) {
        sUpdateUi = updateUi;
    }

    /**
     * 获得被选中的图片列表
     * @return List<Media>
     */
    public static List<Media> getSelectedMediaList() {
        return sSelectedMediaList;
    }

    /**
     * 获得被选中图片的路径列表
     * @return
     */
    public static ArrayList<String> getSelectedMediaPath() {
        ArrayList<String> pathList = new ArrayList<>();
        for(int i = 0; i < sSelectedMediaList.size(); ++i) {
            pathList.add(sSelectedMediaList.get(i).getPath());
        }
        return pathList;
    }

    /**
     * 获得被选中图片的id数组
     * @return
     */
    public static int[] getSelectedMediaIds() {
        int[] mediaIds = new int[sSelectedMediaList.size()];
        for(int i = 0; i < sSelectedMediaList.size(); ++i) {
            mediaIds[i] = sSelectedMediaList.get(i).getMediaId();
        }
        return mediaIds;
    }

    /**
     * 加入图片到列表
     * @param selectMedia 被选择加入的Media对象
     * @return
     */
    public static boolean addSelected(Media selectMedia) {
        if(sSelectedMediaList.size() == ConfigSpec.getInstance().mMaxSelected) {
            ToastUtil.showToast("你最多只能选择" + ConfigSpec.getInstance().mMaxSelected +"张图片");
            return false;
        } else {
            if(sSelectedMediaList == null)
                sSelectedMediaList = new ArrayList<>();
            sSelectedMediaList.add(selectMedia);
            if(sUpdateUi != null) {
                sUpdateUi.updateAddSelectMediaUi();
            }
            Log.d(TAG, "addSelected: " + sSelectedMediaList.size());
            return true;
        }
    }

    /**
     * 获得已选图片的数量
     * @return
     */
    public static int selectedMediaCount() {
        return sSelectedMediaList.size();
    }

    /**
     * 通过Media对象在已选列表的position移除Media对象
     * @param position Media对象在已选列表的位置position
     */
    public static void removeByPosition(int position) {
        sSelectedMediaList.remove(position);
        if(sUpdateUi != null) {
            sUpdateUi.updateRemoveSelectMediaUi();
        }
        Log.d(TAG, "removeByPosition: " + sSelectedMediaList.size());
    }

    /**
     * 通过Media对象的路径移除Media对象
     * @param path 需移除的Media对象路径
     * @return
     */
    public static boolean removeByPath(String path) {
        for(int i = 0; i < sSelectedMediaList.size(); ++i) {
            if(sSelectedMediaList.get(i).getPath().equals(path)) {
                removeByPosition(i);
                return true;
            }
        }
        Log.d(TAG, "removeByPath: " + sSelectedMediaList.size());
        return false;
    }

    //查找路径对应的position，存在则返回position，否则返回-1

    /**
     * 通过获得Media对象的路径获得该对象在已选列表的位置position
     * @param path Media对象路径
     * @return
     */
    public static int getSelectedPosition(String path) {
        int position = -1;
        for(int i = 0; i < sSelectedMediaList.size(); ++i) {
            if(sSelectedMediaList.get(i).getPath().equals(path)) {
                position = i;
            }
        }
        return position;
    }

    /**
     * 清除已选图片列表的数据
     */
    public static void clearData() {
        sSelectedMediaList.clear();
    }

    public interface UpdateUi {
        void updateAddSelectMediaUi();
        void updateRemoveSelectMediaUi();
    }
}
