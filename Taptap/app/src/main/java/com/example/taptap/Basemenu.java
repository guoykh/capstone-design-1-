package com.example.taptap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Basemenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basemenu);
    }

    public void onClickfirst(View view) {
        Intent intent = new Intent(this, Firstmenu.class);
        startActivity(intent);
    }

    public void onClicksecond(View view) {
        Intent intent = new Intent(this, Secondmenu.class);
        startActivity(intent);
    }

    public void onClickthird(View view) {
        Intent intent = new Intent(this, Thirdmenu.class);
        startActivity(intent);
    }
}
