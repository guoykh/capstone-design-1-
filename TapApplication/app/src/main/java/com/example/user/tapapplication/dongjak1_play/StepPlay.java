package com.example.user.tapapplication.dongjak1_play;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.user.tapapplication.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
//YouTubeBaseActivity로 상속 받는것에 유의

public class StepPlay extends YouTubeBaseActivity {
    YouTubePlayerView youTubeView;
    Button button;
    YouTubePlayer.OnInitializedListener listener;
    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongjak_play);
        button = (Button)findViewById(R.id.startbtn);
        youTubeView = (YouTubePlayerView)findViewById(R.id.youtubeView);
        video = (VideoView)findViewById(R.id.footprint);
        String uriPath = "android.resource://"+getPackageName()+"/"+R.raw.tap1;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.setMediaController(new MediaController(this));
        video.requestFocus();
        //리스너 등록부분
        listener = new YouTubePlayer.OnInitializedListener(){
            //초기화 성공시
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("3VWRT1r4qRM");//url의 맨 뒷부분 ID값만 넣으면 됨
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //첫번째 인자는 API키값 두번째는 실행할 리스너객체를 넘겨줌
                youTubeView.initialize("AIzaSyA-4pcloeELnyh3L0GY1ImeLCx7IvfWAP4", listener);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        video.start();
                    }
                },2500);
            }
        });
    }
}
