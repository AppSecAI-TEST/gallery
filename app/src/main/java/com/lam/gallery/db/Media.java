package com.lam.gallery.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2017/7/27.
 */

public class Media implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageName);
        dest.writeString(url);
        dest.writeString(fileName);
        dest.writeString(storeDate);
    }

    //将Parcel容器中的数据转换成对象数据
    public static final Parcelable.Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public Media(Parcel in) {
        imageName = in.readString();
        url = in.readString();
        fileName = in.readString();
        storeDate = in.readString();
    }
}
