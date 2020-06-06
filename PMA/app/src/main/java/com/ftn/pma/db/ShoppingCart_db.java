package com.ftn.pma.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ftn.pma.globals.ShoppingCartDBGlobals;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.TypeOfService;
import com.ftn.pma.model.User;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart_db extends SQLiteOpenHelper {

    public ShoppingCart_db(@Nullable Context context) {
        super(context, User_db.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ User_db.TABLE_NAME3 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(USER_ID) REFERENCES user(ID), FOREIGN KEY(CARS_ID) REFERENCES car(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+User_db.TABLE_NAME3);
        onCreate(db);
    }

    public boolean insertData(String id_user,String id_cars)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues parameters = new ContentValues();
        parameters.put(ShoppingCartDBGlobals.USER_ID,id_user);
        parameters.put(ShoppingCartDBGlobals.CARS_ID,id_cars);
        long rez = db.insert(User_db.TABLE_NAME3,null,parameters);
        if(rez != -1)
        {
            return true;
        }else
            return false;
    }

    public List<ShoppingCart> getAllBuyCars(String user_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor read = db.rawQuery("select * from "+ User_db.TABLE_NAME1 + " where USER_ID=?",new String[]{user_id});
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        ShoppingCart shoppingCart;
        if(read.getCount() > 0)
        {
            read.moveToNext();
            shoppingCart = new ShoppingCart(read.getString(read.getColumnIndex("USER_ID")),read.getString(read.getColumnIndex("CARS_ID")));
            shoppingCarts.add(shoppingCart);
            return shoppingCarts;
        }else
        {
            return null;
        }
    }
}
