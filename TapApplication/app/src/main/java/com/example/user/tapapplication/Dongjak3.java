package com.example.user.tapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.user.tapapplication.dongjak_play.DragPlay;

/**
 * Created by user on 2018-01-14.
 */

public class Dongjak3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongjak3);
    }

    public void onClickDongjak(View view){
        if(view.getId()==R.id.dragbtn){
        Intent i = new Intent(this, DragPlay.class);
        startActivity(i);
        }
    }
}
