package com.example.user.tapapplication;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.example.user.tapapplication.Bluetooth_contact.Ble_MainActivity;


public class MainActivity extends AppCompatActivity{
    String[] PERMISSIONS = {"android.permission.ACCESS_COARSE_LOCATION"};
    static final int PERMISSION_REQUEST_CODE=1;
    private static final int REQUEST_ENABLE_BT=2;
    private BluetoothAdapter Adapter;

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (!Accepted  )
                        {
                            showDialogforPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                            return;
                        }else
                        {
                            //이미 사용자에게 퍼미션 허가를 받음.
                        }
                    }
                }
                break;
        }
    }

    private void showDialogforPermission(String msg) {

        final AlertDialog.Builder myDialog = new AlertDialog.Builder( this);
        myDialog.setTitle("알림");
        myDialog.setMessage(msg);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
                }

            }
        });
        myDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        myDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Adapter = BluetoothAdapter.getDefaultAdapter();
        //블루투스 활성화인지 체크
//        if(!Adapter.isEnabled()){
//            Intent btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(btintent,REQUEST_ENABLE_BT);
//        }

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
