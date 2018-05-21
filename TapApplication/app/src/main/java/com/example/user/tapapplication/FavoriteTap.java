package com.example.user.tapapplication;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
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
import java.util.ArrayList;
import java.util.HashMap;
public class FavoriteTap extends AppCompatActivity {
    private final String dbName = "webnautes";
    private final String tableName = "person";
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE ="phone";
    SQLiteDatabase sampleDB = null;
    ListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_tap);
        list = (ListView) findViewById(R.id.listitem);
        personList = new ArrayList<HashMap<String,String>>();
        showList();
    }
    protected void showList() {
        try {
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);
            if (c != null) {
                if (c.moveToNext()) {
                    do {
                        //테이블에서 두개의 컬럼값을 가져와서
                        String Name = c.getString(c.getColumnIndex("name"));
                        String Phone = c.getString(c.getColumnIndex("phone"));
                        //HashMap에 넣
                        HashMap<String,String> persons = new HashMap<String,String>();
                        persons.put(TAG_NAME,Name);
                        persons.put(TAG_PHONE,Phone);
                        //ArrayList에 추가합니다..
                        personList.add(persons);
                    } while (c.moveToNext());
                }
            }
            ReadDB.close();
            //새로운 apapter를 생성하여 데이터를 넣은 후..
            adapter = new SimpleAdapter(
                    this, personList, R.layout.list_item,
                    new String[]{TAG_NAME},
                    new int[]{ R.id.name}
            );
            //화면에 보여주기 위해 Listview에 연결합니다.
            list.setAdapter(adapter);
        } catch (SQLiteException se) {
            //Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            //Log.e("",  se.getMessage());
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String,String> item = (HashMap<String, String>)parent.getItemAtPosition(position);
                String str = item.get("name");
                Intent i;
                switch (str){
                    case "드래그":
                        i = new Intent(FavoriteTap.this, DragPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "딕":
                        i = new Intent(FavoriteTap.this, DigPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "버팔로":
                        i = new Intent(FavoriteTap.this, BuffPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "볼드롭":
                        i = new Intent(FavoriteTap.this, BalldrPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "볼 체인지":
                        i = new Intent(FavoriteTap.this, BallchPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "브러쉬":
                        i = new Intent(FavoriteTap.this, BrushPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "셔플":
                        i = new Intent(FavoriteTap.this, ShufflePlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "스탬프":
                        i = new Intent(FavoriteTap.this, StampPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "스텝":
                        i = new Intent(FavoriteTap.this, StepPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "크램프 롤":
                        i = new Intent(FavoriteTap.this, CrampPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "탭1":
                        i = new Intent(FavoriteTap.this, Tap1Play.class);
                        startActivity(i);
                        finish();
                        break;
                    case "탭2":
                        i = new Intent(FavoriteTap.this, Tap2Play.class);
                        startActivity(i);
                        finish();
                        break;
                    case "플랩":
                        i = new Intent(FavoriteTap.this, FlapPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "홉셔플":
                        i = new Intent(FavoriteTap.this, HopshuPlay.class);
                        startActivity(i);
                        finish();
                        break;
                    case "힐드롭":
                        i = new Intent(FavoriteTap.this, HeeldrPlay.class);
                        startActivity(i);
                        finish();
                        break;
                }
            }
        });
    }
}// http://webnautes.tistory.com/830 참고사이트