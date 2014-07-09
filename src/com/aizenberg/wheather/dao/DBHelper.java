package com.aizenberg.wheather.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.aizenberg.wheather.TheApplication;

/**
 * Created by Юрий on 05.07.14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String NAME = "aiz_rssfeed";
    public static final int DB_VERSION = 1;
    private static DBHelper instance;


    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    public DBHelper() {
        super(TheApplication.getInstance().getApplicationContext(), NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseUtils.Tables.WEATHER + " ("
                + DatabaseUtils.W_ID + ", "
                + DatabaseUtils.WEATHER_JSON + "); ");


        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseUtils.Tables.FORECAST + " ("
                + DatabaseUtils.W_ID + ", "
                + DatabaseUtils.FORECAST_JSON + "); ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
