package com.ytj.ytjijkplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ytj.ytjijkplayer.bean.VideoijkBean;
import com.ytj.ytjijkplayer.interfaces.IPlayController;
import com.ytj.ytjijkplayer.media.VideoPlayView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View mVideoView;
    private VideoPlayView mVideoPlayView;
    private List<VideoijkBean> mVideoList;
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("播放器测试");
            }
        }


        mVideoList = new ArrayList<>();
        mVideoView = findViewById(R.id.video_view);
        VideoijkBean videoijkBean1 = new VideoijkBean();
        videoijkBean1.setName("标清");
        videoijkBean1.setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        mVideoList.add(videoijkBean1);
        VideoijkBean videoijkBean2 = new VideoijkBean();
        videoijkBean2.setName("高清");
        videoijkBean2.setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4");
        mVideoList.add(videoijkBean2);
        mVideoPlayView = new VideoPlayView(mContext, mVideoView)
                .setTitle("测试")
                .setPlaySource(mVideoList)
                .showThumbnail(new IPlayController.onShowThumbnail() {
                    @Override
                    public void ShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(mContext)
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .into(ivThumbnail);
                    }
                })
                .hideResolution(true);
    }

    @Override
    public void onBackPressed() {
        if (mVideoPlayView != null && mVideoPlayView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mVideoPlayView != null) {
            mVideoPlayView.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoPlayView != null) {
            mVideoPlayView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoPlayView != null) {
            mVideoPlayView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlayView != null) {
            mVideoPlayView.onDestroy();
        }
    }
}
