package com.ytj.ytjijkplayer.media;

/**
 * Created by Administrator on 2017/7/7.
 */

public class PlayStateParams {
    // all possible internal states
    //出错
    public static final int STATE_ERROR = -1;
    //空闲
    public static final int STATE_IDLE = 0;
    //准备中
    public static final int STATE_PREPARING = 1;
    //准备完成
    public static final int STATE_PREPARED = 2;
    //播放中
    public static final int STATE_PLAYING = 3;
    //暂停
    public static final int STATE_PAUSED = 4;
    //播放完成
    public static final int STATE_PLAYBACK_COMPLETED = 5;
}
