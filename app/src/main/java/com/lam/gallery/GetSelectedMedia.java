package com.lam.gallery;

import com.lam.gallery.db.Media;
import com.lam.gallery.db.SelectedMedia;
import com.lam.gallery.ui.UiManager;

import java.util.List;

/**
 * Created by lenovo on 2017/8/3.
 */

public class GetSelectedMedia {

    public static void getSelectedMedia(GetSelectMediaListener getSelectMediaListener) {
        getSelectMediaListener.getSelectedMedia(UiManager.isOriginMedia, SelectedMedia.getSelectedMediaList());
        SelectedMedia.clearData();
        UiManager.setIsOriginMedia(false);
    }

    public interface GetSelectMediaListener {
        void getSelectedMedia(boolean isOrigin, List<Media> mediaList);
    }
}
