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
    public static final String TABLE_NAME1 = "reservation";
    public static final String TABLE_NAME_Car = "car";
    public static final String USER_ID = "USER_ID";
    public static final String TYPE_OF_SERVICE = "TYPE_OF_SERVICE";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
    public static final String TABLE_NAME3 = "shoppingcart";
    public static final String CARS = "CARS";

    public User_db(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT,SURNAME TEXT, TELEPHONE TEXT, EMAIL TEXT,PASSWORD TEXT)");
        db.execSQL("create table "+ TABLE_NAME1 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT,TYPE_OF_SERVICE TEXT, DATE TEXT,TIME TEXT, USER_ID REFERENCES user(ID))");
        db.execSQL("create table "+ TABLE_NAME_Car +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,IMAGE BLOB, BRAND TEXT,MODEL TEXT, PRICE TEXT, POWER TEXT, HORSEPOWER TEXT,TORQUE TEXT, REVATMAXPOWER TEXT,TRANSMISSION TEXT,HEIGHT TEXT,LENGTH TEXT,WIDTH TEXT,RATING TEXT, KLJUC TEXT)");
        db.execSQL("create table "+ TABLE_NAME3 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID REFERENCES user(ID), CARS_ID REFERENCES car(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_Car);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);
        onCreate(db);
    }

//    public boolean insertData(String name,String surname,String telephone,String email, String password)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues parameters = new ContentValues();
//        parameters.put(NAME,name);
//        parameters.put(SURNAME,surname);
//        parameters.put(TELEPHONE,telephone);
//        parameters.put(EMAIL,email);
//        parameters.put(PASSWORD,password);
//        long rez = db.insert(TABLE_NAME,null,parameters);
//        if(rez != -1)
//        {
//            return true;
//        }else
//            return false;
//    }

    //kreiranje pocetnih tabela
    public boolean createTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues parameters = new ContentValues();
        return true;
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

    public boolean updateInfo(String id, String name, String telephone,String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues parameters = new ContentValues();
        String[] split = name.split(" ");
        parameters.put(NAME,split[0]);
        parameters.put(SURNAME,split[1]);
        parameters.put(TELEPHONE,telephone);
        parameters.put(EMAIL,email);
        long rez = db.update(TABLE_NAME,parameters,"ID="+id, null);
        if(rez != -1)
        {
            return true;
        }else
            return false;
    }

    public User getCurrentUser(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor read = db.rawQuery("select * from "+ TABLE_NAME + " where ID=?",new String[]{id});
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
