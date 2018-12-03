package com.creatoweb.demo.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Local_Data extends SQLiteOpenHelper
{
    public Local_Data(Context context) {
        super(context, "recently.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table recent_table(name varchar,url varchar,image varchar,tags varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public void Insert_Recent(String name,String url,String image,String tags)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("name",name);
        cv.put("url",url);
        cv.put("image",image);
        cv.put("tags",tags);
        db.insert("recent_table",null,cv);
    }
}
