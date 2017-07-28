package com.lam.gallery.db;

/**
 * Created by lenovo on 2017/7/27.
 */

public class Media {
    private String imageName;

    private String url;

    private String fileName;

    private String storeDate;

    public Media(String imageName, String url, String fileName, String storeDate) {
        this.imageName = imageName;
        this.url = url;
        this.fileName = fileName;
        this.storeDate = storeDate;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(String storeDate) {
        this.storeDate = storeDate;
    }
}
