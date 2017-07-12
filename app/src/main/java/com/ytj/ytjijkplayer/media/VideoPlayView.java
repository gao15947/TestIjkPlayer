package com.ytj.ytjijkplayer.media;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ytj.ytjijkplayer.R;
import com.ytj.ytjijkplayer.adapter.ResolutionSelectedAdapter;
import com.ytj.ytjijkplayer.bean.VideoijkBean;
import com.ytj.ytjijkplayer.interfaces.IPlayController;
import com.ytj.ytjijkplayer.utils.CommonUtils;
import com.ytj.ytjijkplayer.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Administrator on 2017/7/7.
 */

public class VideoPlayView {
    private static final String TAG = VideoPlayView.class.getSimpleName();

    private Context mContext;
    private Activity mActivity;
    //整个播放布局
    private View mVideoLayout;
    //播放布局
    private IjkVideoView mVideoView;
    //顶部布局
    private View mVideoToolbarLayout;
    private ImageView mVideoToolbarBack;
    private TextView mVideoToolbarTitle;
    //底部布局
    private View mVideoBottomBarLayout;
    private ImageView mVideoPlay;
    private TextView mVideoCurrentTime;
    private TextView mVideoTotalTime;
    private SeekBar mVideoSeekBar;
    private TextView mVideoResolution;
    private ImageView mVideoFullScreen;
    //提示布局
    private View mVideoTipsLayout;
    //错误提示
    private LinearLayout mVideoErrorLayout;
    private TextView mVideoErrorText;
    private ImageView mVideoErrorReplay;
    //网络提示
    private LinearLayout mVideoNetLayout;
    private TextView mVideoNetPlay;
    //收费提示
    private LinearLayout mVideoFreeLayout;
    private TextView mVideoFreePay;

    //屏幕中部播放按钮
    private ImageView mStartPlay;
    //视频封面
    private ImageView mVideoCover;
    //加载布局
    private LinearLayout mLoadingView;
    private TextView mLoadingSpeed;
    //触摸提示布局
    private View mTouchView;
    //触摸改变声音布局
    private LinearLayout mVolumeView;
    private ImageView mVolumeIcon;
    private TextView mVolumeText;
    private ProgressBar mVolumeBar;
    //触摸改变亮度布局
    private LinearLayout mBrightnessView;
    private TextView mBrightnessText;
    private ProgressBar mBrightnessBar;
    //触摸快进/快退布局
    private LinearLayout mFastForwardView;
    private TextView mFastForwardTime;
    private TextView mFastForwardTarget;
    private TextView mFastForwardTotal;

    private boolean isLibInitSuccess = true;
    /**
     * 记录播放器竖屏时的高度
     */
    private int mInitHeight;
    /**
     * 音频管理器
     */
    private AudioManager mAudioManager;

    /**
     * 滑动进度条得到的新位置，和当前播放位置是有区别的,newPosition =0也会调用设置的，故初始化值为-1
     */
    private long mNewPosition = -1;
    /**
     * 当前亮度大小
     */
    private float mBrightness;
    /**
     * 当前声音大小
     */
    private int mCurrentVolume;
    /**
     * 设备最大音量
     */
    private int mMaxVolume;
    /**
     * 获取当前设备的宽度
     */
    private int mScreenWidth;
    /**
     * 是否在拖动进度条中，默认为停止拖动，true为在拖动中，false为停止拖动
     */
    private boolean isDraggingSeekBar = false;
    /**
     * 是否显示控制面板，默认为隐藏，true为显示false为隐藏
     */
    private boolean isShowControl = false;

    /**
     * 是否是竖屏，默认为竖屏，true为竖屏，false为横屏
     */
    private boolean isPortrait = true;

    /**
     * Activity界面方向监听
     */
    //private OrientationEventListener mOrientationEventListener;

    /**
     * 控制面板收起或者显示的轮询监听
     */
    private AutoPlayRunnable mAutoPlayRunnable = new AutoPlayRunnable();
    /**
     * 禁止收起控制面板，默认可以收起，true为禁止false为可触摸
     */
    private boolean isForbidHideControlPan;
    /**
     * 是否隐藏中间播放按钮，默认不隐藏，true为隐藏，false为不隐藏
     */
    private boolean isHideCenterPlayBtn;
    /**
     * 是否隐藏topbar，true为隐藏，false为不隐藏
     */
    private boolean isHideTopBar;
    /**
     * 是否隐藏bottombar，true为隐藏，false为不隐藏
     */
    private boolean isHideBottomBar;
    /**
     * 当前播放地址
     */
    private String mCurrentUrl;

