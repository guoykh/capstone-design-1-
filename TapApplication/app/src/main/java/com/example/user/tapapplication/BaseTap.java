package com.example.user.tapapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class BaseTap extends Activity{
    private String[] items = {"드래그","딕","버팔로","볼 드롭","볼 체인지","브러쉬",
            "셔플","스탬프","스텝","크램프 롤","탭1","탭2","플랩","홉 셔플","힐 드롭"};
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
                    case "드래그":
                        i = new Intent(BaseTap.this, DragPlay.class);
                        startActivity(i);
                        break;
                    case "딕":
                        i = new Intent(BaseTap.this, DigPlay.class);
                        startActivity(i);
                        break;
                    case "버팔로":
                        i = new Intent(BaseTap.this, DigPlay.class);
                        startActivity(i);
                        break;
                    case "볼 드롭":
                        i = new Intent(BaseTap.this, BalldrPlay.class);
                        startActivity(i);
                        break;
                    case "볼 체인지":
                        i = new Intent(BaseTap.this, BallchPlay.class);
                        startActivity(i);
                        break;
                    case "브러쉬":
                        i = new Intent(BaseTap.this, BrushPlay.class);
                        startActivity(i);
                        break;
                    case "셔플":
                        i = new Intent(BaseTap.this, ShufflePlay.class);
                        startActivity(i);
                        break;
                    case "스탬프":
                        i = new Intent(BaseTap.this, StampPlay.class);
                        startActivity(i);
                        break;
                    case "스텝":
                        i = new Intent(BaseTap.this, StepPlay.class);
                        startActivity(i);
                        break;
                    case "크램프 롤":
                        i = new Intent(BaseTap.this, CrampPlay.class);
                        startActivity(i);
                        break;
                    case "탭1":
                        i = new Intent(BaseTap.this, Tap1Play.class);
                        startActivity(i);
                        break;
                    case "탭2":
                        i = new Intent(BaseTap.this, Tap2Play.class);
                        startActivity(i);
                        break;
                    case "플랩":
                        i = new Intent(BaseTap.this, FlapPlay.class);
                        startActivity(i);
                        break;
                    case "홉 셔플":
                        i = new Intent(BaseTap.this, HopshuPlay.class);
                        startActivity(i);
                        break;
                    case "힐 드롭":
                        i = new Intent(BaseTap.this, HeeldrPlay.class);
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
