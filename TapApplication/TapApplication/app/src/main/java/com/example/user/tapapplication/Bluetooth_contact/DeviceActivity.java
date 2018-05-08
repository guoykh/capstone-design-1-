package com.example.user.tapapplication.Bluetooth_contact;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tapapplication.R;

import java.util.UUID;

/**
 * Created by user on 2018-03-31.
 */

public class DeviceActivity extends Activity {
    private static final String TAG = "BLEDevice";
    public static final String BLUETOOTH_DEVICE_1="DEVICE_1";
    public static final String BLUETOOTH_DEVICE_2="DEVICE_2";
    public int data1=0, data2=0;
    private BluetoothAdapter Adapter1,Adapter2;
    private BluetoothDevice Device1,Device2;
    private BluetoothGatt ConnGatt1,ConnGatt2;
    private int Status1,Status2;
    Button Refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_send_data);

        Refresh = (Button)findViewById(R.id.refresh_btn);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothGattService disService1 = ConnGatt1.getService(UUID.fromString("7c3f5818-3255-4307-b138-158e09ec8130"));
                BluetoothGattService disService2 = ConnGatt2.getService(UUID.fromString("d6e6a169-1a81-4ff4-a2b6-66534e32bebe"));
                if (disService1 == null || disService2 == null) {
                    Log.d("", "Dis service not found!");
                    return;
                }
                BluetoothGattCharacteristic characteristic1 = disService1.getCharacteristic(UUID.fromString("f71d47a6-fb4e-4c87-9be9-1b2bea79a2db"));
                BluetoothGattCharacteristic characteristic2 = disService2.getCharacteristic(UUID.fromString("11591b7f-bce5-4e28-ac31-1e54c5c077b1"));
                if (characteristic1 == null || characteristic2 == null) {
                    Log.d("", " charateristic not found!");
                    return;
                }
                boolean result1 = ConnGatt1.readCharacteristic(characteristic1);
                boolean result2 = ConnGatt2.readCharacteristic(characteristic2);
                if (result1 == false || result2 == false) {
                    Log.d("", "reading is failed!");
                }
                if(data1==2){
                    characteristic1.setValue(3,BluetoothGattCharacteristic.FORMAT_UINT8,0);//new byte[] { (byte) 3 });
                    ConnGatt1.writeCharacteristic(characteristic1);
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                }
                if(data2==5){
                    characteristic2.setValue(new byte[] { (byte) 3 });
                    ConnGatt2.writeCharacteristic(characteristic2);
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                }
                TextView tv = (TextView)findViewById(R.id.tap1_state);
                tv.setText("value="+data1);
                tv = (TextView)findViewById(R.id.tap2_state);
                tv.setText("value="+data2);


            }
        });

    }

    private final BluetoothGattCallback mGattcallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Status1 = newState;
                ConnGatt1.discoverServices();
                Status2 = newState;
                ConnGatt2.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Status1 = newState;
                Status2 = newState;
                runOnUiThread(new Runnable() {
                    public void run() {

                    };
                });
            }
        };

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            super.onServicesDiscovered(gatt, status);

        };

        @Override // 아두이노 수신부
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                final int i = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0);

                if(i>=0 && i<=2){
                    data1=i;
                }
                else if( i>2 && i<=5){
                    data2=i;
                }
            }
        }

       /* @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            BluetoothGattService disService1 = gatt.getService(UUID.fromString("7c3f5818-3255-4307-b138-158e09ec8130"));
            BluetoothGattService disService2 = gatt.getService(UUID.fromString("d6e6a169-1a81-4ff4-a2b6-66534e32bebe"));
            if (disService1 == null || disService2 == null) {
                Log.d("", "Dis service not found!");
                return;
            }
            characteristic = disService1.getCharacteristic(UUID.fromString("f71d47a6-fb4e-4c87-9be9-1b2bea79a2db"));
            if (characteristic == null) {
                Log.d("", " charateristic not found!");
                return;
            }
            if(data1==2) {
                characteristic.setValue(3, BluetoothGattCharacteristic.FORMAT_UINT8, 0);//new byte[] { (byte) 3 });
                gatt.writeCharacteristic(characteristic);
            }

            characteristic = disService2.getCharacteristic(UUID.fromString("11591b7f-bce5-4e28-ac31-1e54c5c077b1"));
            if (characteristic == null) {
                Log.d("", " charateristic not found!");
                return;
            }
            if(data1==5) {
                characteristic.setValue(3, BluetoothGattCharacteristic.FORMAT_UINT8, 0);//new byte[] { (byte) 3 });
                gatt.writeCharacteristic(characteristic);
            }

        }*/
    };

    private BluetoothDevice getBTDeviceExtra1(){
        Intent intent = getIntent();
        if(intent == null){
            return null;
        }

        Bundle extras = intent.getExtras();
        if(extras == null){
            return null;
        }
        return extras.getParcelable(BLUETOOTH_DEVICE_1);
    }

    private BluetoothDevice getBTDeviceExtra2(){
        Intent intent = getIntent();
        if(intent == null){
            return null;
        }

        Bundle extras = intent.getExtras();
        if(extras == null){
            return null;
        }
        return extras.getParcelable(BLUETOOTH_DEVICE_2);
    }



    @Override
    protected void onResume() {
        super.onResume();

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ConnGatt1 != null) {
            if ((Status1 != BluetoothProfile.STATE_DISCONNECTING)
                    && (Status1 != BluetoothProfile.STATE_DISCONNECTED)) {
                ConnGatt1.disconnect();
            }
            ConnGatt1.close();
            ConnGatt1 = null;
        }
        if (ConnGatt2 != null) {
            if ((Status2 != BluetoothProfile.STATE_DISCONNECTING)
                    && (Status2 != BluetoothProfile.STATE_DISCONNECTED)) {
                ConnGatt2.disconnect();
            }
            ConnGatt2.close();
            ConnGatt2 = null;
        }
    }


    private void init() {
        // BLE check
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this,"ble_not_supported", Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }

        // BT check
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            Adapter1 = manager.getAdapter();
            Adapter2 = manager.getAdapter();
        }
        if (Adapter1 == null || Adapter2 == null) {
            Toast.makeText(this, "bt_unavailable", Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }

        // check BluetoothDevice
        if (Device1 == null || Device2 == null) {
            Device1 = getBTDeviceExtra1();
            Device2 = getBTDeviceExtra2();
            if (Device1 == null || Device2 == null) {
                finish();
                return;
            }
        }

        // connect to Gatt
        if ((ConnGatt1 == null || ConnGatt2==null)
                && (Status1 == BluetoothProfile.STATE_DISCONNECTED) || Status2 == BluetoothProfile.STATE_CONNECTING) {
            // try to connect
            ConnGatt1 = Device1.connectGatt(this, false, mGattcallback);
            ConnGatt2 = Device2.connectGatt(this, false, mGattcallback);
            Status1 = BluetoothProfile.STATE_CONNECTING;
            Status2 = BluetoothProfile.STATE_CONNECTING;
        } else {
            if (ConnGatt1 != null && ConnGatt2 != null) {
                // re-connect and re-discover Services
                ConnGatt1.connect();
                ConnGatt2.connect();
                ConnGatt1.discoverServices();
                ConnGatt2.discoverServices();
            } else {
                Log.e(TAG, "state error");
                finish();
                return;
            }
        }
    }

}
