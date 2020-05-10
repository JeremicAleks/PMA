package com.ftn.pma.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ftn.pma.model.User;

public class User_db extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Pma.db";
    public static final String TABLE_NAME = "user";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String SURNAME = "SURNAME";
    public static final String TELEPHONE = "TELEPHONE";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    public User_db(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT,SURNAME TEXT, TELEPHONE TEXT, EMAIL TEXT,PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,String surname,String telephone,String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues parameters = new ContentValues();
        parameters.put(NAME,name);
        parameters.put(SURNAME,surname);
        parameters.put(TELEPHONE,telephone);
        parameters.put(EMAIL,email);
        parameters.put(PASSWORD,password);
        long rez = db.insert(TABLE_NAME,null,parameters);
        if(rez != -1)
        {
            return true;
        }else
            return false;
    }

    public User login(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor read = db.rawQuery("select * from "+ TABLE_NAME + " where EMAIL=? and PASSWORD=?",new String[]{email,password});
        User user;
        if(read.getCount() > 0)
        {
            read.moveToFirst();
            user = new User(read.getString(read.getColumnIndex("NAME")), read.getString(read.getColumnIndex("SURNAME")), read.getString(read.getColumnIndex("TELEPHONE")),read.getString(read.getColumnIndex("EMAIL")),read.getString(read.getColumnIndex("PASSWORD")));
            return user;
        }else
        {
            return null;
        }

    }
}
