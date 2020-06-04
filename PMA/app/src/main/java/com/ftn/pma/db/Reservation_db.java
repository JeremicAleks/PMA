package com.ftn.pma.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.TypeOfService;

import java.util.ArrayList;
import java.util.List;

public class Reservation_db extends SQLiteOpenHelper{

    public Reservation_db(@Nullable Context context) {
        super(context, User_db.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ User_db.TABLE_NAME1 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT,TYPEOFSERVICE TEXT, DATE TEXT, TIME TEXT, USER_ID REFERENCES user(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+User_db.TABLE_NAME1);
        onCreate(db);
    }

    public boolean insertData(String email,String type_of_service,String date,String time, String user_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues parameters = new ContentValues();
        parameters.put(User_db.EMAIL,email);
        parameters.put(User_db.TYPE_OF_SERVICE,type_of_service);
        parameters.put(User_db.DATE,date);
        parameters.put(User_db.TIME,time);
        parameters.put(User_db.USER_ID,user_id);
        long rez = db.insert(User_db.TABLE_NAME1,null,parameters);
        if(rez != -1)
        {
            return true;
        }else
            return false;
    }

    public List<Reservation> getAllReservation(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor read = db.rawQuery("select * from "+ User_db.TABLE_NAME1 + " where USER_ID=?",new String[]{id});
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation;
        if(read.getCount() > 0)
        {
            read.moveToFirst();
            do {
                String[] s = read.getString(read.getColumnIndex("TYPE_OF_SERVICE")).split(",");
                List<TypeOfService> type_of_service = new ArrayList<>();
                for(int i=0;i<s.length;i++)
                {
                    System.out.println("SERVIS JE : " + s[i]);
                    if(s[i].equals(TypeOfService.SMALL_SERVICE.toString()))
                    {
                        type_of_service.add(TypeOfService.SMALL_SERVICE);
                    }else if(s[i].equals(TypeOfService.BIG_SERVICE.toString()))
                    {
                        type_of_service.add(TypeOfService.BIG_SERVICE);
                    }else if(s[i].equals(TypeOfService.TECHNICAL_INSPECTION.toString()))
                    {
                        type_of_service.add(TypeOfService.TECHNICAL_INSPECTION);
                    }
                }
                reservation = new Reservation(read.getString(read.getColumnIndex("EMAIL")),type_of_service, read.getString(read.getColumnIndex("DATE")), read.getString(read.getColumnIndex("TIME") ));
                reservations.add(reservation);
            }while(read.moveToNext());

            return reservations;
        }else
        {
            return null;
        }
    }

    public Reservation zadnjaRezervacija(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor read = db.rawQuery("select * from "+ User_db.TABLE_NAME1 + " where USER_ID=?",new String[]{id});
        Reservation reservation;
        if(read.getCount() > 0)
        {
            read.moveToLast();
                String[] s = read.getString(read.getColumnIndex("TYPE_OF_SERVICE")).split(",");
                List<TypeOfService> type_of_service = new ArrayList<>();
                for(int i=0;i<s.length;i++)
                {
                    System.out.println("SERVIS JE : " + s[i]);
                    if(s[i].equals(TypeOfService.SMALL_SERVICE.toString()))
                    {
                        type_of_service.add(TypeOfService.SMALL_SERVICE);
                    }else if(s[i].equals(TypeOfService.BIG_SERVICE.toString()))
                    {
                        type_of_service.add(TypeOfService.BIG_SERVICE);
                    }else if(s[i].equals(TypeOfService.TECHNICAL_INSPECTION.toString()))
                    {
                        type_of_service.add(TypeOfService.TECHNICAL_INSPECTION);
                    }
                }
                reservation = new Reservation(read.getString(read.getColumnIndex("EMAIL")),type_of_service, read.getString(read.getColumnIndex("DATE")), read.getString(read.getColumnIndex("TIME") ));
            return reservation;
        }else
        {
            return null;
        }
    }
}
