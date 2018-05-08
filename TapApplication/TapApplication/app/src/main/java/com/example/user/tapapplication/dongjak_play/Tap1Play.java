package com.example.user.tapapplication.dongjak_play;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.user.tapapplication.R;
import com.example.user.tapapplication.dongjak1_practice.Tap1Practice;


public class Tap1Play extends Activity {
    Button button,practice;
    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongjak_play);

        TextView tv = findViewById(R.id.tap_name);
        tv.setText("탭1");

        button = (Button)findViewById(R.id.startbtn);
        video = (VideoView)findViewById(R.id.footprint);
        String uriPath = "android.resource://"+getPackageName()+"/"+R.raw.tap1;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.setMediaController(new MediaController(this));
        video.requestFocus();

        practice = findViewById(R.id.practice);
        practice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(Tap1Play.this, Tap1Practice.class);
                startActivity(i);
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               video.start();
            }
        });
    }

}
