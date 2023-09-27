package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper
{
    private static final String dbname = "mydb";
    private static final int version = 1;

    public MyHelper(Context context)
    {
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE USER (id INTEGER PRIMARY KEY , USER_NAME TEXT, PASSWORD TEXT, STATUS TEXT)";
        db.execSQL(sql);
       // insertdata("null",null,"false",db)
        insertdata("null","null","false",db);
      //  Toast.makeText(this,""+c,Toast.LENGTH_LONG).show();
        //Log.i("main=",""+c);
         }


    public void insertdata(String user,String pass,String status,SQLiteDatabase db)
    {
        ContentValues value=new ContentValues();
        value.put("USER_NAME",user);
        value.put("PASSWORD",pass);
        value.put("STATUS",status);
        value.put("id",1);
        db.insert("USER",null,value);
       }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists USER");
    }
}
