package com.example.taptap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickBase(View view) {
        Intent intent = new Intent(this, Basemenu.class);
        startActivity(intent);
    }

    public void onClickDance(View view) {
        Intent intent = new Intent(this, Dancemenu.class);
        startActivity(intent);
    }

    public void onClickIncorrect(View view) {
        Intent intent = new Intent(this, Incorretmenu.class);
        startActivity(intent);
    }

    public void onClickBookmark(View view) {
        Intent intent = new Intent(this, Bookmarkmenu.class);
        startActivity(intent);
    }

    public void onClickBattery(View view) {
        Intent intent = new Intent(this, Batterymenu.class);
        startActivity(intent);
    }
}
