package com.lam.gallery.internal.ui.view;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lam.gallery.R;
import com.lam.gallery.internal.entity.ConfigSpec;
import com.lam.gallery.internal.entity.SelectedMedia;

/**
 * Created by lenovo on 2017/8/1.
 */

public class UiManager {
    private static final String TAG = "UiManager";
    public static boolean isOriginMedia;

    public static void setIsOriginMedia(boolean isOriginMedia) {
        UiManager.isOriginMedia = isOriginMedia;
    }

    public static void updateSendButton(Button sendButton) {
        int selectedCount = SelectedMedia.getSelectedMediaList().size();
        if(selectedCount == 0) {
            sendButton.setBackgroundColor(0xFF094909);
            sendButton.setText("发送");
            sendButton.setTextColor(0xFFA1A1A1);
        } else {
            sendButton.setBackgroundColor(0xFF19C917);
            sendButton.setText("发送(" + selectedCount + "/" + ConfigSpec.getInstance().mMaxSelected + ")");
            sendButton.setTextColor(Color.WHITE);
        }
    }

    public static void updatePreViewText(TextView textView) {
        int selectedCount = SelectedMedia.getSelectedMediaList().size();
        if(selectedCount == 0) {
            textView.setText("预览");
            textView.setTextColor(0xFF5B5B5B);
        } else {
            textView.setText("预览(" + selectedCount + ")");
            textView.setTextColor(Color.WHITE);
        }
    }

    public static void listenerUpdateOrigin(ImageView imageView) {
        if(isOriginMedia) {
            imageView.setImageResource(R.drawable.footer_circle_16);
        } else {
            imageView.setImageResource(R.drawable.footer_circle_green_16);
        }
        isOriginMedia = !isOriginMedia;
    }

    public static void updateOriginView(ImageView imageView) {
        if(isOriginMedia) {
            imageView.setImageResource(R.drawable.footer_circle_green_16);
        } else {
            imageView.setImageResource(R.drawable.footer_circle_16);
        }
    }

    public static void updateThumbnailVisibility(RecyclerView rvPreviewThumbnail) {
        Log.d(TAG, "updateThumbnailVisibility: " + SelectedMedia.selectedMediaCount());
        if(SelectedMedia.selectedMediaCount() == 0)
            rvPreviewThumbnail.setVisibility(View.GONE);
        else
            rvPreviewThumbnail.setVisibility(View.VISIBLE);
    }

    public static void updateSelect(String clickPath, ImageView imageView) {
        int selectedPos = SelectedMedia.getSelectedPosition(clickPath);
        if(selectedPos == -1) {   //当点击的图片尚未被选中
            imageView.setImageResource(R.drawable.select_alpha_16);
        } else {   //当点击的图片原为选中的图片
            imageView.setImageResource(R.drawable.select_green_16);
        }
    }

}
