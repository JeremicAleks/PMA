package com.ftn.pma.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ftn.pma.globals.CarDBGlobals;
import com.ftn.pma.model.Car;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Car_db extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Pma.db";


    public Car_db(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ User_db.TABLE_NAME_Car +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,IMAGE BLOB, BRAND TEXT,MODEL TEXT, PRICE TEXT, POWER TEXT, HORSEPOWER TEXT,TORQUE TEXT, REVATMAXPOWER TEXT,TRANSMISSION TEXT,HEIGHT TEXT,LENGHT TEXT,WIDTH TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+User_db.TABLE_NAME_Car);
        onCreate(db);
    }

    public boolean insertDataCar(Car car)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues parameters = new ContentValues();
        parameters.put(CarDBGlobals.BRAND,car.getBrand());
        parameters.put(CarDBGlobals.MODEL,car.getModel());
        parameters.put(CarDBGlobals.PRICE,car.getPrice());
        parameters.put(CarDBGlobals.POWER,car.getPower());
        parameters.put(CarDBGlobals.HORSEPOWER,car.getHorsePower());
        parameters.put(CarDBGlobals.TORQUE,car.getTorque());
        parameters.put(CarDBGlobals.REVATMAXPOWER,car.getRevAtMaxPower());
        parameters.put(CarDBGlobals.TRANSMISSION,car.getTransmission());
        parameters.put(CarDBGlobals.WIDTH,car.getWidth());
        parameters.put(CarDBGlobals.LENGTH,car.getLength());
        parameters.put(CarDBGlobals.HEIGHT,car.getHeight());
        long rez = db.insert(User_db.TABLE_NAME_Car,null,parameters);
        if(rez != -1)
        {
            return true;
        }else
            return false;
    }


    public  void fetchImage(SQLiteDatabase db, byte[] image) throws IOException {

//        FileInputStream fis = new FileInputStream(folder);
//        byte[] byteImage= new byte[fis.available()];
//        fis.read(byteImage);
        ContentValues values = new ContentValues();
        values.put("IMAGE",image);
        db.insert(User_db.TABLE_NAME_Car, null,values);
//        fis.close();
    }

    public Bitmap getImage(int i,SQLiteDatabase db){

        String qu = "select img  from table where feedid=" + i ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null;
    }



}
