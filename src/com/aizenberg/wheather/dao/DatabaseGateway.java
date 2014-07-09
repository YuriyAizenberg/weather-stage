package com.aizenberg.wheather.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.aizenberg.wheather.model.Forecast;
import com.aizenberg.wheather.model.WeatherItem;
import com.aizenberg.wheather.utils.NullExcludeList;

import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */
public class DatabaseGateway {

    private static DatabaseGateway instance;
    private SQLiteDatabase database;

    public static DatabaseGateway getInstance() {
        if (instance == null) {
            instance = new DatabaseGateway();
        }
        return instance;
    }

    private DatabaseGateway() {
        database = DBHelper.getInstance().getWritableDatabase();
    }

    public long saveWeather(WeatherItem weatherItem) {
        deleteWeather(weatherItem.getCityId());
        return database.insertWithOnConflict(DatabaseUtils.Tables.WEATHER.toString(), null, DatabaseUtils.getCV(weatherItem), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long saveForecast(Forecast forecast) {
        deleteForecast(forecast.getCityId());
        return database.insertWithOnConflict(DatabaseUtils.Tables.FORECAST.toString(), null, DatabaseUtils.getCV(forecast), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteForecast(Long id) {
        database.delete(DatabaseUtils.Tables.FORECAST.toString(), id == null ? null : DatabaseUtils.W_ID + " = " + id, null);
    }

    public Forecast getForecast(long cityId) {
        Forecast model = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseUtils.Tables.FORECAST.toString() + " WHERE " + DatabaseUtils.W_ID + " = " + cityId, null);
        if (cursor != null && cursor.moveToFirst()) {
            model = DatabaseUtils.parseForecast(cursor);
        }
        closeCursor(cursor);
        return model;
    }

    public List<Forecast> getForecasts() {
        List<Forecast> forecasts = new NullExcludeList<Forecast>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseUtils.Tables.FORECAST.toString(), null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                forecasts.add(DatabaseUtils.parseForecast(cursor));
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return forecasts;
    }

    public void saveForecasts(List<Forecast> forecasts) {
        for (Forecast forecast : forecasts) {
            saveForecast(forecast);
        }
    }


    public WeatherItem getWeather(long cityId) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseUtils.Tables.WEATHER + " WHERE " + DatabaseUtils.W_ID + " = " + cityId, null);
        WeatherItem model = null;
        if (cursor != null && cursor.moveToFirst()) {
            model = DatabaseUtils.parseWeather(cursor);
        }
        closeCursor(cursor);
        return model;
    }

    public int deleteWeather(long cityId) {
        return database.delete(DatabaseUtils.Tables.WEATHER.toString(), DatabaseUtils.W_ID + " = " + cityId, null);
    }

    public List<WeatherItem> getWeathers() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseUtils.Tables.WEATHER, null);
        List<WeatherItem> weatherItems = new NullExcludeList<WeatherItem>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                weatherItems.add(DatabaseUtils.parseWeather(cursor));
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return weatherItems;
    }

    public void saveWeathers(List<WeatherItem> weatherItems) {
        for (WeatherItem weatherItem : weatherItems) {
            saveWeather(weatherItem);
        }
    }


    private void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


}