    /**
     * 记录进入后台时的播放状态0为播放，1为暂停
     */
    private int mBGStatus;
    /**
     * 当前状态
     */
    private int mStatus = PlayStateParams.STATE_IDLE;
    /**
     * 当前播放位置
     */
    private int mCurrentPosition = 0;
    /**
     * 码流列表
     */
    private List<VideoijkBean> mVideoList = new ArrayList<VideoijkBean>();
    /**
     * 分辨率布局
     */
    private LinearLayout mResolutionLayout;
    /**
     *分辨率列表
     */
    private ListView mResolutionListView;
    /**
     * 当前选择的码流
     */
    private int mCurrentSelected = 0;
    /**
     * 码流列表适配器
     */
    private ResolutionSelectedAdapter mSelectedAdapter;
    /**
     * 当前是否切换视频流，默认为否，true是切换视频流，false没有切换
     */
    private boolean isHasSwitchStream;
    /**
     * 移动网络时是否允许播放
     */
    private boolean isAllowedPlay = false;
    /**
     * 是否是直播 默认为非直播，true为直播false为点播
     */
    private boolean isLive = false;
    //是否收费
    private boolean isCharge = false;
    //免费播放时长
    private int mMaxPlayTime = 0;
    //收费布局显示
    private boolean isShowChargeLayout = false;
    /**
     * 视频播放时信息回调
     */
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    /**
     * 视频的返回键监听
     */
    private IPlayController.onPlayBackListener mPlayerBack;
    //同步进度
    private final static int HANDLER_SHOW_PROGRESS = 1;
    //设置新位置
    private static final int HANDLER_SEEK_NEW_POSITION = 2;
    //隐藏提示的box
    private static final int HANDLER_HIDE_CENTER_BOX = 3;
    //重新播放
    private static final int HANDLER_RESTART_PLAY = 4;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SHOW_PROGRESS:
                    long pos = syncProgress();
                    if (!isDraggingSeekBar && isShowControl) {
                        msg = obtainMessage(HANDLER_SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
                case HANDLER_SEEK_NEW_POSITION:
                    if (!isLive && mNewPosition >= 0) {
                        mVideoView.seekTo((int) mNewPosition);
                        mNewPosition = -1;
                    }
                    break;
                case HANDLER_HIDE_CENTER_BOX:
                    mTouchView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    public VideoPlayView(Activity activity, View view) {
        mContext = activity;
        mActivity = activity;
        initView(view);
    }

    /**
     * 隐藏分辨率按钮，true隐藏，false为显示
     */
    public VideoPlayView hideResolution(boolean isHide) {
        mVideoResolution.setVisibility(isHide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 隐藏中间播放按钮,ture为隐藏，false为不做隐藏处理，但不是显示
     */
    public VideoPlayView hideCenterPlayer(boolean isHide) {
        isHideCenterPlayBtn = isHide;
        mStartPlay.setVisibility(isHideCenterPlayBtn ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 是否隐藏topbar，true为隐藏，false为不隐藏，但不一定是显示
     */
    public VideoPlayView hideHideTopBar(boolean isHide) {
        isHideTopBar = isHide;
        mVideoToolbarLayout.setVisibility(isHideTopBar ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 是否隐藏bottonbar，true为隐藏，false为不隐藏，但不一定是显示
     */
    public VideoPlayView hideBottonBar(boolean isHide) {
        isHideBottomBar = isHide;
        mVideoBottomBarLayout.setVisibility(isHideBottomBar ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 是否隐藏上下bar，true为隐藏，false为不隐藏，但不一定是显示
     */
    public VideoPlayView hideControlPan(boolean isHide) {
        hideBottonBar(isHide);
        hideHideTopBar(isHide);
        return this;
    }

    /**
     * 设置是否禁止隐藏bar
     */
    public VideoPlayView setForbidHideControlPanl(boolean flag) {
        this.isForbidHideControlPan = flag;
        return this;
    }

    /**
     * 设置视频名称
     */
    public VideoPlayView setTitle(String title) {
        mVideoToolbarTitle.setText(title);
        return this;
    }

    /**
     * 是否直播，由外部传入
     *
     * @param live
     */
    public VideoPlayView setLive(boolean live) {
        isLive = live;
        return this;
    }

    /**
     * 设置显示加载网速或者隐藏
     */
    public VideoPlayView setShowSpeed(boolean isShow) {
        mLoadingSpeed.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置最大观看时长
     *
     * @param isCharge    true为收费 false为免费即不做限制
     * @param maxPlayTime 最大能播放时长，单位秒
     * tips:收费视频需再开个监控线程
     */
    public VideoPlayView setChargeTie(boolean isCharge, int maxPlayTime) {
        this.isCharge = isCharge;
        this.mMaxPlayTime = maxPlayTime * 1000;
        return this;
    }

    /**
     * 显示缩略图
     */
    public VideoPlayView showThumbnail(IPlayController.onShowThumbnail showThumbnail) {
        if (showThumbnail != null && mVideoCover != null) {
            showThumbnail.ShowThumbnail(mVideoCover);
        }
        return this;
    }

    /**
     * 设置播放地址
     * 包括视频清晰度列表
     * 对应地址列表
     */
    public VideoPlayView setPlaySource(List<VideoijkBean> list) {
        mVideoList.clear();
        if (list != null && list.size() > 0) {
            mVideoList.addAll(list);
            switchStream(0);
        }
        return this;
    }

    /**
     * 设置播放地址
     * 单个视频VideoijkBean
     */
    public VideoPlayView setPlaySource(VideoijkBean videoijkBean) {
        mVideoList.clear();
        if (videoijkBean != null) {
            mVideoList.add(videoijkBean);
            switchStream(0);
        }
        return this;
    }

    /**
     * 设置播放地址
     * 单个视频地址时
     * 带流名称
     */
    public VideoPlayView setPlaySource(String name, String url) {
        VideoijkBean mVideoijkBean = new VideoijkBean();
        mVideoijkBean.setName(name);
        mVideoijkBean.setUrl(url);
        setPlaySource(mVideoijkBean);
        return this;
    }

    /**
     * 设置播放地址
     * 单个视频地址时
     */
    public VideoPlayView setPlaySource(String url) {
        setPlaySource("标清", url);
        return this;
    }

    public VideoPlayView startPlay() {
        if (isLive) {
            mVideoView.setVideoPath(mCurrentUrl);
            mVideoView.seekTo(0);
        } else {
            if (isHasSwitchStream || mStatus == PlayStateParams.STATE_ERROR) {
                //换源之后声音可播，画面卡住，主要是渲染问题，目前只是提供了软解方式，后期提供设置方式
                mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
                mVideoView.setVideoPath(mCurrentUrl);
                mVideoView.seekTo(mCurrentPosition);
                isHasSwitchStream = false;
            }
        }
        hideStatusUI();
        if (NetworkUtils.getNetworkType(mContext) == 4 && !isAllowedPlay) {
            showMobileNetworkTips();
        } else {
            if (isCharge && mMaxPlayTime < getCurrentPosition()) {
                showChargeTips();
            } else {
                if (isLibInitSuccess) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    mVideoView.start();
                } else {
                    showErrorTips(mActivity.getResources()
                            .getString(R.string.player_error_no_device), false);
                }
            }
        }
        return this;
    }

    /**
     * 设置播放位置
     */
    public VideoPlayView seekTo(int playtime) {
        mVideoView.seekTo(playtime);
        return this;
    }

    /**
     * 设置播放信息监听回调
     */
    public VideoPlayView setOnInfoListener(IMediaPlayer.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
        return this;
    }

    /**
     * 设置播放器中的返回键监听
     */
    public VideoPlayView setPlayerBackListener(IPlayController.onPlayBackListener listener) {
        this.mPlayerBack = listener;
        return this;
    }

    /**
     * 获取界面方向
     */
    public int getScreenOrientation() {
        int rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    public void onPause() {
        mBGStatus = (mVideoView.isPlaying() ? 0 : 1);
        getCurrentPosition();
        mVideoView.onPause();
    }

    public void onResume() {
        mVideoView.onResume();
        if (isLive) {
            mVideoView.seekTo(0);
        } else {
            mVideoView.seekTo(mCurrentPosition);
        }
        if (mBGStatus != 0) {
            pausePlay();
        }
    }

    public void onDestroy() {
        //mOrientationEventListener.disable();
        mHandler.removeMessages(HANDLER_RESTART_PLAY);
        mHandler.removeMessages(HANDLER_SEEK_NEW_POSITION);
        mVideoView.stopPlayback();
    }

    public boolean onBackPressed() {
        if (mResolutionLayout.getVisibility() == View.VISIBLE) {
            hideResolutionSelection();
            return true;
        }

        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }

        return false;
    }

    public void onConfigurationChanged(final Configuration newConfig) {
        isPortrait = (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT);
        doOnConfigurationChanged(isPortrait);
    }

    private void doOnConfigurationChanged(final boolean isPortrait) {
        if (mVideoView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    tryFullScreen(!isPortrait);
                    if (isPortrait) {
                        CommonUtils.setHeight(mContext, mVideoLayout, mInitHeight, false);
                    } else {
                        int heightPixels = mActivity.getResources().getDisplayMetrics().heightPixels;
                        int widthPixels = mActivity.getResources().getDisplayMetrics().widthPixels;
                        CommonUtils.setHeight(mContext, mVideoLayout,
                                Math.min(heightPixels, widthPixels), false);
                    }
                    updateFullScreenButton();
                    setProcessDurationOrientation(isPortrait);
                }
            });
            //mOrientationEventListener.enable();
        }
    }

    private void tryFullScreen(boolean fullScreen) {
        if (mActivity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide();
                } else {
                    supportActionBar.show();
                }
            }
        }
        setFullScreen(fullScreen);
    }

    private void setFullScreen(boolean fullScreen) {
        if (mActivity != null) {
            WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
            if (fullScreen) {
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                mActivity.getWindow().setAttributes(attrs);
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } else {
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mActivity.getWindow().setAttributes(attrs);
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
    }

    /**
     * 设置全屏和非全屏显示的布局
     */
    private void setProcessDurationOrientation(boolean portrait) {
        if (portrait) {
            hideResolution(true);
        } else {
            hideResolution(false);
        }
    }

    /**
     * 选择要播放的流
     */
    private void switchStream(int index) {
        if (mVideoList.size() > index) {
            mVideoResolution.setText(mVideoList.get(index).getName());
            mCurrentUrl = mVideoList.get(index).getUrl();
            mVideoList.get(index).setSelected(true);
            if (mVideoView.isPlaying()) {
                getCurrentPosition();
                mVideoView.release(false);
            }
            isHasSwitchStream = true;
        }
    }

    private void initView(View view) {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            isLibInitSuccess = true;
        } catch (Exception e) {
            isLibInitSuccess = false;
            e.printStackTrace();
        }

        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVideoLayout = view;
        mVideoView = (IjkVideoView) view.findViewById(R.id.ytj_player_video_view);
        mStartPlay = (ImageView) view.findViewById(R.id.ytj_player_video_play);
        mVideoToolbarLayout = view.findViewById(R.id.ytj_player_toolbar_layout);
        mVideoToolbarBack = (ImageView) view.findViewById(R.id.ytj_player_full_screen_back);
        mVideoToolbarTitle = (TextView) view.findViewById(R.id.ytj_player_video_title);
        mVideoBottomBarLayout = view.findViewById(R.id.ytj_player_bar_layout);
        mVideoPlay = (ImageView) view.findViewById(R.id.ytj_player_play);
        mVideoCurrentTime = (TextView) view.findViewById(R.id.ytj_player_current_time);
        mVideoTotalTime = (TextView) view.findViewById(R.id.ytj_player_total_time);
        mVideoSeekBar = (SeekBar) view.findViewById(R.id.ytj_player_progress);
        mVideoResolution = (TextView) view.findViewById(R.id.ytj_player_resolution);
        mVideoFullScreen = (ImageView) view.findViewById(R.id.ytj_player_full_screen);
        mVideoCover = (ImageView) view.findViewById(R.id.ytj_player_video_cover);
        mLoadingView = (LinearLayout) view.findViewById(R.id.ytj_player_video_loading);
        mLoadingSpeed = (TextView) view.findViewById(R.id.ytj_player_video_speed);
        mTouchView = view.findViewById(R.id.ytj_player_touch_layout);
        mVolumeView = (LinearLayout) view.findViewById(R.id.ytj_player_volume_layout);
        mVolumeIcon = (ImageView) view.findViewById(R.id.ytj_player_volume_icon);
        mVolumeText = (TextView) view.findViewById(R.id.ytj_player_volume_text);
        mVolumeBar = (ProgressBar) view.findViewById(R.id.ytj_player_volume_progress);
        mBrightnessView = (LinearLayout) view.findViewById(R.id.ytj_player_brightness_layout);
        mBrightnessText = (TextView) view.findViewById(R.id.ytj_player_brightness_text);
        mBrightnessBar = (ProgressBar) view.findViewById(R.id.ytj_player_brightness_progress);
        mFastForwardView = (LinearLayout) view.findViewById(R.id.ytj_player_fastForward_layout);
        mFastForwardTime = (TextView) view.findViewById(R.id.ytj_player_fastForward_time);
        mFastForwardTarget = (TextView) view.findViewById(R.id.ytj_player_fastForward_target);
        mFastForwardTotal = (TextView) view.findViewById(R.id.ytj_player_fastForward_total);

        mVideoTipsLayout = view.findViewById(R.id.ytj_player_tips_layout);
        //错误提示
        mVideoErrorLayout = (LinearLayout) view.findViewById(R.id.ytj_player_replay);
        mVideoErrorText = (TextView) view.findViewById(R.id.ytj_player_status_text);
        mVideoErrorReplay = (ImageView) view.findViewById(R.id.ytj_player_replay_icon);
        //网络提示
        mVideoNetLayout = (LinearLayout) view.findViewById(R.id.ytj_player_netTie);
        mVideoNetPlay = (TextView) view.findViewById(R.id.ytj_player_netTie_play);
        //收费提示
        mVideoFreeLayout = (LinearLayout) view.findViewById(R.id.ytj_player_freeTie);
        mVideoFreePay = (TextView) view.findViewById(R.id.ytj_player_freeTie_pay);
        //分辨率布局
        mResolutionLayout = (LinearLayout) view.findViewById(R.id.ytj_player_resolution_layout);
        mResolutionListView = (ListView) view.findViewById(R.id.ytj_player_resolution_list_view);
        initEvent();
    }

    private void initEvent() {
        mVideoErrorReplay.setOnClickListener(mOnClickListener);
        mVideoNetPlay.setOnClickListener(mOnClickListener);
        mVideoFreePay.setOnClickListener(mOnClickListener);
        mVideoToolbarBack.setOnClickListener(mOnClickListener);
        mVideoPlay.setOnClickListener(mOnClickListener);
        mVideoSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mVideoResolution.setOnClickListener(mOnClickListener);
        mVideoFullScreen.setOnClickListener(mOnClickListener);
        mStartPlay.setOnClickListener(mOnClickListener);

        mSelectedAdapter = new ResolutionSelectedAdapter(mContext, mVideoList);
        mResolutionListView.setAdapter(mSelectedAdapter);
        mResolutionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideResolutionSelection();
                if (mCurrentSelected == position) {
                    return;
                }
                mCurrentSelected = position;
                switchStream(position);
                for (int i = 0; i < mVideoList.size(); i++) {
                    if (i == position) {
                        mVideoList.get(i).setSelected(true);
                    } else {
                        mVideoList.get(i).setSelected(false);
                    }
                }
                mSelectedAdapter.notifyDataSetChanged();
                startPlay();
            }
        });


        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                if (what == IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH) {
                    mLoadingSpeed.setText(getFormatSize(extra));
                }
                statusChange(what);

                if (mOnInfoListener != null) {
                    mOnInfoListener.onInfo(iMediaPlayer, what, extra);
                }

                if (isCharge && mMaxPlayTime < getCurrentPosition()) {
                    showChargeTips();
                    pausePlay();
                }
                return true;
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(mContext, new PlayGestureListener());
        mVideoLayout.setClickable(true);
        mVideoLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (mAutoPlayRunnable != null) {
                            mAutoPlayRunnable.stop();
                        }
                        break;
                }
                if (gestureDetector.onTouchEvent(event))
                    return true;
                // 处理手势结束
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                return false;
            }
        });

/*        mOrientationEventListener = new OrientationEventListener(mContext) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 0 && orientation <= 30 || orientation >= 330
                        || (orientation >= 150 && orientation <= 210)) {
                    //竖屏
                    if (isPortrait) {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        mOrientationEventListener.disable();
                    }
                } else if ((orientation >= 90 && orientation <= 120)
                        || (orientation >= 240 && orientation <= 300)) {
                    if (!isPortrait) {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        mOrientationEventListener.disable();
                    }
                }
            }
        };*/

