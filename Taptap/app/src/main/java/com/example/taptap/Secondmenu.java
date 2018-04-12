package com.example.taptap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Secondmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondmenu);
    }

    public void onClickShuffle(View view) {
        Intent intent = new Intent(this, Shuffle.class);
        startActivity(intent);
    }

    public void onClickFlap(View view) {
        Intent intent = new Intent(this, Flap.class);
        startActivity(intent);
    }

    public void onClickCramp(View view) {
        Intent intent = new Intent(this, Cramp.class);
        startActivity(intent);
    }

    public void onClickBuffaro(View view) {
        Intent intent = new Intent(this, Buffaro.class);
        startActivity(intent);
    }

    public void onClickBrush(View view) {
        Intent intent = new Intent(this, Brush.class);
        startActivity(intent);
    }

    public void onClickStamp(View view) {
        Intent intent = new Intent(this, Stamp.class);
        startActivity(intent);
    }
}
