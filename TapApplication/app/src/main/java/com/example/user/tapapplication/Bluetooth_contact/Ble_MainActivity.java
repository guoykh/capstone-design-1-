package com.example.user.tapapplication.Bluetooth_contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tapapplication.R;

/**
 * Created by user on 2018-03-31.
 */

public class Ble_MainActivity extends Activity implements BluetoothAdapter.LeScanCallback {
    private BluetoothAdapter Adapter;
    private BluetoothDevice Device1=null, Device2=null;
    private boolean IsScanning;
    Button tap1_btn, tap2_btn, check;
    static final int PERMISSION_REQUEST_CODE=1;
    private static final int REQUEST_ENABLE_BT=2;
    String[] PERMISSIONS = {"android.permission.ACCESS_COARSE_LOCATION"};

    private boolean hasPermissions(String[] permissions) {
        int ret = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions){
            ret = checkCallingOrSelfPermission(perms);
            if (!(ret == PackageManager.PERMISSION_GRANTED)){
                //퍼미션 허가 안된 경우
                return false;
            }
        }

        //모든 퍼미션이 허가된 경우
        return true;
    }

    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

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

        final AlertDialog.Builder myDialog = new AlertDialog.Builder( Ble_MainActivity.this);
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

    public void ContactOnClick(View view){
        TextView tv;
        if(view.getId()==R.id.tap1_contact_btn){
           // startScan();
            tv=(TextView)findViewById(R.id.tap1_tv);
            if(Device1 != null) {
                tv.setText("TapTap1 연결됨");
            }
            else {
                tv.setText("TapTap1 연결 오류");
                Log.d("", "taptap1 connectiong error");
            }
        }
        if(view.getId()==R.id.tap2_contact_btn){
          //  startScan();
            tv=(TextView)findViewById(R.id.tap2_tv);
            if(Device2 != null){
                tv.setText("TapTap2 연결됨");
            }
            else{
                tv.setText("TapTap2 연결 오류");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_check);
        Adapter = BluetoothAdapter.getDefaultAdapter();

        startScan();
        //블루투스 활성화인지 체크
        if(!Adapter.isEnabled()){
         Intent btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
         startActivityForResult(btintent,REQUEST_ENABLE_BT);
        }
        check = (Button)findViewById(R.id.check_contact);
        check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TextView tv = (TextView)findViewById(R.id.state_tv);
                if(Device1 != null && Device2 != null) {
                    Intent intent = new Intent(v.getContext(), DeviceActivity.class);
                    intent.putExtra(DeviceActivity.BLUETOOTH_DEVICE_1,Device1);
                    intent.putExtra(DeviceActivity.BLUETOOTH_DEVICE_2,Device2);
                    startActivity(intent);

                    stopScan();
                }
                else if(Device1 != null && Device2 == null) {
                    tv.setText("TapTap2 연결 안됨");
                }
                else if(Device1 == null && Device2 != null) {
                    tv.setText("TapTap1 연결 안됨");
                }
                else{
                    tv.setText("연결 안됨");
                }
            }
        });

        if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어
            // 있다면 사용자에게 요청
        } else {
            //이미 사용자에게 퍼미션 허가를 받음.
        }

        init();
    }

    @Override
    public void onLeScan(final BluetoothDevice device,final int rssi,
                         final byte[] scanRecord) {
        Log.d("onLeScan","start");
        if ("TapTap1".equals(device.getName())) {
            Device1 = device;
        }
         if ("TapTap2".equals(device.getName())) {
            Device2 = device;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScan();
    }


    public void init(){
        // BLE check
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // BT check
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            Adapter = manager.getAdapter();
        }
        if (Adapter == null) {
            Toast.makeText(this, "bt_unavailable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    private void startScan() {
        if ((Adapter != null) && (!IsScanning)) {
            Adapter.startLeScan(this);
            IsScanning = true;
        }
    }

    private void stopScan() {
        if (Adapter != null) {
            Adapter.stopLeScan(this);
        }
        IsScanning = false;
    }



}