        isPortrait = (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mInitHeight = mVideoLayout.getLayoutParams().height;
        hideAllLayout();

        if (isLibInitSuccess) {
            mVideoCover.setVisibility(View.VISIBLE);
        } else {
            showErrorTips(mActivity.getResources().getString(R.string.player_error_no_device), false);
        }
    }

    private void hideAllLayout() {
        if (!isForbidHideControlPan) {
            mVideoToolbarLayout.setVisibility(View.GONE);
            mVideoBottomBarLayout.setVisibility(View.GONE);
        }
    }

    private void showErrorTips(String tips, boolean isReplay) {
        hideStatusUI();
        mVideoTipsLayout.setVisibility(View.VISIBLE);
        mVideoErrorLayout.setVisibility(View.VISIBLE);
        mVideoNetLayout.setVisibility(View.GONE);
        mVideoFreeLayout.setVisibility(View.GONE);
        mVideoErrorText.setText(tips);
        mVideoErrorReplay.setVisibility(isReplay ? View.VISIBLE : View.GONE);
    }

    private void showChargeTips() {
        hideStatusUI();
        mVideoTipsLayout.setVisibility(View.VISIBLE);
        mVideoErrorLayout.setVisibility(View.GONE);
        mVideoNetLayout.setVisibility(View.GONE);
        mVideoFreeLayout.setVisibility(View.VISIBLE);
    }

