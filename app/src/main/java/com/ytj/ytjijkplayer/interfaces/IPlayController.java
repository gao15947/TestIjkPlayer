package com.ytj.ytjijkplayer.interfaces;

import android.widget.ImageView;

/**
 * Created by Administrator on 2017/7/10.
 */

public interface IPlayController {
    interface onPlayBackListener {
        void onPlayBack();
    }

    interface onShowThumbnail {
        void ShowThumbnail(ImageView ivThumbnail);
    }
}
