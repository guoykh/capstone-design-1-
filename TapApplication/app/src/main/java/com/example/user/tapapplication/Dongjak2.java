package com.example.user.tapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.user.tapapplication.dongjak_play.BrushPlay;
import com.example.user.tapapplication.dongjak_play.BuffPlay;
import com.example.user.tapapplication.dongjak_play.CrampPlay;
import com.example.user.tapapplication.dongjak_play.FlapPlay;
import com.example.user.tapapplication.dongjak_play.StampPlay;
import com.example.user.tapapplication.dongjak_play.ShufflePlay;

/**
 * Created by user on 2018-01-14.
 */

public class Dongjak2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongjak2);
    }
    public void onClickDongjak(View view){
        if(view.getId() == R.id.sufflebtn){
            Intent i = new Intent(this, ShufflePlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.flapbtn){
            Intent i = new Intent(this, FlapPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.cramprbtn){
            Intent i = new Intent(this, CrampPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.buffbtn){
            Intent i = new Intent(this, BuffPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.brusbtn){
            Intent i = new Intent(this, BrushPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.stampbtn){
            Intent i = new Intent(this, StampPlay.class);
            startActivity(i);
        }
    }

}
