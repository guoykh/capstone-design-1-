package com.example.user.tapapplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.example.user.tapapplication.Bluetooth_contact.Ble_MainActivity;


public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickBase(View view) {
        Intent i = new Intent(this, BaseTap.class);
        startActivity(i);
    }
    public void onClickDance(View view) {
        Intent i = new Intent(this, DanceTap.class);
        startActivity(i);
    }

    public void onClickWrong(View view) {
        Intent i = new Intent(this, WrongTap.class);
        startActivity(i);
    }

    public void onClickFav(View view) {
        Intent i = new Intent(this, FavoriteTap.class);
        startActivity(i);
    }

    public void onClickCheck(View view) {
        Intent i = new Intent(this, CheckBattery.class);
        startActivity(i);
    }
    public void onClickBluetooth(View view) {
        Intent i = new Intent(this, Ble_MainActivity.class);
        startActivity(i);
    }



}
