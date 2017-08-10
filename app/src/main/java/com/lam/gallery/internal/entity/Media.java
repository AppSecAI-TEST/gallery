package com.lam.gallery.internal.entity;

/**
 * Created by lenovo on 2017/8/1.
 */

public class Media {
    private int mediaId;
    private String path;

    public Media(int mediaId, String path) {
        this.mediaId = mediaId;
        this.path = path;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