    private void showMobileNetworkTips() {
        hideStatusUI();
        mVideoTipsLayout.setVisibility(View.VISIBLE);
        mVideoErrorLayout.setVisibility(View.GONE);
        mVideoNetLayout.setVisibility(View.VISIBLE);
        mVideoFreeLayout.setVisibility(View.GONE);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.ytj_player_full_screen_back) {
                //退出全屏
                if (!isPortrait) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    if (mPlayerBack != null) {
                        mPlayerBack.onPlayBack();
                    } else {
                        mActivity.finish();
                    }
                }
            } else if (id == R.id.ytj_player_play || id == R.id.ytj_player_video_play) {
                //播放/暂停
                if (mVideoView.isPlaying()) {
                    if (isLive) {
                        mVideoView.stopPlayback();
                    } else {
                        pausePlay();
                    }
                } else {
                    startPlay();
                    if (mVideoView.isPlaying()) {
                        mStatus = PlayStateParams.STATE_PREPARING;
                        hideStatusUI();
                    }
                }
                updatePausePlay();
            } else if (id == R.id.ytj_player_resolution) {
                //分辨率选择
                resolutionSelection();
            } else if (id == R.id.ytj_player_full_screen) {
                //全屏切换
                toggleFullScreen();
            } else if (id == R.id.ytj_player_replay_icon) {
                //重新播放
                startPlay();
            } else if (id == R.id.ytj_player_netTie_play) {
                //继续播放
                if (!isAllowedPlay) {
                    isAllowedPlay = true;
                }
                startPlay();
            } else if (id == R.id.ytj_player_freeTie_pay) {
                //购买
            }
        }
    };

    /**
     * 分辨率选择
     */
    private void resolutionSelection() {
        mResolutionLayout.setVisibility(View.VISIBLE);
        mResolutionListView.setItemsCanFocus(true);
    }

    /**
     * 隐藏分辨率选择布局
     */
    private void hideResolutionSelection() {
        mResolutionLayout.setVisibility(View.GONE);
    }



    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                int position = (int) ((getDuration() * progress * 1.0) / 1000);
                String time = generateTime(position);
                mVideoCurrentTime.setText(time);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isDraggingSeekBar = true;
            mHandler.removeMessages(HANDLER_SHOW_PROGRESS);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mVideoView.seekTo((int) ((getDuration() * seekBar.getProgress() * 1.0) / 1000));
            mHandler.removeMessages(HANDLER_SHOW_PROGRESS);
            isDraggingSeekBar = false;
            mHandler.sendEmptyMessageDelayed(HANDLER_SHOW_PROGRESS, 1000);
        }
    };

    private long syncProgress() {
        if (isDraggingSeekBar) {
            return 0;
        }

        long position = mVideoView.getCurrentPosition();
        long duration = mVideoView.getDuration();
        if (mVideoSeekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mVideoSeekBar.setProgress((int) pos);
            }

            int percent = mVideoView.getBufferPercentage();
            mVideoSeekBar.setSecondaryProgress(percent * 10);
        }

        if (isCharge && mMaxPlayTime + 1000 < getCurrentPosition()) {
            showChargeTips();
            pausePlay();
        } else {
            mVideoCurrentTime.setText(generateTime(position));
            mVideoTotalTime.setText(generateTime(duration));
        }

        return position;
    }

    /**
     * 更新播放、暂停和停止按钮
     */
    private void updatePausePlay() {
        if (mVideoView.isPlaying()) {
            if (isLive) {
                mVideoPlay.setImageResource(R.drawable.ytj_player_controller_stop);
            } else {
                mVideoPlay.setImageResource(R.drawable.ytj_player_controller_pause);
                mStartPlay.setImageResource(R.drawable.ytj_player_center_pause);
            }
        } else {
            mVideoPlay.setImageResource(R.drawable.ytj_player_controller_play);
            mStartPlay.setImageResource(R.drawable.ytj_player_center_play);
        }
    }

    private void pausePlay() {
        mStatus = PlayStateParams.STATE_PAUSED;
        getCurrentPosition();
        mVideoView.pause();
    }

    /**
     * 获取视频播放总时长
     */
    private long getDuration() {
        return mVideoView.getDuration();
    }

    private int getCurrentPosition() {
        if (!isLive) {
            mCurrentPosition = mVideoView.getCurrentPosition();
        } else {
            /*直播*/
            mCurrentPosition = -1;
        }
        return mCurrentPosition;
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mCurrentVolume = -1;
        mBrightness = -1f;
        if (mNewPosition >= 0) {
            mHandler.removeMessages(HANDLER_SEEK_NEW_POSITION);
            mHandler.sendEmptyMessage(HANDLER_SEEK_NEW_POSITION);
        }

        mHandler.removeMessages(HANDLER_HIDE_CENTER_BOX);
        mHandler.sendEmptyMessageDelayed(HANDLER_HIDE_CENTER_BOX, 500);
        if (mAutoPlayRunnable != null) {
            mAutoPlayRunnable.start();
        }
    }

    /**
     * 显示或隐藏操作面板
     */
    private void operatorPan() {
        isShowControl = !isShowControl;
        if (isShowControl) {
            mVideoToolbarLayout.setVisibility(isHideTopBar ? View.GONE : View.VISIBLE);
            mVideoBottomBarLayout.setVisibility(isHideBottomBar ? View.GONE : View.VISIBLE);
            if (isLive) {
                mVideoSeekBar.setVisibility(View.INVISIBLE);
            } else {
                mVideoSeekBar.setVisibility(View.VISIBLE);
            }
            //显示面板的时候再根据状态显示播放按钮
            if (mStatus == PlayStateParams.STATE_PLAYING
                    || mStatus == PlayStateParams.STATE_PREPARED
                    || mStatus == PlayStateParams.STATE_PREPARING
                    || mStatus == PlayStateParams.STATE_PAUSED) {
                if (isHideCenterPlayBtn || !isPortrait) {
                    mStartPlay.setVisibility(View.GONE);
                } else {
                    mStartPlay.setVisibility(isLive ? View.GONE : View.VISIBLE);
                }
            } else {
                mStartPlay.setVisibility(View.GONE);
            }
            updatePausePlay();
            mHandler.sendEmptyMessage(HANDLER_SHOW_PROGRESS);
            mAutoPlayRunnable.start();
        } else {
            if (isHideTopBar) {
                mVideoToolbarLayout.setVisibility(View.GONE);
            } else {
                mVideoToolbarLayout.setVisibility(isForbidHideControlPan ? View.VISIBLE : View.GONE);
            }
            if (isHideBottomBar) {
                mVideoBottomBarLayout.setVisibility(View.GONE);
            } else {
                mVideoBottomBarLayout.setVisibility(isForbidHideControlPan ? View.VISIBLE : View.GONE);
            }
            if (!isLive && mStatus == PlayStateParams.STATE_PAUSED && !mVideoView.isPlaying()) {
                if (isHideCenterPlayBtn) {
                    mStartPlay.setVisibility(View.GONE);
                } else {
                    //暂停时一直显示按钮
                    mStartPlay.setVisibility(View.VISIBLE);
                }
            } else {
                mStartPlay.setVisibility(View.GONE);
            }
            mHandler.removeMessages(HANDLER_SHOW_PROGRESS);
            mAutoPlayRunnable.stop();
        }
    }

    /**
     * 时长格式化显示
     */
    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * 全屏切换
     */
    private void toggleFullScreen() {
        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        updateFullScreenButton();
    }

    /**
     * 更新全屏和半屏按钮
     */
    private void updateFullScreenButton() {
        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mVideoFullScreen.setImageResource(R.drawable.ytj_player_control_full_screen_shrink);
        } else {
            mVideoFullScreen.setImageResource(R.drawable.ytj_player_control_full_screen_stretch);
        }
    }

    /**
     * 隐藏状态界面
     */
    private void hideStatusUI() {
        mStartPlay.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mTouchView.setVisibility(View.GONE);
        mVideoTipsLayout.setVisibility(View.GONE);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mCurrentVolume == -1) {
            mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mCurrentVolume < 0)
                mCurrentVolume = 0;
        }
        int index = (int) (percent * mMaxVolume) + mCurrentVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }
        // 显示
        mTouchView.setVisibility(View.VISIBLE);
        mVolumeView.setVisibility(View.VISIBLE);
        mStartPlay.setVisibility(View.GONE);
        mBrightnessView.setVisibility(View.GONE);
        mFastForwardView.setVisibility(View.GONE);
        mVolumeIcon.setImageResource(i == 0 ? R.drawable.ytj_player_volume_off
                : R.drawable.ytj_player_volume_up);
        mVolumeText.setText(s);
        mVolumeBar.setProgress(i);
    }

    /**
     * 快进或者快退滑动改变进度
     *
     * @param percent
     */
    private void onProgressSlide(float percent) {
        int position = mVideoView.getCurrentPosition();
        long duration = mVideoView.getDuration();
        long deltaMax = Math.min(100 * 1000, duration - position);
        long delta = (long) (deltaMax * percent);
        mNewPosition = delta + position;
        if (mNewPosition > duration) {
            mNewPosition = duration;
        } else if (mNewPosition <= 0) {
            mNewPosition = 0;
            delta = -position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            mTouchView.setVisibility(View.VISIBLE);
            mFastForwardView.setVisibility(View.VISIBLE);
            mStartPlay.setVisibility(View.GONE);
            mVolumeView.setVisibility(View.GONE);
            mBrightnessView.setVisibility(View.GONE);
            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
            mFastForwardTime.setText(text + "s" + "");
            mFastForwardTarget.setText(generateTime(mNewPosition) + "/" + "");
            mFastForwardTotal.setText(generateTime(duration));
        }
    }

    /**
     * 亮度滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = mActivity.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f) {
                mBrightness = 0.50f;
            } else if (mBrightness < 0.01f) {
                mBrightness = 0.01f;
            }
        }
        mTouchView.setVisibility(View.VISIBLE);
        mBrightnessView.setVisibility(View.VISIBLE);
        mStartPlay.setVisibility(View.GONE);
        mFastForwardView.setVisibility(View.GONE);
        mVolumeView.setVisibility(View.GONE);

        WindowManager.LayoutParams lpa = mActivity.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        mBrightnessText.setText(((int) (lpa.screenBrightness * 100)) + "%" + "");
        mBrightnessBar.setProgress((int) (lpa.screenBrightness * 100));
        mActivity.getWindow().setAttributes(lpa);
    }

    /**
     * 状态改变同步UI
     */
    private void statusChange(int what) {
        switch (what) {
            case PlayStateParams.STATE_PLAYBACK_COMPLETED:
                mStatus = PlayStateParams.STATE_PLAYBACK_COMPLETED;
                mCurrentPosition = 0;
                hideAllLayout();
                break;
            case PlayStateParams.STATE_PREPARING:
                mStatus = PlayStateParams.STATE_PREPARING;
                hideStatusUI();
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case PlayStateParams.STATE_PREPARED:
                /*视频缓冲结束后隐藏缩列图*/
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*显示控制bar*/
                        isShowControl = false;
                        operatorPan();
                        /*延迟0.5秒隐藏视频封面隐藏*/
                        mVideoCover.setVisibility(View.GONE);
                    }
                }, 500);
                break;
            case PlayStateParams.STATE_PAUSED:
            case PlayStateParams.STATE_PLAYING:
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (mStatus == PlayStateParams.STATE_PAUSED) {
                    mStatus = PlayStateParams.STATE_PAUSED;
                } else {
                    mStatus = PlayStateParams.STATE_PLAYING;
                }
                hideStatusUI();
                break;
            case PlayStateParams.STATE_ERROR:
            case IMediaPlayer.MEDIA_ERROR_IO:
            case IMediaPlayer.MEDIA_ERROR_MALFORMED:
            case IMediaPlayer.MEDIA_ERROR_UNSUPPORTED:
            case IMediaPlayer.MEDIA_ERROR_TIMED_OUT:
            case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                mStatus = PlayStateParams.STATE_ERROR;
                if (NetworkUtils.getNetworkType(mContext) == 3) {
                    if (isCharge && mMaxPlayTime < getCurrentPosition()) {
                        showChargeTips();
                    } else {
                        showErrorTips(mActivity.getResources()
                                .getString(R.string.player_error_play_failed), true);
                    }
                } else {
                    if (NetworkUtils.getNetworkType(mContext) == 1) {
                        showErrorTips(mActivity.getResources()
                                .getString(R.string.player_error_no_network), true);
                    } else {
                        if (!isAllowedPlay) {
                            showMobileNetworkTips();
                            pausePlay();
                        }
                    }
                }
            default:
                break;
        }
    }

    /**
     * 下载速度格式化显示
     */
    private String getFormatSize(int size) {
        long fileSize = (long) size;
        String showSize = "";
        if (fileSize >= 0 && fileSize < 1024) {
            showSize = fileSize + "Kb/s";
        } else if (fileSize >= 1024 && fileSize < (1024 * 1024)) {
            showSize = Long.toString(fileSize / 1024) + "KB/s";
        } else if (fileSize >= (1024 * 1024) && fileSize < (1024 * 1024 * 1024)) {
            showSize = Long.toString(fileSize / (1024 * 1024)) + "MB/s";
        }
        return showSize;
    }

    /**
     * 收起控制面板轮询，默认3秒无操作，收起控制面板，
     */
    private class AutoPlayRunnable implements Runnable {
        private int AUTO_PLAY_INTERVAL = 3000;
        private boolean mShouldAutoPlay;

        /**
         * 五秒无操作，收起控制面板
         */
        private AutoPlayRunnable() {
            mShouldAutoPlay = false;
        }

        private void start() {
            if (!mShouldAutoPlay) {
                mShouldAutoPlay = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, AUTO_PLAY_INTERVAL);
            }
        }

        private void stop() {
            if (mShouldAutoPlay) {
                mHandler.removeCallbacks(this);
                mShouldAutoPlay = false;
            }
        }

        @Override
        public void run() {
            if (mShouldAutoPlay) {
                mHandler.removeCallbacks(this);
                operatorPan();
            }
        }
    }

    private class PlayGestureListener extends GestureDetector.SimpleOnGestureListener {
        /**
         * 是否是按下的标识，默认为其他动作，true为按下标识，false为其他动作
         */
        private boolean isDownTouch;
        /**
         * 是否声音控制,默认为亮度控制，true为声音控制，false为亮度控制
         */
        private boolean isVolume;
        /**
         * 是否横向滑动，默认为纵向滑动，true为横向滑动，false为纵向滑动
         */
        private boolean isLandscape;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            toggleFullScreen();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            isDownTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            operatorPan();
            hideResolutionSelection();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (isDownTouch) {
                isLandscape = Math.abs(distanceX) >= Math.abs(distanceY);
                isVolume = mOldX > mScreenWidth * 0.5f;
                isDownTouch = false;
            }

            if (isLandscape) {
                if (!isLive) {
                    /*进度设置*/
                    onProgressSlide(-deltaX / mVideoView.getWidth());
                }
            } else {
                float percent = deltaY / mVideoView.getHeight();
                if (isVolume) {
                    /*声音设置*/
                    onVolumeSlide(percent);
                } else {
                    /*亮度设置*/
                    onBrightnessSlide(percent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}
