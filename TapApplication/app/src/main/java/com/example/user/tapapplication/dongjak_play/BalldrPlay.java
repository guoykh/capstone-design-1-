package com.example.user.tapapplication.dongjak_play;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.example.user.tapapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class BalldrPlay extends Activity implements BluetoothAdapter.LeScanCallback {
    Button button,practice;
    VideoView video;
    ToggleButton toggle;
    ImageView iv;
    private final String dbName = "webnautes";
    private final String tableName = "person";

    private final String[] name = new String[]{"볼드롭"};
    private final String[] phone = new String[]{"1"};

    ArrayList<HashMap<String, String>> personList;
    ListView list;
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE ="phone";

    public boolean wing = false; //진동이 울린 적이 있는 지 확인하는 변수
    public int [] ans =  { 3,1,3,1 }; // 답 스위치 위치
    public int arrint = 0;
    SQLiteDatabase sampleDB = null;
    ListAdapter adapter;
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
    Button start;

    private boolean stay = false;

    public int count=0;
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
        setContentView(R.layout.dongjak_play);
        iv=(ImageView)findViewById(R.id.imageView3);
        //iv.setImageResource(R.drawable.first);
        toggle=(ToggleButton)findViewById(R.id.toggleButton);
        TextView tv = findViewById(R.id.tap_name);
        tv.setText("볼 드롭");

        button = (Button)findViewById(R.id.startbtn);
        video = (VideoView)findViewById(R.id.footprint);
        String uriPath = "android.resource://"+getPackageName()+"/"+R.raw.balldrop;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.setMediaController(new MediaController(this));
        video.requestFocus();

        practice = findViewById(R.id.practice);
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
        personList = new ArrayList<HashMap<String, String>>();
        showList();
        if (toggle.isChecked()) {
            toggle.setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.starclick));
        } else {
            toggle.setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.staroff));
        }
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                video.start();
            }
        });

        start = findViewById(R.id.practice_start);
        start.setOnClickListener(new View.OnClickListener() { // 연습시작
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.count_text);
                textView.setText(""+count);
            }
        });
    }

    public void onClickStar1(View v){
        if (toggle.isChecked()){
            toggle.setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.starclick));
            Toast.makeText(BalldrPlay.this,"즐겨찾기 추가",Toast.LENGTH_SHORT).show();}
        else{
            toggle.setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.staroff));
            Toast.makeText(BalldrPlay.this,"즐겨찾기 해제",Toast.LENGTH_SHORT).show();
        }
        try {

            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            //테이블이 존재하지 않으면 새로 생성합니다.
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                    + " (name VARCHAR(20), phone VARCHAR(20) );");

            if (v.getId() == R.id.toggleButton) {
                Cursor c = sampleDB.rawQuery("SELECT * FROM person WHERE name = '볼드롭'", null);

                if (c != null && c.getCount()==0) {

                    sampleDB.execSQL("INSERT INTO " + tableName
                            + " (name, phone)  Values ('" + name[0] + "', '" + phone[0] + "');");
                }
                else if (c.getCount()==1) {
                    sampleDB.execSQL("DELETE FROM person WHERE name = '볼드롭'");
                }
            }

            sampleDB.close();

        } catch (SQLiteException se) {
            //Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            //Log.e("", se.getMessage());

        }

    }

    protected void showList(){

        try {

            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            // Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);
            Cursor c1 = ReadDB.rawQuery("SELECT * FROM " + tableName+ " WHERE name = '볼드롭'", null);

            if (c1!=null){
                if(c1.moveToFirst()){
                    do{String Name = c1.getString(c1.getColumnIndex("name"));

                        if (Name !=null){
                            toggle.setChecked(true);
                            toggle.setBackgroundDrawable(getResources().
                                    getDrawable(R.drawable.starclick));
                        }
                        else {toggle.setChecked(false);
                            toggle.setBackgroundDrawable(getResources().
                                    getDrawable(R.drawable.staroff));
                        }

                    }while(c1.moveToNext());
                }
            }

            ReadDB.close();

        } catch (SQLiteException se) {

        }
    }
    // http://webnautes.tistory.com/830 참고사이트
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
                Toast.makeText(BalldrPlay.this,"연결 성공",Toast.LENGTH_SHORT).show();
                iv.setImageResource(R.drawable.first);
                handler.postDelayed(moving,8000);
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
                Toast.makeText(BalldrPlay.this,"연습을 시작해주세요",Toast.LENGTH_SHORT).show();
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
                stay=true;
                if(i>0 && i<3) { // data1에 1,2할당
                    data1 = i;
                    Log.d("Read", "data1 was read : " + data1);

                    if (i == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageResource(R.drawable.left1);
                                iv.invalidate();
                            }
                        });

                    }

                    if (i == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageResource(R.drawable.left2);
                                iv.invalidate();
                            }
                        });
                        characteristic.setValue(11, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                        boolean X = gatt.writeCharacteristic(characteristic);
                        count++;
                    }
                }

                if(i>2 && i<5){
                    data2 = i; //data2에 3,4 할당
                    Log.d("Read","data2 was read : "+data2);

                    if (i==3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageResource(R.drawable.rignt3);
                                iv.invalidate();
                            }
                        });

                    }

                    if (i==4) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageResource(R.drawable.right4);
                                iv.invalidate();
                            }
                        });
                        characteristic.setValue(13, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                        boolean X = gatt.writeCharacteristic(characteristic);
                        count++;
                    }
                }

                if (i == 17) {
                    data1 = 5;
                    Log.d("Read", "data1 was read : " + data1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageResource(R.drawable.num5);
                            iv.invalidate();
                        }
                    });
                    count++;
                }

                  /*  if (i==6) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageResource(R.drawable.num6);
                                iv.invalidate();
                            }
                        });

                    }*/ //공중에 떠있는 발 일단 보류

                if (i == 18) {
                    data2 = 7; //data2에 3,4 할당
                    Log.d("Read", "data2 was read : " + data2);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageResource(R.drawable.num7);
                            iv.invalidate();
                        }
                    });
                    count++;
                }

           /*         if (i==8) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageResource(R.drawable.num8);
                                iv.invalidate();
                            }
                        });

                    }*/ //공중에 떠있는 발 일단 보류
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic){



            if("TapTap1".equals(gatt.getDevice().getName())){

                Log.d("onCharacteristicChanged", "onCharacteristicChanged (TapTap1) is called");
                boolean newresult = ConnGatt1.readCharacteristic(characteristic);
                boolean skip=false;

                if(newresult){



                   /* // 현재 순서의 스위치가 아니거나 뒷발이 떨어졌다는 신호가 오면 진동 보냄
                    if(arrint < 4) {
                        if(data1 == 2) skip=true; // 2는 버림

                        if((skip==false && data1 != ans[arrint]) || data1 == 1 || data1 == 6) {
                            characteristic.setValue(10+ans[arrint], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                            gatt.writeCharacteristic(characteristic);
                            wing = true;
                            arrint++;

                        }
                    }
                    if(arrint == 4) {
                        if(wing) count++; //진동이 한 번이라도 울린 적이 있으면 틀린 횟수세는 변수 증가
                    }*/
                }


            }


            if("TapTap2".equals(gatt.getDevice().getName())) {

                Log.d("onCharacteristicChanged", "onCharacteristicChanged (TapTap2) is called");
                boolean newresult = ConnGatt2.readCharacteristic(characteristic);
                boolean skip = false;
                if (newresult) {

/*                    if(arrint < 4) {

                        if(data2 == 4) skip=true; // 4는 버림

                        if((skip==false && data2 != ans[arrint]) || data2 == 3 || data2 == 8 ) {
                            characteristic.setValue(10+ans[arrint], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                            gatt.writeCharacteristic(characteristic);
                            wing = true;
                            arrint++;
                        }



                    }
                    if(arrint == 4) {

                        if(wing) count++; //진동이 한 번이라도 울린 적이 있으면 틀린 횟수세는 변수 증가
                    }*/
                }
            }
        }


    };

    @Override
    protected void onResume() { // setContentView(R.layout.check_send_data); 를 넣어서 호출을 해보자
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
