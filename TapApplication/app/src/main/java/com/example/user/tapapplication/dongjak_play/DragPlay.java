package com.example.user.tapapplication.dongjak_play;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.example.user.tapapplication.R;

import java.util.ArrayList;
import java.util.HashMap;


public class DragPlay extends Activity {
    Button button;
    ToggleButton toggle;
    VideoView video;

    private final String dbName = "webnautes";
    private final String tableName = "person";

    private final String[] name = new String[]{"drag", "tap1", "tap2"};
    private final String[] phone = new String[]{"1"};

    ArrayList<HashMap<String, String>> personList;
    ListView list;
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE ="phone";

    SQLiteDatabase sampleDB = null;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongjak_play);

        TextView tv = findViewById(R.id.tap_name);
        tv.setText("드래그");
        toggle = (ToggleButton)findViewById(R.id.toggleButton) ;
        button = (Button)findViewById(R.id.startbtn);
        video = (VideoView)findViewById(R.id.footprint);
        String uriPath = "android.resource://"+getPackageName()+"/"+R.raw.drag;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.setMediaController(new MediaController(this));
        video.requestFocus();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                video.start();
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
    }


    public void onClickStar1(View v){
        if (toggle.isChecked()){
            toggle.setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.starclick));}
        else{
            toggle.setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.staroff));
        }
        try {

            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            //테이블이 존재하지 않으면 새로 생성합니다.
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                    + " (name VARCHAR(20), phone VARCHAR(20) );");

            if (v.getId() == R.id.toggleButton) {
                Cursor c = sampleDB.rawQuery("SELECT * FROM person WHERE name = 'drag'", null);

                if (c != null && c.getCount()==0) {

                    sampleDB.execSQL("INSERT INTO " + tableName
                            + " (name, phone)  Values ('" + name[0] + "', '" + phone[0] + "');");
                }
                else if (c.getCount()==1) {
                    sampleDB.execSQL("DELETE FROM person WHERE name = 'drag'");
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
            Cursor c1 = ReadDB.rawQuery("SELECT * FROM " + tableName+ " WHERE name = 'drag'", null);

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
}
