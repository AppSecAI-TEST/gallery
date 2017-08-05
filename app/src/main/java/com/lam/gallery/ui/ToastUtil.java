package com.lam.gallery.ui;
import android.widget.Toast;

import com.lam.gallery.manager.GalleryApplication;

public class ToastUtil {

    private static Toast mToast;

    private ToastUtil() {
    }

    public static void showToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(GalleryApplication.getContext(), message, Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }

    public static void hideToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
