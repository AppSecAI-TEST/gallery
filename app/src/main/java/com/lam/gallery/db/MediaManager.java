package com.lam.gallery.db;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.lam.gallery.GalleryApplication;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/7/27.
 */

public class MediaManager {

    public List<Media> findAllMedia() {
        List<Media> mediaList = new ArrayList<>();
        Cursor cursor = GalleryApplication.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        Log.d(TAG, "init: " + cursor.getCount());
        while(cursor.moveToNext()) {
            //获取图片名
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片保存路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));

            String storeDate = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

            mediaList.add(new Media(name, path, fileName, storeDate));
        }
        return mediaList;
    }

    public List<Media> findMediaByFileName(String fileName) {
        List<Media> mediaList = new ArrayList<>();
        Cursor cursor = GalleryApplication.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "bucket_display_name = ?", new String[] {fileName}, null);
        while(cursor.moveToNext()) {
            //获取图片名
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片保存路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            String storeDate = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

            mediaList.add(new Media(name, path, fileName, storeDate));
        }
        return mediaList;
    }
}
