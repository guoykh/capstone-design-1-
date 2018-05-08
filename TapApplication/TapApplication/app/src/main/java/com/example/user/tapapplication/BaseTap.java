package com.example.user.tapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseTap extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_tap);
    }

    public void onClickdongjak1(View view) {
        Intent i = new Intent(this, Dongjak1.class);
        startActivity(i);
    }

    public void onClickdongjak2(View view) {
        Intent i = new Intent(this, Dongjak2.class);
        startActivity(i);
    }

    public void onClickdongjak3(View view) {
        Intent i = new Intent(this, Dongjak3.class);
        startActivity(i);
    }
}
