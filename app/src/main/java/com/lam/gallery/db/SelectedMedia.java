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
        return selectedMediaList;
    }

    /**
     * 加入图片到列表
     * @param selectMedia 被选择加入的Media对象
     * @return
     */
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

    /**
     * 获得已选图片的数量
     * @return
     */
    public static int selectedMediaCount() {
        return selectedMediaList.size();
    }

    /**
     * 通过Media对象在已选列表的position移除Media对象
     * @param position Media对象在已选列表的位置position
     */
    public static void removeByPosition(int position) {
        selectedMediaList.remove(position);
        if(sUpdateUi != null) {
            sUpdateUi.updateRemoveSelectMediaUi();
        }
        Log.d(TAG, "removeByPosition: " + selectedMediaList.size());
    }

    /**
     * 通过Media对象的路径移除Media对象
     * @param path 需移除的Media对象路径
     * @return
     */
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

    /**
     * 通过获得Media对象的路径获得该对象在已选列表的位置position
     * @param path Media对象路径
     * @return
     */
    public static int getSelectedPosition(String path) {
        int position = -1;
        for(int i = 0; i < selectedMediaList.size(); ++i) {
            if(selectedMediaList.get(i).getPath().equals(path)) {
                position = i;
            }
        }
        return position;
    }

    /**
     * 清除已选图片列表的数据
     */
    public static void clearData() {
        selectedMediaList.clear();
    }

    public interface UpdateUi {
        void updateAddSelectMediaUi();
        void updateRemoveSelectMediaUi();
    }
}
