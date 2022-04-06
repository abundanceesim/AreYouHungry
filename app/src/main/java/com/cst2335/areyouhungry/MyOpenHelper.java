package com.cst2335.areyouhungry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String TAG = "FavouritesActivity";
    public static final String filename = "MyDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "MyData";

    public MyOpenHelper(Context context) {
        super(context, filename, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
