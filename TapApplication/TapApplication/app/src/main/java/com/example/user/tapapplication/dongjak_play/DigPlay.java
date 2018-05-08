package com.example.user.tapapplication.dongjak_play;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.user.tapapplication.R;


public class DigPlay extends Activity {
    Button button;
    VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongjak_play);

        TextView tv = findViewById(R.id.tap_name);
        tv.setText("딕");

        button = (Button)findViewById(R.id.startbtn);
        video = (VideoView)findViewById(R.id.footprint);
        String uriPath = "android.resource://"+getPackageName()+"/"+R.raw.dig;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.setMediaController(new MediaController(this));
        video.requestFocus();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                video.start();
            }
        });
    }
}
