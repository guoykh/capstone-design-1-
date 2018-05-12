package com.example.user.tapapplication.Bluetooth_contact;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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

public class DeviceActivity extends Activity{
    private static final String TAG = "BLEDevice";
    public static final String BLUETOOTH_DEVICE_1="DEVICE_1";
    public static final String BLUETOOTH_DEVICE_2="DEVICE_2";
    public int data1=-1, data2=-1;
    private BluetoothAdapter Adapter1,Adapter2;
    private BluetoothDevice Device1,Device2;
    private BluetoothGatt ConnGatt1,ConnGatt2;
    private BluetoothGattService disService1, disService2;
    private BluetoothGattCharacteristic characteristic1, characteristic2;
    private int Status1,Status2;
    private boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_send_data);

        init();

    }

    private void setNotifySensor(BluetoothGatt gatt){
        BluetoothDevice device = gatt.getDevice();
        if(("TapTap1").equals(device.getName())) {
            disService1 = gatt.getService(UUID.fromString("7c3f5818-3255-4307-b138-158e09ec8130"));
            characteristic1 = disService1.getCharacteristic(UUID.fromString("f71d47a6-fb4e-4c87-9be9-1b2bea79a2db"));
            boolean a = gatt.setCharacteristicNotification(characteristic1, true);
            if (a) {
                Log.d("setNotifySensor", "Tap1 Success");
            }
            BluetoothGattDescriptor desc = characteristic1.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

            Log.i("BLE","Descriptor is "+desc);
            Log.i("BLE","Descriptor write: "+gatt.writeDescriptor(desc));
        }
        if(("TapTap2").equals(device.getName())) {
            disService2 = gatt.getService(UUID.fromString("d6e6a169-1a81-4ff4-a2b6-66534e32bebe"));
            characteristic2 = disService2.getCharacteristic(UUID.fromString("11591b7f-bce5-4e28-ac31-1e54c5c077b1"));
            boolean a = gatt.setCharacteristicNotification(characteristic2, true);
            if(a){
                Log.d("setNotifySensor","Tap2 Success");
            }
            BluetoothGattDescriptor desc = characteristic2.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            Log.i("BLE","Descriptor is "+desc);
            Log.i("BLE","Descriptor write: "+gatt.writeDescriptor(desc));
        }

    }

    private final BluetoothGattCallback mGattcallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("onConnStateChanged","called");
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
            if(status==BluetoothGatt.GATT_SUCCESS){
                Log.d("called","onServicesDiscovered called");
                setNotifySensor(gatt);
            }

        };

        @Override // 아두이노 수신부
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            Log.d("onChaRead","CallBack Success");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                final int i = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0);

                if(i>=0 && i<=2){
                    data1=i;
                    Log.d("Read","data1 was read : "+data1);
                    if(data1 == 2){
                        characteristic.setValue(3, BluetoothGattCharacteristic.FORMAT_UINT8, 0);//new byte[] { (byte) 3 });
                        boolean X = gatt.writeCharacteristic(characteristic);
                        if (X) {
                            Log.d("Send","data1 보내기 성공");
                        }
                        else{
                            Log.d("", "sending is failed : taptap1");
                        }
                    }
                }
                else if( i>2 && i<=5){
                    data2=i;
                    Log.d("Read","data2 was read : "+data2);
                    if(data2 == 5){
                        characteristic.setValue(3, BluetoothGattCharacteristic.FORMAT_UINT8, 0);//new byte[] { (byte) 3 });
                        boolean X = gatt.writeCharacteristic(characteristic);
                        if (X) {
                            Log.d("Send","data2 보내기 성공");
                        }
                        else{
                            Log.d("", "sending is failed : taptap2");
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic){

            Log.d("onCharacteristicChanged", "onCharacteristicChanged function is called");

            if("TapTap1".equals(gatt.getDevice().getName())){
                boolean newresult = ConnGatt1.readCharacteristic(characteristic);

                if(newresult){
                    Log.d("onCharacteristicChanged","TapTap1 was read");
                    Log.d("onCharacteristicChanged","data1: "+data1+", data2: "+data2);
                }
                else{
                    Log.d("onCharacteristicChanged", "onCharacteristicChanged reading failed");
                }
            }
            if("TapTap2".equals(gatt.getDevice().getName())){
                boolean newresult = ConnGatt2.readCharacteristic(characteristic);

                if(newresult){
                    Log.d("onCharacteristicChanged","TapTap2 was read");
                    Log.d("onCharacteristicChanged","data1: "+data1+", data2: "+data2);
                }
                else{
                    Log.d("onCharacteristicChanged", "onCharacteristicChanged reading failed");
                }
            }

        }



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
        else {
            Log.d("getExtras1","Success");
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
        else {
            Log.d("getExtras2","Success");
        }
        return extras.getParcelable(BLUETOOTH_DEVICE_2);
    }



    @Override
    protected void onResume() { // setContentView(R.layout.check_send_data); 를 넣어서 호출을 해보자
        super.onResume();
        Log.d("","onResume start");
        init();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("onStop","start");
        running = false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy()","Destroy");
        running = false;
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
        Log.d("init","start init()");
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
                Log.d("Device","getExtra failed");
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
            Log.d("Connect","Connecting");
        } else {
            if (ConnGatt1 != null && ConnGatt2 != null) {
                // re-connect and re-discover Services
                Log.d("reconnect","called");
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
