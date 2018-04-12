package com.example.taptap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Thirdmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdmenu);
    }

    public void onClickDrag(View view) {
        Intent intent = new Intent(this, Drag.class);
        startActivity(intent);
    }
}
