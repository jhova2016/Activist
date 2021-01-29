package com.example.activist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BD_Calles extends SQLiteOpenHelper {
    private static final String TABLE_CREATE = "CREATE TABLE tabladecalles(_id INTEGER PRIMARY KEY AUTOINCREMENT, photopath TEXT,name TEXT,direction TEXT,phone TEXT,electorkey TEXT,directboss TEXT,notes TEXT,date TEXT,up TEXT)";
    private static final String DB_NAME = "registros3.sqlite";
    private static final int DB_VERSION = 1;
    public BD_Calles(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
