package com.lam.gallery.manager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.lam.gallery.db.Media;
import com.lam.gallery.db.MediaFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/8/1.
 */

public class MediaManager {

    private static final String TAG = "MediaManager";
    public void findAllMedia(InitDataListener listener) {
        List<Media> mediaList = new ArrayList<>();
        List<MediaFile> mediaFileList = new ArrayList<>();
        MediaFile mediaFile = new MediaFile(0, "所有图片", null, 0);
        mediaFileList.add(mediaFile);
        Cursor cursor = GalleryApplication.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_added desc");
        while(cursor != null && cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            int mediaId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            mediaList.add(new Media(mediaId, path));
            mediaFileList.get(0).setCount(mediaFileList.get(0).getCount() + 1);
            if(mediaFileList.get(0).getFileCoverPath() == null) {
                mediaFileList.get(0).setFileCoverPath(path);
                mediaFileList.get(0).setCoverPathId(mediaId);
            }
            boolean isExist = false;
            for(int i = 1; i < mediaFileList.size(); ++i) {
                if(mediaFileList.get(i).getFileName().equals(fileName)) {   //当原文件存在时
                    mediaFileList.get(i).setCount(mediaFileList.get(i).getCount() + 1);
                    isExist = true;
                    break;
                }
            }
            if(! isExist) {
                mediaFile = new MediaFile(1, fileName, path, mediaId);
                mediaFileList.add(mediaFile);
            }
        }
        if(cursor != null)
            cursor.close();
        listener.getData(mediaList, mediaFileList);
    }

    public List<Media> findMediaListByFileName(String fileName) {
        List<Media> selectFileMediaList = new ArrayList<>();
        Cursor cursor = GalleryApplication.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "bucket_display_name = ?", new String[] {fileName}, "date_added desc");
        while(cursor != null && cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            int mediaId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            selectFileMediaList.add(new Media(mediaId, path));
        }
        if(cursor != null)
            cursor.close();
        return selectFileMediaList;
    }

    public List<Media> findAllMedia() {
        List<Media> selectFileMediaList = new ArrayList<>();
        Cursor cursor = GalleryApplication.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_added desc");
        while(cursor != null && cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            int mediaId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            selectFileMediaList.add(new Media(mediaId, path));
        }
        if(cursor != null)
            cursor.close();
        return selectFileMediaList;
    }

    public static Bitmap getThumbnail(int id) {
        ContentResolver resolver = GalleryApplication.getContext().getContentResolver();
        return MediaStore.Images.Thumbnails.getThumbnail(resolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
    }

    public interface InitDataListener {
        void getData(List<Media> mediaList, List<MediaFile> mediaFileList);
    }

    public static int findPosByPath(List<Media> mediaList, String path) {
        for(int i = 0; i < mediaList.size(); ++i) {
            if(mediaList.get(i).getPath().equals(path))
                return i;
        }
        return -1;
    }
}
