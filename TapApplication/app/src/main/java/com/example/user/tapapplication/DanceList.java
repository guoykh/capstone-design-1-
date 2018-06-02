package com.example.user.tapapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.tapapplication.dongjak_play.BallchPlay;
import com.example.user.tapapplication.dongjak_play.BalldrPlay;
import com.example.user.tapapplication.dongjak_play.BrushPlay;
import com.example.user.tapapplication.dongjak_play.BuffPlay;
import com.example.user.tapapplication.dongjak_play.CrampPlay;
import com.example.user.tapapplication.dongjak_play.DigPlay;
import com.example.user.tapapplication.dongjak_play.DragPlay;
import com.example.user.tapapplication.dongjak_play.FlapPlay;
import com.example.user.tapapplication.dongjak_play.HeeldrPlay;
import com.example.user.tapapplication.dongjak_play.HopshuPlay;
import com.example.user.tapapplication.dongjak_play.ShufflePlay;
import com.example.user.tapapplication.dongjak_play.StampPlay;
import com.example.user.tapapplication.dongjak_play.StepPlay;
import com.example.user.tapapplication.dongjak_play.Tap1Play;
import com.example.user.tapapplication.dongjak_play.Tap2Play;

public class DanceList extends Activity{
    private String[] items = {"exo - ko ko bop"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basetap);
        ListView list = findViewById(R.id.list);
        ListAdapter lAdapter = new ListAdapter(this);
        list.setAdapter(lAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String)parent.getAdapter().getItem(position);
                Intent i;
                switch (str){
                    case "exo - ko ko bop":
                        i = new Intent(DanceList.this, DanceTap.class);
                        startActivity(i);
                        break;
                }
            }
        });
    }

    class ListAdapter extends ArrayAdapter {
        Activity context;

        ListAdapter(Activity context){
            super(context,R.layout.basetap_item,items);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();
            View row = inflater.inflate(R.layout.basetap_item,null);

            ImageView itemImage = row.findViewById(R.id.imageView);

            TextView nameText = row.findViewById(R.id.list_name);
            nameText.setText(items[position]);
            return row;
        }
    }

/*
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
    }*/
}
