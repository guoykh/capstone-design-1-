package com.example.user.tapapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 이수경 on 2018-05-01.
 */

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context ct){
        super(ct, "myDB", null,1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table donjak(name, count);");
    }

    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2){

    }
}
