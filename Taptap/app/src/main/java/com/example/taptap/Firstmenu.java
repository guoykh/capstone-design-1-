package com.example.taptap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Firstmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenu2);

    }

    public void onClickTap1(View view) {
        Intent intent = new Intent(this, Tap1.class);
        startActivity(intent);
    }

    public void onClickTap2(View view) {
        Intent intent = new Intent(this, Tap2.class);
        startActivity(intent);
    }

    public void onClickHop(View view) {
        Intent intent = new Intent(this, Hop.class);
        startActivity(intent);
    }

    public void onClickHill(View view) {
        Intent intent = new Intent(this, Hill.class);
        startActivity(intent);
    }

    public void onClickDig(View view) {
        Intent intent = new Intent(this, Dig.class);
        startActivity(intent);
    }

    public void onClickBallC(View view) {
        Intent intent = new Intent(this, BallC.class);
        startActivity(intent);
    }

    public void onClickBallD(View view) {
        Intent intent = new Intent(this, BallD.class);
        startActivity(intent);
    }

    public void onClickStep(View view) {
        Intent intent = new Intent(this, Step.class);
        startActivity(intent);
    }
}
