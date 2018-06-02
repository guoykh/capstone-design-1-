package com.example.user.tapapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.UUID;

public class DanceTap extends YouTubeBaseActivity implements BluetoothAdapter.LeScanCallback{

    YouTubePlayerView youTubeView;

    Button player,practice;
    private boolean stay = false;
    YouTubePlayer.OnInitializedListener listener;
    private boolean IsScanning;
    String[] PERMISSIONS = {"android.permission.ACCESS_COARSE_LOCATION"};
    static final int PERMISSION_REQUEST_CODE=1;
    private static final int REQUEST_ENABLE_BT=2;
    public Handler handler;
    public boolean blechecked = false;
    private static final String TAG = "BLEDevice";
    private BluetoothAdapter Adapter;
    private BluetoothDevice Device1=null, Device2=null;
    private BluetoothGatt ConnGatt1,ConnGatt2;
    private BluetoothGattService disService1, disService2;
    private BluetoothGattCharacteristic characteristic1, characteristic2;
    private int Status1,Status2;
    private boolean running = true;
    public int data1=-1, data2=-1;
    ImageView iv;
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
        setContentView(R.layout.dance_tap);
        iv = (ImageView)findViewById(R.id.imageView3);
        player = (Button)findViewById(R.id.dan_player);
        youTubeView = (YouTubePlayerView)findViewById(R.id.dan_view);
        //리스너 등록부분

        listener = new YouTubePlayer.OnInitializedListener(){
            //초기화 성공시
            @Override

            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo("2UVHmtjg35U");//url의 맨 뒷부분 ID값만 넣으면 됨https://youtu.be/2UVHmtjg35U
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        player.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //첫번째 인자는 API키값 두번째는 실행할 리스너객체를 넘겨줌

                youTubeView.initialize("AIzaSyDjJW-rnopXMuC2pUMMYM_MRm9oUWdq5U4", listener);

            }

        });

        practice = findViewById(R.id.dan_prac);
        practice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handler = new Handler();
                Adapter = BluetoothAdapter.getDefaultAdapter();
                //블루투스 활성화인지 체크
                if(!Adapter.isEnabled()){
                    Intent btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(btintent,REQUEST_ENABLE_BT);
                }
                handler.post(check); // 기기 연결 체크
            }
        });

    }
    private final Runnable check = new Runnable() {
        @Override
        public void run() {
            Log.d("run","bluetoothcheck run");
            startScan();
            if(Device1 != null && Device2 != null) {
                Log.d("run","연결 성공");
                stopScan();
                blechecked=true;
                init();
                Toast.makeText(DanceTap.this,"연결 성공",Toast.LENGTH_SHORT).show();
                iv.setImageResource(R.drawable.first);
                handler.postDelayed(moving,8000);
                //iv.invalidate();
            }
            else if(Device1 != null && Device2 == null) {
                Log.d("run","TapTap2 연결 안됨");
                handler.postDelayed(check,1000); // 1초후 핸들러 post
            }
            else if(Device1 == null && Device2 != null) {
                Log.d("run","TapTap1 연결 안됨");
                handler.postDelayed(check,1000);
            }
            else{
                Log.d("run","연결 안됨");
                handler.postDelayed(check,1000);
            }
        }
    };
    private final Runnable moving = new Runnable() {
        @Override
        public void run() {
            if(stay==false){
                Toast.makeText(DanceTap.this,"연습을 시작해주세요",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi,
                         final byte[] scanRecord) {
        Log.d("onLeScan","start");
        if ("TapTap1".equals(device.getName())) {
            Device1 = device;
            Log.d("onLeScan","Device1 found");
        }
        if ("TapTap2".equals(device.getName())) {
            Device2 = device;
            Log.d("onLeScan","Device2 found");
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

    private void setNotifySensor(BluetoothGatt gatt){
        BluetoothDevice device = gatt.getDevice();
        if(("TapTap1").equals(device.getName())) {
            disService1 = gatt.getService(UUID.fromString("7c3f5818-3255-4307-b138-158e09ec8130"));
            characteristic1 = disService1.getCharacteristic(UUID.fromString("f71d47a6-fb4e-4c87-9be9-1b2bea79a2db"));
            boolean a = gatt.setCharacteristicNotification(characteristic1, true);
            if (a) {
                Log.d("setNotifySensor", "Tap1 Success");
            }
            BluetoothGattDescriptor descriptor1 = characteristic1.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            Log.i("Descriptor","Descriptor1 is "+descriptor1);
            descriptor1.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            Log.i("Descriptor","Descriptor1 write: "+gatt.writeDescriptor(descriptor1));
            SystemClock.sleep(500);
        }
        if(("TapTap2").equals(device.getName())) {
            disService2 = gatt.getService(UUID.fromString("d6e6a169-1a81-4ff4-a2b6-66534e32bebe"));
            characteristic2 = disService2.getCharacteristic(UUID.fromString("11591b7f-bce5-4e28-ac31-1e54c5c077b1"));
            boolean a = gatt.setCharacteristicNotification(characteristic2, true);
            if(a){
                Log.d("setNotifySensor","Tap2 Success");
            }
            BluetoothGattDescriptor descriptor2 = characteristic2.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            Log.i("Descriptor","Descriptor2 is "+descriptor2);
            descriptor2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            Log.i("Descriptor","Descriptor2 write: "+gatt.writeDescriptor(descriptor2));
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
                    Log.d("onCharacteristicChanged","data2: "+data2);
                }
                else{
                    Log.d("onCharacteristicChanged", "onCharacteristicChanged reading failed");
                }
            }

        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("","onResume start");
        if(blechecked) {
            init();
        }
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
        if(blechecked){
            blechecked=false;
        }
        if(IsScanning == true) {
            Adapter = BluetoothAdapter.getDefaultAdapter();
            stopScan();
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
        if(blechecked){
            blechecked=false;
        }
        if(IsScanning == true) {
            Adapter = BluetoothAdapter.getDefaultAdapter();
            stopScan();
        }
    }

    private void init() {
        // connect to Gatt
        if ((ConnGatt1 == null || ConnGatt2 == null)
                && (Status1 == BluetoothProfile.STATE_DISCONNECTED) || Status2 == BluetoothProfile.STATE_CONNECTING) {
            // try to connect
            ConnGatt1 = Device1.connectGatt(this, false, mGattcallback);
            ConnGatt2 = Device2.connectGatt(this, false, mGattcallback);
            Status1 = BluetoothProfile.STATE_CONNECTING;
            Status2 = BluetoothProfile.STATE_CONNECTING;
            Log.d("Connect", "Connecting");
        } else {
            if (ConnGatt1 != null && ConnGatt2 != null) {
                // re-connect and re-discover Services
                Log.d("reconnect", "called");
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
//출처 : http://mystudyroom.net/2017/10/13/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-6-%EC%9C%A0%ED%8A%9C%EB%B8%8C-%EC%9E%AC%EC%83%9D%ED%95%98%EA%B8%B0/