package com.example.user.tapapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoriteTap extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_tap);
        setList();
        //ArrayAdapter<String> adapter;

       // ListView lv = (ListView)findViewById(R.id.listitem);




        //TextView tv = (TextView)findViewById(R.id.text_view);
        //MainActivity app = (MainActivity) getApplicationContext();
        //tv.setText(app.getAppData());
    }

    private void setList() {

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME_KEY");


        ArrayList<String> item = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, item);

        ListView list = (ListView)findViewById(R.id.listitem);
        list.setAdapter(adapter);
        //list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        item.add(username);
        adapter.notifyDataSetChanged();
    }

}