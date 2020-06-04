package com.ftn.pma.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ShoppingCart_db extends SQLiteOpenHelper {

    public ShoppingCart_db(@Nullable Context context) {
        super(context, User_db.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ User_db.TABLE_NAME3 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(USER_ID) REFERENCES user(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+User_db.TABLE_NAME3);
        onCreate(db);
    }
}
