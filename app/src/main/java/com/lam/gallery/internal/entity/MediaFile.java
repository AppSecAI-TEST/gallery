package com.lam.gallery.internal.entity;

/**
 * Created by lenovo on 2017/8/1.
 */

public class MediaFile {
    private int count;
    private String fileName;
    private String fileCoverPath;
    private int coverPathId;

    public MediaFile(int count, String fileName, String fileCoverPath, int coverPathId) {
        this.count = count;
        this.fileName = fileName;
        this.fileCoverPath = fileCoverPath;
        this.coverPathId = coverPathId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileCoverPath() {
        return fileCoverPath;
    }

    public void setFileCoverPath(String fileCoverPath) {
        this.fileCoverPath = fileCoverPath;
    }

    public int getCoverPathId() {
        return coverPathId;
    }

    public void setCoverPathId(int coverPathId) {
        this.coverPathId = coverPathId;
    }
}
